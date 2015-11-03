package gui.preference;

import utils.Utils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * Created by sad.keyvanfar on 11/3/2015.
 */
public abstract class PreferenceJPanel extends JPanel {

    protected final ResourceBundle messagesBundle = java.util.ResourceBundle.getBundle("messages/messages"); // NOI18N

    public PreferenceJPanel(String panelName, String iconPath) {
        super();
        layoutComponents(panelName, iconPath);
    }

    public PreferenceJPanel(LayoutManager layout, boolean isDoubleBuffered, String panelName, String iconPath) {
        super(layout, isDoubleBuffered);
        layoutComponents(panelName, iconPath);
    }

    public PreferenceJPanel(LayoutManager layout, String panelName, String iconPath) {
        super(layout);
        layoutComponents(panelName, iconPath);
    }

    public PreferenceJPanel(boolean isDoubleBuffered, String panelName, String iconPath) {
        super(isDoubleBuffered);
        layoutComponents(panelName, iconPath);
    }

    private void layoutComponents(String panelName, String iconPath) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        Border innerBorder = BorderFactory.createTitledBorder("Save");
        Border outerBorder = BorderFactory.createEmptyBorder(5,5,5,5);
        setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));

        layoutComponentsOfImagePanel(panelName, iconPath);
    }

    private void layoutComponentsOfImagePanel(String labelPanel, String iconPath) {
        JPanel imagePanel = new JPanel();
        JLabel panelImageLabel;
        JLabel panelLabel;
        imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.X_AXIS));
        imagePanel.setBackground(Color.WHITE);
        panelImageLabel = new JLabel();
        panelImageLabel.setIcon(Utils.createIcon(messagesBundle.getString(iconPath)));
        panelLabel = new JLabel(labelPanel);
        imagePanel.add(panelImageLabel);
        imagePanel.add(panelLabel);
        imagePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(imagePanel);
        add(new JSeparator());
    }

}
