package gui.preference;

import model.dto.PreferenceProxyDTO;
import utils.Utils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * Created by Saeed on 9/23/2015.
 */
class PreferenceProxyPanel extends PreferenceJPanel {

    private JPanel proxyPanel;
    private JLabel useProxyLabel;
    private JCheckBox useProxyCheckBox;

    private JLabel proxyAddressLabel;
    private JTextField proxyAddressTextField;
    private JLabel portLabel;
    private JTextField portTextField;
    private JLabel userNameLabel;
    private JTextField userNameTextField;
    private JLabel passwordLabel;
    private JPasswordField socksPasswordField;

    private JLabel useSocksLabel;
    private JCheckBox useSocksCheckBox;

    private PreferenceProxyDTO preferenceProxyDTO;

    public PreferenceProxyPanel(PreferenceProxyDTO preferenceProxyDTO) {
        super("Proxy", "preferenceProxyPanel.iconPath");

        this.preferenceProxyDTO = preferenceProxyDTO;

        proxyPanel = new JPanel();
        useProxyLabel = new JLabel("Use Proxy");
        useProxyCheckBox = new JCheckBox();
        useSocksLabel =new JLabel("Use Socks");
        useSocksCheckBox = new JCheckBox();

        layoutComponentsOfGeneralPanel();
    }

    private void layoutComponentsOfGeneralPanel() {
        proxyPanel.setLayout(new GridBagLayout());
        proxyPanel.setBackground(Color.WHITE);

        GridBagConstraints gc = new GridBagConstraints();


        ///////////////// First row ////////////////////////////////////////////
        gc.gridy = 0;

        gc.weightx = 1;
        gc.weighty = 0.1;

        gc.gridx = 0;
        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets(0, 0, 0, 5);
        proxyPanel.add(useProxyLabel, gc);

        gc.gridx = 1;
        gc.insets = new Insets(0, 0, 0, 0);
        gc.anchor = GridBagConstraints.LINE_START;
        proxyPanel.add(useProxyCheckBox, gc);
        ///////////////// Next row ////////////////////////////////////////////
        gc.gridy++;

        gc.gridx = 0;
        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets(0, 0, 0, 5);
        proxyPanel.add(useSocksLabel, gc);

        gc.gridx = 1;
        gc.insets = new Insets(0, 0, 0, 0);
        gc.anchor = GridBagConstraints.LINE_START;
        proxyPanel.add(useSocksCheckBox, gc);

        proxyPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(proxyPanel);
    }

}
