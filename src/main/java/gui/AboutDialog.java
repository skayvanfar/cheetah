package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Saeed on 9/25/2015.
 */
public class AboutDialog extends JDialog {

    private AboutPanel aboutPanel;

    public AboutDialog(JFrame parent) {
        super(parent, "About...", false);

        setLayout(new BorderLayout());

        aboutPanel = new AboutPanel();
        add(aboutPanel, BorderLayout.CENTER);

        setMinimumSize(new Dimension(350,250));
        setSize(550, 200);
        setLocationRelativeTo(parent);
    }

}
