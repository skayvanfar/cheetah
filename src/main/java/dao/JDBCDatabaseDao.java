package dao;

import enums.ConnectionStatus;
import enums.DownloadStatus;
import exception.DriverNotFoundException;
import model.Download;
import model.DownloadRange;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saeed on 9/30/2015.
 */
public class JDBCDatabaseDao implements DatabaseDao {

    private Connection con;

    private String connectionUrl;
    private String driver;
    private int port; //todo ?
    private String userName;
    private String password;

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
    public boolean connect() throws SQLException {
        if (con != null) return false;

        con = DriverManager.getConnection(connectionUrl, userName, password);

        System.out.println("Connected: " + con);

        return true;
    }

    @Override
    public boolean disconnect() {
        boolean result = false;
        if (con != null) {
            try {
                con.close();
                System.out.println("Disconnected");
                con = null;
                result = true;
            } catch (SQLException e) {
                System.out.println("Can't close connection");
            }
        }
        return result;
    }

    private boolean isTablesExist() throws SQLException {
        Statement existStatement = con.createStatement();
        String downloadCreateSql = "SELECT name FROM sqlite_master WHERE type='table' AND name='DOWNLOAD'";

        ResultSet checkResult = existStatement.executeQuery(downloadCreateSql);
        if(checkResult.next())
            return true;
        else
            return false;
    }

    public void createTablesIfNotExist() throws SQLException {

        connect();
        if (!isTablesExist()) {
            Statement createStatement = con.createStatement();

            // Set auto-commit to false
            con.setAutoCommit(false);

            String downloadCreateSql = "CREATE TABLE DOWNLOAD " +
                    "(ID INT PRIMARY KEY NOT NULL," +
                    " URL TEXT NOT NULL," +
                    " DOWNLOAD_PATH TEXT NOT NULL," +
                    " DOWNLOAD_RANGE_PATH TEXT NOT NULL," +
                    " SIZE INT NOT NULL, " +
                    " STATUS INT NOT NULL)";

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
            System.out.println("result: " + result);

        }
        disconnect();
    }

    @Override
    public void save(Download download) throws SQLException {
        connect();
        String checkSql = "SELECT COUNT(*) AS count FROM DOWNLOAD WHERE ID = ?";
        PreparedStatement checkStatement = con.prepareStatement(checkSql);

        String pkDownloadRangeSql = "SELECT ID FROM DOWNLOAD_RANGE WHERE ID = ?";
        PreparedStatement pkDownloadRangeStatement = con.prepareStatement(checkSql);

        String insertDownloadSql = "INSERT INTO DOWNLOAD (ID, URL, DOWNLOAD_PATH, DOWNLOAD_RANGE_PATH, SIZE, STATUS) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement insertDownloadStatement = con.prepareStatement(insertDownloadSql);
        String insertDownloadRangeSql = "INSERT INTO DOWNLOAD_RANGE (NUMBER, RANGE_SIZE, CONNECTION_STATUS,DOWNLOAD_RANGE_FILE, START_RANGE, END_RANGE, DOWNLOAD_ID) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement insertDownloadRangeStatement = con.prepareStatement(insertDownloadRangeSql, Statement.RETURN_GENERATED_KEYS);

        String updateDownloadSql = "UPDATE DOWNLOAD SET URL=?, DOWNLOAD_PATH=?, DOWNLOAD_RANGE_PATH=?, SIZE=?, STATUS=? WHERE ID = ?";
        PreparedStatement updateDownloadStatement = con.prepareStatement(updateDownloadSql);
        String updateDownloadRangeSql = "UPDATE DOWNLOAD_RANGE SET NUMBER=?, RANGE_SIZE=?, CONNECTION_STATUS=?, DOWNLOAD_RANGE_FILE=?, START_RANGE=?, END_RANGE=? WHERE ID = ?";
        PreparedStatement updateDownloadRangeStatement = con.prepareStatement(updateDownloadRangeSql);

        // download info
        int id = download.getId();
        String url = download.getUrl();
        String downloadPath = download.getDownloadPath();
        String downloadRangePath = download.getDownloadRangePath();
        int size = download.getRealSize();
        DownloadStatus downloadStatus = download.getStatus();

        checkStatement.setInt(1, id);

        ResultSet checkResult = checkStatement.executeQuery();
        checkResult.next();

        int count = checkResult.getInt(1); //

        // downloadRange info
        List<DownloadRange> downloadRanges = download.getDownloadRangeList();

        con.setAutoCommit(false);

        if (count == 0) {
            System.out.println("Inserting Download with ID " + id);

            int col = 1;
            insertDownloadStatement.setInt(col++, id);
            insertDownloadStatement.setString(col++, url);
            insertDownloadStatement.setString(col++, downloadPath);
            insertDownloadStatement.setString(col++, downloadRangePath);
            insertDownloadStatement.setInt(col++, size);
            insertDownloadStatement.setInt(col++, downloadStatus.getValue());

            insertDownloadStatement.executeUpdate();

            for (DownloadRange downloadRange : downloadRanges) {
                System.out.println("Inserting DownloadRange with NUMBER " + downloadRange.getNumber());

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
            System.out.println("Updating Download with ID " + id);

            int col = 1;
            updateDownloadStatement.setString(col++, url);
            updateDownloadStatement.setString(col++, downloadPath);
            updateDownloadStatement.setString(col++, downloadRangePath);
            updateDownloadStatement.setInt(col++, size);
            updateDownloadStatement.setInt(col++, downloadStatus.getValue());
            updateDownloadStatement.setInt(col++, id);

            updateDownloadStatement.executeUpdate();

            for (DownloadRange downloadRange : downloadRanges) {
                System.out.println("Updating DownloadRange with NUMBER " + downloadRange.getNumber());
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
    public List<Download> load() throws SQLException, MalformedURLException {

        connect();

        List<Download> downloads = new ArrayList<>();

        String selectDownloadSql = "SELECT ID, URL, DOWNLOAD_PATH, DOWNLOAD_RANGE_PATH, SIZE, STATUS FROM DOWNLOAD ORDER BY ID";
        Statement selectDownloadStatement = con.createStatement();

        String selectDownloadRangeSql = "SELECT ID, NUMBER, RANGE_SIZE, CONNECTION_STATUS, DOWNLOAD_RANGE_FILE, START_RANGE, END_RANGE FROM DOWNLOAD_RANGE WHERE DOWNLOAD_ID = ?";


        ResultSet DownloadResultSet = selectDownloadStatement.executeQuery(selectDownloadSql);

        while (DownloadResultSet.next()) {
            PreparedStatement selectDownloadRangeStatement = con.prepareStatement(selectDownloadRangeSql);
            int id = DownloadResultSet.getInt("ID");
            String url = DownloadResultSet.getString("URL");
            String downloadPath = DownloadResultSet.getString("DOWNLOAD_PATH");
            String downloadRangePath = DownloadResultSet.getString("DOWNLOAD_RANGE_PATH");
            int size = DownloadResultSet.getInt("SIZE");
            int status = DownloadResultSet.getInt("STATUS");

            Download download = new Download(id, new URL(url), 8, downloadPath, downloadRangePath);
            download.setSize(size);
            download.setStatus(DownloadStatus.valueOf(status));

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

                downloadRange = new DownloadRange(number, new URL(url), new File(downloadRangeFile), startRange, endRange); // todo 0 0
                downloadRange.setId(downloadRangeId);
                downloadRange.setRangeSize(rangeSize);
                downloadRange.setConnectionStatus(ConnectionStatus.valueOf(connectionStatus));

                download.addDownloadRange(downloadRange);
            }
            downloads.add(download);

            downloadRangeResultSet.close();
            selectDownloadRangeStatement.close();
        }

        DownloadResultSet.close();
        selectDownloadStatement.close();
        disconnect();

        return downloads;
    }

    public boolean delete(int id) throws SQLException {
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
        return false;
    }
}
