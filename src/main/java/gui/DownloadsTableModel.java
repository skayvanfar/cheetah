package gui;

import model.Download;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.*;
import java.util.List;

/**
 * Created by Saeed on 9/10/2015.
 */
public class DownloadsTableModel extends AbstractTableModel implements Observer {

    // These are the names for the table's columns.
    private static final String[] columnNames = {"URL", "Size", "Progress", "Transfer Rate","Status"};

    // These are the classes for each column's values.
    private static final Class[] columnClasses = {String.class, String.class, JProgressBar.class, String.class, String.class};

    // The table's list of downloads.
    // data
    private List<Download> downloadList = new ArrayList<>();

    // TODO Maybe used after
    public void setDownloads(java.util.List<Download> downloads) {
        this.downloadList = downloads;
    }

    // Add a new download to the table.
    public void addDownload(Download download) {
        // Register to be notified when the download changes.
        download.addObserver(this);

        downloadList.add(download);

        // Fire table row insertion notification to table.
        fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
    }

    // Get a download for the specified row.
    public Download getDownload(int row) {
        return (Download) downloadList.get(row);
    }

    // Remove a download from the list.
    public void clearDownload(int row) {
        downloadList.remove(row);

        // Fire table row deletion notification to table.
        fireTableRowsDeleted(row, row);
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
                return download.getUrl();
            case 1: // Size
                String size = download.getSize();
                return (size.equals("-1")) ? "" : size;
            case 2: // Progress
                return new Float(download.getProgress());
            case 3: // Transfer Rate ////////////////////////////////////////
                return download.getTransferRate();
            case 4: // Status
                return download.getStatus().getDesc(); // Download.STATUSES[download.getStatus().ordinal()]
        }
        return null;
    }

    /* Update is called when a Download notifies its
       observers of any changes */
    public void update(Observable o, Object arg) {
        int index = downloadList.indexOf(o);
        // Fire table row update notification to table.
        fireTableRowsUpdated(index, index);
    }


}
