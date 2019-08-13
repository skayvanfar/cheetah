/*
 * Cheetah - A Free Fast Downloader
 *
 * Copyright © 2015 Saeed Kayvanfar
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package model;

import concurrent.MyThreadFactory;
import concurrent.TimingThreadPool;
import enums.ConnectionStatus;
import gui.listener.DownloadRangeStatusListener;
import ir.sk.concurrencyutils.annotation.NotThreadSafe;

import java.io.File;
import java.net.URL;
import java.util.Objects;
import java.util.Vector;
import java.util.concurrent.*;

/**
 * Skeletal Implementation of DownloadRange interface.
 *
 * @author <a href="kayvanfar.sj@gmail.com">Saeed Kayvanfar</a> 10/17/2015
 * @see model.DownloadRange
 */
@NotThreadSafe
public abstract class AbstractDownloadRange implements DownloadRange, Callable<Void> {

    // Max size of download buffer.
    protected static final int MAX_BUFFER_SIZE = 1024; // 1024 - 4096

    private final static int N_CPUS = Runtime.getRuntime().availableProcessors();
  //  private final static ExecutorService downloadRangeExec = Executors.newFixedThreadPool(N_CPUS + 1);

    protected final static ThreadPoolExecutor downloadRangeExec = new TimingThreadPool(N_CPUS, N_CPUS,
            0L, java.util.concurrent.TimeUnit.MICROSECONDS,
            new LinkedBlockingQueue<>(), new MyThreadFactory("downloadRangeExec"),
            new ThreadPoolExecutor.CallerRunsPolicy());

    protected int id;
    protected URL url; // download URL
    protected int number;
    protected int rangeSize; // size of download in bytes
    protected int rangeDownloaded; // number of bytes downloaded
    protected volatile ConnectionStatus connectionStatus; // current status of download

    protected int startRange;
    protected int endRange;

    protected File downloadRangeFile;

    protected int connectTimeout;
    protected int readTimeout;

    // to quick stop thread
    protected boolean stop = false;

    protected Thread thread = null;

    private Vector<DownloadRangeStatusListener> downloadRangeStatusListeners;

    public AbstractDownloadRange(int number, URL url ,File downloadRangeFile, int startRange, int endRange) {
        //     this.id = id;
        this.number = number;
        this.url = url;
        rangeSize = -1;
        rangeDownloaded = 0; //todo for cancel act
        connectionStatus = ConnectionStatus.CONNECTING;

        this.startRange = startRange;
        this.endRange = endRange;

        this.downloadRangeFile = downloadRangeFile;

        downloadRangeStatusListeners = new Vector<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setId(int id) {
        this.id = id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public URL getUrl() {
        return url;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUrl(URL url) {
        this.url = url;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNumber() {
        return number;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getRangeSize() {
        return rangeSize;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setRangeSize(int rangeSize) {
        this.rangeSize = rangeSize;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getRangeDownloaded() {
        return rangeDownloaded;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setRangeDownloaded(int rangeDownloaded) {
        this.rangeDownloaded = rangeDownloaded;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConnectionStatus getConnectionStatus() {
        return connectionStatus;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setConnectionStatus(ConnectionStatus connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getStartRange() {
        return startRange;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStartRange(int startRange) {
        this.startRange = startRange;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getEndRange() {
        return endRange;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEndRange(int endRange) {
        this.endRange = endRange;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File getDownloadRangeFile() {
        return downloadRangeFile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDownloadRangeFile(File downloadRangeFile) {
        this.downloadRangeFile = downloadRangeFile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getConnectTimeout() {
        return connectTimeout;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getReadTimeout() {
        return readTimeout;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void addDownloadRangeStatusListener(DownloadRangeStatusListener downloadRangeStatusListener) { // todo may be go to DownloadRange
        Objects.requireNonNull(downloadRangeStatusListener, "downloadRangeStatusListener");
        if (!downloadRangeStatusListeners.contains(downloadRangeStatusListener)) {
            downloadRangeStatusListeners.addElement(downloadRangeStatusListener);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void deleteDownloadRangeStatusListener(DownloadRangeStatusListener downloadRangeStatusListener) {
        Objects.requireNonNull(downloadRangeStatusListener, "downloadRangeStatusListener");
        downloadRangeStatusListeners.removeElement(downloadRangeStatusListener);
    }


    // Pause this download.
    @Override
    public void disConnect() {
        if (connectionStatus == ConnectionStatus.CONNECTING || connectionStatus == ConnectionStatus.SEND_GET || connectionStatus == ConnectionStatus.RECEIVING_DATA )
            connectionStatus = ConnectionStatus.DISCONNECTING;
        stop = true;
   //     thread.interrupt();
        stateChanged(0); // TODO two time call and print "disconnect from download .... "
        downloadRangeExec.shutdown();
    }

    /**
     * {@inheritDoc}
     */
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
        downloadRangeExec.shutdown();
    }

    protected void download() {
        downloadRangeExec.submit(this);
    }

    @Override
    public abstract Void call();

    // Notify observers that this download's status has changed.
    protected void stateChanged(int readed) {
        for (DownloadRangeStatusListener downloadRangeStatusListener : downloadRangeStatusListeners)
            downloadRangeStatusListener.downloadStatusChanged(this, readed);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetData() {
        rangeSize = -1;
        rangeDownloaded = 0;
        connectionStatus = ConnectionStatus.CONNECTING;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractDownloadRange that = (AbstractDownloadRange) o;
        return id == that.id &&
                number == that.number &&
                rangeSize == that.rangeSize &&
                rangeDownloaded == that.rangeDownloaded &&
                startRange == that.startRange &&
                endRange == that.endRange &&
                Objects.equals(url, that.url) &&
                connectionStatus == that.connectionStatus &&
                Objects.equals(downloadRangeFile, that.downloadRangeFile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url, number, rangeSize, rangeDownloaded, connectionStatus, startRange, endRange, downloadRangeFile);
    }

    @Override
    public String toString() {
        return "AbstractDownloadRange{" +
                "id=" + id +
                ", url=" + url +
                ", number=" + number +
                ", rangeSize=" + rangeSize +
                ", rangeDownloaded=" + rangeDownloaded +
                ", connectionStatus=" + connectionStatus +
                ", startRange=" + startRange +
                ", endRange=" + endRange +
                ", downloadRangeFile=" + downloadRangeFile +
                '}';
    }
}
