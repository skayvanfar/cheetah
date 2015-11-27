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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="kayvanfar.sj@gmail.com">Saeed Kayvanfar</a> 9/15/2015
 */
public class FileUtil {

    public static void joinDownloadedParts(List<File> files, File path, String fileName) {
      //  String homeDir = System.getProperty("user.home");
        File outputFile = outputFile(new File(path + File.separator + fileName));

        FileOutputStream fos;
        FileInputStream fis;
        byte[] fileBytes;
        int bytesRead;
        try {
            fos = new FileOutputStream(outputFile,true);
            for (File file : files) {
                fis = new FileInputStream(file);
                fileBytes = new byte[(int) file.length()];
                bytesRead = fis.read(fileBytes, 0,(int)  file.length());
                assert(bytesRead == fileBytes.length);
                assert(bytesRead == (int) file.length());
                fos.write(fileBytes);
                fos.flush();
                fileBytes = null;
                fis.close();
                fis = null;
            }
            fos.close();
            fos = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File outputFile(File file) {
        if (!file.exists()) {
            return file;
        } else {
            String filenameAndExtension = file.getName();
            String fileName = filenameAndExtension.substring(0, filenameAndExtension.lastIndexOf('.'));
            String extension = filenameAndExtension.substring(filenameAndExtension.lastIndexOf('.') + 1);
            if (fileName.lastIndexOf('_') != -1) {
                String firstPartFileName = fileName.substring(0, fileName.lastIndexOf('_'));
                int lastPartFileName = Integer.parseInt(fileName.substring(fileName.lastIndexOf('_') + 1)) + 1;
                return outputFile(new File(file.getParent() + File.separator + firstPartFileName + "_" + lastPartFileName + "." + extension));
            } else {
                return outputFile(new File(file.getParent() + File.separator + fileName + "_1" + "." + extension));
            }

        }
    }
    public static File outputFile(List<File> files, FileNameComparator fileNameComparator) {
        List<File> outputFiles = new ArrayList<>();
        for (File file : files) {
            File outputFile = outputFile(file);
            outputFiles.add(outputFile);
        }

        return Collections.max(outputFiles, fileNameComparator);
    }

    // Get file name portion of URL.
    public static String getFileName(File file) {
        String fileName = file.toString();
        return fileName.substring(fileName.lastIndexOf(File.separator) + 1);
    }

}
