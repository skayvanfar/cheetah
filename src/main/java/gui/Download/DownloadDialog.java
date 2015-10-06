package gui.Download;

import model.Download;
import model.DownloadRange;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Saeed on 9/14/2015.
 */
public class DownloadDialog extends JDialog {

    private DownloadInfoPanel downloadInfoPanel;
    private DownloadPropertiesPanel downloadPropertiesPanel;
    private JTabbedPane tabbedPane;

    private Download download;////**********

    public Download getDownload() {
        return download;
    }

    public DownloadDialog(JFrame parent, Download download) {
        super(parent, "Download Dialog", false);

        this.download = download;

        setLayout(new BorderLayout());

        downloadInfoPanel = new DownloadInfoPanel(download);
        downloadPropertiesPanel = new DownloadPropertiesPanel();
        tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Download Info", downloadInfoPanel);
        tabbedPane.addTab("Download Properties", downloadPropertiesPanel);

        add(tabbedPane, BorderLayout.CENTER);

        setSize(530, 230);
        setLocationRelativeTo(parent);
    }

    public void addDownloadRange(DownloadRange downloadRange) {
        downloadInfoPanel.addDownloadRange(downloadRange);
    }

    public void setDownloadRanges(List<DownloadRange> downloadRanges) {
        downloadInfoPanel.setDownloadRanges(downloadRanges);
    }

}
