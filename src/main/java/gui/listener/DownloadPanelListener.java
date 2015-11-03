package gui.listener;

import enums.DownloadStatus;
import model.Download;

/**
 * Created by Saeed on 9/10/2015.
 */
public interface DownloadPanelListener {
    public void stateChangedEventOccured(DownloadStatus downloadState);
    public void downloadSelected(Download download);
}
