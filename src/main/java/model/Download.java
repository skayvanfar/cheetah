package model;

import enums.ConnectionStatus;
import enums.DownloadStatus;
import enums.SizeType;
import enums.TimeUnit;
import gui.listener.DownloadInfoListener;
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
public class Download extends Observable implements Observer , Runnable{

    // Max size of download buffer.
    private static final int MAX_BUFFER_SIZE = 1024;

    private URL url; // download URL
    private int size; // size of download in bytes
    private int downloaded; // number of bytes downloaded
    private DownloadStatus status; // current status of download
    private String transferRate; // rate of transfer

    private List<DownloadRange> downloadRangeList = new ArrayList<>();

 //   private float previousProgress;

    private DownloadInfoListener downloadInfoListener;

    // Constructor for Download.
    public Download(URL url) {
        this.url = url;
        size = -1;
        downloaded = 0;
        status = DownloadStatus.DOWNLOADING;

        // Begin the download.
        download();
    }

    // Get this download's URL.
    public String getUrl() {
        return url.toString();
    }

    // Get this download's size.
    public String getSize() {
        return ConnectionUtil.roundSizeTypeFormat((float) size, SizeType.BYTE);
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

    public List<DownloadRange> getDownloadRangeList() {
        return downloadRangeList;
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
        for (DownloadRange downloadRange : downloadRangeList)
            downloadRange.resume();
        status = DownloadStatus.DOWNLOADING;
        stateChanged();
   //     download();
    }

    // Cancel this download.
    public void cancel() {
        for (DownloadRange downloadRange : downloadRangeList)
            downloadRange.disConnect();
        status = DownloadStatus.CANCELLED;
        stateChanged();
    }

    // Mark this download as having an error.
    private void error() {
        status = DownloadStatus.ERROR;
        stateChanged();
    }

    // Start or resume downloading.
    private void download() {
        Thread thread = new Thread(this);
        thread.start();


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

            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// for 8 connection
            createDownloadRanges(connection, 8);
            connection.disconnect();
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

        // if connection is able to part download
        if (connection.getResponseCode() == 206) {
            for (int i = 0;  i < connectionSize; i++) {
                DownloadRange downloadRange = new DownloadRange(i, url, startRange, endRange);
                downloadRangeList.add(downloadRange);
                downloadRange.addObserver(this);

                if (downloadInfoListener != null) {
                    downloadInfoListener.newDownloadRangeEventOccured(downloadRange);
                }

                startRange = endRange + 1;
                if (i != connectionSize - 2) {
                    endRange = startRange + partSize - 1;
                } else {
                    endRange = 0;
                }
            }
        } else {
            DownloadRange downloadRange = new DownloadRange(0, url, startRange, size);
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
            files.add(downloadRange.getFile());
        }

       // files.sort(new FileComperarto()); TODO must sorted

        FileUtil.joinDownloadedParts(files, ConnectionUtil.getFileName(url));

        status = DownloadStatus.COMPLETE;
        stateChanged();
    }

    // Notify observers that this download's status has changed.
    private void stateChanged() {
        /////////////////////////////////////////////// save previous progress
    //    previousProgress = getProgress();

        downloadInfoListener.downloadInfoChanged();

     //   setChanged();
     //   notifyObservers();
    }

    // event that come from DownloadRange TODO may be synchrinized
    @Override
    public void update(Observable o, Object arg) {
        DownloadRange downloadRange = (DownloadRange) o;
        updateInfo(downloadRange, (int) arg);
        stateChanged();
    }


//    private int previousDownloaded = 0;
//    private long previousTime = 0;
    private synchronized void updateInfo(DownloadRange downloadRange, int read) {
        downloaded += read;

        switch (downloadRange.getConnectionStatus()) {
            case DISCONNECTED:

                if (downloadRange.getConnectionStatus() == ConnectionStatus.DISCONNECTED) {
                    if (isDisConnect()) {
                        status = DownloadStatus.PAUSED;
                    }
                }

                System.out.println("disconnect from download .... ");
                break;
            case ERROR:
                System.out.println("error");
                break;
            case WAITING_RESPONSE:
                System.out.println("WAITING_RESPONSE");
                break;
            case COMPLETED:
                System.out.println("COMPLETED download Range");
                System.out.println("downloaded = " + downloaded + " size = " + size + "  downloadRange = " + downloadRange.getDownloaded() + " startRange= " +
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
            if (downloadRange.getConnectionStatus() != ConnectionStatus.DISCONNECTED) {
                state = false;
                break;
            }
        }
        return state;
    }

}
