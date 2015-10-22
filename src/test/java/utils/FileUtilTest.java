package utils;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class FileUtilTest {

    @Test
    public void testJoinDownloadedParts() throws Exception {
        Preferences preferences = Preferences.userRoot().node("db");
       // preferences.putInt("a", 1);
        preferences.clear();
    //    System.out.println(preferences.getInt("a", 11));
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

    @Test
    public void testOutputFileByFiles() {
        String expectedValue = "t_1.txt";

        URL url = Thread.currentThread().getContextClassLoader().getResource("t.txt");
        URL url2 = Thread.currentThread().getContextClassLoader().getResource("t_2.txt");

        File file = new File(url.getPath());
        File file2 = new File(url.getPath());

        List<File> files = new ArrayList<>();
        files.add(file);
        files.add(file2);

        File newFile = FileUtil.outputFile(files);
        String actualValue = newFile.getName();
        System.out.println(newFile);

        Assert.assertEquals(expectedValue, actualValue);
    }
}