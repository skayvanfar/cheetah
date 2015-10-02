package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Saeed on 9/25/2015.
 */
public class AboutPanel extends JPanel {

    public void paintComponent(Graphics g) {
        g.setColor(getForeground());
        g.setFont(new Font("Edwardian Script ITC", Font.BOLD, 28));
        g.drawString("Chita. A Fast Free Downloader", 20, 80);
        g.drawLine(20, 90, 450, 90);
        g.setFont(new Font("Dialog", Font.PLAIN, 12));
        g.drawString("This Program producted by:", 40, 120);
        g.setFont(new Font("Dialog", Font.BOLD, 14));
        g.drawString("Saeed Kayvanfar", 40, 140);
    }
}
