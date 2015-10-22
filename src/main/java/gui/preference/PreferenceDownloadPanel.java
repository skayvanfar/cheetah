package gui.preference;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Created by Saeed on 9/13/2015.
 */
class PreferenceDownloadPanel extends JPanel {
    public PreferenceDownloadPanel() {
        JButton jButton = new JButton("PreferenceDownloadPanel");
        add(jButton);

        Dimension dim = getPreferredSize();
        dim.width = 250;
        setPreferredSize(dim);
        setMinimumSize(dim);

        Border innerBorder = BorderFactory.createTitledBorder("Connection");
        Border outerBorder = BorderFactory.createEmptyBorder(5,5,5,5);
        setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));

        layoutComponents();
    }

    private void layoutComponents() {

    }
}
