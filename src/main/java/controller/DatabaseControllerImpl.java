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

package controller;

import dao.DatabaseDao;
import dao.JDBCDatabaseDao;
import ir.sk.concurrencyutils.annotation.ThreadSafe;
import model.Download;

import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.List;

/**
 * @author <a href="kayvanfar.sj@gmail.com">Saeed Kayvanfar</a> 9/30/2015
 */
@ThreadSafe
public class DatabaseControllerImpl implements DatabaseController {

    private final DatabaseDao databaseDao;

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
    public List<Download> load() throws SQLException, MalformedURLException {
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
