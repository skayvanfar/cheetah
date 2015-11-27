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

package utils;

import enums.SizeType;
import enums.TimeUnit;
import org.apache.commons.io.FilenameUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;

/**
 * @author <a href="kayvanfar.sj@gmail.com">Saeed Kayvanfar</a>
 */
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
        String expectedValue = "index.html";
        String actualValue = ConnectionUtil.getFileName(url);
        System.out.println("actualValue: " + actualValue);
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetRealFileWithoutQueryString() throws Exception {
        String expectedValue = "index.html";
        String actualValue = ConnectionUtil.getFileName(url);
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