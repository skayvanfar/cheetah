package gui;

import enums.DownloadStatus;
import gui.listener.DownloadStatusListener;
import model.Download;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.*;

/**
 * Created by Saeed on 9/10/2015.
 */
public class DownloadsTableModel extends AbstractTableModel implements DownloadStatusListener {

    // These are the names for the table's columns.
    private static final String[] columnNames = {"Name", "Size", "Progress", "Transfer Rate", "Status", "Description"};

    // These are the classes for each column's values.
    private static final Class[] columnClasses = {String.class, String.class, JProgressBar.class, String.class, String.class, String.class};

    // The table's list of downloadDialogs.
    private List<Download> downloadList = new ArrayList<>();

    public List<Download> getDownloadList() {
        return downloadList;
    }

    public DownloadsTableModel() {

    }

    // TODO Maybe used after
    public void setDownloads(List<Download> downloads) {
        for (Download download : downloadList) {
            download.deleteDownloadStatusListener(this);
        }
        downloadList.clear();
        this.downloadList = downloads;
        for (Download download : downloadList) {
            // Register to be notified when the downloadDialog changes.
            download.addDownloadStatusListener(this);
        }
        fireTableDataChanged();
    }

    // Add a new DownloadDialog to the table.
    public void addDownload(Download download) {
        // Register to be notified when the download changes.
        download.addDownloadStatusListener(this);

        downloadList.add(download); //todo must check first

        // Fire table row insertion notification to table.
        fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
    }

    // Get a download for the specified row.
    public Download getDownload(int row) {
        return downloadList.get(row);
    }

    // Remove a download from the list.
    public void clearDownload(Download download) {
        int row = downloadList.indexOf(download);
        //   download.dispose();
        downloadList.remove(row);

        // Fire table row deletion notification to table.
        fireTableRowsDeleted(row, row);
    }

    public void clearDownloads(List<Download> downloads) {
        for (Download download : downloads) {
            int index = downloadList.indexOf(download);
            //    downloadDialog.dispose();
            downloadList.remove(download);
            fireTableRowsDeleted(index, index);
        }
    }

    public List<Download> getDownloadsByStatus(DownloadStatus downloadStatus) {
        List<Download> selectedDownloads = new ArrayList<>();

        for (Download download : downloadList) {
            if (download.getStatus() == downloadStatus) {
                selectedDownloads.add(download);
            }
        }

        return selectedDownloads;
    }

    // Get table's column count.
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    // Get a column's name.
    public String getColumnName(int col) {
        return columnNames[col];
    }

    // Get a column's class.
    public Class<?> getColumnClass(int col) {
        return columnClasses[col];
    }

    // Get table's row count.
    @Override
    public int getRowCount() {
        return downloadList.size();
    }

    // Get value for a specific row and column combination.
    @Override
    public Object getValueAt(int row, int col) {
        Download download = downloadList.get(row);
        switch (col) {
            case 0: // URL
                return download.getDownloadName();
            case 1: // Size
                String size = String.valueOf(download.getFormattedSize());
                return (size.equals("-1")) ? "" : size;
            case 2: // Progress
                return download.getProgress();
            case 3: // Transfer Rate
                return download.getTransferRate();
            case 4: // Status
                return download.getStatus().getDesc(); // Download.STATUSES[download.getStatus().ordinal()]
            case 5: // Status
                return download.getDescription();
        }
        return null;
    }

    /* Update is called when a Download notifies its
       observers of any changes */
    //   public void update(Observable o, Object arg) {
    //      int index = downloadList.indexOf(o);
    // Fire table row update notification to table.
    //      fireTableRowsUpdated(index, index);
    //   }

    @Override
    public void downloadStatusChanged(Download download) { //// TODO ???????????????? use Event
        int index = downloadList.indexOf(download);
        // Fire table row update notification to table.
        fireTableRowsUpdated(index, index);
    }

}
