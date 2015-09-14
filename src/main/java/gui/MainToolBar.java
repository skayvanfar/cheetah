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

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("messages/messages"); // NOI18N

        newDownloadButton = new JButton(bundle.getString("mainToolBar.newDownloadButton.label"));
        newDownloadButton.setIcon(Utils.createIcon(bundle.getString("mainToolBar.newDownloadButton.iconPath")));
        newDownloadButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        newDownloadButton.setHorizontalTextPosition(SwingConstants.CENTER);
        newDownloadButton.setToolTipText(bundle.getString("mainToolBar.newDownloadButton.toolTip"));

        // These are the buttons for managing the selected download.
        pauseButton = new JButton(bundle.getString("mainToolBar.pauseButton.label"));
        pauseButton.setIcon(Utils.createIcon(bundle.getString("mainToolBar.pauseButton.iconPath")));
        pauseButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        pauseButton.setHorizontalTextPosition(SwingConstants.CENTER);
        pauseButton.setToolTipText(bundle.getString("mainToolBar.pauseButton.toolTip"));

        resumeButton = new JButton(bundle.getString("mainToolBar.resumeButton.label"));
        resumeButton.setIcon(Utils.createIcon(bundle.getString("mainToolBar.resumeButton.iconPath")));
        resumeButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        resumeButton.setHorizontalTextPosition(SwingConstants.CENTER);
        resumeButton.setToolTipText(bundle.getString("mainToolBar.resumeButton.toolTip"));

        cancelButton = new JButton(bundle.getString("mainToolBar.cancelButton.label"));
        cancelButton.setIcon(Utils.createIcon(bundle.getString("mainToolBar.cancelButton.iconPath")));
        cancelButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        cancelButton.setHorizontalTextPosition(SwingConstants.CENTER);
        cancelButton.setToolTipText(bundle.getString("mainToolBar.cancelButton.toolTip"));

        clearButton = new JButton(bundle.getString("mainToolBar.clearButton.label"));
        clearButton.setIcon(Utils.createIcon(bundle.getString("mainToolBar.clearButton.iconPath")));
        clearButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        clearButton.setHorizontalTextPosition(SwingConstants.CENTER);
        clearButton.setToolTipText(bundle.getString("mainToolBar.clearButton.toolTip"));

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
