package utils;

import enums.SizeType;

import java.net.HttpURLConnection;
import java.net.URL;
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

    // Get file extension portion of file of URL.
    public static String getFileExtension(URL url) {
        String fileName = url.getFile();
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }

    public static String roundSizeTypeFormat(float transferRate, SizeType sizeType) {
        if (transferRate < 999) {
            String transferRateFormated = String.format("%.2f", transferRate);
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
}
