package gui;

import controller.DatabaseController;
import controller.DatabaseControllerImpl;
import gui.Download.DownloadDialog;
import gui.listener.*;
import model.Download;
import model.DownloadRange;
import org.apache.log4j.Logger;
import utils.Utils;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.SQLException;
import java.util.*;
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

    public DownloadPanel(JFrame parent) {
        this.parent = parent;
        setLayout(new BorderLayout());

        databaseController = new DatabaseControllerImpl("org.sqlite.JDBC", "jdbc:sqlite:test.db", 0, "", "");

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
            databaseController.connect();
            downloads = databaseController.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        DownloadDialog downloadDialog = null;
        for (Download download : downloads) {
            calculateDownloaded(download);
            downloadDialog = new DownloadDialog(parent, download); //todo may table not show
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

    // TODO Maybe used after
 //   public void setDownloads(java.util.List<Download> downloads) {
 //       downloadsTableModel.setDownloads(downloads);
 //   }

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

 //   public int getNextDownloadRangeID() {
 //       List<Integer> downloadRangeIds = new ArrayList<>();
 //       for (DownloadDialog downloadDialog : downloadsTableModel.getDownloadDialogList()) {
 //           for (DownloadRange downloadRange : downloadDialog.getDownload().getDownloadRangeList()) {
 //               downloadRangeIds.add(downloadRange.getId());
 //           }
 //       }
 //       Integer [] a = (Integer[]) downloadRangeIds.toArray();
 //       return Utils.findMissingNumbers(a, 0);
  //  }

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
        try {
            databaseController.delete(selectedDownloadDialog.getDownload().getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        clearing = false;
        selectedDownloadDialog = null; //todo just this ...
    }

    // Clear all completed downloads.
    public void actionClearAllCompleted() {
        downloadsTableModel.clearAllCompletedDownloads();
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

    @Override
    public void downloadNeedSaved(Download download) {
        System.out.println("DownloadPanel downloadNeedSaved");
        try {
            databaseController.save(download);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
