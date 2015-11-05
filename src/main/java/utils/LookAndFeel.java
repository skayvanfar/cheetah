package utils;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Saeed on 11/4/2015.
 */
public class LookAndFeel {

    public static void setLaf(String laf) {
        try {
            switch (laf) {
                case "Metal":
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                    break;
                case "Nimbus":
                    UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                    break;
                case "System":
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    public static void changeLaf(JFrame frame, String laf) {
        switch (laf) {
            case "Metal":
                updateLAF(UIManager.getCrossPlatformLookAndFeelClassName());
                break;
            case "Nimbus":
                updateLAF("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                break;
            case "System":
                updateLAF(UIManager.getSystemLookAndFeelClassName());
                break;
            case "":
        }
    }

    public static void changeLaf(JFrame frame, String laf, Dimension topFrameDimension) {
        switch (laf) {
            case "Metal":
                updateLAF(UIManager.getCrossPlatformLookAndFeelClassName());
                break;
            case "Nimbus":
                updateLAF("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                break;
            case "System":
                updateLAF(UIManager.getSystemLookAndFeelClassName());
                break;
            case "":
        }
        frame.setSize(topFrameDimension);
    }



    // Assuming that value is the class name of the new look-and-feel, here is the snippet to update all windows and sub-components:
    public static void updateLAF(String value) {
        if (UIManager.getLookAndFeel().getClass().getName().equals(value)) {
            return;
        }
        try {
            UIManager.setLookAndFeel(value);
            for (Frame frame : Frame.getFrames()) {
                updateLAFRecursively(frame);
            }
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    public static void updateLAFRecursively(Window window) {
        for (Window childWindow : window.getOwnedWindows()) {
            updateLAFRecursively(childWindow);
        }
        SwingUtilities.updateComponentTreeUI(window);
        window.pack();
    }


}
