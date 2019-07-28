/*
 * Cheetah - A Free Fast Downloader
 *
 * Copyright © 2015 Saeed Kayvanfar
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package dao;

import enums.ConnectionStatus;
import enums.DownloadStatus;
import enums.ProtocolType;
import exception.DriverNotFoundException;
import ir.sk.concurrencyutils.annotation.GuardedBy;
import ir.sk.concurrencyutils.annotation.ThreadSafe;
import model.Download;
import model.DownloadRange;
import model.httpImpl.HttpDownload;
import model.httpImpl.HttpDownloadRange;
import model.httpsImpl.HttpsDownload;
import model.httpsImpl.HttpsDownloadRange;
import org.apache.log4j.Logger;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="kayvanfar.sj@gmail.com">Saeed Kayvanfar</a> 9/30/2015
 */
@ThreadSafe
public class JDBCDatabaseDao implements DatabaseDao {

    // Logger
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    @GuardedBy("this")
    private Connection con;

    @GuardedBy("this")
    private final String connectionUrl;

    @GuardedBy("this")
    private final String driver;

    @GuardedBy("this")
    private final int port; //todo ?

    @GuardedBy("this")
    private final String userName;

    @GuardedBy("this")
    private final String password;

    public JDBCDatabaseDao(String driver, String connectionURL, int port, String userName, String password) {
        this.driver = driver;
        this.connectionUrl = connectionURL;
        this.port = port;
        this.userName = userName;
        this.password = password;

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new DriverNotFoundException("Driver not found");
        }

    }

    @Override
    public synchronized boolean connect() throws SQLException {
        if (con != null) return false;

        con = DriverManager.getConnection(connectionUrl, userName, password);

        logger.info("Connected: " + con);

        return true;
    }

    @Override
    public synchronized boolean disconnect() {
        boolean result = false;
        if (con != null) {
            try {
                con.close();
                logger.info("Disconnected");
                con = null;
                result = true;
            } catch (SQLException e) {
                logger.error("Can't close connection");
            }
        }
        return result;
    }

    private synchronized boolean isTablesExist() throws SQLException {
        Statement existStatement = con.createStatement();
        String downloadCreateSql = "SELECT name FROM sqlite_master WHERE type='table' AND name='DOWNLOAD'";

        ResultSet checkResult = existStatement.executeQuery(downloadCreateSql);
        return checkResult.next();
    }

    public synchronized void createTablesIfNotExist() throws SQLException {

        connect();
        if (!isTablesExist()) {
            Statement createStatement = con.createStatement();

            // Set auto-commit to false
            con.setAutoCommit(false);

            String downloadCreateSql = "CREATE TABLE DOWNLOAD " +
                    "(ID INT PRIMARY KEY NOT NULL," +
                    " URL TEXT NOT NULL," +
                    " DOWNLOAD_NAME_FILE TEXT NOT NULL," +
                    " DOWNLOAD_PATH TEXT NOT NULL," +
                    " DOWNLOAD_RANGE_PATH TEXT NOT NULL," +
                    " SIZE INT NOT NULL," +
                    " STATUS INT NOT NULL," +
                    " PROTOCOL_TYPE INT NOT NULL," +
                    " DESCRIPTION TEXT)";

            createStatement.addBatch(downloadCreateSql);

            String downloadRangeCreateSql = "CREATE TABLE DOWNLOAD_RANGE " +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " NUMBER INT NOT NULL," +
                    " RANGE_SIZE INT NOT NULL, " +
                    " CONNECTION_STATUS INT NOT NULL," +
                    " DOWNLOAD_RANGE_FILE TEXT NOT NULL," +
                    " START_RANGE INT NOT NULL," +
                    " END_RANGE INT NOT NULL," +
                    " DOWNLOAD_ID INT NOT NULL," +
                    " FOREIGN KEY (DOWNLOAD_ID) REFERENCES DOWNLOAD(ID) ON DELETE CASCADE)";

            createStatement.addBatch(downloadRangeCreateSql);

            int[] result = createStatement.executeBatch();

            //Explicitly commit statements to apply changes
            con.commit();

            //    result = createStatement.executeUpdate(downloadRangeCreateSql);
            createStatement.close();
            con.close();
            logger.info("result: " + Arrays.toString(result));
        }
        disconnect();
    }

    @Override
    public synchronized void save(Download download) throws SQLException {
        connect();
        String checkSql = "SELECT COUNT(*) AS count FROM DOWNLOAD WHERE ID = ?";
        PreparedStatement checkStatement = con.prepareStatement(checkSql);

        String pkDownloadRangeSql = "SELECT ID FROM DOWNLOAD_RANGE WHERE ID = ?";
        PreparedStatement pkDownloadRangeStatement = con.prepareStatement(checkSql);

        String insertDownloadSql = "INSERT INTO DOWNLOAD (ID, URL, DOWNLOAD_NAME_FILE, DOWNLOAD_PATH, DOWNLOAD_RANGE_PATH, SIZE, STATUS, PROTOCOL_TYPE, DESCRIPTION) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement insertDownloadStatement = con.prepareStatement(insertDownloadSql);
        String insertDownloadRangeSql = "INSERT INTO DOWNLOAD_RANGE (NUMBER, RANGE_SIZE, CONNECTION_STATUS,DOWNLOAD_RANGE_FILE, START_RANGE, END_RANGE, DOWNLOAD_ID) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement insertDownloadRangeStatement = con.prepareStatement(insertDownloadRangeSql, Statement.RETURN_GENERATED_KEYS);

        String updateDownloadSql = "UPDATE DOWNLOAD SET URL=?, DOWNLOAD_NAME_FILE=?, DOWNLOAD_PATH=?, DOWNLOAD_RANGE_PATH=?, SIZE=?, STATUS=?, PROTOCOL_TYPE=?, DESCRIPTION=? WHERE ID = ?";
        PreparedStatement updateDownloadStatement = con.prepareStatement(updateDownloadSql);
        String updateDownloadRangeSql = "UPDATE DOWNLOAD_RANGE SET NUMBER=?, RANGE_SIZE=?, CONNECTION_STATUS=?, DOWNLOAD_RANGE_FILE=?, START_RANGE=?, END_RANGE=? WHERE ID = ?";
        PreparedStatement updateDownloadRangeStatement = con.prepareStatement(updateDownloadRangeSql);

        // download info
        int id = download.getId();
        String url = download.getUrl().toString();
        String downloadNameFile = download.getDownloadName();
        File downloadPath = download.getDownloadPath();
        File downloadRangePath = download.getDownloadRangePath();
        int size = download.getSize();
        DownloadStatus downloadStatus = download.getStatus();
        ProtocolType protocolType = download.getProtocolType();
        String description = download.getDescription();

        checkStatement.setInt(1, id);

        ResultSet checkResult = checkStatement.executeQuery();
        checkResult.next();

        int count = checkResult.getInt(1); //

        // downloadRange info
        List<DownloadRange> downloadRanges = download.getDownloadRangeList();

        con.setAutoCommit(false);

        if (count == 0) {
            logger.info("Inserting download with ID " + id);

            int col = 1;
            insertDownloadStatement.setInt(col++, id);
            insertDownloadStatement.setString(col++, url);
            insertDownloadStatement.setString(col++, downloadNameFile);
            insertDownloadStatement.setString(col++, downloadPath.toString());
            insertDownloadStatement.setString(col++, downloadRangePath.toString());
            insertDownloadStatement.setInt(col++, size);
            insertDownloadStatement.setInt(col++, downloadStatus.getValue());
            insertDownloadStatement.setInt(col++, protocolType.getValue());
            insertDownloadStatement.setString(col++, description);

            insertDownloadStatement.executeUpdate();

            for (DownloadRange downloadRange : downloadRanges) {
                logger.info("Inserting DownloadRange with NUMBER " + downloadRange.getNumber());

                //       int downloadRangeId = downloadRange.getId();
                int number = downloadRange.getNumber();
                int rangeSize = downloadRange.getRangeSize();
                ConnectionStatus connectionStatus = downloadRange.getConnectionStatus();
                String downloadRangeFile = downloadRange.getDownloadRangeFile().getPath();
                int startRange = downloadRange.getStartRange();
                int endRange = downloadRange.getEndRange();

                int col2 = 1;
                //   insertDownloadRangeStatement.setInt(col2++, downloadRangeId);
                insertDownloadRangeStatement.setInt(col2++, number);
                insertDownloadRangeStatement.setInt(col2++, rangeSize);
                insertDownloadRangeStatement.setInt(col2++, connectionStatus.getValue());
                insertDownloadRangeStatement.setString(col2++, downloadRangeFile);
                insertDownloadRangeStatement.setInt(col2++, startRange);
                insertDownloadRangeStatement.setInt(col2++, endRange);
                insertDownloadRangeStatement.setInt(col2++, id);

                insertDownloadRangeStatement.executeUpdate();
                ResultSet generatedKeys = insertDownloadRangeStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    downloadRange.setId((int) generatedKeys.getLong(1));
                }
                else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }

        } else {
            logger.info("Updating download with ID " + id);

            int col = 1;
            updateDownloadStatement.setString(col++, url);
            updateDownloadStatement.setString(col++, downloadNameFile);
            updateDownloadStatement.setString(col++, downloadPath.toString());
            updateDownloadStatement.setString(col++, downloadRangePath.toString());
            updateDownloadStatement.setInt(col++, size);
            updateDownloadStatement.setInt(col++, downloadStatus.getValue());
            updateDownloadStatement.setInt(col++, protocolType.getValue());
            updateDownloadStatement.setString(col++, description);
            updateDownloadStatement.setInt(col++, id);

            updateDownloadStatement.executeUpdate();

            for (DownloadRange downloadRange : downloadRanges) {
                logger.info("Updating DownloadRange with NUMBER " + downloadRange.getNumber());
                int downloadRangeId = downloadRange.getId();
                int number = downloadRange.getNumber();
                int rangeSize = downloadRange.getRangeSize();
                ConnectionStatus connectionStatus = downloadRange.getConnectionStatus();
                String downloadRangeFile = downloadRange.getDownloadRangeFile().getPath();
                int startRange = downloadRange.getStartRange();
                int endRange = downloadRange.getEndRange();

                int col2 = 1;
                updateDownloadRangeStatement.setInt(col2++, number);
                updateDownloadRangeStatement.setInt(col2++, rangeSize);
                updateDownloadRangeStatement.setInt(col2++, connectionStatus.getValue());
                updateDownloadRangeStatement.setString(col2++, downloadRangeFile);
                updateDownloadRangeStatement.setInt(col2++, startRange);
                updateDownloadRangeStatement.setInt(col2++, endRange);
                updateDownloadRangeStatement.setInt(col2++, downloadRangeId);

                updateDownloadRangeStatement.executeUpdate();
            }
        }
        con.commit();
        updateDownloadRangeStatement.close();
        insertDownloadRangeStatement.close();
        updateDownloadStatement.close();
        insertDownloadStatement.close();
        checkStatement.close();
        disconnect();
    }

    @Override
    public synchronized List<Download> load() throws SQLException, MalformedURLException {

        connect();

        List<Download> downloads = new ArrayList<>();

        String selectDownloadSql = "SELECT ID, URL, DOWNLOAD_NAME_FILE, DOWNLOAD_PATH, DOWNLOAD_RANGE_PATH, SIZE, STATUS, PROTOCOL_TYPE, DESCRIPTION FROM DOWNLOAD ORDER BY ID";
        Statement selectDownloadStatement = con.createStatement();

        String selectDownloadRangeSql = "SELECT ID, NUMBER, RANGE_SIZE, CONNECTION_STATUS, DOWNLOAD_RANGE_FILE, START_RANGE, END_RANGE FROM DOWNLOAD_RANGE WHERE DOWNLOAD_ID = ?";


        ResultSet downloadResultSet = selectDownloadStatement.executeQuery(selectDownloadSql);

        while (downloadResultSet.next()) {
            PreparedStatement selectDownloadRangeStatement = con.prepareStatement(selectDownloadRangeSql);
            int id = downloadResultSet.getInt("ID");
            String url = downloadResultSet.getString("URL");
            String downloadName = downloadResultSet.getString("DOWNLOAD_NAME_FILE");
            File downloadPath = new File(downloadResultSet.getString("DOWNLOAD_PATH"));
            File downloadRangePath = new  File(downloadResultSet.getString("DOWNLOAD_RANGE_PATH"));
            int size = downloadResultSet.getInt("SIZE");
            int status = downloadResultSet.getInt("STATUS");
            ProtocolType protocolType = ProtocolType.valueOf(downloadResultSet.getInt("PROTOCOL_TYPE"));
            String description = downloadResultSet.getString("DESCRIPTION");

            ////**************************** todo must use sterategy pattern
            Download download = null;
            switch (protocolType) {
                case HTTP:
                    download = new HttpDownload(id, new URL(url), downloadName, 8, downloadPath, downloadRangePath, protocolType);
                    break;
                case FTP:
                    // todo must be created ...
                    break;
                case HTTPS:
                    download = new HttpsDownload(id, new URL(url), downloadName, 8, downloadPath, downloadRangePath, protocolType);
                    break;
            }
            download.setSize(size);
            download.setStatus(DownloadStatus.valueOf(status));
            download.setDescription(description);
            ////*****************************

            selectDownloadRangeStatement.setInt(1, id);
            ResultSet downloadRangeResultSet = selectDownloadRangeStatement.executeQuery();

            DownloadRange downloadRange = null;
            while (downloadRangeResultSet.next()) {
                int downloadRangeId = downloadRangeResultSet.getInt("ID");
                int number = downloadRangeResultSet.getInt("NUMBER");
                int rangeSize = downloadRangeResultSet.getInt("RANGE_SIZE");
                int connectionStatus = downloadRangeResultSet.getInt("CONNECTION_STATUS");
                String downloadRangeFile = downloadRangeResultSet.getString("DOWNLOAD_RANGE_FILE");
                int startRange = downloadRangeResultSet.getInt("START_RANGE");
                int endRange = downloadRangeResultSet.getInt("END_RANGE");

                switch (protocolType) {
                    case HTTP:
                        downloadRange = new HttpDownloadRange(number, new URL(url), new File(downloadRangeFile), startRange, endRange); // todo 0 0
                        break;
                    case FTP:
                        // todo must be created ...
                        break;
                    case HTTPS:
                        downloadRange = new HttpsDownloadRange(number, new URL(url), new File(downloadRangeFile), startRange, endRange);
                        break;
                }

                downloadRange.setId(downloadRangeId);
                downloadRange.setRangeSize(rangeSize);
                downloadRange.setConnectionStatus(ConnectionStatus.valueOf(connectionStatus));

                download.addDownloadRange(downloadRange);
            }
            downloads.add(download);

            downloadRangeResultSet.close();
            selectDownloadRangeStatement.close();
        }

        downloadResultSet.close();
        selectDownloadStatement.close();
        disconnect();

        return downloads;
    }

    public synchronized void delete(int id) throws SQLException {
        connect();
        String cascadeSql = "PRAGMA foreign_keys = ON";
        //    con.setAutoCommit(false);
        Statement statement = con.createStatement();
        statement.execute(cascadeSql);

        String deleteSql = "DELETE FROM DOWNLOAD WHERE ID = ?";
        PreparedStatement deleteStatement = con.prepareStatement(deleteSql);

        deleteStatement.setInt(1, id);
        deleteStatement.executeUpdate();
        //    con.commit();
        disconnect();
    }
}
