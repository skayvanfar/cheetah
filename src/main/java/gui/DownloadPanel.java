package gui;

import comparator.FileNameComparator;
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
import utils.FileUtil;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
public class DownloadPanel extends JPanel implements DownloadInfoListener, DownloadStatusListener, ActionListener {

    // Logger
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private final java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("messages/messages"); // NOI18N

    // DatabaseController
    private DatabaseController databaseController;

    // Table showing downloads.
    private JTable downloadTable;

    private JPopupMenu popup;
    JMenuItem openItem;
    JMenuItem openFolderItem;
    JMenuItem resumeItem;
    JMenuItem pauseItem;
    JMenuItem clearItem;
    JMenuItem reJoinItem;
    JMenuItem reDownloadItem;
    JMenuItem moveToQueueItem;
    JMenuItem removeFromQueueItem;
    JMenuItem propertiesItem;


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
        popup = initPopupMenu();

        downloadTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
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
                         popup.show(downloadTable, e.getX(), e.getY());
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

        DownloadDialog downloadDialog;
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

    private JPopupMenu initPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();

        openItem = new JMenuItem(bundle.getString("downloadPanel.openItem.name"));
        openItem.addActionListener(this);
        openFolderItem = new JMenuItem(bundle.getString("downloadPanel.openFolderItem.name"));
        openFolderItem.addActionListener(this);

        resumeItem = new JMenuItem(bundle.getString("downloadPanel.resumeItem.name"));
        resumeItem.addActionListener(this);
        pauseItem = new JMenuItem(bundle.getString("downloadPanel.pauseItem.name"));
        pauseItem.addActionListener(this);
        clearItem = new JMenuItem(bundle.getString("downloadPanel.clearItem.name"));
        clearItem.addActionListener(this);

        reJoinItem = new JMenuItem(bundle.getString("downloadPanel.reJoinItem.name"));
        reJoinItem.addActionListener(this);
        reDownloadItem = new JMenuItem(bundle.getString("downloadPanel.reDownloadItem.name"));
        reDownloadItem.addActionListener(this);

        moveToQueueItem = new JMenuItem(bundle.getString("downloadPanel.moveToQueueItem.name"));
        moveToQueueItem.addActionListener(this);
        removeFromQueueItem = new JMenuItem(bundle.getString("downloadPanel.removeFromQueueItem.name"));
        removeFromQueueItem.addActionListener(this);

        propertiesItem = new JMenuItem(bundle.getString("downloadPanel.propertiesItem.name"));
        propertiesItem.addActionListener(this);

        popupMenu.add(openItem);
        popupMenu.add(openFolderItem);
        popupMenu.add(new JPopupMenu.Separator());
        popupMenu.add(resumeItem);
        popupMenu.add(pauseItem);
        popupMenu.add(clearItem);
        popupMenu.add(new JPopupMenu.Separator());
        popupMenu.add(reJoinItem);
        popupMenu.add(reDownloadItem);
        popupMenu.add(new JPopupMenu.Separator());
        popupMenu.add(moveToQueueItem);
        popupMenu.add(removeFromQueueItem);
        popupMenu.add(new JPopupMenu.Separator());
        popupMenu.add(propertiesItem);

        return popupMenu;
    }

    public void addDownload(final Download download) {
        download.setDownloadInfoListener(this);

        selectedDownload = download;

        downloadAskDialog = new DownloadAskDialog(parent);

      //  File downloadPath = FileUtil.outputFile(new File(download.getDownloadPath() + File.separator + download.getDownloadName()));
      //  File downloadRangePath = FileUtil.outputFile(new File(download.getDownloadRangePath() + File.separator + download.getDownloadName()));

        File downloadPath = new File(download.getDownloadPath() + File.separator + download.getDownloadName());
        File downloadRangePath = new File(download.getDownloadRangePath() + File.separator + download.getDownloadName());

        List<File> outPutfiles = new ArrayList<>();
        outPutfiles.add(downloadPath);
        outPutfiles.add(downloadRangePath);

        File path = FileUtil.outputFile(outPutfiles, new FileNameComparator());
        System.out.println(path);


        String downloadPathName = download.getDownloadPath() + File.separator + FileUtil.getFileName(path);

        downloadAskDialog.setInfo(download.getUrl().toString(), downloadPathName);
        downloadAskDialog.setDownloadAskDialogListener(new DownloadAskDialogListener() {
            @Override
            public void startDownloadEventOccured(String path) {
                File pathFile = new File(path);
                download.setDownloadPath(pathFile.getParentFile());
                download.setDownloadName(pathFile.getName());
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
            FileUtils.forceDelete(new File(selectedDownload.getDownloadRangePath() + File.separator + selectedDownload.getDownloadName())); // todo must again
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
                FileUtils.forceDelete(new File(download.getDownloadRangePath() + File.separator + download.getDownloadName())); // todo must again
            }
        } catch (SQLException | IOException e) {
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
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (download.getStatus() == DownloadStatus.DOWNLOADING) {
                    downloadAskDialog.setInfo(download.getUrl().toString(), download.getFormattedSize(), download.isResumeCapability());
                } else {
                    System.out.println("newDownloadInfoGot with error");
                    DownloadAskDialog downloadAskDialog = new DownloadAskDialog(parent);
                }
            }
        });
    }

    public void setDownloadsByDownloadPath(List<String> fileExtensions) {
        List<Download> selectedDownloads = new ArrayList<>();
        for (Download download : downloadList)
            for (String downloadPath : fileExtensions)
                if (FilenameUtils.getExtension(download.getDownloadName()).equals(downloadPath))
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
                            if (FilenameUtils.getExtension(download.getDownloadName()).equals(downloadPath) && download.getStatus().equals(DownloadStatus.COMPLETE))
                                selectedDownloads.add(download);
                            break;
                        case UNFINISHED:
                            if (FilenameUtils.getExtension(download.getDownloadName()).equals(downloadPath) && !download.getStatus().equals(DownloadStatus.COMPLETE))
                                selectedDownloads.add(download);
                            break;
                        default:
                            if (FilenameUtils.getExtension(download.getDownloadName()).equals(downloadPath))
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

    @Override
    public void actionPerformed(ActionEvent e) {
        int row = downloadTable.getSelectedRow();
        JMenuItem clicked= (JMenuItem) e.getSource();


            if (clicked == openItem) {
                actionResume();
            } else if (clicked == openFolderItem) {
          //      mainToolbarListener.resumeEventOccured();
            } else if (clicked == resumeItem) {
                actionResume(); // todo may need  mainToolbar.setStateOfButtonsControl(false, false, false); // canceled
            }  else if (clicked == pauseItem) {
                actionPause();
            } else if (clicked == clearItem) {
                int action = JOptionPane.showConfirmDialog(parent, "Do you realy want to delete selected file?", "Confirm delete", JOptionPane.OK_CANCEL_OPTION);
                if (action == JOptionPane.OK_OPTION) {
                    actionClear();
                }
            } else if (clicked == reJoinItem) {
         //       mainToolbarListener.clearAllCompletedEventOccured();
            } else if (clicked == reDownloadItem) {
        //        mainToolbarListener.preferencesEventOccured();
            } else if (clicked == moveToQueueItem) {
        //        mainToolbarListener.preferencesEventOccured();
            } else if (clicked == removeFromQueueItem) {
        //        mainToolbarListener.preferencesEventOccured();
            } else if (clicked == propertiesItem) {
                DownloadDialog downloadDialog = getDownloadDialogByDownload(selectedDownload);
                if (!downloadDialog.isVisible()) {
                    downloadDialog.setVisible(true);
                }
            }

    }
}
