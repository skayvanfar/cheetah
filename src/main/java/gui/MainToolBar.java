package gui;

import gui.listener.MainToolbarListener;
import utils.Utils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Saeed on 9/10/2015.
 */
public class MainToolBar extends JToolBar implements ActionListener {

    private JButton newDownloadButton;
    private JButton pauseButton;
    private JButton resumeButton;
    private JButton cancelButton;
    private JButton clearButton;

    private MainToolbarListener mainToolbarListener;

    public MainToolBar() {
        setBorder(BorderFactory.createEtchedBorder());

        newDownloadButton = new JButton("Add URL");
        newDownloadButton.setIcon(Utils.createIcon("/images/primo/label_blue_new.png"));
        newDownloadButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        newDownloadButton.setHorizontalTextPosition(SwingConstants.CENTER);
        newDownloadButton.setToolTipText("Add URL");

        // These are the buttons for managing the selected download.
        pauseButton = new JButton("Pause");
        pauseButton.setIcon(Utils.createIcon("/images/primo/button_blue_pause.png"));
        pauseButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        pauseButton.setHorizontalTextPosition(SwingConstants.CENTER);
        pauseButton.setToolTipText("Pause");

        resumeButton = new JButton("Resume");
        resumeButton.setIcon(Utils.createIcon("/images/primo/others/download.png"));
        resumeButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        resumeButton.setHorizontalTextPosition(SwingConstants.CENTER);
        resumeButton.setToolTipText("Resume");

        cancelButton = new JButton("Cancel");
        cancelButton.setIcon(Utils.createIcon("/images/primo/button_blue_stop.png"));
        cancelButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        cancelButton.setHorizontalTextPosition(SwingConstants.CENTER);
        cancelButton.setToolTipText("Cancel");

        clearButton = new JButton("Clear");
        clearButton.setIcon(Utils.createIcon("/images/primo/others/delete.png"));
        clearButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        clearButton.setHorizontalTextPosition(SwingConstants.CENTER);
        clearButton.setToolTipText("Clear");

        newDownloadButton.addActionListener(this);
        pauseButton.addActionListener(this);
        resumeButton.addActionListener(this);
        cancelButton.addActionListener(this);
        clearButton.addActionListener(this);

        add(newDownloadButton);
        add(pauseButton);
        add(resumeButton);
        add(cancelButton);
        add(clearButton);

        pauseButton.setEnabled(false);
        resumeButton.setEnabled(false);
        cancelButton.setEnabled(false);
        clearButton.setEnabled(false);
    }

    public void setMainToolbarListener(MainToolbarListener mainToolbarListener) {
        this.mainToolbarListener = mainToolbarListener;
    }

    public void setStateOfButtonsControl(boolean pauseButtonState, boolean resumeButtonState, boolean cancelButtonState, boolean clearButtonState) {
        pauseButton.setEnabled(pauseButtonState);
        resumeButton.setEnabled(resumeButtonState);
        cancelButton.setEnabled(cancelButtonState);
        clearButton.setEnabled(clearButtonState);
    }

    // can use inner class instead this on every button
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clicked= (JButton) e.getSource();
        if (mainToolbarListener != null) {
            if (clicked == newDownloadButton) {
                mainToolbarListener.newDownloadEventOccured();
            } else if (clicked == pauseButton) {
                mainToolbarListener.pauseEventOccured();
            } else if (clicked == resumeButton) {
                mainToolbarListener.resumeEventOccured();
            } else if (clicked == cancelButton) {
                mainToolbarListener.cancelEventOccured();
            } else if (clicked == clearButton) {
                mainToolbarListener.clearEventOccured();
            }
        }
    }
}
