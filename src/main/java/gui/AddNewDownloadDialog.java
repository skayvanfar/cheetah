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

import gui.listener.AddNewDownloadListener;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author <a href="kayvanfar.sj@gmail.com">Saeed Kayvanfar</a> 9/10/2015
 */
class AddNewDownloadDialog extends JDialog {

    // Logger
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    // Add download text field.
    private JTextField newTextField;
    private JButton newButton;

    private JPanel useAuthorizationPanel;
    private JLabel useAuthorizationLabel;
    private JCheckBox useAuthorizationCheckBox;
    private JLabel userIDLabel;
    private JTextField userIDTextField;
    private JLabel passwordLabel;
    private JPasswordField passwordField;

    private AddNewDownloadListener addNewDownloadListener;

    public AddNewDownloadDialog(JFrame parent) {
        super(parent, "Add New download", false);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("messages/messages"); // NOI18N

        newButton = new JButton(bundle.getString("addNewDownloadDialog.newButton.label"));
        newButton.setToolTipText(bundle.getString("addNewDownloadDialog.newButton.toolTip"));
        newTextField = new JTextField(44);

        useAuthorizationPanel = new JPanel();
        useAuthorizationLabel = new JLabel(bundle.getString("addNewDownloadDialog.useAuthorizationLabel.label"));
        useAuthorizationCheckBox = new JCheckBox();
        userIDLabel = new JLabel(bundle.getString("addNewDownloadDialog.userIDLabel.label"));
        userIDTextField = new JTextField(8);
        userIDTextField.setEnabled(false);
        passwordLabel = new JLabel(bundle.getString("addNewDownloadDialog.passwordLabel.label"));
        passwordField = new JPasswordField(8);
        passwordField.setEnabled(false);

        // Set up menmomics
        newButton.setMnemonic(KeyEvent.VK_O);

        useAuthorizationLabel.setDisplayedMnemonic(KeyEvent.VK_N);
        useAuthorizationLabel.setLabelFor(useAuthorizationCheckBox);

        Border innerBorder = BorderFactory.createTitledBorder(bundle.getString("addNewDownloadDialog.useAuthorizationPanel.label"));
        Border outerBorder = BorderFactory.createEmptyBorder(5,5,5,5);
        useAuthorizationPanel.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));

        layoutControls();

        // can use ActionListener for class and use that
        newButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                addAction();
            }
        });

        useAuthorizationCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                JCheckBox checkBox = (JCheckBox) e.getItem();
                if (checkBox.isSelected()) {
                    userIDTextField.setEnabled(true);
                    passwordField.setEnabled(true);
                } else {
                    userIDTextField.setEnabled(false);
                    passwordField.setEnabled(false);
                }
            }
        });
        pack();

       // setSize(740, 150);
        setResizable(true);
        setLocationRelativeTo(parent);
    }

    // TODO for controls set
    private void layoutControls() {

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();

        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        Insets rightPadding = new Insets(0, 0, 0, 15);
        Insets noPadding = new Insets(0, 0, 0, 0);

        ///////////////// First row ////////////////////////////////////////////
        gc.weightx = 1;
        gc.weighty = 1;

        gc.gridy = 0;
        gc.gridx = 0;
        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = rightPadding;
        panel.add(newButton, gc);

        gc.gridwidth = 3;
        gc.gridx = 1;
        gc.gridy = 0;
        gc.insets = noPadding;
        gc.anchor = GridBagConstraints.LINE_START;
        panel.add(newTextField, gc);
        ////////////Next row ////////////////////////////
        gc.gridy++;

        //     gc.weighty= 10;
        gc.gridwidth = 1;
        gc.gridx = 0;
        gc.insets = rightPadding;
        gc.anchor = GridBagConstraints.LINE_END;
        panel.add(useAuthorizationLabel, gc);

        gc.gridwidth = 2;
        gc.gridx = 1;
        // gc.gridy = 1;
        gc.insets = noPadding;
        gc.anchor = GridBagConstraints.LINE_START;
        panel.add(useAuthorizationCheckBox, gc);

        useAuthorizationPanel.add(userIDLabel); // todo may use GridBagLayout
        useAuthorizationPanel.add(userIDTextField);
        useAuthorizationPanel.add(passwordLabel);
        useAuthorizationPanel.add(passwordField);


        gc.gridwidth = 2;
        gc.gridx = 2;
        gc.insets = rightPadding;
        gc.anchor = GridBagConstraints.LINE_END;
        panel.add(useAuthorizationPanel, gc);

        add(panel);
    }

    public void setAddNewDownloadListener(AddNewDownloadListener addNewDownloadListener) {
        this.addNewDownloadListener = addNewDownloadListener;
    }

    // can be in DownloadManagerUI
    // Add a new download.
    private void addAction() {
        URL verifiedUrl = verifyUrl(newTextField.getText());
        if (verifiedUrl != null) {

            if (addNewDownloadListener != null) {
                addNewDownloadListener.newDownloadEventOccured(verifiedUrl);
            }
            newTextField.setText(""); // reset new text field
            setVisible(false);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Invalid download URL", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // can be in DownloadManagerUI
    // Verify download URL.
    private URL verifyUrl(String url) {
        // Only allow HTTP URLs.
        if (!url.toLowerCase().startsWith("http://") && !url.toLowerCase().startsWith("https://"))
            return null;

        // Verify format of URL.
        URL verifiedUrl;
        try {
            verifiedUrl = new URL(url);
        } catch (MalformedURLException e) {
            logger.error("URL isn't correct");
            return null;
        }

        // Make sure URL specifies a file.
        if (verifiedUrl.getFile().length() < 2)
            return null;

        return verifiedUrl;
    }

    public void onPaste(){
        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable t = c.getContents(this);
        if (t == null)
            return;
        try {
        String textURL = (String) t.getTransferData(DataFlavor.stringFlavor);
            if (verifyUrl(textURL) != null) {
                newTextField.setText(textURL);
            }
        } catch (Exception e){
            e.printStackTrace();
        }//try

    }//onPaste
}
