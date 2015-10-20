package gui;

import controller.DatabaseController;
import controller.DatabaseControllerImpl;
import enums.DownloadCategory;
import enums.DownloadStatus;
import gui.Download.DownloadAskDialog;
import gui.Download.DownloadDialog;
import gui.listener.*;
import model.Download;
import model.DownloadRange;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saeed on 9/10/2015.
 */
public class DownloadPanel extends JPanel implements DownloadInfoListener, DownloadStatusListener {

    // Logger
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    // DatabaseController
    private DatabaseController databaseController;

    // Table showing downloads.
    private JTable downloadTable;

    // Download table's data model.
    private DownloadsTableModel downloadsTableModel;

    // Currently selected download.
    private Download selectedDownload;


    private List<Download> downloadList;

    // List of DownloadDialogs
    private List<DownloadDialog> downloadDialogs;

    private DownloadAskDialog downloadAskDialog;

    private List<String> fileExtensions;
    private DownloadCategory downloadCategory = DownloadCategory.ALL;

    public void addDownloadDialog(DownloadDialog downloadDialog) {
        if (downloadDialog == null)
            throw new NullPointerException();
        if (!downloadDialogs.contains(downloadDialog))
            downloadDialogs.add(downloadDialog);
    }

    private void deleteDownloadDialog(DownloadDialog downloadDialog) {
        downloadDialogs.remove(downloadDialog);
    }

    private DownloadDialog getDownloadDialogByDownload(Download download) {
        for (DownloadDialog downloadDialog : downloadDialogs)
            if (downloadDialog.getDownload().equals(download))
                return downloadDialog;
        return null;
    }

    // Flag for whether or not table selection is being cleared.
    private boolean clearing;

    private DownloadPanelListener downloadPanelListener;

    private JFrame parent;

    public DownloadPanel(JFrame parent, String databasePath) {
        this.parent = parent;
        setLayout(new BorderLayout());

        downloadDialogs = new ArrayList<>();

        String connectionUrl = "jdbc:sqlite:"+ databasePath + File.separator + "chita.db";
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
                DownloadDialog downloadDialog = getDownloadDialogByDownload(selectedDownload);
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

        try {
            downloadList = databaseController.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        DownloadDialog downloadDialog = null;
        for (Download download : downloadList) {
            calculateDownloaded(download);
            download.setDownloadInfoListener(this);
            download.addDownloadStatusListener(this);
            downloadDialog = new DownloadDialog(parent, download);
            downloadDialogs.add(downloadDialog);
            downloadsTableModel.addDownload(download);
            downloadDialog.setDownloadRanges(download.getDownloadRangeList());
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

    public void addDownload(final Download download) {
        download.setDownloadInfoListener(this);

        selectedDownload = download;

        downloadAskDialog = new DownloadAskDialog(parent);
        downloadAskDialog.setInfo(download.getUrl().toString(), download.getDownloadNameFile().getName(), download.getDownloadPath());
        downloadAskDialog.setDownloadAskDialogListener(new DownloadAskDialogListener() {
            @Override
            public void startDownloadEventOccured() {
                if (!downloadList.contains(download)) {
                    downloadList.add(download);
                    setDownloadsByDownloadPath(fileExtensions, downloadCategory);
                }

                DownloadDialog downloadDialog = new DownloadDialog(parent, download);
                if (!downloadDialogs.contains(downloadDialog))
                    downloadDialogs.add(downloadDialog);

                downloadDialog.setVisible(true);

                download.createDownloadRanges();
            }

            @Override
            public void cancelDownloadEventOccured() {
                selectedDownload = null;
            }

            @Override
            public void laterDownloadEventOccured() {

            }
        });
        downloadAskDialog.setVisible(true);
        selectedDownload.resume();
    }

    public int getNextDownloadID() {
        return downloadList.size() + 1;
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
    public void actionPauseAll() {
        List<Download> downloadList = downloadsTableModel.getDownloadList();
        for (Download download : downloadList) {
            if (download.getStatus() == DownloadStatus.DOWNLOADING)
                download.pause();
        }
    }

    // Clear the selected download.
    public void actionClear() {
        if (selectedDownload == null) return;
        //     Download download = selectedDownloadDialog.getDownload();

        clearing = true;
        downloadsTableModel.clearDownload(selectedDownload);
        if (downloadList.contains(selectedDownload))
            downloadList.remove(selectedDownload);
        clearing = false;

        //    selectedDownloadDialog = null;
        DownloadDialog downloadDialog = getDownloadDialogByDownload(selectedDownload);
        downloadDialogs.remove(downloadDialog);
        downloadDialog.dispose();
        downloadDialog = null;


        try {
            databaseController.delete(selectedDownload.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            FileUtils.forceDelete(new File(selectedDownload.getDownloadRangePath() + File.separator + selectedDownload.getDownloadNameFile())); // todo must again
        } catch (IOException e) {
            e.printStackTrace();
        }

        selectedDownload = null;
        tableSelectionChanged();
    }

    // Clear all completed downloads.
    public void actionClearAllCompleted() {

        List<Download> selectedDownloads = downloadsTableModel.getDownloadsByStatus(DownloadStatus.COMPLETE);

        clearing = true;
        downloadsTableModel.clearDownloads(selectedDownloads);
        downloadList.removeAll(selectedDownloads);
        clearing = false;

        try {
            for (Download download : selectedDownloads) {
                if (selectedDownload == download)
                    selectedDownload = null;
                DownloadDialog downloadDialog = getDownloadDialogByDownload(download);
                downloadDialogs.remove(downloadDialog);
                downloadDialog.dispose();
                databaseController.delete(download.getId());
                FileUtils.forceDelete(new File(download.getDownloadRangePath() + File.separator + download.getDownloadNameFile())); // todo must again
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
        if (selectedDownload != null)
            selectedDownload.deleteDownloadStatusListener(DownloadPanel.this);

    /* If not in the middle of clearing a download,
       set the selected download and register to
       receive notifications from it. */
        if (!clearing && downloadTable.getSelectedRow() > -1) {
            selectedDownload = downloadsTableModel.getDownload(downloadTable.getSelectedRow());
            selectedDownload.addDownloadStatusListener(DownloadPanel.this);
            downloadPanelListener.stateChangedEventOccured(selectedDownload.getStatus());
        } else {
            downloadPanelListener.stateChangedEventOccured(null);
        }
    }

    public void setDownloadPanelListener(DownloadPanelListener downloadPanelListener) {
        this.downloadPanelListener = downloadPanelListener;
    }

    @Override
    public void downloadStatusChanged(Download download) {
        // Update buttons if the selected download has changed.
        if (selectedDownload != null && selectedDownload.equals(download))
            downloadPanelListener.stateChangedEventOccured(selectedDownload.getStatus());
    }

    @Override
    public void newDownloadRangeEventOccured(DownloadRange downloadRange) {
        getDownloadDialogByDownload(selectedDownload).addDownloadRange(downloadRange);
    }

    @Override
    public void downloadNeedSaved(Download download) {
        try {
            databaseController.save(download);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void newDownloadInfoGot(final Download download) {
        if (download.getStatus() == DownloadStatus.DOWNLOADING) {
            downloadAskDialog.setInfo(download.getUrl().toString(), download.getDownloadNameFile().getName(), download.getDownloadPath(), download.getFormattedSize(), download.isResumeCapability());
        } else {
            System.out.println("newDownloadInfoGot with error");
            DownloadAskDialog downloadAskDialog = new DownloadAskDialog(parent);
        }
    }

    public void setDownloadsByDownloadPath(List<String> fileExtensions) {
        List<Download> selectedDownloads = new ArrayList<>();
        for (Download download : downloadList)
            for (String downloadPath : fileExtensions)
                if (FilenameUtils.getExtension(download.getDownloadNameFile().getName()).equals(downloadPath))
                    selectedDownloads.add(download);

        downloadsTableModel.setDownloads(selectedDownloads);
    }

    public void setDownloadsByDownloadPath(List<String> fileExtensions, DownloadCategory downloadCategory) { //todo use Sterategy pattern, have bad code
        this.fileExtensions = fileExtensions;
        this.downloadCategory = downloadCategory;
        List<Download> selectedDownloads = new ArrayList<>();
        for (Download download : downloadList) {
            if (fileExtensions != null) {
                for (String downloadPath : fileExtensions) {
                    switch (downloadCategory) {
                        case FINISHED:
                            if (FilenameUtils.getExtension(download.getDownloadNameFile().getName()).equals(downloadPath) && download.getStatus().equals(DownloadStatus.COMPLETE))
                                selectedDownloads.add(download);
                            break;
                        case UNFINISHED:
                            if (FilenameUtils.getExtension(download.getDownloadNameFile().getName()).equals(downloadPath) && !download.getStatus().equals(DownloadStatus.COMPLETE))
                                selectedDownloads.add(download);
                            break;
                        default:
                            if (FilenameUtils.getExtension(download.getDownloadNameFile().getName()).equals(downloadPath))
                                selectedDownloads.add(download);
                    }
                }
            } else {
                switch (downloadCategory) {
                    case FINISHED:
                        if (download.getStatus().equals(DownloadStatus.COMPLETE))
                            selectedDownloads.add(download);
                        break;
                    case UNFINISHED:
                        if (!download.getStatus().equals(DownloadStatus.COMPLETE))
                            selectedDownloads.add(download);
                        break;
                    default:
                        selectedDownloads.add(download);
                }
            }
        }

        downloadsTableModel.setDownloads(selectedDownloads);
    }
}
