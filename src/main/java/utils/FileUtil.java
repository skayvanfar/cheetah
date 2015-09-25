package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Saeed on 9/15/2015.
 */
public class FileUtil {

    public static void joinDownloadedParts(List<File> files, String path, String fileName) {
      //  String homeDir = System.getProperty("user.home");
        File outputFile = outputFile(new File(path + File.separator + fileName));

        FileOutputStream fos;
        FileInputStream fis;
        byte[] fileBytes;
        int bytesRead = 0;
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

}
