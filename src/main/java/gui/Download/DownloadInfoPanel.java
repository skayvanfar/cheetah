package gui.Download;

import model.Download;
import model.DownloadRange;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Saeed on 9/14/2015.
 */
public class DownloadInfoPanel extends JPanel {

    // Table showing download ranges.
    private JTable downloadRangeTable;

    // Download Range table's data model.
    private DownloadRangesTableModel downloadRangesTableModel;

    private Download download; // TODO must be Interface. may not needed hear

    public DownloadInfoPanel(Download download) {

        this.download = download;

        setLayout(new BorderLayout());

        // Set up Downloads table.
        downloadRangesTableModel = new DownloadRangesTableModel();

        ////////////////////////////////////////////////////////////////////////////////////////////////////
        // set DonloadRangeList to model of table
        //   downloadRangesTableModel.setDownloadRanges(download.getDownloadRangeList());

////////////////////////////////////////////////////////////////////////////////////////////

        // set DonloadRangeList to model of table
        downloadRangesTableModel.setDownloadRanges(download.getDownloadRangeList());

        downloadRangeTable = new JTable(downloadRangesTableModel);


        add(new JScrollPane(downloadRangeTable), BorderLayout.CENTER);
    }

    public void addDownloadRange(DownloadRange downloadRange) {
        downloadRangesTableModel.addDownloadRange(downloadRange);
    }
}
