package gui.listener;

import model.DownloadRange;

/**
 * Created by Saeed on 10/17/2015.
 */
public interface DownloadRangeStatusListener {
    public void downloadStatusChanged(DownloadRange downloadRange, int readed);
}
