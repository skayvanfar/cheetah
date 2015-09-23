package gui.preference;

import gui.listener.PreferenceCategoryPanelListener;
import gui.listener.PreferencesListener;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Saeed on 9/13/2015.
 */
public class PreferenceDialog extends JDialog implements PreferenceCategoryPanelListener {

    private GeneralPanel generalPanel;
    private SavePanel savePanel;
    private DownloadPanel downloadPanel;
  //  private JTabbedPane tabPane;

    private PreferenceCategoryPanel preferenceCategoryPanel;
    private CardLayout cardLayout;
    private JPanel preferenceCards;

    private PreferencesListener preferencesListener;

    public PreferenceDialog(JFrame parent) {

        super(parent, "Preferences", false);

        setLayout(new BorderLayout());

        generalPanel = new GeneralPanel();
        savePanel = new SavePanel();
        downloadPanel = new DownloadPanel();
    //    tabPane = new JTabbedPane();

  //      tabPane.addTab("General", generalPanel);
  //      tabPane.addTab("Save To", savePanel);
  //      tabPane.addTab("Download", downloadPanel);

//        add(tabPane, BorderLayout.CENTER);

        preferenceCategoryPanel = new PreferenceCategoryPanel();
        preferenceCategoryPanel.setPreferenceCategoryPanelListener(this);

        cardLayout = new CardLayout();
        preferenceCards = new JPanel();
        preferenceCards.setLayout(cardLayout);
        preferenceCards.add(generalPanel, "generalPanel");
        preferenceCards.add(savePanel, "savePanel");
        preferenceCards.add(downloadPanel, "downloadPanel");
        preferenceCards.add(savePanel, "savePanel");

        add(preferenceCategoryPanel, BorderLayout.WEST);
        add(preferenceCards, BorderLayout.CENTER);

        setSize(400, 250);
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

    @Override
    public void generalSelectedEventOccured() {
        cardLayout.show(preferenceCards, "general");
    }

    @Override
    public void downloadSelectedEventOccured() {
        cardLayout.show(preferenceCards, "download");
    }

    @Override
    public void saveToSelectedEventOccured() {

    }

    @Override
    public void fileTypesSelectedEventOccured() {

    }

    @Override
    public void connectionSelectedEventOccured() {

    }

}
