package model;

import enums.ConnectionStatus;
import enums.DownloadStatus;
import utils.ConnectionUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Observable;

/**
 * Created by Saeed on 9/14/2015.
 */
public class DownloadRange extends Observable implements Runnable {


    // Max size of download buffer.
    private static final int MAX_BUFFER_SIZE = 1024; // 1024 - 4096

    private URL url; // download URL
    private int number;
    private int rangeSize; // size of download in bytes
    private int rangeDownloaded; // number of bytes downloaded
    private ConnectionStatus connectionStatus; // current status of download

    private int startRange;
    private int endRange;

    public int getStartRange() {
        return startRange;
    }

    public int getEndRange() {
        return endRange;
    }

    private File file;

    // temp amount read
    private int read;

    public File getFile() {
        return file;
    }

    public int getRead() {
        return read;
    }

    public DownloadRange(int number, URL url, int startRange, int endRange) {
        this.number = number;
        this.url = url;
        rangeSize = -1;
        rangeDownloaded = 0;
        read = 0;
        connectionStatus = ConnectionStatus.CONNECTING;

        this.startRange = startRange;
        this.endRange = endRange;

        // Begin the download.
        download();
    }

    public int getNumber() {
        return number;
    }

    public String getDownloaded() {
        return String.valueOf(rangeDownloaded);
    }

    // Get this downloads status.
    public ConnectionStatus getConnectionStatus() {
        return connectionStatus;
    }

    // Pause this download.
    public void disConnect() {
        connectionStatus = ConnectionStatus.DISCONNECTING;
        stateChanged(0); // TODO two time call and print "disconnect from download .... "
    }

    // Pause this download.
  //  public void pause() {
//        connectionStatus = ConnectionStatus.DISCONNECTED;
//        stateChanged();
//    }

    // Resume this download.
    public void resume() {
        connectionStatus = ConnectionStatus.SEND_GET;
        stateChanged(0);
        download();
    }

    // Cancel this download.
 //   public void cancel() {
 //       connectionStatus = ConnectionStatus.DISCONNECTED;
 //       stateChanged();
 //   }

    // Mark this download as having an error.
    private void error() {
        connectionStatus = ConnectionStatus.ERROR;
        stateChanged(0);
    }

    private void download() {
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {

        RandomAccessFile randomAccessFile = null;
        InputStream stream = null;

        try {
            // Open connection to URL.
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            String rangePropertyValue = "bytes=" + (startRange + rangeDownloaded) + "-";

            if (endRange != 0) {
                rangePropertyValue += endRange;
            }

            // Specify what portion of file to download.
            connection.setRequestProperty("Range", rangePropertyValue);


            connectionStatus = ConnectionStatus.SEND_GET;
            stateChanged(0);

            // Connect to server.
            connection.connect();

            // Make sure response code is in the 200 range.
            if (connection.getResponseCode() / 100 != 2) {
                error();
            }

            int rangeContentLength = 0;
            rangeContentLength = connection.getContentLength();

            if (rangeContentLength < 1) {
                error();
            }

            /* Set the size for this download if it
              hasn't been already set. */
            if (rangeSize == -1) {
                rangeSize = rangeContentLength; // like 10
                stateChanged(0);
            }

            String partFileName = ConnectionUtil.getFileName(url) + ".00" + (number + 1);
            file = new File(partFileName);

            // Open file and seek to the end of it.
            randomAccessFile = new RandomAccessFile(partFileName, "rw");
            randomAccessFile.seek(rangeDownloaded);

            stream = connection.getInputStream();

            // set status for read data from stream
            connectionStatus = ConnectionStatus.RECEIVING_DATA;
            stateChanged(0);

            while (connectionStatus == ConnectionStatus.RECEIVING_DATA) {
                /* Size buffer according to how much of the
                  file is left to download. */
                byte buffer[];
                if (rangeSize - rangeDownloaded > MAX_BUFFER_SIZE) {
                    buffer = new byte[MAX_BUFFER_SIZE];
                } else {
                    buffer = new byte[rangeSize - rangeDownloaded];
                }

                // Read from server into buffer.
                read = stream.read(buffer);

                if (read == -1) {
                    System.out.println("-1   rangeDownload size: " + rangeSize + " downloaded: " + rangeDownloaded);
                    break;
                }

                // Write buffer to file.
                randomAccessFile.write(buffer, 0, read);
                rangeDownloaded += read;
         //       if (getProgress() - previousProgress > 1) { // when 1% changed
                if (read != 0) {
                    stateChanged(read);
                }
         //       }

                if (rangeSize == rangeDownloaded) {
                    System.out.println("rangeDownload size: " + rangeSize + " downloaded: " + rangeDownloaded);
                    break;
                }

            }

            if (connectionStatus == ConnectionStatus.DISCONNECTING) {
                connectionStatus = ConnectionStatus.DISCONNECTED;
     //           stateChanged();
            }

            /* Change status to complete if this point was
              reached because downloading has finished. */
            if (connectionStatus == ConnectionStatus.RECEIVING_DATA) {
                connectionStatus = ConnectionStatus.COMPLETED;
    //            stateChanged();
            }
            stateChanged(0);
        } catch (Exception e) {
            e.printStackTrace();
            error();
        } finally {
            // Close file.
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (Exception e) {
                }
            }

            // Close connection to server.
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e) {
                }
            }
        }
    }

    // Notify observers that this download's status has changed.
    private void stateChanged(int read) {
        setChanged();
        notifyObservers(read);
    }

}
