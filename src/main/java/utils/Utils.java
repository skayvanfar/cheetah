package utils;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Saeed on 9/11/2015.
 */
public class Utils {
    public static String getFileExtension(String name) {
        int pointIndex = name.lastIndexOf(".");

        if (pointIndex == -1) {
            return null;
        }

        if (pointIndex == name.length() - 1) {
            return null;
        }

        return name.substring(pointIndex + 1, name.length());
    }

    public static ImageIcon createIcon(String path) {
        URL url = System.class.getResource(path);

        if (url == null) {
            System.out.println("Unable to load image: " + path);
        }

        ImageIcon icon = new ImageIcon(url);

        return icon;
    }

    public static Font createFont(String path) {
        URL url = System.class.getResource(path);

        if (url == null) {
            System.out.println("Unable to load font: " + path);
        }

        Font font = null;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
        } catch (FontFormatException e) {
            System.err.println("Bad format in font file: " + path);
        } catch (IOException e) {
            System.out.println("Unable to read font file: " + path);
        }

        return font;
    }

    public static int findMissingNumbers(Integer arr[], int position) {

        int count = 1;

        if (position == arr.length - 1)
            return -1;

        for (; position < arr[arr.length - 1]; position++) {

            if ((arr[position] - count) != position) {
                return position + count;
        //        count++;
        //        break;
            }
        }
        return -1;

    }
}
