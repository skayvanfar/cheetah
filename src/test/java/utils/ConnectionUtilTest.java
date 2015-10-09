package utils;

import enums.SizeType;
import enums.TimeUnit;
import org.apache.commons.io.FilenameUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import static org.junit.Assert.*;

public class ConnectionUtilTest {

    private URL url;

    @Before
    public void setUp() throws Exception {
        url = new URL("http://localhost/index.html");
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetFileName() throws Exception {
        String expectedValue = "index.html";
        String actualValue = ConnectionUtil.getFileName(url);
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetRealFileByQueryString() throws Exception {
        url = new URL("http://localhost/index.html?2225");
        File expectedValue = new File("index.html");
        File actualValue = ConnectionUtil.getRealFile(url);
        System.out.println("actualValue: " + actualValue);
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetRealFileWithoutQueryString() throws Exception {
        File expectedValue = new File("index.html");
        File actualValue = ConnectionUtil.getRealFile(url);
        System.out.println("actualValue: " + actualValue);
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetFileExtension() throws Exception {
        String expectedValue = "html";
        String actualValue = ConnectionUtil.getFileExtension(url);
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testRoundSizeTypeFormat() throws Exception {
        String expectedValue = "141.879 KB";
        String actualValue = ConnectionUtil.roundSizeTypeFormat(145284.232552245f, SizeType.BYTE);
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testCalculateTransferRateInUnit() throws Exception {
        float expectedValue = 6227.5f;
        float actualValue = ConnectionUtil.calculateTransferRateInUnit(12455, 2000, TimeUnit.SEC);
        Assert.assertEquals("testCalculateTransferRateInUnit", expectedValue, actualValue, 0.01);
    }

    @Test
    public void testUrl() throws Exception {
     //   url = new URL("http://btn-ict.ir/request.php?718");
     //   url = new URL("http://heather.cs.ucdavis.edu/~matloff/Linux/LinuxInstall.pdf");

   //     HttpURLConnection conn = (HttpURLConnection) url.openConnection();
   //     String raw = conn.getHeaderField("Content-Disposition");
        // raw = "attachment; filename=abc.jpg"
  //      if(raw != null && raw.indexOf("=") != -1) {
   //         String fileName = raw.split("=")[1]; //getting value after '='
  //          System.out.println("http://btn-ict.ir/request.php :" + fileName);
  //      } else {
              // fall back to random generated file name?
   //         System.out.println("no Content-Disposition");
   //     }

  //      url.getContent();
  //      System.out.println( url.getContent());
   //     System.out.println(ConnectionUtil.getFileName(url));

        String url2 = "http://www.example.com/some/path/to/a/file.xml";

        String name = FilenameUtils.getName(url.getFile());
        String baseName = FilenameUtils.getBaseName(url.getFile());
        String extension = FilenameUtils.getExtension(url.getFile());

        System.out.println("names : " + name);
        System.out.println("Basename : " + baseName);
        System.out.println("extension : " + extension);
    }
}