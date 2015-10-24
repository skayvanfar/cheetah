package model;

import enums.ConnectionStatus;
import gui.listener.DownloadRangeStatusListener;

import java.io.File;
import java.net.URL;

/**
 * Created by Saeed on 10/17/2015.
 */
public interface DownloadRange {

    public int getId();

    public void setId(int id);

    public URL getUrl();

    public void setUrl(URL url);

    public int getNumber();

    public void setNumber(int number);

    public int getRangeSize();

    public void setRangeSize(int rangeSize);

    public int getRangeDownloaded();

    public void setRangeDownloaded(int rangeDownloaded);

    public ConnectionStatus getConnectionStatus();

    public void setConnectionStatus(ConnectionStatus connectionStatus);

    public int getStartRange();

    public void setStartRange(int startRange);

    public int getEndRange();

    public void setEndRange(int endRange);

    public File getDownloadRangeFile();

    public void setDownloadRangeFile(File downloadRangeFile);

    /**
     * Adds an DownloadRangeStatusListener to the set of downloadRangeStatusListeners for this object, provided
     * that it is not the same as some DownloadRangeStatusListener already in the set.
     * The order in which notifications will be delivered to multiple
     * downloadRangeStatusListeners is not specified. See the class comment.
     *
     * @param   downloadRangeStatusListener   an DownloadRangeStatusListener to be added.
     * @throws NullPointerException   if the parameter is null.
     */
    public void addDownloadRangeStatusListener(DownloadRangeStatusListener downloadRangeStatusListener);

    /**
     * Deletes an DownloadRangeStatusListener from the set of downloadRangeStatusListeners of this object.
     * Passing <CODE>null</CODE> to this method will have no effect.
     * @param   downloadRangeStatusListener   the DownloadRangeStatusListener to be deleted.
     */
    public void deleteDownloadRangeStatusListener(DownloadRangeStatusListener downloadRangeStatusListener);

    // Pause this download.
    public void disConnect();

    // Resume this download.
    public void resume();
}
