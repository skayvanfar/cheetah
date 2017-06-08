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
public class PreferenceDialog extends JDialog {



    private JPanel confirmationPanel;
    private JButton okButton;
    private JButton cancelButton;
    private JButton defaultButton;

    private PreferenceCategoryPanel preferenceCategoryPanel;
    private PreferencePanels preferencePanels;

    private PreferencesListener preferencesListener;

    JFrame parent;

    public PreferenceDialog(JFrame parent, PreferencesDTO preferencesDTO) {

        super(parent, "Preferences", true);
        this.parent = parent;
        setLayout(new BorderLayout());

        preferenceCategoryPanel = new PreferenceCategoryPanel();
        preferencePanels = new PreferencePanels(parent, preferencesDTO);
        preferenceCategoryPanel.setPreferenceCategoryPanelListener(preferencePanels);

        confirmationPanel = new JPanel();
        confirmationPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        okButton = new JButton("Ok");
        cancelButton = new JButton("Cancel");
        defaultButton = new JButton("Defaults");

        confirmationPanel.add(okButton);
        confirmationPanel.add(cancelButton);
        confirmationPanel.add(defaultButton);

        add(preferenceCategoryPanel, BorderLayout.WEST);
        add(preferencePanels, BorderLayout.CENTER);
        add(confirmationPanel, BorderLayout.SOUTH);

        setPanelBackgroundColor(Color.WHITE);



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
        preferencePanels.setPanelBackgroundColor(color);
        confirmationPanel.setBackground(color);
    }

    private void okButtonClicked() {
        PreferencesDTO preferenceDTO = preferencePanels.getPreferencesDTO();
        if (preferencesListener != null) {
            preferencesListener.preferencesSet(preferenceDTO);
        }
        setPreferenceEffects(preferenceDTO);
    }

    public void setPreferencesDTO(PreferencesDTO preferencesDTO) {
        preferencePanels.setPreferencesDTO(preferencesDTO);
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

}
