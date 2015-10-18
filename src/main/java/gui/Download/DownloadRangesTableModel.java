package gui.Download;

import gui.listener.DownloadRangeStatusListener;
import model.DownloadRange;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Saeed on 9/14/2015.
 */
public class DownloadRangesTableModel extends AbstractTableModel implements DownloadRangeStatusListener {

    // These are the names for the table's columns.
    private static final String[] columnNames = {"Number", "Downloaded", "Status"};

    // These are the classes for each column's values.
    private static final Class[] columnClasses = {String.class, String.class, String.class};

    // The table's list of downloadRanges.
    private List<DownloadRange> downloadRangeList = new ArrayList<>();

    // TODO Maybe used after
    public void setDownloadRanges(java.util.List<DownloadRange> downloadRanges) {
        for (DownloadRange downloadRange : downloadRangeList) {
            downloadRange.deleteDownloadRangeStatusListener(this);
        }
        downloadRangeList.clear();
        this.downloadRangeList = downloadRanges;
        for (DownloadRange downloadRange : downloadRangeList) {
            // Register to be notified when the download changes.
            downloadRange.addDownloadRangeStatusListener(this);
        }
    }

    // Add a new download to the table.
    public void addDownloadRange(DownloadRange downloadRange) {
        if (!downloadRangeList.contains(downloadRange)) {
            // Register to be notified when the download changes.downloadRange.addObserver(this);
            downloadRange.addDownloadRangeStatusListener(this);

            downloadRangeList.add(downloadRange);

            // Fire table row insertion notification to table.
            fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
        }
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
        return downloadRangeList.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        DownloadRange downloadRange = downloadRangeList.get(rowIndex);
        switch (columnIndex) {
            case 0: // Number
                return downloadRange.getNumber();
            case 1: // Downloaded
                return downloadRange.getRangeDownloaded();
            case 2: // Status
                return downloadRange.getConnectionStatus().getDesc(); // Download.STATUSES[download.getStatus().ordinal()]
        }
        return null;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#isCellEditable(int, int)
     */
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }


    /* is called when a DownloadRange notifies its
      listeners of any changes */
    @Override
    public void downloadStatusChanged(DownloadRange downloadRange) {
        int index = downloadRangeList.indexOf(downloadRange);

        // Fire table row update notification to table.
        fireTableRowsUpdated(index, index);
    }
}
