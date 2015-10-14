package model;

import enums.ConnectionStatus;
import enums.DownloadStatus;
import enums.SizeType;
import enums.TimeUnit;
import gui.listener.DownloadInfoListener;
import gui.listener.DownloadStatusListener;
import utils.ConnectionUtil;
import utils.FileUtil;

import javax.swing.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.*;

/**
 * Created by Saeed on 9/10/2015.
 */
public class Download extends Observable implements Observer , Runnable {

    private int id;
    private URL url; // download URL
    private File downloadNameFile;
    private int size; // size of download in bytes
    private int downloaded; // number of bytes downloaded
    private DownloadStatus status; // current status of download
    private String transferRate; // rate of transfer

    private int responseCode;
    private int partCount;

    private String downloadPath;
    private String downloadRangePath;

    private List<DownloadRange> downloadRangeList = new ArrayList<>();

    private DownloadInfoListener downloadInfoListener;

    private Vector<DownloadStatusListener> downloadStatusListeners;

    /**
     * Adds an DownloadStatusListener to the set of downloadStatusListeners for this object, provided
     * that it is not the same as some DownloadStatusListener already in the set.
     * The order in which notifications will be delivered to multiple
     * DownloadDialogListeners is not specified. See the class comment.
     *
     * @param   downloadStatusListener   an DownloadStatusListener to be added.
     * @throws NullPointerException   if the parameter o is null.
     */
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
    public synchronized void deleteDownloadStatusListener(DownloadStatusListener downloadStatusListener) {
        downloadStatusListeners.removeElement(downloadStatusListener);
    }

    // Constructor for Download.
    public Download(int id, URL url, File downloadNameFile, int partCount, String downloadPath, String downloadRangePath) {
        this.id = id;
        this.url = url;
        this.downloadNameFile = downloadNameFile;
        size = -1;
        downloaded = 0;
        status = DownloadStatus.DOWNLOADING;
        this.partCount = partCount;

        this.downloadPath = downloadPath;
        this.downloadRangePath = downloadRangePath;

        downloadStatusListeners = new Vector<>();
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

    public File getDownloadNameFile() {
        return downloadNameFile;
    }

    public void setDownloadNameFile(File downloadNameFile) {
        this.downloadNameFile = downloadNameFile;
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
        startTransferRate();
    }

    // Mark this download as having an error.
    private void error() {
        status = DownloadStatus.ERROR;
        stateChanged();

        if (downloadInfoListener != null)
            downloadInfoListener.downloadNeedSaved(this);
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
        HttpURLConnection connection = null;
        try {
            // Open connection to URL.
            connection = (HttpURLConnection) url.openConnection();

            // Specify what portion of file to download.
            connection.setRequestProperty("Range", "bytes=0-");

            // Connect to server.
            connection.setRequestMethod("HEAD");
            connection.connect();

            responseCode = connection.getResponseCode();

            // Make sure response code is in the 200 range.
            if (responseCode / 100 != 2) {
        //        error();
                status = DownloadStatus.ERROR;
            }

            // Check for valid content length.
            int contentLength = connection.getContentLength();
            if (contentLength < 1) {
       //         error();
                status = DownloadStatus.ERROR;
            }

            /* Set the size for this download if it
              hasn't been already set. */
            if (size == -1) {
                size = contentLength;
       //         stateChanged();
        //        status = DownloadStatus.ERROR;
            }

            connection.disconnect();
            stateChanged();

   //         createDownloadRanges(connection, partCount);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            error();
        } catch (IOException e) {
            e.printStackTrace();
            error();
        } finally {
            if (connection != null) ///???
                connection.disconnect();
        }
        SwingUtilities.invokeLater(new Runnable() { // togo cut and  past to Download dialog
            public void run() {
                downloadInfoListener.newDownloadInfoGot(Download.this);
            }
        });
    }

    public void createDownloadRanges() {
        int partSize= ConnectionUtil.getPartSizeOfDownload(size, partCount);
        int startRange = 0;
        int endRange = partSize - 1;

        DownloadRange downloadRange= null;

        // if connection is able to part download
        if (responseCode == 206) {
            for (int i = 0;  i < partCount; i++) {
             //   String fileName = ConnectionUtil.getFileName(url);
                String partFileName = downloadNameFile + ".00" + (i + 1);
                downloadRange = new DownloadRange(i + 1, url, new File(downloadRangePath + File.separator + downloadNameFile + File.separator + partFileName), startRange, endRange);

                addDownloadRange(downloadRange);

                downloadRange.resume();

                startRange = endRange + 1;
                if (i != partCount - 2) {
                    endRange = startRange + partSize - 1;
                } else {
                    endRange = 0;
                }
            }
        } else {
       //     String fileName = ConnectionUtil.getFileName(url);
            String partFileName = downloadNameFile + ".00" + 1;
            downloadRange = new DownloadRange(1, url, new File(downloadRangePath + File.separator + downloadNameFile + File.separator + partFileName), startRange, size);
            addDownloadRange(downloadRange);
            downloadRange.resume();
        }
        if (downloadInfoListener != null)
            downloadInfoListener.downloadNeedSaved(this);
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

        FileUtil.joinDownloadedParts(files, downloadPath, downloadNameFile);

        status = DownloadStatus.COMPLETE;
        stateChanged();
        if (downloadInfoListener != null)
            downloadInfoListener.downloadNeedSaved(this);
    }

    // Notify observers that this download's status has changed.
    private void stateChanged() {
        for (final DownloadStatusListener downloadStatusListener : downloadStatusListeners)
            SwingUtilities.invokeLater(new Runnable() { // togo cut and  past to Download dialog
                public void run() {
                    downloadStatusListener.downloadStatusChanged(Download.this);
                }
            });
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

 //   private boolean isLastDownloadRange(ConnectionStatus connectionStatus) {
//        boolean result = true;
 //       for (DownloadRange downloadRange : downloadRangeList) {
 //           if (downloadRange.getConnectionStatus() != connectionStatus) {
 ////               result = false;
 ///           }
  //      }
  //      return result;
  //  }

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
