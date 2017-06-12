/*
 * Cheetah - A Free Fast Downloader
 *
 * Copyright © 2015 Saeed Kayvanfar
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package gui;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import service.StatusMessageAppender;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * @author <a href="kayvanfar.sj@gmail.com">Saeed Kayvanfar</a> 9/10/2015
 */
class MessagePanel extends JPanel {

    // Logger
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final Logger messageLogger = Logger.getLogger("message");

    private final java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("messages/messages"); // NOI18N

    private JPanel messageJPanel;
    private JButton clearButton;
    private JTextArea messageJTextArea;

    private JFrame parent;

    public MessagePanel(JFrame parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(Color.white);

        messageJPanel = new JPanel();
        messageJPanel.setBackground(Color.WHITE);
        clearButton = new JButton(bundle.getString("messagePanel.clearButton.name"));
        messageJPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        Border innerBorder2 = BorderFactory.createTitledBorder("");
        Border outerBorder2 = BorderFactory.createEmptyBorder(0,0,0,0);
        messageJPanel.setBorder(BorderFactory.createCompoundBorder(outerBorder2, innerBorder2));

        clearButton.addActionListener(e -> messageJTextArea.setText(""));

        messageJPanel.add(clearButton);
        add(messageJPanel, BorderLayout.NORTH);

        messageJTextArea = new JTextArea();
        messageJTextArea.setEditable(false);

        Border innerBorder = BorderFactory.createTitledBorder("Logs");
        Border outerBorder = BorderFactory.createEmptyBorder(5,5,5,5);
        messageJTextArea.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));

        messageJTextArea.setFont(new Font("Dialog", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(messageJTextArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder(""));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        //Define log pattern layout
        PatternLayout layout = new PatternLayout("%d %-5p [%c{1}] %m%n");

        StatusMessageAppender appender = new StatusMessageAppender(messageJTextArea);
        appender.setLayout(layout);
      //  LogManager.getRootLogger().addAppender(appender);
        appender.activateOptions();
        LogManager.getLogger("message").addAppender(appender);

      //  messageLogger.info("testtttttt");
    }
}
