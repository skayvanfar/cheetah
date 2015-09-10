package gui;

import gui.listener.DownloadPanelListener;
import model.Download;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
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

    public DownloadPanel() {

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


        add(new JScrollPane(downloadTable), BorderLayout.CENTER);
    }

    // TODO Maybe used after
    public void setDownloads(java.util.List<Download> downloads) {
        downloadsTableModel.setDownloads(downloads);
    }

    public void addDownload(Download download) {
        downloadsTableModel.addDownload(download);
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
            downloadPanelListener.rowSelectedEventOccured();
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
           //         updateButtons();
                    downloadPanelListener.stateChangedEventOccured(selectedDownload.getStatus());
                }
            });
    }
}
