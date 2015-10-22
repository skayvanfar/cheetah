package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Saeed on 9/25/2015.
 */
class AboutPanel extends JPanel {

    private final java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("messages/messages"); // NOI18N

    public void paintComponent(Graphics g) {
        g.setColor(getForeground());
        g.setFont(new Font("Edwardian Script ITC", Font.BOLD, 28));
        g.drawString(bundle.getString("aboutPanel.program.name"), 20, 80);
        g.drawLine(20, 90, 450, 90);
        g.setFont(new Font("Dialog", Font.PLAIN, 12));
        g.drawString(bundle.getString("aboutPanel.program.productionMessage"), 40, 120);
        g.setFont(new Font("Dialog", Font.BOLD, 14));
        g.drawString(bundle.getString("aboutPanel.program.producer"), 40, 140);
    }
}
