package gui.Download;

import model.Download;
import model.DownloadRange;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Created by Saeed on 9/14/2015.
 */
public class DownloadDialog extends JDialog {

    private DownloadInfoPanel downloadInfoPanel;
    private DownloadPropertiesPanel downloadPropertiesPanel;

    private Download download;////**********

    private final java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("messages/messages"); // NOI18N

    public Download getDownload() {
        return download;
    }

    public DownloadDialog(JFrame parent, Download download) {
        super(parent, "Download Dialog", false);

        this.download = download;

        setLayout(new BorderLayout());

        downloadInfoPanel = new DownloadInfoPanel(download);
        downloadPropertiesPanel = new DownloadPropertiesPanel(download);
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab(bundle.getString("downloadDialog.tabbedPane.downloadInfoPanel"), downloadInfoPanel);
        tabbedPane.addTab(bundle.getString("downloadDialog.tabbedPane.downloadPropertiesPanel"), downloadPropertiesPanel);

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
