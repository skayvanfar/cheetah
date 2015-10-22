package model.htmlImpl;

import enums.DownloadStatus;
import enums.ProtocolType;
import model.*;
import utils.ConnectionUtil;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * Created by Saeed on 10/17/2015.
 */
public class HttpDownload extends AbstractDownload implements model.Download {

    private boolean resumeCapability;

    public HttpDownload(int id, URL url, String downloadName, int partCount, File downloadPath, File downloadRangePath, ProtocolType protocolType) {
        super(id, url, downloadName, partCount, downloadPath, downloadRangePath, protocolType);
        resumeCapability = false;
    }

    @Override
    public boolean isResumeCapability() {
        return resumeCapability;
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

            resumeCapability = responseCode == 206;

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
        SwingUtilities.invokeLater(new Runnable() { // togo cut and  past to Download dialog todo move to other loc
            public void run() {
                downloadInfoListener.newDownloadInfoGot(HttpDownload.this); // todo may be Download
            }
        });
    }

    public void createDownloadRanges() {
        int partSize= ConnectionUtil.getPartSizeOfDownload(size, partCount);
        int startRange = 0;
        int endRange = partSize - 1;

        model.DownloadRange downloadRange;

        // if connection is able to part download
        if (responseCode == 206) {
            for (int i = 0;  i < partCount; i++) {
                //   String fileName = ConnectionUtil.getFileName(url);
                String partFileName = downloadName + ".00" + (i + 1);
                downloadRange = new HttpDownloadRange(i + 1, url, new File(downloadRangePath + File.separator + downloadName + File.separator + partFileName), startRange, endRange);

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
            String partFileName = downloadName + ".00" + 1;
            downloadRange = new HttpDownloadRange(1, url, new File(downloadRangePath + File.separator + downloadName + File.separator + partFileName), startRange, size);
            addDownloadRange(downloadRange);
            downloadRange.resume();
        }
        if (downloadInfoListener != null)
            downloadInfoListener.downloadNeedSaved(this);
        startTransferRate();
    }
}
