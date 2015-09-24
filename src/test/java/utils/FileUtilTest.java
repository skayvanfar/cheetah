package utils;

import model.DownloadRange;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FileUtilTest {

    @Test
    public void testJoinDownloadedParts() throws Exception {
     //   List<File> files = new ArrayList<>();
  //      for (int i = 0; i < 9; i++) {
   //         File f = new File("/splitDownloaded/LinuxInstall.pdf.00" + (i + 1));
   //         System.out.println(f.exists());
   //         files.add(new File("LinuxInstall.pdf.00" + (i + 1)));
   //     }
  //      FileUtil.joinDownloadedParts(files, "pdf");
    }

    @Test
    public void testOutputFile() {
        String expectedValue = "t_1.txt";

        URL url = Thread.currentThread().getContextClassLoader().getResource("t.txt");
        File file = new File(url.getPath());
        File newFile = FileUtil.outputFile(file);
        String actualValue = newFile.getName();

        Assert.assertEquals(expectedValue, actualValue);

    }

}