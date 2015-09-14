package gui.preference;

import gui.listener.PreferencesListener;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Saeed on 9/13/2015.
 */
public class PreferenceDialog extends JDialog {

    private GeneralPanel generalPanel;
    private SavePanel savePanel;
    private DownloadPanel downloadPanel;
    private JTabbedPane tabPane;


    private PreferencesListener preferencesListener;

    public PreferenceDialog(JFrame parent) {

        super(parent, "Preferences", false);

        setLayout(new BorderLayout());

        generalPanel = new GeneralPanel();
        savePanel = new SavePanel();
        downloadPanel = new DownloadPanel();
        tabPane = new JTabbedPane();

        tabPane.addTab("General", generalPanel);
        tabPane.addTab("Save To", savePanel);
        tabPane.addTab("Download", downloadPanel);

        add(tabPane, BorderLayout.CENTER);

        setSize(320, 230);
        setLocationRelativeTo(parent);
    }

    public void setDefaults(String user, String password, int port) {
        //   userField.setText(user);
        //    passField.setText(password);
        //    portSpinner.setValue(port);
    }

    public void setPreferencesListener(PreferencesListener preferencesListener) {
        this.preferencesListener = preferencesListener;
    }

}
