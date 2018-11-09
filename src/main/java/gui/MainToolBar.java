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

import gui.listener.MainToolbarListener;
import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 * @author <a href="kayvanfar.sj@gmail.com">Saeed Kayvanfar</a> 9/10/2015
 */
class MainToolBar extends JToolBar implements ActionListener {

    private JButton newDownloadButton;
    private JButton pauseAllButton;
    private JButton pauseButton;
    private JButton resumeButton;
    private JButton clearButton;
    private JButton clearAllCompletedButton;
    private JButton reJoinButton;
    private JButton reDownloadButton;
    private JButton propertiesButton;
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

        clearAllCompletedButton = new JButton(bundle.getString("mainToolBar.clearAllCompletedButton.label"));
        clearAllCompletedButton.setIcon(Utils.createIcon(bundle.getString("mainToolBar.clearAllCompletedButton.iconPath")));
        clearAllCompletedButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        clearAllCompletedButton.setHorizontalTextPosition(SwingConstants.CENTER);
        clearAllCompletedButton.setToolTipText(bundle.getString("mainToolBar.clearAllCompletedButton.toolTip"));

        reJoinButton = new JButton(bundle.getString("mainToolBar.reJoinButton.label"));
        reJoinButton.setIcon(Utils.createIcon(bundle.getString("mainToolBar.reJoinButton.iconPath")));
        reJoinButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        reJoinButton.setHorizontalTextPosition(SwingConstants.CENTER);
        reJoinButton.setToolTipText(bundle.getString("mainToolBar.reJoinButton.toolTip"));

        reDownloadButton = new JButton(bundle.getString("mainToolBar.reDownloadButton.label"));
        reDownloadButton.setIcon(Utils.createIcon(bundle.getString("mainToolBar.reDownloadButton.iconPath")));
        reDownloadButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        reDownloadButton.setHorizontalTextPosition(SwingConstants.CENTER);
        reDownloadButton.setToolTipText(bundle.getString("mainToolBar.reDownloadButton.toolTip"));

        propertiesButton = new JButton(bundle.getString("mainToolBar.propertiesButton.label"));
        propertiesButton.setIcon(Utils.createIcon(bundle.getString("mainToolBar.propertiesButton.iconPath")));
        propertiesButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        propertiesButton.setHorizontalTextPosition(SwingConstants.CENTER);
        propertiesButton.setToolTipText(bundle.getString("mainToolBar.propertiesButton.toolTip"));

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
        reJoinButton.addActionListener(this);
        reDownloadButton.addActionListener(this);
        propertiesButton.addActionListener(this);
        preferencesButton.addActionListener(this);

        add(newDownloadButton);
        add(resumeButton);
        add(pauseButton);
        add(pauseAllButton);
        add(clearButton);
        add(clearAllCompletedButton);
        add(reJoinButton);
        add(reDownloadButton);
        add(propertiesButton);
        add(preferencesButton);

        setStateOfButtons();
    }

    private void setStateOfButtons() {
        resumeButton.setEnabled(false);
        pauseButton.setEnabled(false);
        pauseAllButton.setEnabled(true);
        clearButton.setEnabled(false);
        clearAllCompletedButton.setEnabled(true);
        reJoinButton.setEnabled(false);
        reDownloadButton.setEnabled(false);
        propertiesButton.setEnabled(false);
        preferencesButton.setEnabled(true);
    }

    public void setMainToolbarListener(MainToolbarListener mainToolbarListener) {
        Objects.requireNonNull(mainToolbarListener, "mainToolbarListener");
        this.mainToolbarListener = mainToolbarListener;
    }

    public void setStateOfButtonsControl(boolean pauseState, boolean resumeState, boolean clearState, boolean reJoinState, boolean reDownloadState, boolean propertiesState) {
        resumeButton.setEnabled(resumeState);
        pauseButton.setEnabled(pauseState);
        clearButton.setEnabled(clearState);
        reJoinButton.setEnabled(reJoinState);
        reDownloadButton.setEnabled(reDownloadState);
        propertiesButton.setEnabled(propertiesState);
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
            } else if (clicked == reJoinButton) {
                mainToolbarListener.reJoinEventOccured();
            } else if (clicked == reDownloadButton) {
                mainToolbarListener.reDownloadEventOccured();
            } else if (clicked == propertiesButton) {
                mainToolbarListener.propertiesEventOccured();
            } else if (clicked == preferencesButton) {
                mainToolbarListener.preferencesEventOccured();
            }
        }
    }
}
