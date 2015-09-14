package model;

import enums.DownloadState;

import java.io.BufferedInputStream;
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

    enum SizeType {BYTE, KILOBYTE, MEGABYTE, TERABYTE}

    ;

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
    }

    // Get this download's URL.
    public String getUrl() {
        return url.toString();
    }

    // Get this download's size.
    public String getSize() {
        return roundSizeTypeFormat((float) size, SizeType.BYTE);
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
    }

    // Resume this download.
    public void resume() {
        status = DownloadState.DOWNLOADING;
        stateChanged();
        download();
    }

    // Cancel this download.
    public void cancel() {
        status = DownloadState.CANCELLED;
        stateChanged();
    }

    // Mark this download as having an error.
    private void error() {
        status = DownloadState.ERROR;
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
                while (status == DownloadState.DOWNLOADING) {
                    Integer previousDownloaded = threadLocal.get();

                    if (previousDownloaded == null) {
                        previousDownloaded = 0;
                    }
                    int newDownloaded = downloaded;
                    int differenceDownloaded = (newDownloaded - previousDownloaded); // in Byte

                    // save new downloaded into threadLocal
                    threadLocal.set(newDownloaded);

                    // calculate differenceDownloaded
                    transferRate = roundSizeTypeFormat(differenceDownloaded, SizeType.BYTE);
                    stateChanged();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        /**     Second way that use SwingWorker
         SwingWorker<Void, String> worker = new PausableSwingWorker<Void, String>() {

         ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>();

         @Override protected Void doInBackground() throws Exception {
         while (status == DownloadState.DOWNLOADING) {
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

    public String roundSizeTypeFormat(float transferRate, SizeType sizeType) {
        if (transferRate < 999) {
            String transferRateFormated = String.format("%.2f", transferRate);
            switch (sizeType) {
                case BYTE:
                    return transferRateFormated + " B";
                case KILOBYTE:
                    return transferRateFormated + " KB";
                case MEGABYTE:
                    return transferRateFormated + " MB";
                case TERABYTE:
                    return transferRateFormated + " TB";
                default:
                    return String.valueOf(transferRate);
            }
        } else {
            return roundSizeTypeFormat(transferRate / 1024, SizeType.values()[sizeType.ordinal() + 1]);
        }
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

            ////////////////////////////////////////////// for test performance
            BufferedInputStream bin = new BufferedInputStream(stream);
            ////////////////////////// for test
            long before = System.currentTimeMillis();
            //////////////////////////////////////////////////////////////////

            while (status == DownloadState.DOWNLOADING) {
                /* Size buffer according to how much of the
                  file is left to download. */
                byte buffer[];
                if (size - downloaded > MAX_BUFFER_SIZE) {
                    buffer = new byte[MAX_BUFFER_SIZE];
                } else {
                    buffer = new byte[size - downloaded];
                }



                //////////////////????????????????????????? for test performance
                //      int bytesAvailable = stream.available( );
                //       System.out.println("bytesAvailable  ==  " + bytesAvailable);

                //       int bytesRead=0;
                //      int bytesToRead=1024;
                //      byte[] input = new byte[bytesToRead];
                //      int result = 0;
                //       while (bytesRead < bytesToRead) {
                //          result = stream.read(input, bytesRead, bytesToRead - bytesRead);
                //           System.out.println("result   ==  " + result);
                //           if (result == -1) {
                //               break;
                //           }
                //           bytesRead += result;
                //       }

                //       if (result == -1) {
                //           break;
                //       }

                //       file.write(input, 0, bytesToRead);
                //       downloaded += result;
                //////////////////???????????????????????/?
                ///    int bytesAvailable = stream.available();
                //      if (bytesAvailable == 0 && downloaded != size)
                //           continue;

                //       byte[] input = new byte[bytesAvailable];
                //       int bytesRead = stream.read(input, 0, bytesAvailable);
                //       if (bytesRead == 0)
                //          break;
                //       file.write(input, 0, bytesRead);
                //      downloaded += bytesRead;

                /////////////////////>?????????????????????

                //       int bytesAvailable = stream.available( );
                //       System.out.println("bytesAvailable  ==  " + bytesAvailable)
                /////////////////////////////////////////////////////////////////////


                // Read from server into buffer.
                int read = stream.read(buffer);
                if (read == -1)
                    break;

                // Write buffer to file.
                file.write(buffer, 0, read);
                downloaded += read;
                if (getProgress() - previousProgress > 1) { // when 1% changed
                    stateChanged();
                }
            }

            ////////////////////////////////////////////// for test performance
            long after = System.currentTimeMillis();
            long diff = after - before;
            System.out.println("long time is: " + diff);
            ///////////////////////////////////////////////////////////////////

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
        /////////////////////////////////////////////// save previous progress
        previousProgress = getProgress();

        setChanged();
        notifyObservers();
    }
}
