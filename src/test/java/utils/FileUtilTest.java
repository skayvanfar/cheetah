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

import comparator.FileNameComparator;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * @author <a href="kayvanfar.sj@gmail.com">Saeed Kayvanfar</a>
 */
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
        String expectedValue = "t~1.txt";

        URL url = Thread.currentThread().getContextClassLoader().getResource("t.txt");
        assert url != null;
        File file = new File(url.getPath());
        File newFile = FileUtil.outputFile(file);
        String actualValue = newFile.getName();

        Assert.assertEquals(expectedValue, actualValue);

    }

    @Test
    public void testOutputFileByFiles() {
       /* String expectedValue = "t_1.txt";

        URL url = Thread.currentThread().getContextClassLoader().getResource("t.txt");
        URL url2 = Thread.currentThread().getContextClassLoader().getResource("t_2.txt");

        assert url != null;
        File file = new File(url.getPath());
        File file2 = new File(url2.getPath());

        List<File> files = new ArrayList<>();
        files.add(file);
        files.add(file2);

        File newFile = FileUtil.outputFile(files, new FileNameComparator());
        String actualValue = newFile.getName();
        System.out.println(newFile);

        Assert.assertEquals(expectedValue, actualValue);*/
    }
}