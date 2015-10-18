package gui.Download;

import gui.listener.DownloadAskDialogListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private JButton pathButton;
    private JFileChooser pathFileChooser;
    private JLabel sizeLabel;
    private JTextField sizeTextField;
    private JButton startDownloadButton;
    private JButton cancelDownloadButton;
    private JButton laterDownloadButton;

    private DownloadAskDialogListener downloadAskDialogListener;

    java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("messages/messages"); // NOI18N

    public DownloadAskDialog(JFrame parent) {
        super(parent, "Download File", false);

        urlLabel = new JLabel(bundle.getString("downloadAskDialog.urlLabel.label"));
        urlTextField = new JTextField(50);
        urlTextField.setEditable(false);
        downloadNameLabel = new JLabel(bundle.getString("downloadAskDialog.downloadNameLabel.label"));
        downloadNameField = new JTextField(25);
        downloadNameField.setEditable(false);
        pathLabel = new JLabel(bundle.getString("downloadAskDialog.pathLabel.label"));
        pathTextField = new JTextField(35);
        pathButton = new JButton(bundle.getString("downloadAskDialog.pathButton.label"));
        pathButton.setToolTipText(bundle.getString("downloadAskDialog.pathButton.toolTip"));
        pathFileChooser = new JFileChooser();
        sizeLabel = new JLabel(bundle.getString("downloadAskDialog.sizeLabel.label"));
        sizeTextField = new JTextField(10);
        sizeTextField.setEditable(false);
        startDownloadButton = new JButton(bundle.getString("downloadAskDialog.startDownloadButton.label"));
        startDownloadButton.setToolTipText(bundle.getString("downloadAskDialog.startDownloadButton.toolTip"));
        cancelDownloadButton = new JButton(bundle.getString("downloadAskDialog.cancelDownloadButton.label"));
        cancelDownloadButton.setToolTipText(bundle.getString("downloadAskDialog.cancelDownloadButton.toolTip"));
        laterDownloadButton = new JButton(bundle.getString("downloadAskDialog.laterDownloadButton.label"));
        laterDownloadButton.setToolTipText(bundle.getString("downloadAskDialog.laterDownloadButton.toolTip"));

        startDownloadButton.addActionListener(this);
        cancelDownloadButton.addActionListener(this);
        laterDownloadButton.addActionListener(this);
        pathButton.addActionListener(this);

        layoutControls();
        pack();
        //  setSize(510, 210);
        setResizable(true);
        setLocationRelativeTo(parent);
    }

    private void layoutControls() {

        JPanel panel = new JPanel();

        panel.setLayout(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();

        //   Insets panelPadding = new Insets(10, 10, 10, 10);
        Insets rightPadding = new Insets(0, 0, 0, 15);
        Insets noPadding = new Insets(0, 0, 0, 0);

        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        ///////////////// First row ////////////////////////////////////////////
        gc.weightx = 1;
        gc.weighty = 0.25;

        gc.gridy = 0;
        gc.gridx = 0;
        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = rightPadding;
        panel.add(urlLabel, gc);

        gc.gridwidth = 3;
        gc.gridx = 1;
        gc.gridy = 0;
        gc.insets = noPadding;
        gc.anchor = GridBagConstraints.LINE_START;
        panel.add(urlTextField, gc);
////////////Next row ////////////////////////////
        gc.gridy++;

        //     gc.weighty= 10;
        gc.gridwidth = 1;
        gc.gridx = 0;
        gc.insets = rightPadding;
        gc.anchor = GridBagConstraints.LINE_END;
        panel.add(downloadNameLabel, gc);

        gc.gridwidth = 3;
        gc.gridx = 1;
        // gc.gridy = 1;
        gc.insets = noPadding;
        gc.anchor = GridBagConstraints.LINE_START;
        panel.add(downloadNameField, gc);

        ///////////////// Next row ////////////////////////////////////////////
        ////////////Next row ////////////////////////////
        gc.gridy++;

        //     gc.weighty= 10;
        gc.gridwidth = 1;
        gc.gridx = 0;
        gc.insets = rightPadding;
        gc.anchor = GridBagConstraints.LINE_END;
        panel.add(pathLabel, gc);


        //  gc.fill = GridBagConstraints.HORIZONTAL;
        gc.gridwidth = 2;
        gc.gridx = 1;
        // gc.gridy = 1;
        gc.insets = noPadding;
        gc.anchor = GridBagConstraints.WEST;
        panel.add(pathTextField, gc);

        gc.fill = GridBagConstraints.NONE;
        gc.gridwidth = 1;
        gc.gridx = 3;
        // gc.gridy = 1;
        gc.insets = noPadding;
        gc.anchor = GridBagConstraints.LINE_END;
        panel.add(pathButton, gc);

        ///////////////// Next row ////////////////////////////////////////////
        ////////////Next row ////////////////////////////
        gc.gridy++;

        //   gc.weighty= 10;
        gc.gridwidth = 1;
        gc.gridx = 0;
        gc.insets = rightPadding;
        gc.anchor = GridBagConstraints.LINE_END;
        panel.add(sizeLabel, gc);

        gc.gridwidth = 3;
        gc.gridx = 1;
        //    gc.gridy = 1;
        gc.insets = noPadding;
        gc.anchor = GridBagConstraints.LINE_START;
        panel.add(sizeTextField, gc);

        ///////////////// Next row ////////////////////////////////////////////
        //    gc.weighty= 10;
        gc.gridwidth = 1;
        gc.gridy++;
        gc.gridx = 1;
        gc.insets = noPadding;
        //    gc.anchor = GridBagConstraints.LINE_START; // FIRST_LINE_END
        panel.add(startDownloadButton, gc);

        //  gc.gridy++;
        gc.gridx = 2;
        //    gc.gridy = 1;
        //   gc.anchor = GridBagConstraints.CENTER; // FIRST_LINE_START
        gc.insets = noPadding;
        panel.add(cancelDownloadButton, gc);

        gc.gridx = 3;
        //    gc.gridy = 1;
        //    gc.anchor = GridBagConstraints.LINE_END; // FIRST_LINE_START
        gc.insets = noPadding;
        panel.add(laterDownloadButton, gc);

        add(panel);
    }

    public void setDownloadAskDialogListener(DownloadAskDialogListener downloadAskDialogListener) {
        this.downloadAskDialogListener = downloadAskDialogListener;
    }

    public void setInfo(String url, String downloadNameFile, String path, String size) {
        urlTextField.setText(url);
        downloadNameField.setText(downloadNameFile);
        pathTextField.setText(path);
        sizeTextField.setText(size);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!sizeTextField.getText().equals("")) {
            JButton clicked= (JButton) e.getSource();
            if (downloadAskDialogListener != null) {
                if (clicked == startDownloadButton) {
                    downloadAskDialogListener.startDownloadEventOccured();
                } else if (clicked == cancelDownloadButton) {
                    downloadAskDialogListener.cancelDownloadEventOccured();
                } else if (clicked == laterDownloadButton) {
                    downloadAskDialogListener.laterDownloadEventOccured();
                }
            }
            dispose();
        }
    }
}
