package gui.Download;

import gui.listener.DownloadDialogListener;
import gui.listener.DownloadInfoListener;
import model.Download;
import model.DownloadRange;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

/**
 * Created by Saeed on 9/14/2015.
 */
public class DownloadDialog extends JDialog implements DownloadInfoListener {

    private DownloadInfoPanel downloadInfoPanel;
    private DownloadPropertiesPanel downloadPropertiesPanel;
    private JTabbedPane tabbedPane;

    private Download download; // TODO may not needed hear

 //   private Vector<DownloadDialogListener> downloadDialogListenerList;

    /**
     * Adds an DownloadDialogListener to the set of downloadDialogListenerList for this object, provided
     * that it is not the same as some DownloadDialogListener already in the set.
     * The order in which notifications will be delivered to multiple
     * DownloadDialogListeners is not specified. See the class comment.
     *
     * @param   downloadDialogListener   an DownloadDialogListener to be added.
     * @throws NullPointerException   if the parameter o is null.
     */
  //  public synchronized void addDownloadDialogListener(DownloadDialogListener downloadDialogListener) {
  //      if (downloadDialogListener == null)
  //          throw new NullPointerException();
  //      if (!downloadDialogListenerList.contains(downloadDialogListener)) {
  //          downloadDialogListenerList.addElement(downloadDialogListener);
  //      }
  //  }

    /**
     * Deletes an DownloadDialogListener from the set of downloadDialogListenerList of this object.
     * Passing <CODE>null</CODE> to this method will have no effect.
  //   * @param   downloadDialogListener   the DownloadDialogListener to be deleted.
     */
  //  public synchronized void deleteDownloadDialogListener(DownloadDialogListener downloadDialogListener) {
    //    downloadDialogListenerList.removeElement(downloadDialogListener);
   // }

    public DownloadDialog(JFrame parent, Download download) {
        super(parent, "Download Dialog", false);

        this.download = download;

        download.setDownloadInfoListener(this);

        setLayout(new BorderLayout());

        downloadInfoPanel = new DownloadInfoPanel(download);
        downloadPropertiesPanel = new DownloadPropertiesPanel();
        tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Download Info", downloadInfoPanel);
        tabbedPane.addTab("Download Properties", downloadPropertiesPanel);

        add(tabbedPane, BorderLayout.CENTER);

        setSize(530, 230);
        setLocationRelativeTo(parent);
    }

    @Override
    public void newDownloadRangeEventOccured(DownloadRange downloadRange) {
        downloadInfoPanel.addDownloadRange(downloadRange);
    }

}
