package gui.preference;

import model.dto.PreferenceConnectionDTO;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by Saeed on 9/23/2015.
 */
class PreferenceConnectionPanel extends JPanel {

    private JLabel maxConnectionNumberLabel;
    private JComboBox<Integer> maxConnectionNumberComboBox; /// todo encapsulation ....
 //   private JLabel downloadLimitsLabel;

    private PreferenceConnectionDTO preferenceConnectionDTO;

    public PreferenceConnectionPanel(PreferenceConnectionDTO preferenceConnectionDTO) {
        Dimension dim = getPreferredSize();
        dim.width = 250;
        setPreferredSize(dim);
        setMinimumSize(dim);

        this.preferenceConnectionDTO = preferenceConnectionDTO;

        maxConnectionNumberLabel = new JLabel("Max Connections Number:");
        maxConnectionNumberComboBox = new JComboBox<>(new Integer[]{1, 2, 4, 8, 16, 24, 32});
    //    maxConnectionNumberComboBox.setSelectedIndex(3);
        maxConnectionNumberComboBox.setEditable(false);

        maxConnectionNumberLabel.setDisplayedMnemonic(KeyEvent.VK_N);
        maxConnectionNumberLabel.setLabelFor(maxConnectionNumberComboBox);

        Border innerBorder = BorderFactory.createTitledBorder("Connection");
        Border outerBorder = BorderFactory.createEmptyBorder(5,5,5,5);
        setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));

        layoutComponents();

        setPreferenceConnectionDTO(preferenceConnectionDTO);
    }

    private void layoutComponents() {
        setLayout(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();

        ///////////////// First row ////////////////////////////////////////////
        gc.gridy = 0;

        gc.weightx = 1;
        gc.weighty = 0.1;

        gc.gridx = 0;
        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets(0, 0, 0, 5);
        add(maxConnectionNumberLabel, gc);

        gc.gridx = 1;
        gc.gridy = 0;
        gc.insets = new Insets(0, 0, 0, 0);
        gc.anchor = GridBagConstraints.LINE_START;
        add(maxConnectionNumberComboBox, gc);
    }

    public void setPreferenceConnectionDTO(PreferenceConnectionDTO preferenceConnectionDTO) {
        maxConnectionNumberComboBox.setSelectedItem(preferenceConnectionDTO.getMaxConnectionNumber());
    }

    public PreferenceConnectionDTO getPreferenceConnectionDTO() {
        preferenceConnectionDTO.setMaxConnectionNumber((Integer) maxConnectionNumberComboBox.getSelectedItem());
        return preferenceConnectionDTO;
    }
}
