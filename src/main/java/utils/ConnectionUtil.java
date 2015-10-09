package utils;

import enums.SizeType;
import enums.TimeUnit;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Saeed on 9/13/2015.
 */
public class ConnectionUtil {

    public static void printHttpURLConnectionHeaders(HttpURLConnection httpURLConnection) {
        //////////////////////?????????????????????? print hears of response
        Map<String, List<String>> s = httpURLConnection.getHeaderFields();
        Set<Map.Entry<String, List<String>>> set = s.entrySet();
        for (Map.Entry<String, List<String>> ss : set) {
            System.out.println("key: " + ss.getKey() + "  value: " + ss.getValue());
        }
    }

    // Get file name portion of URL.
    public static String getFileName(URL url) {
        String fileName = url.getFile();
        return fileName.substring(fileName.lastIndexOf('/') + 1);
    }

    public static File getRealFile(URL url) throws IOException {
        File file = null;
        URLConnection conn = (URLConnection) url.openConnection();
        String raw = conn.getHeaderField("Content-Disposition");
        // raw = "attachment; filename=abc.jpg"
        if(raw != null && raw.indexOf('=') != -1) {
            file = new File(raw.split("=")[1]); //getting value after '='
        } else {
            // fall back to random generated file name?
            String fileName = url.getFile();
            if (fileName.lastIndexOf('?') != -1) {
                file = new File(fileName.substring(fileName.lastIndexOf('/') + 1, fileName.lastIndexOf('?')));
            } else {
                file = new File(fileName.substring(fileName.lastIndexOf('/') + 1));
            }
        }
        return file;
    }

    // Get file extension portion of file of URL.
    public static String getFileExtension(URL url) {
        String fileName = url.getFile();
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }

    public static String roundSizeTypeFormat(float transferRate, SizeType sizeType) {
        if (transferRate < 999) {
            String transferRateFormated = String.format("%.3f", transferRate);
            switch (sizeType) {
                case BYTE:
                    return transferRateFormated + " B";
                case KILOBYTE:
                    return transferRateFormated + " KB";
                case MEGABYTE:
                    return transferRateFormated + " MB";
                case TERABYTE:
                    return transferRateFormated + " TB";
                default:
                    return String.valueOf(transferRate);
            }
        } else {
            return roundSizeTypeFormat(transferRate / 1024, SizeType.values()[sizeType.ordinal() + 1]);
        }
    }

    public static int getPartSizeOfDownload(int downloadSize, int connectionSize) {
        return downloadSize /connectionSize;
    }

    public static float calculateTransferRateInUnit(float differenceDownloaded, int longTime, TimeUnit timeUnit) {

        int unitTime = 0;
        switch (timeUnit) {
            case SEC:
                unitTime = 1000;
                break;
            case MIN:
                unitTime = 60 * 1000;
                break;
            case HOUR:
                unitTime = 60 * 60 * 1000;
                break;
            default:
                unitTime = 1000;
        }

        return (differenceDownloaded * unitTime) / longTime;
    }
}
