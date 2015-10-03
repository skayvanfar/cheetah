package gui.Download;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Saeed on 10/3/2015.
 */
public class DownloadAskDialog extends JDialog {

    private JLabel urlLabel;
    private JTextField urlTextField;
    private JLabel sizeLabel;
    private JTextField sizeTextField;
    private JButton startDownloadButton;
    private JButton cancelDownloadButton;
    private JButton downloadLaterButton;

    public DownloadAskDialog(JFrame parent) {
        super(parent, "Download File", false);

        urlLabel = new JLabel("URL:");
        urlTextField = new JTextField(35);
        sizeLabel = new JLabel("Size:");
        sizeTextField = new JTextField(5);
        startDownloadButton = new JButton("Start Download");
        cancelDownloadButton = new JButton("Cancel Download");
        downloadLaterButton = new JButton("Download Later");

        layoutControls();
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
        add(sizeLabel, gc);

        gc.gridx = 1;
        gc.gridy = 1;
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
        gc.anchor = GridBagConstraints.LINE_START; // FIRST_LINE_START
        gc.insets = noPadding;
        add(cancelDownloadButton, gc);
    }
}
