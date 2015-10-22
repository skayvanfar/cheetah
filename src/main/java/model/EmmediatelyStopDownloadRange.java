package model;

import enums.ConnectionStatus;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Observable;

/**
 * Created by saeed on 10/12/15.
 */
public class EmmediatelyStopDownloadRange extends Observable implements Runnable {
    // Logger
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    // Max size of download buffer.
    private static final int MAX_BUFFER_SIZE = 1024; // 1024 - 4096

    private int id;
    private URL url; // download URL
    private int number;
    private int rangeSize; // size of download in bytes
    private int rangeDownloaded; // number of bytes downloaded
    private ConnectionStatus connectionStatus; // current status of download

    private int startRange;
    private int endRange;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStartRange() {
        return startRange;
    }

    public int getEndRange() {
        return endRange;
    }

    public int getRangeSize() {
        return rangeSize;
    }

    public void setRangeSize(int rangeSize) {
        this.rangeSize = rangeSize;
    }

    public File downloadRangeFile;

    // temp amount read
    private int read;

    //  private String downloadRangePath;

    // to quick stop thread
    private boolean stop = false;
    private boolean directStop = false;

    RandomAccessFile randomAccessFile = null;
    InputStream stream = null;

    public File getDownloadRangeFile() {
        return downloadRangeFile;
    }

    public int getRead() {
        return read;
    }

    public EmmediatelyStopDownloadRange(int number, URL url ,File downloadRangeFile, int startRange, int endRange) {
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
    }

    public int getNumber() {
        return number;
    }

    public String getRangeDownloaded() {
        return String.valueOf(rangeDownloaded);
    }

    public void setRangeDownloaded(int rangeDownloaded) {
        this.rangeDownloaded = rangeDownloaded;
    }

    // Get this downloads status.
    public ConnectionStatus getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(ConnectionStatus connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    // Pause this download.
    public void disConnect() {
        if (connectionStatus == ConnectionStatus.CONNECTING || connectionStatus == ConnectionStatus.SEND_GET || connectionStatus == ConnectionStatus.RECEIVING_DATA )
            connectionStatus = ConnectionStatus.DISCONNECTING;
        setFlags(true);
        thread.interrupt();
        stateChanged(0); // TODO two time call and print "disconnect from download .... "
        stopDownload();
    }

    // Resume this download.
    public void resume() {
        if (connectionStatus != ConnectionStatus.COMPLETED) {
            setFlags(false);
            download();
        }
    }

    // Mark this download as having an error.
    private void error() {
        connectionStatus = ConnectionStatus.ERROR;
        stateChanged(0);
    }

    Thread thread = null;

    private void download() {
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {

        //      RandomAccessFile randomAccessFile = null;
        //      InputStream stream = null;

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
                rangeSize = rangeContentLength;
                stateChanged(0);
            }

            File downloadRangeDirTemp = downloadRangeFile.getParentFile();
            if (!downloadRangeDirTemp.exists()) // todo must not deleted
                downloadRangeDirTemp.mkdir();

            // Open file and seek to the end of it.
            randomAccessFile = new RandomAccessFile(downloadRangeFile, "rw");
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

                if (stop) //todo may be better way for stop thread
                    break;

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

            /* Change status to complete if this point was
              reached because downloading has finished. */
            //       if (connectionStatus == ConnectionStatus.RECEIVING_DATA && stop == false) {
            //          connectionStatus = ConnectionStatus.COMPLETED;
            //           randomAccessFile.close();
            //      } else {
            //           connectionStatus = ConnectionStatus.DISCONNECTED;
            //           randomAccessFile.close();
            //       }
            //        stateChanged(0);
            if (!directStop)
                stopDownload();

        } catch (Exception e) {
            e.printStackTrace();
            error();
        } finally {
            // Close file.
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (Exception ignored) {
                }
            }

            // Close connection to server.
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

    private void stopDownload() {
        try {
            /* Change status to complete if this point was
              reached because downloading has finished. */
            if (connectionStatus == ConnectionStatus.RECEIVING_DATA && !stop) {
                connectionStatus = ConnectionStatus.COMPLETED;
                randomAccessFile.close();
            } else {
                connectionStatus = ConnectionStatus.DISCONNECTED;
                randomAccessFile.close();
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
                } catch (Exception ignored) {
                }
            }

            // Close connection to server.
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception ignored) {
                }
            }
        }

    }

    // Notify observers that this download's status has changed.
    private void stateChanged(int read) {
        setChanged();
        notifyObservers(read);
    }

    private void setFlags(boolean flag) {
        stop = flag;
        directStop = flag;
    }

    @Override
    public String toString() {
        return "DownloadRange{" +
                "id=" + id +
                ", url=" + url +
                ", number=" + number +
                ", rangeSize=" + rangeSize +
                ", rangeDownloaded=" + rangeDownloaded +
                ", connectionStatus=" + connectionStatus +
                ", startRange=" + startRange +
                ", endRange=" + endRange +
                ", read=" + read +
                ", downloadRangeFile='" + downloadRangeFile + '\'' +
                ", stop=" + stop +
                '}';
    }
}
