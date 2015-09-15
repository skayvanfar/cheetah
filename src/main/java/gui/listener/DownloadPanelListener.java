package gui.listener;

import enums.DownloadStatus;

/**
 * Created by Saeed on 9/10/2015.
 */
public interface DownloadPanelListener {
    public void stateChangedEventOccured(DownloadStatus downloadState);
}
