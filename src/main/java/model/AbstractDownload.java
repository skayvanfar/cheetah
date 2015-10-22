package model;

import enums.*;
import gui.listener.DownloadInfoListener;
import gui.listener.DownloadRangeStatusListener;
import gui.listener.DownloadStatusListener;
import utils.ConnectionUtil;
import utils.FileUtil;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Saeed on 10/17/2015.
 */
public abstract class AbstractDownload implements Download, Runnable, DownloadRangeStatusListener {

    protected int id;
    protected URL url; // download URL
    protected String downloadName;
    protected int size; // size of download in bytes
    protected DownloadStatus status; // current status of download
    protected String transferRate; // rate of transfer
    protected ProtocolType protocolType;

    protected int downloaded; // number of bytes downloaded

    protected int responseCode;
    protected int partCount;

    protected File downloadPath;
    protected File downloadRangePath;

    protected List<DownloadRange> downloadRangeList;

    protected DownloadInfoListener downloadInfoListener;

    protected Vector<DownloadStatusListener> downloadStatusListeners;

    // Constructor for Download.
    public AbstractDownload(int id, URL url, String downloadName, int partCount, File downloadPath, File downloadRangePath, ProtocolType protocolType) {
        this.id = id;
        this.url = url;
        this.downloadName = downloadName;
        size = -1;
        downloaded = 0;
        status = DownloadStatus.DOWNLOADING;
        this.partCount = partCount;

        this.downloadPath = downloadPath;
        this.downloadRangePath = downloadRangePath;

        this.protocolType = protocolType;

        downloadRangeList = new ArrayList<>();

        downloadStatusListeners = new Vector<>();
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
    public String getDownloadName() {
        return downloadName;
    }

    @Override
    public void setDownloadName(String downloadName) {
        this.downloadName = downloadName;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public DownloadStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(DownloadStatus status) {
        this.status = status;
    }

    @Override
    public String getTransferRate() {
        return transferRate;
    }

    @Override
    public ProtocolType getProtocolType() {
        return protocolType;
    }

    @Override
    public void setProtocolType(ProtocolType protocolType) {
        this.protocolType = protocolType;
    }

    @Override
    public int getDownloaded() {
        return downloaded;
    }

    @Override
    public void setDownloaded(int downloaded) {
        this.downloaded = downloaded;
    }

    @Override
    public File getDownloadPath() {
        return downloadPath;
    }

    @Override
    public void setDownloadPath(File downloadPath) {
        this.downloadPath = downloadPath;
    }

    @Override
    public File getDownloadRangePath() {
        return downloadRangePath;
    }

    @Override
    public void setDownloadRangePath(File downloadRangePath) {
        this.downloadRangePath = downloadRangePath;
    }

    @Override
    public int getResponseCode() { // todo must go to below class
        return responseCode;
    }

    @Override
    public abstract boolean isResumeCapability();

    // Get this download's size.
    @Override
    public String getFormattedSize() {
        return ConnectionUtil.roundSizeTypeFormat((float) size, SizeType.BYTE);
    }

    // Get this download's progress.
    @Override
    public float getProgress() {
        return ((float) downloaded / size); // *100
    }

    @Override
    public List<DownloadRange> getDownloadRangeList() {
        return downloadRangeList;
    }

    @Override
    public void setDownloadRangeList(List<DownloadRange> downloadRangeList) {
        this.downloadRangeList = downloadRangeList;
    }

    /**
     * Adds an DownloadStatusListener to the set of downloadStatusListeners for this object, provided
     * that it is not the same as some DownloadStatusListener already in the set.
     * The order in which notifications will be delivered to multiple
     * DownloadDialogListeners is not specified. See the class comment.
     *
     * @param   downloadStatusListener   an DownloadStatusListener to be added.
     * @throws NullPointerException   if the parameter o is null.
     */
    @Override
    public synchronized void addDownloadStatusListener(DownloadStatusListener downloadStatusListener) {
        if (downloadStatusListener == null)
            throw new NullPointerException();
        if (!downloadStatusListeners.contains(downloadStatusListener)) {
            downloadStatusListeners.addElement(downloadStatusListener);
        }
    }

    /**
     * Deletes an DownloadStatusListener from the set of downloadStatusListeners of this object.
     * Passing <CODE>null</CODE> to this method will have no effect.
     * @param   downloadStatusListener   the DownloadStatusListener to be deleted.
     */
    @Override
    public synchronized void deleteDownloadStatusListener(DownloadStatusListener downloadStatusListener) {
        downloadStatusListeners.removeElement(downloadStatusListener);
    }

    @Override
    public void setDownloadInfoListener(DownloadInfoListener downloadInfoListener) {
        this.downloadInfoListener = downloadInfoListener;
    }

    @Override
    public void removeDownloadInfo(DownloadInfoListener downloadInfoListener) {
        if (this.downloadInfoListener.equals(downloadInfoListener))
            this.downloadInfoListener = null;
    }

    // Pause this download.
    @Override
    public void pause() {
        for (DownloadRange downloadRange : downloadRangeList)
            downloadRange.disConnect();
        status = DownloadStatus.DISCONNECTING;
        stateChanged();
    }

    // Resume this download.
    @Override
    public void resume() {
        status = DownloadStatus.DOWNLOADING;
        stateChanged();
        if (!downloadRangeList.isEmpty()) {
            for (DownloadRange downloadRange : downloadRangeList)
                downloadRange.resume();
            startTransferRate();
        } else {
            download();
        }
    }

    // Mark this download as having an error.
    protected void error() {
        status = DownloadStatus.ERROR;
        stateChanged();

        if (downloadInfoListener != null)
            downloadInfoListener.downloadNeedSaved(this);
    }

    protected void startTransferRate() {
        new Thread(new Runnable() {
            ///////////////////////////////////////////////////////////////////////////////////// Download Watch
            ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

            @Override
            public void run() {
                while (status == DownloadStatus.DOWNLOADING) {
                    Integer previousDownloaded = threadLocal.get();

                    if (previousDownloaded == null) {
                        previousDownloaded = 0;
                    }
                    int newDownloaded = downloaded;
                    float differenceDownloaded = ConnectionUtil.calculateTransferRateInUnit((newDownloaded - previousDownloaded), 1000, TimeUnit.SEC); // in Byte

                    // save new downloaded into threadLocal
                    threadLocal.set(newDownloaded);

                    // calculate differenceDownloaded
                    transferRate = ConnectionUtil.roundSizeTypeFormat(differenceDownloaded, SizeType.BYTE) + "/sec";
                    stateChanged();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    // Start or resume downloading.
    protected void download() {
        Thread thread = new Thread(this);
        thread.start();

        /**     Second way that use SwingWorker
         SwingWorker<Void, String> worker = new PausableSwingWorker<Void, String>() {

         ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>();

         @Override protected Void doInBackground() throws Exception {
         while (status == DownloadStatus.DOWNLOADING) {
         Integer previousDownloaded = threadLocal.get();

         if (previousDownloaded == null) {
         previousDownloaded = 0;
         }
         int newDownloaded = downloaded;
         int differenceDownloaded = (newDownloaded - previousDownloaded) / 1024; // in KB

         // save new downloaded into threadLocal
         threadLocal.set(newDownloaded);

         // calculate differenceDownloaded
         String differenceDownloadedString = differenceDownloaded + "KB/sec";
         publish(differenceDownloadedString);
         //  stateChanged();
         try {
         Thread.sleep(1000);
         } catch (InterruptedException e) {
         e.printStackTrace();
         }
         }
         return null;
         }

         @Override protected void process(List<String> chunks) {
         // Here we receive the values that we publish().
         // They may come grouped in chunks.
         transferRate = chunks.get(chunks.size() - 1);

         }
         };
         worker.execute(); */
    }

    @Override
    public abstract void run();

    @Override
    public abstract void createDownloadRanges();

    // add a new downloadRange if not in downloadRangeList
    @Override
    public void addDownloadRange(DownloadRange downloadRange) {
        if (!downloadRangeList.contains(downloadRange)) {
            downloadRangeList.add(downloadRange);
            downloadRange.addDownloadRangeStatusListener(this);

            if (downloadInfoListener != null) {
                downloadInfoListener.newDownloadRangeEventOccured(downloadRange);
            }
        }
    }

    protected void endOfDownload() {

        List<File> files = new ArrayList<>();
        for (DownloadRange downloadRange : downloadRangeList) {
            files.add(downloadRange.getDownloadRangeFile());
        }

        // files.sort(new FileComperarto());

        FileUtil.joinDownloadedParts(files, downloadPath, downloadName);

        status = DownloadStatus.COMPLETE;
        stateChanged();
        if (downloadInfoListener != null)
            downloadInfoListener.downloadNeedSaved(this);
    }

    // Notify observers that this download's status has changed.
    protected void stateChanged() {
        for (final DownloadStatusListener downloadStatusListener : downloadStatusListeners)
            SwingUtilities.invokeLater(new Runnable() { // togo cut and  past to Download dialog
            public void run() { // todo must got to class that listen this class
                downloadStatusListener.downloadStatusChanged(AbstractDownload.this); // todo dDOwnload maybe
            }
        });
    }

    public void downloadStatusChanged(DownloadRange downloadRange, int readed) {
        updateInfo(downloadRange, readed);
        stateChanged();
    }



    private synchronized void updateInfo(DownloadRange downloadRange, int readed) {
        downloaded += readed;

        switch (downloadRange.getConnectionStatus()) {
            case DISCONNECTED:
                if (isDisConnect()) {
                    status = DownloadStatus.PAUSED;
                    if (downloadInfoListener != null)
                        downloadInfoListener.downloadNeedSaved(this);
                } else {
                    status = DownloadStatus.DISCONNECTING;
                }
                System.out.println("disconnect from download .... ");
                //      if (isLastDownloadRange(ConnectionStatus.DISCONNECTED)) {

                //      }
                break;
            case ERROR:
                System.out.println("error");
                status = DownloadStatus.ERROR;
                if (downloadInfoListener != null)
                    downloadInfoListener.downloadNeedSaved(this);
                break;
            case WAITING_RESPONSE:
                System.out.println("WAITING_RESPONSE");
                break;
            case COMPLETED:
                System.out.println("COMPLETED download Range");
                System.out.println("downloaded = " + downloaded + " size = " + size + "  downloadRange = " + downloadRange.getRangeDownloaded() + " startRange= " +
                        downloadRange.getStartRange() + " end range= " + downloadRange.getEndRange());

                if (downloaded >= size) { // when all parts downloaded
                    endOfDownload();
                }
                break;
            default:
        }
    }

    private boolean isDisConnect() {
        boolean state = true;
        for (DownloadRange downloadRange : downloadRangeList) {
            if (downloadRange.getConnectionStatus() != ConnectionStatus.DISCONNECTED && downloadRange.getConnectionStatus() != ConnectionStatus.COMPLETED) {
                state = false;
                break;
            }
        }
        return state;
    }
}
