package gui;

import gui.listener.AddNewDownloadListener;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URL;

/**
 * Created by Saeed on 9/10/2015.
 */
public class AddNewDownloadDialog extends JDialog {

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
        super(parent, "Add New Download", false);

        setLayout(new FlowLayout(FlowLayout.LEFT));

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("messages/messages"); // NOI18N

        newButton = new JButton(bundle.getString("addNewDownloadDialog.newButton.label"));
        newButton.setToolTipText(bundle.getString("addNewDownloadDialog.newButton.toolTip"));
        newTextField = new JTextField(44);

        useAuthorizationPanel = new JPanel();
        useAuthorizationLabel = new JLabel("Use Authorization:");
        useAuthorizationCheckBox = new JCheckBox();
        userIDLabel = new JLabel("User Id:");
        userIDTextField = new JTextField(8);
        userIDTextField.setEnabled(false);
        passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(8);
        passwordField.setEnabled(false);

        Border innerBorder = BorderFactory.createTitledBorder("Use Authorization");
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

        setSize(603, 156);
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    // TODO for controls set
    private void layoutControls() {
        add(newButton);
        add(newTextField);

        add(useAuthorizationLabel);
        add(useAuthorizationCheckBox);
        useAuthorizationPanel.add(userIDLabel); // todo may use GridBagLayout
        useAuthorizationPanel.add(userIDTextField);
        useAuthorizationPanel.add(passwordLabel);
        useAuthorizationPanel.add(passwordField);

        add(useAuthorizationPanel);
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
                    "Invalid Download URL", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // can be in DownloadManagerUI
    // Verify download URL.
    private URL verifyUrl(String url) {
        // Only allow HTTP URLs.
        if (!url.toLowerCase().startsWith("http://"))
            return null;

        // Verify format of URL.
        URL verifiedUrl = null;
        try {
            verifiedUrl = new URL(url);
        } catch (Exception e) {
            return null;
        }

        // Make sure URL specifies a file.
        if (verifiedUrl.getFile().length() < 2)
            return null;

        return verifiedUrl;
    }
}
