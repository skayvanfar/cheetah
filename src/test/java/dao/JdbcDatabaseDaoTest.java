package dao;

import model.Download;
import model.DownloadRange;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JdbcDatabaseDaoTest {

    DatabaseDao databaseDao;
  //  private int withConnectionTest =

    @Before
    public void setUp() throws Exception {
        databaseDao = new JDBCDatabaseDao("org.sqlite.JDBC", "jdbc:sqlite:test.db", 0, "", "");
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

   //     Download download = new Download(1, new URL("http://localhost/test.txt"), 8, "", "");
   ///     DownloadRange downloadRange = new DownloadRange(1, new URL("http://localhost/test.txt"), "", 0, 0);
  //      List<DownloadRange> downloadRanges = new ArrayList<>();
   //     downloadRanges.add(downloadRange);
   //     download.setDownloadRangeList(downloadRanges);

  //      databaseDao.connect();
  //      databaseDao.save(download);
    }

    @Test
    public void testLoad() throws Exception {
/*       boolean expectedValue = true;

        Download download = new Download(1, new URL("http://localhost/test.txt"), 8, "", "");
        DownloadRange downloadRange = new DownloadRange(0,1, new URL("http://localhost/test.txt"), "", 0, 0);
        List<DownloadRange> downloadRanges = new ArrayList<>();
        downloadRanges.add(downloadRange);
        download.setDownloadRangeList(downloadRanges);

        databaseDao.connect();
        List<Download> downloads = databaseDao.load();

        for (Download download1 : downloads) {
            System.out.println(download1.toString());
            for (DownloadRange downloadRange1 : download1.getDownloadRangeList()) {
                System.out.println(downloadRange1.toString());
            }
        }*/
    }
}