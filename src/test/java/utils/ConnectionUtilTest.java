package utils;

import enums.SizeType;
import enums.TimeUnit;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
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
}