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

package model.httpsImpl;

import concurrent.annotation.NotThreadSafe;
import enums.ConnectionStatus;
import model.AbstractDownloadRange;
import org.apache.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;

/**
 * Https implementation of DownloadRange interface.
 *
 * @author <a href="kayvanfar.sj@gmail.com">Saeed Kayvanfar</a> 10/26/2015
 * @see model.DownloadRange
 */
@NotThreadSafe
public class HttpsDownloadRange extends AbstractDownloadRange implements model.DownloadRange {

    // Logger
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public HttpsDownloadRange(int number, URL url, File downloadRangeFile, int startRange, int endRange) {
        super(number, url, downloadRangeFile, startRange, endRange);
    }

    @Override
    public Void call() {
        RandomAccessFile randomAccessFile = null;
        InputStream stream = null;

        try {
            // Open connection to URL.
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            String rangePropertyValue = "bytes=" + (startRange + rangeDownloaded) + "-";
            if (endRange != 0) {
                rangePropertyValue += endRange;
            }

            // Specify what portion of file to download.
            connection.setRequestProperty("Range", rangePropertyValue);


            connectionStatus = ConnectionStatus.SEND_GET;
            stateChanged(0);

            connection.connect();

            // Make sure response code is in the 200 range.
            if (connection.getResponseCode() / 100 != 2) {
                error();
                return null;
            }

            long rangeContentLength = connection.getContentLengthLong();

            if (rangeContentLength < 1) {
                error();
                return null;
            }

            /* Set the size for this download if it
              hasn't been already set. */
            if (rangeSize == -1) {
                rangeSize = (int) rangeContentLength; // TODO: consider making rangeSize long if sizes exceed 2GB
                stateChanged(0);
            }

            File downloadRangeDirTemp = downloadRangeFile.getParentFile();
            if (!downloadRangeDirTemp.exists()) {
                downloadRangeDirTemp.mkdir();
            }

            // Open file and seek to the end of it.
            randomAccessFile = new RandomAccessFile(downloadRangeFile, "rw");
            randomAccessFile.seek(rangeDownloaded);

            stream = connection.getInputStream();

            // set status for read data from stream
            connectionStatus = ConnectionStatus.RECEIVING_DATA;
            stateChanged(0);

            byte[] buffer = new byte[MAX_BUFFER_SIZE];

            while (connectionStatus == ConnectionStatus.RECEIVING_DATA && rangeDownloaded < rangeSize) {
                int remaining = rangeSize - rangeDownloaded;
                if (remaining <= 0 || stop) break;

                int bytesRead = stream.read(buffer, 0, Math.min(buffer.length, remaining));

                if (bytesRead == -1) {
                    break;
                }

                randomAccessFile.write(buffer, 0, bytesRead);
                rangeDownloaded += bytesRead;

                if (bytesRead > 0) {
                    stateChanged(bytesRead);
                }

                Thread.sleep(5);
            }

            // Final check to validate completion
            if (rangeDownloaded == rangeSize && !stop) {
                connectionStatus = ConnectionStatus.COMPLETED;
            } else {
                connectionStatus = ConnectionStatus.DISCONNECTED;
            }
            stateChanged(0);
        } catch (Exception e) {
            e.printStackTrace();
            error();
        } finally {
            // Close resources safely
            try {
                if (randomAccessFile != null) randomAccessFile.close();
            } catch (Exception ignored) {}

            try {
                if (stream != null) stream.close();
            } catch (Exception ignored) {}
        }
        return null;
    }
}
