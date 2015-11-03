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
 //   private PreferenceFileTypesPanel preferenceFileTypesPanel;
    private PreferenceSavePanel preferenceSavePanel;
    private PreferenceDownloadPanel preferenceDownloadPanel;

    private PreferenceConnectionPanel preferenceConnectionPanel;
    private PreferenceProxyPanel preferenceProxyPanel;
    private PreferenceSitesLoginPanel preferenceSitesLoginPanel;
 //   private PreferenceDialUpVPN preferenceDialUpVPN;
 //   private PreferenceSoundPanel preferenceSoundPanel;
    private JPanel confirmationPanel;
    private JButton okButton;
    private JButton cancelButton;
    private JButton defaultButton;

    private PreferenceCategoryPanel preferenceCategoryPanel;
    private CardLayout cardLayout;
    private JPanel preferenceCards;

    private PreferencesListener preferencesListener;

    private PreferencesDTO preferencesDTO;

    public PreferenceDialog(JFrame parent, PreferencesDTO preferencesDTO) {

        super(parent, "Preferences", true);

        setLayout(new BorderLayout());

        this.preferencesDTO = preferencesDTO;

        preferenceGeneralPanel = new PreferenceGeneralPanel(preferencesDTO.getPreferenceGeneralDTO());
     //   preferenceFileTypesPanel = new PreferenceFileTypesPanel();
        preferenceSavePanel = new PreferenceSavePanel(preferencesDTO.getPreferencesSaveDTO());
        preferenceDownloadPanel = new PreferenceDownloadPanel();
        preferenceConnectionPanel = new PreferenceConnectionPanel(preferencesDTO.getPreferenceConnectionDTO());
        preferenceProxyPanel = new PreferenceProxyPanel(preferencesDTO.getPreferenceProxyDTO());
        preferenceSitesLoginPanel = new PreferenceSitesLoginPanel();
     //   preferenceDialUpVPN = new PreferenceDialUpVPN();
     //   preferenceSoundPanel = new PreferenceSoundPanel();

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
     //   preferenceCards.add(preferenceFileTypesPanel, "File Types");
        preferenceCards.add(preferenceSavePanel, "Save To");
        preferenceCards.add(preferenceDownloadPanel, "Download");
        preferenceCards.add(preferenceConnectionPanel, "Connection");
        preferenceCards.add(preferenceProxyPanel, "Proxy");
        preferenceCards.add(preferenceSitesLoginPanel, "Site Login");
     //   preferenceCards.add(preferenceDialUpVPN, "dial Up / VPN");
    //    preferenceCards.add(preferenceSoundPanel, "Sounds");

        confirmationPanel.add(okButton);
        confirmationPanel.add(cancelButton);
        confirmationPanel.add(defaultButton);

        add(preferenceCategoryPanel, BorderLayout.WEST);
        add(preferenceCards, BorderLayout.CENTER);
        add(confirmationPanel, BorderLayout.SOUTH);

        setPanelBackgroundColor(Color.WHITE);

        cardLayout.show(preferenceCards, "General");

        //    setResizable(false);

        setMinimumSize(new Dimension(350,250));
        setSize(800, 500);
        setLocationRelativeTo(parent);
        pack();

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                okButtonClicked();
                PreferenceDialog.this.setVisible(false);
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PreferenceDialog.this.setVisible(false);
            }
        });

        defaultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                preferencesListener.preferenceReset();
            }
        });
    }

    private void setPanelBackgroundColor(Color color) {
        preferenceCategoryPanel.setBackground(color);
        preferenceCards.setBackground(color);
        confirmationPanel.setBackground(color);

        preferenceGeneralPanel.setBackground(color);
    //    preferenceFileTypesPanel.setBackground(color);
        preferenceSavePanel.setBackground(color);
        preferenceDownloadPanel.setBackground(color);
        preferenceConnectionPanel.setBackground(color);
        preferenceProxyPanel.setBackground(color);
        preferenceSitesLoginPanel.setBackground(color);
    //    preferenceDialUpVPN.setBackground(color);
    //    preferenceSoundPanel.setBackground(color);
    }

    private void okButtonClicked() {
        PreferencesDTO preferenceDTO = new PreferencesDTO();
        preferenceDTO.setPreferenceConnectionDTO(preferenceConnectionPanel.getPreferenceConnectionDTO());
        preferenceDTO.setPreferencesSaveDTO(preferenceSavePanel.getPreferenceSaveDTO());
        if (preferencesListener != null) {
            preferencesListener.preferencesSet(preferenceDTO);
        }
    }

    public void setPreferencesDTO(PreferencesDTO preferencesDTO) {
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
