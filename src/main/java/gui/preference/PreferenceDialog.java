package gui.preference;

import gui.listener.PreferenceCategoryPanelListener;
import gui.listener.PreferencesListener;
import model.dto.PreferencesDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Saeed on 9/13/2015.
 */
public class PreferenceDialog extends JDialog implements PreferenceCategoryPanelListener {

    private PreferenceGeneralPanel preferenceGeneralPanel;
    private PreferenceFileTypesPanel preferenceFileTypesPanel;
    private PreferenceSavePanel preferenceSavePanel;
    private PreferenceDownloadPanel preferenceDownloadPanel;

    private PreferenceConnectionPanel preferenceConnectionPanel;
    private PreferenceProxySocksPanel preferenceProxySocksPanel;
    private PreferenceSitesLoginsPanel preferenceSitesLoginsPanel;
    private PreferenceDialUpVPN preferenceDialUpVPN;
    private PreferenceSoundPanel preferenceSoundPanel;
    private JPanel confirmationPanel;
    private JButton okButton;
    private JButton cancelButton;
    private JButton defaultButton;

  //  private JTabbedPane tabPane;

    private PreferenceCategoryPanel preferenceCategoryPanel;
    private CardLayout cardLayout;
    private JPanel preferenceCards;

    private PreferencesListener preferencesListener;

    public PreferenceDialog(JFrame parent) {

        super(parent, "Preferences", false);

        setLayout(new BorderLayout());

        preferenceGeneralPanel = new PreferenceGeneralPanel();
        preferenceFileTypesPanel = new PreferenceFileTypesPanel();
        preferenceSavePanel = new PreferenceSavePanel();
        preferenceDownloadPanel = new PreferenceDownloadPanel();
        preferenceConnectionPanel = new PreferenceConnectionPanel();
        preferenceProxySocksPanel = new PreferenceProxySocksPanel();
        preferenceSitesLoginsPanel = new PreferenceSitesLoginsPanel();
        preferenceDialUpVPN = new PreferenceDialUpVPN();
        preferenceSoundPanel = new PreferenceSoundPanel();
    //    tabPane = new JTabbedPane();

  //      tabPane.addTab("General", generalPanel);
  //      tabPane.addTab("Save To", savePanel);
  //      tabPane.addTab("Download", downloadPanel);

//        add(tabPane, BorderLayout.CENTER);

        preferenceCategoryPanel = new PreferenceCategoryPanel();
        preferenceCategoryPanel.setPreferenceCategoryPanelListener(this);

        confirmationPanel = new JPanel();
        confirmationPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        okButton = new JButton("Ok");
        cancelButton = new JButton("Cancel");
        defaultButton = new JButton("Defaults");

        cardLayout = new CardLayout();
        preferenceCards = new JPanel();
        preferenceCards.setLayout(cardLayout);
        preferenceCards.add(preferenceGeneralPanel, "General");
        preferenceCards.add(preferenceFileTypesPanel, "File Types");
        preferenceCards.add(preferenceSavePanel, "Save To");
        preferenceCards.add(preferenceDownloadPanel, "Downloads");
        preferenceCards.add(preferenceConnectionPanel, "Connection");
        preferenceCards.add(preferenceProxySocksPanel, "Proxy / Socks");
        preferenceCards.add(preferenceSitesLoginsPanel, "Site Logins");
        preferenceCards.add(preferenceDialUpVPN, "dial Up / VPN");
        preferenceCards.add(preferenceSoundPanel, "Sounds");

        confirmationPanel.add(okButton);
        confirmationPanel.add(cancelButton);
        confirmationPanel.add(defaultButton);

        add(preferenceCategoryPanel, BorderLayout.WEST);
        add(preferenceCards, BorderLayout.CENTER);
        add(confirmationPanel, BorderLayout.SOUTH);

        setMinimumSize(new Dimension(350,250));
        setSize(600, 400);
        setLocationRelativeTo(parent);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                okButtonClicked();
            }
        });
    }

    private void okButtonClicked() {
        PreferencesDTO preferenceDTO = new PreferencesDTO();
        preferenceDTO.setPreferenceConnectionDTO(preferenceConnectionPanel.getPreferenceConnectionDTO());
        preferenceDTO.setPreferencesSaveDTO(preferenceSavePanel.getPreferenceSaveDTO());
        if (preferencesListener != null) {
            preferencesListener.preferencesSet(preferenceDTO);
        }
    }

    public void setDefaults(PreferencesDTO preferencesDTO) {
        preferenceConnectionPanel.setPreferenceConnectionDTO(preferencesDTO.getPreferenceConnectionDTO());
        preferenceSavePanel.setPreferenceSaveDTO(preferencesDTO.getPreferencesSaveDTO());
    }

    public void setPreferencesListener(PreferencesListener preferencesListener) {
        this.preferencesListener = preferencesListener;
    }

    @Override
    public void nodeSelectedEventOccured(String nodeName) {
        cardLayout.show(preferenceCards, nodeName);
    }

}
