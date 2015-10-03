package model;

import com.google.common.io.Files;
import enums.ConnectionStatus;
import enums.DownloadStatus;
import enums.SizeType;
import enums.TimeUnit;
import gui.listener.DownloadInfoListener;
import gui.listener.DownloadSaveListener;
import org.apache.commons.io.FileUtils;
import utils.ConnectionUtil;
import utils.FileUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Saeed on 9/10/2015.
 */
public class Download extends Observable implements Observer , Runnable {

    private int id;
    private URL url; // download URL
    private int size; // size of download in bytes
    private int downloaded; // number of bytes downloaded
    private DownloadStatus status; // current status of download
    private String transferRate; // rate of transfer

    private int partCount;

    private String downloadPath;
    private String downloadRangePath;

    private List<DownloadRange> downloadRangeList = new ArrayList<>();

    private DownloadInfoListener downloadInfoListener;

    private DownloadSaveListener downloadSaveListener;

    // Constructor for Download.
    public Download(int id, URL url, int partCount, String downloadPath, String downloadRangePath) {
        this.id = id;
        this.url = url;
        size = -1;
        downloaded = 0;
        status = DownloadStatus.DOWNLOADING;
        this.partCount = partCount;

        this.downloadPath = downloadPath;
        this.downloadRangePath = downloadRangePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Get this download's URL.
    public String getUrl() {
        return url.toString();
    }

    // Get this download's size.
    public String getSize() {
        return ConnectionUtil.roundSizeTypeFormat((float) size, SizeType.BYTE);
    }

    public int getRealSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setDownloaded(int downloaded) {
        this.downloaded = downloaded;
    }

    // Get this download's progress.
    public float getProgress() {
        return ((float) downloaded / size); // *100
    }

    public String getTransferRate() {
        return transferRate;
    }

    // Get this download's status.
    public DownloadStatus getStatus() {
        return status;
    }

    public void setStatus(DownloadStatus status) {
        this.status = status;
    }

    public List<DownloadRange> getDownloadRangeList() {
        return downloadRangeList;
    }

    public int countOfDownloadRange() {
        return downloadRangeList.size();
    }

    public String getDownloadRangePath() {
        return downloadRangePath;
    }

    public void setDownloadRangePath(String downloadRangePath) {
        this.downloadRangePath = downloadRangePath;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public void setDownloadRangeList(List<DownloadRange> downloadRangeList) {
        this.downloadRangeList = downloadRangeList;
    }

    public void setDownloadInfoListener(DownloadInfoListener downloadInfoListener) {
        this.downloadInfoListener = downloadInfoListener;
    }

    public void setDownloadSaveListener(DownloadSaveListener downloadSaveListener) {
        this.downloadSaveListener = downloadSaveListener;
    }

    // Pause this download.
    public void pause() {
        for (DownloadRange downloadRange : downloadRangeList)
            downloadRange.disConnect();
        status = DownloadStatus.DISCONNECTING;
        stateChanged();
    }

    // Resume this download.
    public void resume() {
        status = DownloadStatus.DOWNLOADING;
        stateChanged();
        if (!downloadRangeList.isEmpty()) {
            for (DownloadRange downloadRange : downloadRangeList)
                downloadRange.resume();
        } else {
            download();
        }
    }

    public void downloadRangeReturned() { //////////////////////////////////////////////
        for (DownloadRange downloadRange : downloadRangeList) {
            if (downloadInfoListener != null) {
                downloadInfoListener.newDownloadRangeEventOccured(downloadRange);
            }
        }
    }

    // Cancel this download.
  //  public void cancel() {
 ///       for (DownloadRange downloadRange : downloadRangeList)
  //          downloadRange.disConnect();
  //      status = DownloadStatus.CANCELLED;
  //      stateChanged();
  //  }

    // Mark this download as having an error.
    private void error() {
        status = DownloadStatus.ERROR;
        stateChanged();

        if (downloadSaveListener != null)
            downloadSaveListener.downloadNeedSaved(this);
    }

    private void startTransferRate() {
        new Thread(new Runnable() {
            ///////////////////////////////////////////////////////////////////////////////////// Download Watch
            ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>();

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
    private void download() {
        Thread thread = new Thread(this);
        thread.start();

        startTransferRate();

     //   run();

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
    public void run() {
        try {
            // Open connection to URL.
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Specify what portion of file to download.
            connection.setRequestProperty("Range", "bytes=0-");

            // Connect to server.
            connection.setRequestMethod("HEAD");
            connection.connect();

            // Make sure response code is in the 200 range.
            if (connection.getResponseCode() / 100 != 2) {
                error();
            }

            // Check for valid content length.
            int contentLength = connection.getContentLength();
            if (contentLength < 1) {
                error();
            }

            /* Set the size for this download if it
              hasn't been already set. */
            if (size == -1) {
                size = contentLength;
                stateChanged();
            }

            connection.disconnect();

            createDownloadRanges(connection, partCount);
        } catch (IOException e) {
            e.printStackTrace();
            error();
        } finally {

        }
    }

    private void createDownloadRanges(HttpURLConnection connection, int connectionSize) throws IOException {
        int partSize= ConnectionUtil.getPartSizeOfDownload(size, connectionSize);
        int startRange = 0;
        int endRange = partSize - 1;

        DownloadRange downloadRange= null;

        // if connection is able to part download
        if (connection.getResponseCode() == 206) {
            for (int i = 0;  i < connectionSize; i++) {
                String fileName = ConnectionUtil.getFileName(url);
                String partFileName = fileName + ".00" + (i + 1);
                downloadRange = new DownloadRange(i + 1, url, new File(downloadRangePath + File.separator + fileName + File.separator + partFileName), startRange, endRange);

                addDownloadRange(downloadRange);

                downloadRange.resume();

                startRange = endRange + 1;
                if (i != connectionSize - 2) {
                    endRange = startRange + partSize - 1;
                } else {
                    endRange = 0;
                }
            }
        } else {
            String fileName = ConnectionUtil.getFileName(url);
            String partFileName = fileName + ".00" + 1;
            downloadRange = new DownloadRange(1, url, new File(downloadRangePath + File.separator + fileName + File.separator + partFileName), startRange, size);
            addDownloadRange(downloadRange);
            downloadRange.resume();
        }
        if (downloadSaveListener != null)
            downloadSaveListener.downloadNeedSaved(this);
    }

    // add a new downloadRange if not in downloadRangeList
    public void addDownloadRange(DownloadRange downloadRange) {
        if (!downloadRangeList.contains(downloadRange)) {
            downloadRangeList.add(downloadRange);
            downloadRange.addObserver(this);

            if (downloadInfoListener != null) {
                downloadInfoListener.newDownloadRangeEventOccured(downloadRange);
            }
        }
    }

    private void endOfDownload() {

        List<File> files = new ArrayList<>();
        for (DownloadRange downloadRange : downloadRangeList) {
            files.add(downloadRange.getDownloadRangeFile());
        }

       // files.sort(new FileComperarto());

        FileUtil.joinDownloadedParts(files, downloadPath, ConnectionUtil.getFileName(url));

        status = DownloadStatus.COMPLETE;
        stateChanged();
        if (downloadSaveListener != null)
            downloadSaveListener.downloadNeedSaved(this);
    }

    // Notify observers that this download's status has changed.
    private void stateChanged() {
        if (downloadInfoListener != null)
            downloadInfoListener.downloadInfoChanged();

    }

    // event that come from DownloadRange TODO may be synchrinized
    @Override
    public void update(Observable o, Object arg) {
        DownloadRange downloadRange = (DownloadRange) o;
        updateInfo(downloadRange, (int) arg);
        stateChanged();
    }

    private synchronized void updateInfo(DownloadRange downloadRange, int read) {
        downloaded += read;

        switch (downloadRange.getConnectionStatus()) {
            case DISCONNECTED:
                if (isDisConnect()) {
                    status = DownloadStatus.PAUSED;
                    if (downloadSaveListener != null)
                        downloadSaveListener.downloadNeedSaved(this);
                }
                System.out.println("disconnect from download .... ");
          //      if (isLastDownloadRange(ConnectionStatus.DISCONNECTED)) {

          //      }
                break;
            case ERROR:
                System.out.println("error");
                status = DownloadStatus.ERROR;
                if (downloadSaveListener != null)
                    downloadSaveListener.downloadNeedSaved(this);
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

    private boolean isLastDownloadRange(ConnectionStatus connectionStatus) {
        boolean result = true;
        for (DownloadRange downloadRange : downloadRangeList) {
            if (downloadRange.getConnectionStatus() != connectionStatus) {
                result = false;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "Download{" +
                "downloadRangePath='" + downloadRangePath + '\'' +
                ", downloaded=" + downloaded +
                ", status=" + status +
                ", transferRate='" + transferRate + '\'' +
                ", partCount=" + partCount +
                ", downloadPath='" + downloadPath + '\'' +
                ", size=" + size +
                ", url=" + url +
                ", id=" + id +
                '}';
    }
}
