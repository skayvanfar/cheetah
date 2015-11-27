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

package gui.Download;

import gui.listener.DownloadPropertiesPanelListener;
import model.Download;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author <a href="kayvanfar.sj@gmail.com">Saeed Kayvanfar</a> 9/14/2015
 */
class DownloadPropertiesPanel extends JPanel {

    private JLabel fileNameLabel;
    private JLabel fileNameValueLabel;
    private JLabel fileTypeLabel;
    private JLabel fileTypeValueLabel;
    private JLabel statusLabel;
    private JLabel statusValueLabel;
    private JLabel sizeLabel;
    private JLabel sizeValueLabel;
    private JLabel urlLabel;
    private JTextField urlTextField;
    private JLabel descriptionLabel;
    private JTextField descriptionTextField;
    private JButton okButton;

    private DownloadPropertiesPanelListener downloadPropertiesPanelListener;

    public void setDownloadPropertiesPanelListener(DownloadPropertiesPanelListener downloadPropertiesPanelListener) {
        this.downloadPropertiesPanelListener = downloadPropertiesPanelListener;
    }

    public DownloadPropertiesPanel(final Download download) {
        setBackground(Color.WHITE);
        fileNameLabel = new JLabel("Name:");
        fileNameValueLabel = new JLabel(download.getDownloadName());
        fileTypeLabel = new JLabel("Type:");
        fileTypeValueLabel = new JLabel();
        statusLabel = new JLabel("Status:");
        statusValueLabel = new JLabel(download.getStatus().getDesc());
        sizeLabel = new JLabel("Size:");
        sizeValueLabel = new JLabel(download.getFormattedSize());
        urlLabel = new JLabel("URL:");
        urlTextField = new JTextField(download.getUrl().toString(), 45);
        descriptionLabel = new JLabel("Description:");
        descriptionTextField = new JTextField(download.getDescription(), 45);
        okButton = new JButton("OK");

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (downloadPropertiesPanelListener != null)
                    downloadPropertiesPanelListener.okButtonClicked(descriptionTextField.getText());
            }
        });

        layoutControls();
    }

    private void layoutControls() {

        JPanel panel = new JPanel();

        panel.setBackground(Color.WHITE);

        panel.setLayout(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();

        Insets rightPadding = new Insets(0, 0, 0, 15);
        Insets noPadding = new Insets(0, 0, 0, 0);

        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        ///////////////// First row ////////////////////////////////////////////
        gc.weightx = 1;
        gc.weighty = 1;

        gc.gridy = 0;
        gc.gridx = 0;
        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = rightPadding;
        panel.add(fileNameLabel, gc);

        gc.gridx = 1;
        gc.gridy = 0;
        gc.insets = noPadding;
        gc.anchor = GridBagConstraints.LINE_START;
        panel.add(fileNameValueLabel, gc);

        ////////////Next row ////////////////////////////
        gc.gridy++;

        gc.gridx = 0;
        gc.insets = rightPadding;
        gc.anchor = GridBagConstraints.LINE_END;
        panel.add(fileTypeLabel, gc);

        gc.gridx = 1;
        gc.insets = noPadding;
        gc.anchor = GridBagConstraints.LINE_START;
        panel.add(fileTypeValueLabel, gc);

        ////////////Next row ////////////////////////////
        gc.gridy++;

        gc.gridx = 0;
        gc.insets = rightPadding;
        gc.anchor = GridBagConstraints.LINE_END;
        panel.add(statusLabel, gc);

        gc.gridx = 1;
        gc.insets = noPadding;
        gc.anchor = GridBagConstraints.LINE_START;
        panel.add(statusValueLabel, gc);

        ////////////Next row ////////////////////////////
        gc.gridy++;

        gc.gridx = 0;
        gc.insets = rightPadding;
        gc.anchor = GridBagConstraints.LINE_END;
        panel.add(sizeLabel, gc);

        gc.gridx = 1;
        gc.insets = noPadding;
        gc.anchor = GridBagConstraints.LINE_START;
        panel.add(sizeValueLabel, gc);
        ///////////////// Next row ////////////////////////////////////////////
        gc.gridy++;

        gc.gridx = 0;
        gc.insets = rightPadding;
        gc.anchor = GridBagConstraints.LINE_END; // FIRST_LINE_END
        panel.add(urlLabel, gc);

        gc.gridx = 1;
        gc.anchor = GridBagConstraints.LINE_START; // FIRST_LINE_START
        gc.insets = noPadding;
        panel.add(urlTextField, gc);

        ///////////////// Next row ////////////////////////////////////////////
        gc.gridy++;

        gc.gridx = 0;
        gc.insets = rightPadding;
        gc.anchor = GridBagConstraints.LINE_END; // FIRST_LINE_END
        panel.add(descriptionLabel, gc);

        gc.gridx = 1;
        gc.anchor = GridBagConstraints.LINE_START; // FIRST_LINE_START
        gc.insets = noPadding;
        panel.add(descriptionTextField, gc);

        ///////////////// Next row ////////////////////////////////////////////
        gc.gridy++;

        gc.gridx = 1;
        gc.insets = rightPadding;
        gc.anchor = GridBagConstraints.LINE_END; // FIRST_LINE_END
        panel.add(okButton, gc);

        add(panel);
    }
}
