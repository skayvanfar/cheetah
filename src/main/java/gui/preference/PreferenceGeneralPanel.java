package gui.preference;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Created by Saeed on 9/13/2015.
 */
public class PreferenceGeneralPanel extends JPanel {

    private JCheckBox citizenCheck;

    public PreferenceGeneralPanel() {

            JButton jButton = new JButton("PreferenceGeneralPanel");
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

    public void layoutComponents() {
        setLayout(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();

        ///////////////// First row ////////////////////////////////////////////

    }

    public void setDefaults(String user, String password, int port) {
        //   userField.setText(user);
        //    passField.setText(password);
        //    portSpinner.setValue(port);
    }
}
