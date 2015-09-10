package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by Saeed Kayvanfar on 9/10/2015.
 */
public class DownloadManagerGUI extends JFrame {

    public DownloadManagerGUI(String name) {
        super(name);

        setLayout(new BorderLayout());


        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                System.out.println("Window Closing");
                dispose();
                System.gc();
            }
        });
        setMinimumSize(new Dimension(500, 400));
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setVisible(true);
    }
}
