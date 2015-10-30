package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Saeed on 9/25/2015.
 */
class AboutPanel extends JPanel {

    private final java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("messages/messages"); // NOI18N

    private JLabel programNameLabel;
    private JLabel productionMessageLabel;
    private JLabel producerLabel;

    public AboutPanel() {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        programNameLabel = new JLabel(bundle.getString("aboutPanel.program.name"));
        productionMessageLabel = new JLabel(bundle.getString("aboutPanel.program.productionMessage"));
        producerLabel = new JLabel(bundle.getString("aboutPanel.program.producer"));

        programNameLabel.setFont(new Font("Edwardian Script ITC", Font.BOLD, 28));
        productionMessageLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
        producerLabel.setFont(new Font("Dialog", Font.BOLD, 14));

        add(programNameLabel);
        add(new JSeparator());
        add(productionMessageLabel);
        add(producerLabel);
    }

    @Override
    public Insets getInsets() {
        return new Insets(50,50,70,50);
    }

}
