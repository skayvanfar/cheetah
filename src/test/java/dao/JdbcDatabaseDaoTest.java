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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="kayvanfar.sj@gmail.com">Saeed Kayvanfar</a>
 */
public class JdbcDatabaseDaoTest {

    DatabaseDao databaseDao;
  //  private int withConnectionTest =

    @Before
    public void setUp() throws Exception {
    //    databaseDao = new JDBCDatabaseDao("org.sqlite.JDBC", "jdbc:sqlite:test.db", 0, "", "");
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testConnect() throws Exception {
    //    boolean expectedValue = true;
 //       boolean resultValue = databaseDao.connect();
  //      Assert.assertEquals(expectedValue, resultValue);
    }

    @Test
    public void testDisconnect() throws Exception {
    //    boolean expectedValue = true;
    //    if (databaseDao.connect()) {
    //        boolean resultValue = databaseDao.disconnect();
    //        Assert.assertEquals(expectedValue, resultValue);
    //    } else {
    //        throw new Exception("Other method must first work.");
    //    }
    }

    @Test
    public void testSave() throws Exception {
   //     boolean expectedValue = true;

   //     download download = new download(1, new URL("http://localhost/test.txt"), 8, "", "");
   ///     DownloadRange downloadRange = new DownloadRange(1, new URL("http://localhost/test.txt"), "", 0, 0);
  //      List<DownloadRange> downloadRanges = new ArrayList<>();
   //     downloadRanges.add(downloadRange);
   //     download.setDownloadRangeList(downloadRanges);

  //      databaseDao.connect();
  //      databaseDao.save(download);
    }

    @Test
    public void testDelete() throws Exception {
     //   databaseDao.connect();
     //   databaseDao.delete(1);
    }

    @Test
    public void testLoad() throws Exception {
/*       boolean expectedValue = true;

        download download = new download(1, new URL("http://localhost/test.txt"), 8, "", "");
        DownloadRange downloadRange = new DownloadRange(0,1, new URL("http://localhost/test.txt"), "", 0, 0);
        List<DownloadRange> downloadRanges = new ArrayList<>();
        downloadRanges.add(downloadRange);
        download.setDownloadRangeList(downloadRanges);

        databaseDao.connect();
        List<download> downloads = databaseDao.load();

        for (download download1 : downloads) {
            System.out.println(download1.toString());
            for (DownloadRange downloadRange1 : download1.getDownloadRangeList()) {
                System.out.println(downloadRange1.toString());
            }
        }*/
    }
}