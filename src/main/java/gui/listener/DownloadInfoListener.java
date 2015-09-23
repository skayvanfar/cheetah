package gui.listener;

import model.DownloadRange;

/**
 * Created by Saeed on 9/15/2015.
 */
public interface DownloadInfoListener { // TODO may not need this, may can use Download that implement Observable
    public void newDownloadRangeEventOccured(DownloadRange downloadRange);
    public void downloadInfoChanged(); // == state Chnaged
}
