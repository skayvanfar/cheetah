package utils;

import java.net.HttpURLConnection;
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
}
