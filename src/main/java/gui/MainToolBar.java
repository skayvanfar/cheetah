package gui;

import gui.listener.MainToolbarListener;
import utils.Utils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Saeed on 9/10/2015.
 */
class MainToolBar extends JToolBar implements ActionListener {

    private JButton newDownloadButton;
    private JButton pauseAllButton;
    private JButton pauseButton;
    private JButton resumeButton;
    private JButton clearButton;
    private JButton clearAllCompletedButton;
    private JButton preferencesButton;
  //  private JButton schedulerButton;

    private MainToolbarListener mainToolbarListener;

    public MainToolBar() {

        setBorder(BorderFactory.createEtchedBorder());

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("messages/messages"); // NOI18N

        newDownloadButton = new JButton(bundle.getString("mainToolBar.newDownloadButton.label"));
        newDownloadButton.setIcon(Utils.createIcon(bundle.getString("mainToolBar.newDownloadButton.iconPath")));
        newDownloadButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        newDownloadButton.setHorizontalTextPosition(SwingConstants.CENTER);
        newDownloadButton.setToolTipText(bundle.getString("mainToolBar.newDownloadButton.toolTip"));

        resumeButton = new JButton(bundle.getString("mainToolBar.resumeButton.label"));
        resumeButton.setIcon(Utils.createIcon(bundle.getString("mainToolBar.resumeButton.iconPath")));
        resumeButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        resumeButton.setHorizontalTextPosition(SwingConstants.CENTER);
        resumeButton.setToolTipText(bundle.getString("mainToolBar.resumeButton.toolTip"));

        // These are the buttons for managing the selected download.
        pauseButton = new JButton(bundle.getString("mainToolBar.pauseButton.label"));
        pauseButton.setIcon(Utils.createIcon(bundle.getString("mainToolBar.pauseButton.iconPath")));
        pauseButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        pauseButton.setHorizontalTextPosition(SwingConstants.CENTER);
        pauseButton.setToolTipText(bundle.getString("mainToolBar.pauseButton.toolTip"));

        pauseAllButton = new JButton(bundle.getString("mainToolBar.pauseAllButton.label"));
        pauseAllButton.setIcon(Utils.createIcon(bundle.getString("mainToolBar.pauseAllButton.iconPath")));
        pauseAllButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        pauseAllButton.setHorizontalTextPosition(SwingConstants.CENTER);
        pauseAllButton.setToolTipText(bundle.getString("mainToolBar.pauseAllButton.toolTip"));

        clearButton = new JButton(bundle.getString("mainToolBar.clearButton.label"));
        clearButton.setIcon(Utils.createIcon(bundle.getString("mainToolBar.clearButton.iconPath")));
        clearButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        clearButton.setHorizontalTextPosition(SwingConstants.CENTER);
        clearButton.setToolTipText(bundle.getString("mainToolBar.clearButton.toolTip"));

        clearAllCompletedButton = new JButton(bundle.getString("mainToolBar.clearCompletedButton.label"));
        clearAllCompletedButton.setIcon(Utils.createIcon(bundle.getString("mainToolBar.clearCompletedButton.iconPath")));
        clearAllCompletedButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        clearAllCompletedButton.setHorizontalTextPosition(SwingConstants.CENTER);
        clearAllCompletedButton.setToolTipText(bundle.getString("mainToolBar.clearCompletedButton.toolTip"));

        preferencesButton = new JButton(bundle.getString("mainToolBar.preferencesButton.label"));
        preferencesButton.setIcon(Utils.createIcon(bundle.getString("mainToolBar.preferencesButton.iconPath")));
        preferencesButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        preferencesButton.setHorizontalTextPosition(SwingConstants.CENTER);
        preferencesButton.setToolTipText(bundle.getString("mainToolBar.preferencesButton.toolTip"));

        newDownloadButton.addActionListener(this);
        pauseButton.addActionListener(this);
        resumeButton.addActionListener(this);
        pauseAllButton.addActionListener(this);
        clearButton.addActionListener(this);
        clearAllCompletedButton.addActionListener(this);
        preferencesButton.addActionListener(this);

        add(newDownloadButton);
        add(resumeButton);
        add(pauseButton);
        add(pauseAllButton);
        add(clearButton);
        add(clearAllCompletedButton);
        add(preferencesButton);

        resumeButton.setEnabled(false);
        pauseButton.setEnabled(false);
        pauseAllButton.setEnabled(true);
        clearButton.setEnabled(false);
        clearAllCompletedButton.setEnabled(true);
        preferencesButton.setEnabled(true);
    }

    public void setMainToolbarListener(MainToolbarListener mainToolbarListener) {
        this.mainToolbarListener = mainToolbarListener;
    }

    public void setStateOfButtonsControl(boolean pauseButtonState, boolean resumeButtonState, boolean clearButtonState) {
        resumeButton.setEnabled(resumeButtonState);
        pauseButton.setEnabled(pauseButtonState);
        clearButton.setEnabled(clearButtonState);
    }

    // can use inner class instead this on every button
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clicked= (JButton) e.getSource();
        if (mainToolbarListener != null) {
            if (clicked == newDownloadButton) {
                mainToolbarListener.newDownloadEventOccured();
            } else if (clicked == resumeButton) {
                mainToolbarListener.resumeEventOccured();
            } else if (clicked == pauseButton) {
                mainToolbarListener.pauseEventOccured();
            }  else if (clicked == pauseAllButton) {
                mainToolbarListener.pauseAllEventOccured();
            } else if (clicked == clearButton) {
                mainToolbarListener.clearEventOccured();
            } else if (clicked == clearAllCompletedButton) {
                mainToolbarListener.clearAllCompletedEventOccured();
            } else if (clicked == preferencesButton) {
                mainToolbarListener.preferencesEventOccured();
            }
        }
    }
}
