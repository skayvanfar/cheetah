package gui.preference;

import gui.listener.PreferenceCategoryPanelListener;
import gui.listener.PreferencesListener;
import model.dto.PreferencesDTO;
import model.dto.PreferencesInterfaceDTO;
import model.dto.PreferencesProxyDTO;
import service.ProxyService;
import service.impl.ProxyServiceImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

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
    private PreferenceInterfacePanel preferenceInterfacePanel;

    private JPanel confirmationPanel;
    private JButton okButton;
    private JButton cancelButton;
    private JButton defaultButton;

    private PreferenceCategoryPanel preferenceCategoryPanel;
    private CardLayout cardLayout;
    private JPanel preferenceCards;

    private PreferencesListener preferencesListener;

    private PreferencesDTO preferencesDTO;

    JFrame parent;

    public PreferenceDialog(JFrame parent, PreferencesDTO preferencesDTO) {

        super(parent, "Preferences", true);
        this.parent = parent;
        setLayout(new BorderLayout());

        this.preferencesDTO = preferencesDTO;

        preferenceGeneralPanel = new PreferenceGeneralPanel(preferencesDTO.getPreferencesGeneralDTO());
     //   preferenceFileTypesPanel = new PreferenceFileTypesPanel();
        preferenceSavePanel = new PreferenceSavePanel(preferencesDTO.getPreferencesSaveDTO());
        preferenceDownloadPanel = new PreferenceDownloadPanel();
        preferenceConnectionPanel = new PreferenceConnectionPanel(preferencesDTO.getPreferencesConnectionDTO());
        preferenceProxyPanel = new PreferenceProxyPanel(preferencesDTO.getPreferencesProxyDTO());
        preferenceSitesLoginPanel = new PreferenceSitesLoginPanel();
     //   preferenceDialUpVPN = new PreferenceDialUpVPN();
     //   preferenceSoundPanel = new PreferenceSoundPanel();
        preferenceInterfacePanel = new PreferenceInterfacePanel(preferencesDTO.getPreferencesInterfaceDTO(), parent);

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
        preferenceCards.add(preferenceInterfacePanel, "Interface");

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
   //     setSize(800, 500);
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
                preferencesListener.preferenceReset();
                okButtonClicked();
                PreferenceDialog.this.setVisible(false);
            }
        });

        defaultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                preferencesListener.preferenceDefaults();
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
        preferenceInterfacePanel.setBackground(color);
    }

    private void okButtonClicked() {
        PreferencesDTO preferenceDTO = new PreferencesDTO();
        preferenceDTO.setPreferencesSaveDTO(preferenceSavePanel.getPreferenceSaveDTO());
        preferenceDTO.setPreferencesConnectionDTO(preferenceConnectionPanel.getPreferencesConnectionDTO());
        preferenceDTO.setPreferencesProxyDTO(preferenceProxyPanel.getPreferencesProxyDTO());
        preferenceDTO.setPreferencesInterfaceDTO(preferenceInterfacePanel.getPreferencesInterfaceDTO());
        if (preferencesListener != null) {
            preferencesListener.preferencesSet(preferenceDTO);
        }
        setPreferenceEffects(preferenceDTO);
    }

    public void setPreferencesDTO(PreferencesDTO preferencesDTO) {
        preferenceSavePanel.setPreferenceSaveDTO(preferencesDTO.getPreferencesSaveDTO());
        preferenceConnectionPanel.setPreferencesConnectionDTO(preferencesDTO.getPreferencesConnectionDTO());
        preferenceInterfacePanel.setPreferencesInterfaceDTO(preferencesDTO.getPreferencesInterfaceDTO());
        preferenceProxyPanel.setPreferencesProxyDTO(preferencesDTO.getPreferencesProxyDTO());
    }

    private void setPreferenceEffects(PreferencesDTO preferencesDTO) {
       PreferencesInterfaceDTO preferencesInterfaceDTO = preferencesDTO.getPreferencesInterfaceDTO();
        utils.LookAndFeel.changeLaf(parent, preferencesInterfaceDTO.getLookAndFeelName(), new Dimension(900, 580));
        setLocale(new Locale(preferencesInterfaceDTO.getLocalName()));
        ProxyService proxyService = new ProxyServiceImpl();
        PreferencesProxyDTO preferencesProxyDTO = preferencesDTO.getPreferencesProxyDTO();
        if (preferencesProxyDTO.getProxySettingType() == 0) {
            proxyService.setNoProxy();
        } else if (preferencesProxyDTO.getProxySettingType() == 1) {
            proxyService.setNoProxy(); // todo must change proxyService.setUseSystemProxy();
        } else if (preferencesProxyDTO.getProxySettingType() == 2 && preferencesProxyDTO.isUseProxyNotSocks()) {
            proxyService.setHttpProxy(preferencesProxyDTO.getHttpProxyAddress(), preferencesProxyDTO.getHttpProxyPort(),
                    preferencesProxyDTO.getHttpProxyUserName(), preferencesProxyDTO.getHttpProxyPassword());
//            proxyService.setHttpsProxy(preferencesProxyDTO.getHttpsProxyAddress(), preferencesProxyDTO.getHttpsProxyPort(),
//                    preferencesProxyDTO.getHttpsProxyUserName(), preferencesProxyDTO.getHttpsProxyPassword());
//            proxyService.setFtpProxy(preferencesProxyDTO.getFtpProxyAddress(), preferencesProxyDTO.getFtpProxyPort(),
//                    preferencesProxyDTO.getFtpProxyUserName(), preferencesProxyDTO.getFtpProxyPassword());
        } else if (preferencesProxyDTO.getProxySettingType() == 2 && !preferencesProxyDTO.isUseProxyNotSocks()) {
            proxyService.setSocksProxy(preferencesProxyDTO.getSocksProxyAddress(), preferencesProxyDTO.getSocksProxyPort(),
                    preferencesProxyDTO.getSocksProxyUserName(), preferencesProxyDTO.getSocksProxyPassword());
        }
    }

    public void setPreferencesListener(PreferencesListener preferencesListener) {
        this.preferencesListener = preferencesListener;
    }

    @Override
    public void nodeSelectedEventOccured(String nodeName) {
        cardLayout.show(preferenceCards, nodeName);
    }

}
