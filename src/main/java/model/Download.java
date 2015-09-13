package model;

import enums.DownloadState;

import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Observable;

/**
 * Created by Saeed on 9/10/2015.
 */
public class Download extends Observable implements Runnable {

    // Max size of download buffer.
    private static final int MAX_BUFFER_SIZE = 1024;

    // These are the status names.
    public static final String STATUSES[] = {"Downloading",
            "Paused", "Complete", "Cancelled", "Error"};

    private URL url; // download URL
    private int size; // size of download in bytes
    private int downloaded; // number of bytes downloaded
    private DownloadState status; // current status of download
    private String transferRate; // rate of transfer

    private Thread downloadwatch;

    // if really any thing changed //////////////////////////////////////////////
  //  private boolean changed = false;
    private float previousProgress;

    // Constructor for Download.
    public Download(URL url) {
        this.url = url;
        size = -1;
        downloaded = 0;
        status = DownloadState.DOWNLOADING;

        // Begin the download.
        download();

        downloadwatch = new Thread(new Runnable() {
            ///////////////////////////////////////////////////////////////////////////////////// Download Watch
            ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>();

            @Override
            public void run() {
                do {
                    Integer previousDownloaded = threadLocal.get();

                    if (previousDownloaded == null) {
                        previousDownloaded = 0;
                    }
                    int newDownloaded = downloaded;
                    int differenceDownloaded = (newDownloaded - previousDownloaded) / 1024; // in KB

                    // save new downloaded into threadLocal
                    threadLocal.set(newDownloaded);

                    // calculate differenceDownloaded
                    transferRate = differenceDownloaded + "KB/sec";
                    stateChanged();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } while (downloaded != size); ////////////////////// ????????
            }
        });
        downloadwatch.start();
    }

    // Get this download's URL.
    public String getUrl() {
        return url.toString();
    }

    // Get this download's size.
    public int getSize() {
        return size;
    }

    // Get this download's progress.
    public float getProgress() {
        return ((float) downloaded / size) * 100;
    }

    public String getTransferRate() {
        return transferRate;
    }

    // Get this download's status.
    public DownloadState getStatus() {
        return status;
    }

    // Pause this download.
    public void pause() {
        status = DownloadState.PAUSED;
        stateChanged();
        try {
            downloadwatch.wait(); /////?????????????????????????????///
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Resume this download.
    public void resume() {
        status = DownloadState.DOWNLOADING;
        stateChanged();
        download();
        downloadwatch.resume(); /////???????????????????????///
    }

    // Cancel this download.
    public void cancel() {
        status = DownloadState.CANCELLED;
        stateChanged();
        downloadwatch.stop(); /////???????????????????????///
    }

    // Mark this download as having an error.
    private void error() {
        status = DownloadState.ERROR;
        stateChanged();
        downloadwatch.stop(); /////???????????????????????///
    }

    // Start or resume downloading.
    private void download() {
        Thread thread = new Thread(this);
        thread.start();
    }

    // Get file name portion of URL.
    private String getFileName(URL url) {
        String fileName = url.getFile();
        return fileName.substring(fileName.lastIndexOf('/') + 1);
    }

    // Download file.
    public void run() {
        RandomAccessFile file = null;
        InputStream stream = null;

        try {
            // Open connection to URL.
            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

            // Specify what portion of file to download.
            connection.setRequestProperty("Range",
                    "bytes=" + downloaded + "-");

            // Connect to server.
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

            // Open file and seek to the end of it.
            file = new RandomAccessFile(getFileName(url), "rw");
            file.seek(downloaded);

            stream = connection.getInputStream();
            while (status == DownloadState.DOWNLOADING) {
        /* Size buffer according to how much of the
           file is left to download. */
                byte buffer[];
                if (size - downloaded > MAX_BUFFER_SIZE) {
                    buffer = new byte[MAX_BUFFER_SIZE];
                } else {
                    buffer = new byte[size - downloaded];
                }

                // Read from server into buffer.
                int read = stream.read(buffer);
                if (read == -1)
                    break;

                // Write buffer to file.
                file.write(buffer, 0, read);
                downloaded += read;
                if (getProgress() - previousProgress > 1) { ///////////////////////////////////////// when 1% changed
                    stateChanged();
                }
            }

      /* Change status to complete if this point was
         reached because downloading has finished. */
            if (status == DownloadState.DOWNLOADING) {
                status = DownloadState.COMPLETE;
                stateChanged();
            }
        } catch (Exception e) {
            error();
        } finally {
            // Close file.
            if (file != null) {
                try {
                    file.close();
                } catch (Exception e) {}
            }

            // Close connection to server.
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e) {}
            }
        }
    }

    // Notify observers that this download's status has changed.
    private void stateChanged() {
        /////////////////////////////////////////////// save previous progress
        previousProgress = getProgress();

        setChanged();
        notifyObservers();
    }
}
