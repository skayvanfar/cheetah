package model.htmlImpl;

import enums.ConnectionStatus;
import model.*;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Saeed on 10/17/2015.
 */
public class HttpDownloadRange extends AbstractDownloadRange implements model.DownloadRange {

    public HttpDownloadRange(int number, URL url, File downloadRangeFile, int startRange, int endRange) {
        super(number, url, downloadRangeFile, startRange, endRange);
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

            int rangeContentLength;
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
         //       byte buffer[];
        //        if (rangeSize - rangeDownloaded > MAX_BUFFER_SIZE) {
        //            buffer = new byte[MAX_BUFFER_SIZE];
        //        } else {
        //            buffer = new byte[rangeSize - rangeDownloaded];
       //         }

                if (stop) //todo may be better way for stop thread
                    break;


                int bytesAvailable = stream.available();
                byte[] buffer = new byte[bytesAvailable];

                // Read from server into buffer.
                // temp amount read
                int bytesRead = stream.read(buffer, 0, bytesAvailable);

                if (bytesRead == -1) {
                    System.out.println("-1   rangeDownload size: " + rangeSize + " downloaded: " + rangeDownloaded);
                    break;
                }

                // Write buffer to file.
                randomAccessFile.write(buffer, 0, bytesRead);
                rangeDownloaded += bytesRead;
                //       if (getProgress() - previousProgress > 1) { // when 1% changed
                if (bytesRead != 0) {
                    stateChanged(bytesRead);
                }
                //       }

                if (rangeSize == rangeDownloaded) {
                    System.out.println("rangeDownload size: " + rangeSize + " downloaded: " + rangeDownloaded);
                    break;
                }
                Thread.sleep(5);
            }

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
}
