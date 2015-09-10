package gui.listener;

import enums.DownloadState;

/**
 * Created by Saeed on 9/10/2015.
 */
public interface DownloadPanelListener {
    public void rowSelectedEventOccured();
    public void stateChangedEventOccured(DownloadState downloadState);
}
