package gui.preference;

import javax.swing.*;
import javax.swing.border.Border;

/**
 * Created by Saeed on 9/23/2015.
 */
public class PreferenceSitesLoginsPanel extends JPanel {

    public PreferenceSitesLoginsPanel() {
        JButton jButton = new JButton("PreferenceSitesLoginsPanel");
        add(jButton);

        Border innerBorder = BorderFactory.createTitledBorder("Connection");
        Border outerBorder = BorderFactory.createEmptyBorder(5,5,5,5);
        setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));

        layoutComponents();
    }

    private void layoutComponents() {

    }
}
