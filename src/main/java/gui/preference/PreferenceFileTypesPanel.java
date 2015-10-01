package gui.preference;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Created by Saeed on 9/23/2015.
 */
public class PreferenceFileTypesPanel extends JPanel {

    public PreferenceFileTypesPanel() {
        JButton jButton = new JButton("PreferenceFileTypesPanel");
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
