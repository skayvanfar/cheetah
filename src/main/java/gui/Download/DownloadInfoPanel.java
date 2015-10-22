package gui.Download;

import model.Download;
import model.DownloadRange;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Saeed on 9/14/2015.
 */
class DownloadInfoPanel extends JPanel {

    // Table showing download ranges.
    private JTable downloadRangeTable;

    // Download Range table's data model.
    private DownloadRangesTableModel downloadRangesTableModel;

    private Download download;

    public DownloadInfoPanel(Download download) {

        this.download = download;

        setLayout(new BorderLayout());

        // Set up Downloads table.
        downloadRangesTableModel = new DownloadRangesTableModel();

        downloadRangeTable = new JTable(downloadRangesTableModel);
        downloadRangeTable.setShowGrid(false);

        setColumnWidths();

        add(new JScrollPane(downloadRangeTable), BorderLayout.CENTER);
    }

    public void addDownloadRange(DownloadRange downloadRange) {

        downloadRangesTableModel.addDownloadRange(downloadRange);
    }

    public void setDownloadRanges(java.util.List<DownloadRange> downloadRanges) {
        downloadRangesTableModel.setDownloadRanges(downloadRanges);
    }

    private void setColumnWidths(){
        downloadRangeTable.getColumnModel().getColumn(0).setPreferredWidth(56);
        downloadRangeTable.getColumnModel().getColumn(0).setMaxWidth(70);
        downloadRangeTable.getColumnModel().getColumn(1).setPreferredWidth(70);
        downloadRangeTable.getColumnModel().getColumn(1).setMaxWidth(150);

        downloadRangeTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
    }
}
