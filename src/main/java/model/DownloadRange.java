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
    private int size; // size of download in bytes
    private int downloaded; // number of bytes downloaded
    private ConnectionStatus connectionStatus; // current status of download

    private int startRange;
    private int endRange;

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
        size = -1;
        downloaded = 0;
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
        return downloaded + "";
    }

    // Get this downloads status.
    public ConnectionStatus getConnectionStatus() {
        return connectionStatus;
    }

    // Pause this download.
    public void disConnect() {
        connectionStatus = ConnectionStatus.DISCONNECTED;
        stateChanged();
    }

    // Pause this download.
    public void pause() {
        connectionStatus = ConnectionStatus.DISCONNECTED;
        stateChanged();
    }

    // Resume this download.
    public void resume() {
        connectionStatus = ConnectionStatus.SEND_GET;
        stateChanged();
        download();
    }

    // Cancel this download.
    public void cancel() {
        connectionStatus = ConnectionStatus.DISCONNECTED;
        stateChanged();
    }

    // Mark this download as having an error.
    private void error() {
        connectionStatus = ConnectionStatus.ERROR;
        stateChanged();
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


            String rangePropertyValue = "bytes=" + startRange + downloaded + "-";

            if (endRange != 0) {
                rangePropertyValue += endRange;
            }

            // Specify what portion of file to download.
            connection.setRequestProperty("Range", rangePropertyValue);


            connectionStatus = ConnectionStatus.SEND_GET;
            stateChanged();

            // Connect to server.
            connection.connect();

            // Make sure response code is in the 200 range.
            if (connection.getResponseCode() / 100 != 2) {
                error();
            }

            // Check for valid content length.
            int contentLength = endRange - startRange;
            if (contentLength < 1) {
                error();
            }

            /* Set the size for this download if it
              hasn't been already set. */
            if (size == -1) {
                size = contentLength;
                stateChanged();
            }

            file = new File(ConnectionUtil.getFileName(url) + number); //////////////////////////???????????????????????????????????????????/

            // Open file and seek to the end of it.
            randomAccessFile = new RandomAccessFile(ConnectionUtil.getFileName(url) + number, "rw");
            randomAccessFile.seek(downloaded);

            stream = connection.getInputStream();

            ////////////////////////////////////////////// for test performance
            BufferedInputStream bin = new BufferedInputStream(stream);

            // set status for read data from stream
            connectionStatus = ConnectionStatus.RECEIVING_DATA;
            stateChanged();

            ////////////////////////// for test
            long before = System.currentTimeMillis();
            //////////////////////////////////////////////////////////////////

            while (connectionStatus == ConnectionStatus.RECEIVING_DATA) {
                /* Size buffer according to how much of the
                  file is left to download. */
                byte buffer[];
                if (size - downloaded > MAX_BUFFER_SIZE) {
                    buffer = new byte[MAX_BUFFER_SIZE];
                } else {
                    buffer = new byte[size - downloaded];
                }

                // Read from server into buffer.
                read = stream.read(buffer);

                // Write buffer to file.
                randomAccessFile.write(buffer, 0, read);
                downloaded += read;
         //       if (getProgress() - previousProgress > 1) { // when 1% changed
                if (read != 0) {
                    stateChanged();
                }
         //       }
                if (size == downloaded)
                    break;
            }

            ////////////////////////////////////////////// for test performance
            long after = System.currentTimeMillis();
            long diff = after - before;
            System.out.println("long time is: " + diff);
            ///////////////////////////////////////////////////////////////////

            /* Change status to complete if this point was
              reached because downloading has finished. */
            if (connectionStatus == ConnectionStatus.RECEIVING_DATA) {
                connectionStatus = ConnectionStatus.COMPLETED;
                stateChanged();
            }
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
    private void stateChanged() {
        setChanged();
        notifyObservers();
    }

}
