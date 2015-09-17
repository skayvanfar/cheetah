package gui;

import gui.Download.DownloadDialog;
import gui.listener.DownloadInfoListener;
import gui.listener.DownloadPanelListener;
import gui.listener.DownloadStatusListener;
import model.Download;
import model.DownloadRange;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

/**
 * Created by Saeed on 9/10/2015.
 */
public class DownloadPanel extends JPanel implements Observer {

    // Table showing downloads.
    private JTable downloadTable;

    // Download table's data model.
    private DownloadsTableModel downloadsTableModel;

    // Currently selected download.
    private Download selectedDownload;

    // Flag for whether or not table selection is being cleared.
    private boolean clearing;

    private DownloadPanelListener downloadPanelListener;

    private JFrame parent;

    private DownloadDialog downloadDialog;

    public DownloadPanel(JFrame parent) {
        this.parent = parent;
        setLayout(new BorderLayout());

        // Set up Downloads table.
        downloadsTableModel = new DownloadsTableModel();
        downloadTable = new JTable(downloadsTableModel);

        downloadTable.getSelectionModel().addListSelectionListener(new
        ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                tableSelectionChanged();
            }
        });
        // Allow only one row at a time to be selected.
        downloadTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        // Set up ProgressBar as renderer for progress column.
        ProgressRenderer renderer = new ProgressRenderer(0, 100);
        renderer.setStringPainted(true); // show progress text
        downloadTable.setDefaultRenderer(JProgressBar.class, renderer);

        // Set table's row height large enough to fit JProgressBar.
        downloadTable.setRowHeight((int) renderer.getPreferredSize().getHeight());

        downloadTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                int row = downloadTable.rowAtPoint(e.getPoint());
                downloadTable.getSelectionModel().setSelectionInterval(row, row);
                if (e.getButton() == MouseEvent.BUTTON3) { // TODO right click
                    //     popup.show(table, e.getX(), e.getY());
                } else  if (e.getClickCount() == 2) {  // double click
                    if (!downloadDialog.isVisible()) {
                        downloadDialog.setVisible(true);
                    }
                }

            }
        });

        add(new JScrollPane(downloadTable), BorderLayout.CENTER);
    }

    // TODO Maybe used after
    public void setDownloads(java.util.List<Download> downloads) {
        downloadsTableModel.setDownloads(downloads);
    }

    public void addDownload(Download download) {
        downloadDialog = new DownloadDialog(parent, download);
        downloadsTableModel.addDownload(download);
        downloadDialog.setVisible(true);
    }

    public void refresh() {
        downloadsTableModel.fireTableDataChanged();
    }

    // Pause the selected download.
    public void actionPause() {
        selectedDownload.pause();
    }

    // Resume the selected download.
    public void actionResume() {
        selectedDownload.resume();
    }

    // Cancel the selected download.
    public void actionCancel() {
        selectedDownload.cancel();
    }

    // Clear the selected download.
    public void actionClear() {
        clearing = true;
        downloadsTableModel.clearDownload(downloadTable.getSelectedRow());
        clearing = false;
        selectedDownload = null;
    }

    // Called when table row selection changes.
    private void tableSelectionChanged() {
    /* Unregister from receiving notifications
       from the last selected download. */
        if (selectedDownload != null)
            selectedDownload.deleteObserver(DownloadPanel.this);

    /* If not in the middle of clearing a download,
       set the selected download and register to
       receive notifications from it. */
        if (!clearing && downloadTable.getSelectedRow() > -1) {
            selectedDownload =
                    downloadsTableModel.getDownload(downloadTable.getSelectedRow());
            selectedDownload.addObserver(DownloadPanel.this);
            downloadPanelListener.stateChangedEventOccured(selectedDownload.getStatus());
        }
    }

    public void setDownloadPanelListener(DownloadPanelListener downloadPanelListener) {
        this.downloadPanelListener = downloadPanelListener;
    }

    /* Update is called when a Download notifies its
       observers of any changes. */
    @Override
    public void update(Observable o, Object arg) {
        // Update buttons if the selected download has changed.
        if (selectedDownload != null && selectedDownload.equals(o))
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    downloadPanelListener.stateChangedEventOccured(selectedDownload.getStatus());
                }
            });
    }

    public void cancelAllDownload() {
        List<Download> downloadList = downloadsTableModel.getDownloadList();
        for (Download download : downloadList) {
            download.cancel();
        }
    }

 //   @Override TODO can use a EventListener instead observer fro better performance, see DownloadStatusListener
///   public void downloadStatusChanged(Download download) {
        // Update buttons if the selected download has changed.
 //       if (selectedDownload != null && selectedDownload.equals(download))
  //          SwingUtilities.invokeLater(new Runnable() {
  //              public void run() {
  //                  downloadPanelListener.stateChangedEventOccured(selectedDownload.getStatus());
   //             }
   //         });
  //  }
}
