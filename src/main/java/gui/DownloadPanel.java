package gui;

import controller.DatabaseController;
import controller.DatabaseControllerImpl;
import enums.DownloadStatus;
import gui.Download.DownloadDialog;
import gui.listener.*;
import model.Download;
import model.DownloadRange;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import utils.ConnectionUtil;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Saeed on 9/10/2015.
 */
public class DownloadPanel extends JPanel implements DownloadDialogListener, DownloadSaveListener {

    // Logger
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    // DatabaseController
    private DatabaseController databaseController;

    // Table showing downloads.
    private JTable downloadTable;

    // Download table's data model.
    private DownloadsTableModel downloadsTableModel;

    // Currently selected downloadDialog.
    private DownloadDialog selectedDownloadDialog;

    // Flag for whether or not table selection is being cleared.
    private boolean clearing;

    private DownloadPanelListener downloadPanelListener;

    private JFrame parent;

    public DownloadPanel(JFrame parent, String databasePath) {
        this.parent = parent;
        setLayout(new BorderLayout());


        String connectionUrl = "jdbc:sqlite:"+ databasePath + File.separator + "test.db";
        databaseController = new DatabaseControllerImpl("org.sqlite.JDBC", connectionUrl, 0, "", "");

        try {
            databaseController.createTablesIfNotExist();
        } catch (SQLException e) {
            e.printStackTrace();
        }

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

        List<Download> downloads = null;
        try {
     //       databaseController.connect();
            downloads = databaseController.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        DownloadDialog downloadDialog = null;
        for (Download download : downloads) {
            calculateDownloaded(download);
            downloadDialog = new DownloadDialog(parent, download);
            downloadsTableModel.addDownloadDialog(downloadDialog);
        }

    }

    private void calculateDownloaded(Download download) {
        int downloaded = 0;
        for (DownloadRange downloadRange : download.getDownloadRangeList()) {
            long rangeDownloaded = downloadRange.getDownloadRangeFile().length();
            downloadRange.setRangeDownloaded((int) rangeDownloaded);
            downloaded += rangeDownloaded;
        }
        download.setDownloaded(downloaded);
    }

    public void addDownload(Download download) {
        selectedDownloadDialog = new DownloadDialog(parent, download);
        selectedDownloadDialog.setDownloadSaveListener(this);
        downloadsTableModel.addDownloadDialog(selectedDownloadDialog);

        selectedDownloadDialog.setVisible(true);

        selectedDownloadDialog.resume();
    }

    public int getNextDownloadID() {
        List<DownloadDialog> downloadDialogs = downloadsTableModel.getDownloadDialogList();
        return downloadDialogs.size() + 1;
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
    public void actionPauseAll() {
        List<DownloadDialog> downloadDialogList = downloadsTableModel.getDownloadDialogList();
        for (DownloadDialog downloadDialog : downloadDialogList) {
            if (downloadDialog.getStatus() == DownloadStatus.DOWNLOADING)
                downloadDialog.pause();
        }
    }

    // Clear the selected download.
    public void actionClear() {
        if (selectedDownloadDialog == null) return;
        Download download = selectedDownloadDialog.getDownload();

        clearing = true;
        downloadsTableModel.clearDownloadDialog(selectedDownloadDialog);
        clearing = false;

        selectedDownloadDialog = null;

        try {
            databaseController.delete(download.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
             FileUtils.forceDelete(new File(download.getDownloadRangePath() + File.separator + ConnectionUtil.getFileName(new URL(download.getUrl())))); // todo must again
        } catch (IOException e) {
             e.printStackTrace();
        }
        tableSelectionChanged();
    }

    // Clear all completed downloads.
    public void actionClearAllCompleted() {

        List<DownloadDialog> selectedDownloadDialogs = downloadsTableModel.getDownloadDialogsByStatus(DownloadStatus.COMPLETE);

        clearing = true;
        downloadsTableModel.clearDownloadDialogs(selectedDownloadDialogs);
        clearing = false;

        try {
            for (DownloadDialog downloadDialog : selectedDownloadDialogs) {
                if (selectedDownloadDialog == downloadDialog)
                    selectedDownloadDialog = null;
                databaseController.delete(downloadDialog.getDownload().getId());
                FileUtils.forceDelete(new File(downloadDialog.getDownload().getDownloadRangePath() + File.separator + ConnectionUtil.getFileName(new URL(downloadDialog.getDownload().getUrl())))); // todo must again
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        tableSelectionChanged();
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
        } else {
            downloadPanelListener.stateChangedEventOccured(null);
        }
    }

    public void setDownloadPanelListener(DownloadPanelListener downloadPanelListener) {
        this.downloadPanelListener = downloadPanelListener;
    }

    @Override
    public void downloadStatusChanged(DownloadDialog downloadDialog) {
        // Update buttons if the selected download has changed.
        if (selectedDownloadDialog != null && selectedDownloadDialog.equals(downloadDialog))
            downloadPanelListener.stateChangedEventOccured(selectedDownloadDialog.getStatus());
    }

    @Override
    public void downloadNeedSaved(Download download) {
        try {
            databaseController.save(download);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
