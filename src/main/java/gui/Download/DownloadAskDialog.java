package gui.Download;

import gui.listener.DownloadAskDialogListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by Saeed on 10/3/2015.
 */
public class DownloadAskDialog extends JDialog implements ActionListener {

    private JLabel urlLabel;
    private JTextField urlTextField;
    private JLabel downloadNameLabel;
    private JTextField downloadNameField;
    private JLabel pathLabel;
    private JTextField pathTextField;
    private JLabel sizeLabel;
    private JTextField sizeTextField;
    private JButton startDownloadButton;
    private JButton cancelDownloadButton;
    private JButton downloadLaterButton;

    private DownloadAskDialogListener downloadAskDialogListener;

    public DownloadAskDialog(JFrame parent) {
        super(parent, "Download File", false);

        urlLabel = new JLabel("URL:");
        urlTextField = new JTextField(35);
        urlTextField.setEditable(false);
        downloadNameLabel = new JLabel("Name: ");
        downloadNameField = new JTextField(20);
        downloadNameField.setEditable(false);
        pathLabel = new JLabel("Save To: ");
        pathTextField = new JTextField(35);
        sizeLabel = new JLabel("Size:");
        sizeTextField = new JTextField(5);
        sizeTextField.setEditable(false);
        startDownloadButton = new JButton("Start Download");
        cancelDownloadButton = new JButton("Cancel Download");
        downloadLaterButton = new JButton("Download Later");

        startDownloadButton.addActionListener(this);
        cancelDownloadButton.addActionListener(this);
        downloadLaterButton.addActionListener(this);

        layoutControls();

        setSize(650, 350);
        setLocationRelativeTo(parent);
    }

    private void layoutControls() {
        setLayout(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();

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
        add(urlLabel, gc);

        gc.gridx = 1;
        gc.gridy = 0;
        gc.insets = noPadding;
        gc.anchor = GridBagConstraints.LINE_START;
        add(urlTextField, gc);
////////////Next row ////////////////////////////
        gc.gridy++;

        gc.gridx = 0;
        gc.insets = rightPadding;
        gc.anchor = GridBagConstraints.LINE_END;
        add(downloadNameLabel, gc);

        gc.gridx = 1;
       // gc.gridy = 1;
        gc.insets = noPadding;
        gc.anchor = GridBagConstraints.LINE_START;
        add(downloadNameField, gc);

        ///////////////// Next row ////////////////////////////////////////////
        ////////////Next row ////////////////////////////
        gc.gridy++;

        gc.gridx = 0;
        gc.insets = rightPadding;
        gc.anchor = GridBagConstraints.LINE_END;
        add(pathLabel, gc);

        gc.gridx = 1;
        // gc.gridy = 1;
        gc.insets = noPadding;
        gc.anchor = GridBagConstraints.LINE_START;
        add(pathTextField, gc);

        ///////////////// Next row ////////////////////////////////////////////
        ////////////Next row ////////////////////////////
        gc.gridy++;

        gc.gridx = 0;
        gc.insets = rightPadding;
        gc.anchor = GridBagConstraints.LINE_END;
        add(sizeLabel, gc);

        gc.gridx = 1;
    //    gc.gridy = 1;
        gc.insets = noPadding;
        gc.anchor = GridBagConstraints.LINE_START;
        add(sizeTextField, gc);

        ///////////////// Next row ////////////////////////////////////////////
        gc.gridy++;
        gc.gridx = 0;
        gc.insets = rightPadding;
        gc.anchor = GridBagConstraints.LINE_END; // FIRST_LINE_END
        add(startDownloadButton, gc);

        //  gc.gridy++;
        gc.gridx = 1;
    //    gc.gridy = 1;
        gc.anchor = GridBagConstraints.LINE_START; // FIRST_LINE_START
        gc.insets = noPadding;
        add(cancelDownloadButton, gc);
    }

    public void setDownloadAskDialogListener(DownloadAskDialogListener downloadAskDialogListener) {
        this.downloadAskDialogListener = downloadAskDialogListener;
    }

    public void setInfo(String url, String downloadNameFile, String size) {
        urlTextField.setText(url);
        downloadNameField.setText(downloadNameFile);
        sizeTextField.setText(size);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clicked= (JButton) e.getSource();
        if (downloadAskDialogListener != null) {
            if (clicked == startDownloadButton) {
                downloadAskDialogListener.startDownloadEventOccured();
            } else if (clicked == cancelDownloadButton) {
                downloadAskDialogListener.cancelDownloadEventOccured();
            } else if (clicked == downloadLaterButton) {
                downloadAskDialogListener.laterDownloadEventOccured();
            }
        }
        dispose();
    }
}
