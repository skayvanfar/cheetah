package gui;

import javax.swing.*;

/**
 * Created by Saeed Kayvanfar on 9/10/2015.
 */
public class Application {

    // Set application title.
    private final static String NAME_OF_CUI = "Chita";

    public static void main( String[] args ) {

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                 new DownloadManagerGUI(NAME_OF_CUI);
            }
        });

    }
}
