package controller;

import dao.DatabaseDao;
import dao.JDBCDatabaseDao;
import model.Download;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Saeed on 9/30/2015.
 */
public class DatabaseControllerImpl implements DatabaseController {

    private DatabaseDao databaseDao;

    public DatabaseControllerImpl(String driver, String connectionURL, int port, String userName, String password) {
        databaseDao = new JDBCDatabaseDao(driver, connectionURL, port, userName, password);
    }

    @Override
    public boolean connect() throws Exception {
        return databaseDao.connect();
    }

    @Override
    public boolean disconnect() {
        return databaseDao.disconnect();
    }

    @Override
    public void save(Download download) throws SQLException {
        databaseDao.save(download);
    }

    @Override
    public List<Download> load() throws Exception {
        return databaseDao.load();
    }

    @Override
    public void delete(int id) throws SQLException {
        databaseDao.delete(id);
    }

    @Override
    public void createTablesIfNotExist() throws SQLException {
        databaseDao.createTablesIfNotExist();
    }
}
