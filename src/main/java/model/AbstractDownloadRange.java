package model;

import enums.ConnectionStatus;
import gui.listener.DownloadRangeStatusListener;

import java.io.File;
import java.net.URL;
import java.util.Vector;

/**
 * Created by Saeed on 10/17/2015.
 */
public abstract class AbstractDownloadRange implements DownloadRange, Runnable {

    // Max size of download buffer.
    protected static final int MAX_BUFFER_SIZE = 1024; // 1024 - 4096

    protected int id;
    protected URL url; // download URL
    protected int number;
    protected int rangeSize; // size of download in bytes
    protected int rangeDownloaded; // number of bytes downloaded
    protected ConnectionStatus connectionStatus; // current status of download

    protected int startRange;
    protected int endRange;

    protected File downloadRangeFile;

    // temp amount read
    protected int read;

    // to quick stop thread
    protected boolean stop = false;

    private Thread thread = null;

    private Vector<DownloadRangeStatusListener> downloadRangeStatusListeners;

    public AbstractDownloadRange(int number, URL url ,File downloadRangeFile, int startRange, int endRange) {
        //     this.id = id;
        this.number = number;
        this.url = url;
        rangeSize = -1;
        rangeDownloaded = 0; //todo for cancel act
        read = 0;
        connectionStatus = ConnectionStatus.CONNECTING;

        this.startRange = startRange;
        this.endRange = endRange;

        this.downloadRangeFile = downloadRangeFile;

        downloadRangeStatusListeners = new Vector<>();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public URL getUrl() {
        return url;
    }

    @Override
    public void setUrl(URL url) {
        this.url = url;
    }

    @Override
    public int getNumber() {
        return number;
    }

    @Override
    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public int getRangeSize() {
        return rangeSize;
    }

    @Override
    public void setRangeSize(int rangeSize) {
        this.rangeSize = rangeSize;
    }

    @Override
    public int getRangeDownloaded() {
        return rangeDownloaded;
    }

    @Override
    public void setRangeDownloaded(int rangeDownloaded) {
        this.rangeDownloaded = rangeDownloaded;
    }

    @Override
    public ConnectionStatus getConnectionStatus() {
        return connectionStatus;
    }

    @Override
    public void setConnectionStatus(ConnectionStatus connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    @Override
    public int getStartRange() {
        return startRange;
    }

    @Override
    public void setStartRange(int startRange) {
        this.startRange = startRange;
    }

    @Override
    public int getEndRange() {
        return endRange;
    }

    @Override
    public void setEndRange(int endRange) {
        this.endRange = endRange;
    }

    @Override
    public File getDownloadRangeFile() {
        return downloadRangeFile;
    }

    @Override
    public void setDownloadRangeFile(File downloadRangeFile) {
        this.downloadRangeFile = downloadRangeFile;
    }

    @Override
    public int getRead() {
        return read;
    }

    /**
     * Adds an DownloadRangeStatusListener to the set of downloadRangeStatusListeners for this object, provided
     * that it is not the same as some DownloadRangeStatusListener already in the set.
     * The order in which notifications will be delivered to multiple
     * downloadRangeStatusListeners is not specified. See the class comment.
     *
     * @param   downloadRangeStatusListener   an DownloadRangeStatusListener to be added.
     * @throws NullPointerException   if the parameter is null.
     */
    public synchronized void addDownloadRangeStatusListener(DownloadRangeStatusListener downloadRangeStatusListener) { // todo may be go to DownloadRange
        if (downloadRangeStatusListener == null)
            throw new NullPointerException();
        if (!downloadRangeStatusListeners.contains(downloadRangeStatusListener)) {
            downloadRangeStatusListeners.addElement(downloadRangeStatusListener);
        }
    }

    /**
     * Deletes an DownloadRangeStatusListener from the set of downloadRangeStatusListeners of this object.
     * Passing <CODE>null</CODE> to this method will have no effect.
     * @param   downloadRangeStatusListener   the DownloadRangeStatusListener to be deleted.
     */
    public synchronized void deleteDownloadRangeStatusListener(DownloadRangeStatusListener downloadRangeStatusListener) {
        downloadRangeStatusListeners.removeElement(downloadRangeStatusListener);
    }


    // Pause this download.
    @Override
    public void disConnect() {
        if (connectionStatus == ConnectionStatus.CONNECTING || connectionStatus == ConnectionStatus.SEND_GET || connectionStatus == ConnectionStatus.RECEIVING_DATA )
            connectionStatus = ConnectionStatus.DISCONNECTING;
        stop = true;
        thread.interrupt();
        stateChanged(0); // TODO two time call and print "disconnect from download .... "
    }

    // Resume this download.
    @Override
    public void resume() {
        if (connectionStatus != ConnectionStatus.COMPLETED) {
            stop = false;
            download();
        }
    }

    // Mark this download as having an error.
    protected void error() {
        connectionStatus = ConnectionStatus.ERROR;
        stateChanged(0);
    }

    protected void download() {
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public abstract void run();

    // Notify observers that this download's status has changed.
    protected void stateChanged(int read) {
        for (DownloadRangeStatusListener downloadRangeStatusListener : downloadRangeStatusListeners)
            downloadRangeStatusListener.downloadStatusChanged(this);
        //     setChanged();
        //     notifyObservers(read);
    }
}
