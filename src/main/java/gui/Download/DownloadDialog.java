package gui.Download;

import enums.DownloadStatus;
import gui.listener.DownloadDialogListener;
import gui.listener.DownloadInfoListener;
import gui.listener.DownloadSaveListener;
import model.Download;
import model.DownloadRange;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

/**
 * Created by Saeed on 9/14/2015.
 */
public class DownloadDialog extends JDialog implements DownloadInfoListener, DownloadSaveListener {

    private DownloadInfoPanel downloadInfoPanel;
    private DownloadPropertiesPanel downloadPropertiesPanel;
    private JTabbedPane tabbedPane;

    private Download download;////**********

    private Vector<DownloadDialogListener> downloadDialogListenerList; ////**********

    private DownloadSaveListener downloadSaveListener;

    public Download getDownload() {
        return download;
    }

    /**
     * Adds an DownloadDialogListener to the set of downloadDialogListenerList for this object, provided
     * that it is not the same as some DownloadDialogListener already in the set.
     * The order in which notifications will be delivered to multiple
     * DownloadDialogListeners is not specified. See the class comment.
     *
     * @param   downloadDialogListener   an DownloadDialogListener to be added.
     * @throws NullPointerException   if the parameter o is null.
     */
    public synchronized void addDownloadDialogListener(DownloadDialogListener downloadDialogListener) {
        if (downloadDialogListener == null)
            throw new NullPointerException();
        if (!downloadDialogListenerList.contains(downloadDialogListener)) {
            downloadDialogListenerList.addElement(downloadDialogListener);
        }
    }

    /**
     * Deletes an DownloadDialogListener from the set of downloadDialogListenerList of this object.
     * Passing <CODE>null</CODE> to this method will have no effect.
     * @param   downloadDialogListener   the DownloadDialogListener to be deleted.
     */
    public synchronized void deleteDownloadDialogListener(DownloadDialogListener downloadDialogListener) {
        downloadDialogListenerList.removeElement(downloadDialogListener);
    }

    public void setDownloadSaveListener(DownloadSaveListener downloadSaveListener) {
        this.downloadSaveListener = downloadSaveListener;
    }

    public DownloadDialog(JFrame parent, Download download) {
        super(parent, "Download Dialog", false);

        this.download = download;

        downloadDialogListenerList = new Vector<>();

        download.setDownloadInfoListener(this);

        download.setDownloadSaveListener(this);

        setLayout(new BorderLayout());

        downloadInfoPanel = new DownloadInfoPanel(download);
        downloadPropertiesPanel = new DownloadPropertiesPanel();
        tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Download Info", downloadInfoPanel);
        tabbedPane.addTab("Download Properties", downloadPropertiesPanel);

        add(tabbedPane, BorderLayout.CENTER);

        setSize(530, 230);
        setLocationRelativeTo(parent);

        download.downloadRangeReturned(); ////////////////////////////////////////////////////////////////////////
    }

    // Pause this download.
    public void pause() {
        download.pause();
    }

    // Resume this download.
    public void resume() {
        download.resume();
    }

    public DownloadStatus getStatus() {
        return download.getStatus();
    }


    @Override
    public void newDownloadRangeEventOccured(DownloadRange downloadRange) {
        downloadInfoPanel.addDownloadRange(downloadRange);
    }

    @Override
    public void downloadInfoChanged() {
        for (final DownloadDialogListener downloadDialogListener : downloadDialogListenerList) {
            SwingUtilities.invokeLater(new Runnable() { // togo cut and  past to Download dialog
                public void run() {
                    downloadDialogListener.downloadStatusChanged(DownloadDialog.this);
                }
            });
        }
    }

    @Override
    public void downloadNeedSaved(Download download) {
        if (downloadSaveListener != null) {
            downloadSaveListener.downloadNeedSaved(download);
        }
    }
}
