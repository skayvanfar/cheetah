package gui.preference;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Saeed on 9/13/2015.
 */
public class PreferenceGeneralPanel extends JPanel {

    private JCheckBox citizenCheck;

    public PreferenceGeneralPanel() {
        Dimension dim = getPreferredSize();
        dim.width = 250;
        setPreferredSize(dim);
        setMinimumSize(dim);
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
