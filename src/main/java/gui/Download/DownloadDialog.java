package gui.Download;

import gui.listener.DownloadInfoListener;
import model.Download;
import model.DownloadRange;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Saeed on 9/14/2015.
 */
public class DownloadDialog extends JDialog implements DownloadInfoListener {

    private DownloadInfoPanel downloadInfoPanel;
    private DownloadPropertiesPanel downloadPropertiesPanel;
    private JTabbedPane tabbedPane;

    private Download download; // TODO may not needed hear

    public DownloadDialog(Download download) {
     //   super(parent, "Download Dialog", false);

        this.download = download;

        download.setDownloadInfoListener(this);

        setLayout(new BorderLayout());

        downloadInfoPanel = new DownloadInfoPanel(download);
        downloadPropertiesPanel = new DownloadPropertiesPanel();
        tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Download Info", downloadInfoPanel);
        tabbedPane.addTab("Download Properties", downloadPropertiesPanel);

        add(tabbedPane, BorderLayout.CENTER);

        setSize(320, 230);
   //     setLocationRelativeTo(parent);
    }

    @Override
    public void newDownloadRangeEventOccured(DownloadRange downloadRange) {
        downloadInfoPanel.addDownloadRange(downloadRange);
    }
}
