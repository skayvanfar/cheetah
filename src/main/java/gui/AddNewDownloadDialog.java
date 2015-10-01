package gui;

import gui.listener.AddNewDownloadListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

/**
 * Created by Saeed on 9/10/2015.
 */
public class AddNewDownloadDialog extends JDialog {

    // Add download text field.
    private JTextField newTextField;
    private JButton newButton;

    private AddNewDownloadListener addNewDownloadListener;

    public AddNewDownloadDialog(JFrame parent) {
        super(parent, "Add New Download", false);

        setLayout(new FlowLayout(FlowLayout.LEFT));

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("messages/messages"); // NOI18N

        newButton = new JButton(bundle.getString("addNewDownloadDialog.newButton.label"));
        newButton.setToolTipText(bundle.getString("addNewDownloadDialog.newButton.toolTip"));
        newTextField = new JTextField(44);

        add(newButton);
        add(newTextField);

        // can use ActionListener for class and use that
        newButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                addAction();
            }
        });

        setSize(603, 74);
        setLocationRelativeTo(parent);
    }

    // TODO for controls set
    private void layoutControls() {

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
