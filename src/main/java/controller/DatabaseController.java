package controller;

import model.Download;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Saeed on 9/30/2015.
 */
public interface DatabaseController {
    public boolean connect() throws Exception;
    public boolean disconnect();
    public void save(Download download) throws SQLException;
    public List<Download> load() throws SQLException, Exception;
    public void delete(int id) throws SQLException;
}
