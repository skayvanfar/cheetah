package gui.preference;

import model.dto.PreferencesInterfaceDTO;
import utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by Saeed on 11/4/2015.
 */
public class PreferenceInterfacePanel extends PreferenceJPanel {

    private JPanel interfacePanel;
    private JLabel lookAndFeelSelectLabel;
    private JComboBox<String> lookAndFeelSelectComboBox;
    private JLabel localLabel;
    private JComboBox<String> localComboBox;

    private PreferencesInterfaceDTO preferencesInterfaceDTO;

    public PreferenceInterfacePanel(PreferencesInterfaceDTO preferencesInterfaceDTO, final JFrame parentFrame) {
        super("Interface", "preferenceInterfacePanel.iconPath");

        this.preferencesInterfaceDTO = preferencesInterfaceDTO;

        interfacePanel = new JPanel();
        lookAndFeelSelectLabel = new JLabel("Select Look and Feel:");
        lookAndFeelSelectComboBox = new JComboBox<>(new String[] {"System", "Metal", "Nimbus"});
        localLabel = new JLabel("Language");
        localComboBox = new JComboBox<>(new String[] {"English", "Persian"});
        localComboBox.setEnabled(false);  // todo for next release

        lookAndFeelSelectComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                utils.LookAndFeel.changeLaf(parentFrame, (String) e.getItem(), new Dimension(900, 580));
            }
        });

        layoutComponentsOfInterfacePanel();

        setPreferencesInterfaceDTO(preferencesInterfaceDTO);

    }

    private void layoutComponentsOfInterfacePanel() {
        interfacePanel.setLayout(new GridBagLayout());
        interfacePanel.setBorder(BorderFactory.createTitledBorder("Connections"));
        interfacePanel.setBackground(Color.WHITE);

        GridBagConstraints gc = new GridBagConstraints();


        ///////////////// First row ////////////////////////////////////////////
        gc.gridy = 0;

        gc.weightx = 1;
        gc.weighty = 0.1;

        gc.gridx = 0;
        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets(0, 0, 0, 5);
        interfacePanel.add(lookAndFeelSelectLabel, gc);

        gc.gridx = 1;
        gc.insets = new Insets(0, 0, 0, 0);
        gc.anchor = GridBagConstraints.LINE_START;
        interfacePanel.add(lookAndFeelSelectComboBox, gc);
        ///////////////// Next row ////////////////////////////////////////////
        gc.gridy++;

        gc.gridx = 0;
        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets(0, 0, 0, 5);
        interfacePanel.add(localLabel, gc);

        gc.gridx = 1;
        gc.insets = new Insets(0, 0, 0, 0);
        gc.anchor = GridBagConstraints.LINE_START;
        interfacePanel.add(localComboBox, gc);

        interfacePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(interfacePanel);
    }

    public PreferencesInterfaceDTO getPreferencesInterfaceDTO() {
        preferencesInterfaceDTO.setLookAndFeelName((String) lookAndFeelSelectComboBox.getSelectedItem());
        preferencesInterfaceDTO.setLocalName((String) localComboBox.getSelectedItem());
        return preferencesInterfaceDTO;
    }

    public void setPreferencesInterfaceDTO(PreferencesInterfaceDTO preferencesInterfaceDTO) {
        this.preferencesInterfaceDTO = preferencesInterfaceDTO;
        lookAndFeelSelectComboBox.setSelectedItem(preferencesInterfaceDTO.getLookAndFeelName());
        localComboBox.setSelectedItem(preferencesInterfaceDTO.getLocalName());
    }
}
