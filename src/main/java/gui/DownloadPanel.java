package gui;

import gui.Download.DownloadDialog;
import gui.listener.DownloadDialogListener;
import gui.listener.DownloadInfoListener;
import gui.listener.DownloadPanelListener;
import gui.listener.DownloadStatusListener;
import model.Download;
import model.DownloadRange;
import org.apache.log4j.Logger;

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
public class DownloadPanel extends JPanel implements DownloadDialogListener {

    // Logger
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    // Table showing downloads.
    private JTable downloadTable;

    // Download table's data model.
    private DownloadsTableModel downloadsTableModel;

    // Currently selected download.
  //  private Download selectedDownload;

    private DownloadDialog selectedDownloadDialog;

    // Flag for whether or not table selection is being cleared.
    private boolean clearing;

    private DownloadPanelListener downloadPanelListener;

    private JFrame parent;

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
                    if (!selectedDownloadDialog.isVisible()) {
                        selectedDownloadDialog.setVisible(true);
                    }
                }

            }
        });

        add(new JScrollPane(downloadTable), BorderLayout.CENTER);
    }

    // TODO Maybe used after
 //   public void setDownloads(java.util.List<Download> downloads) {
 //       downloadsTableModel.setDownloads(downloads);
 //   }

    public void addDownload(Download download) {
        selectedDownloadDialog = new DownloadDialog(parent, download);
        downloadsTableModel.addDownloadDialog(selectedDownloadDialog);
        selectedDownloadDialog.setVisible(true);
    }

    public void refresh() {
        downloadsTableModel.fireTableDataChanged();
    }

    // Pause the selected download.
    public void actionPause() {
        selectedDownloadDialog.pause();
    }

    // Resume the selected download.
    public void actionResume() {
        selectedDownloadDialog.resume();
    }

    // Cancel the selected download.
    public void actionCancel() {
        selectedDownloadDialog.cancel();
    }

    // Clear the selected download.
    public void actionClear() {
        clearing = true;
        downloadsTableModel.clearDownload(downloadTable.getSelectedRow());
        clearing = false;
        selectedDownloadDialog = null; //todo just this ...
    }

    // Called when table row selection changes.
    private void tableSelectionChanged() {
    /* Unregister from receiving notifications
       from the last selected download. */
        if (selectedDownloadDialog != null)
            selectedDownloadDialog.deleteDownloadDialogListener(DownloadPanel.this);

    /* If not in the middle of clearing a download,
       set the selected download and register to
       receive notifications from it. */
        if (!clearing && downloadTable.getSelectedRow() > -1) {
            selectedDownloadDialog = downloadsTableModel.getDownloadDialog(downloadTable.getSelectedRow());
            selectedDownloadDialog.addDownloadDialogListener(DownloadPanel.this);
            downloadPanelListener.stateChangedEventOccured(selectedDownloadDialog.getStatus());
        }
    }

    public void setDownloadPanelListener(DownloadPanelListener downloadPanelListener) {
        this.downloadPanelListener = downloadPanelListener;
    }

    public void cancelAllDownload() {
        List<DownloadDialog> downloadDialogList = downloadsTableModel.getDownloadDialogList();
        for (DownloadDialog downloadDialog : downloadDialogList) {
            downloadDialog.cancel();
        }
    }

    @Override
    public void downloadStatusChanged(DownloadDialog downloadDialog) {
        // Update buttons if the selected download has changed.
        if (selectedDownloadDialog != null && selectedDownloadDialog.equals(downloadDialog))
            downloadPanelListener.stateChangedEventOccured(selectedDownloadDialog.getStatus());
    }

}
