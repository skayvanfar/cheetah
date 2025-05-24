package model.httpsImpl;

import enums.DownloadStatus;
import enums.ProtocolType;
import gui.listener.DownloadInfoListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.net.ssl.HttpsURLConnection;
import java.io.File;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class HttpsDownloadTest {

    private HttpsDownload httpsDownload;
    private URL url;
    private File downloadPath;
    private File downloadRangePath;
    private DownloadInfoListener downloadInfoListener;

    @BeforeEach
    void setUp() throws Exception {
        url = new URL("https://example.com/file");
        downloadPath = new File("downloads");
        downloadRangePath = new File("ranges");
        httpsDownload = new HttpsDownload(1, url, "file", 1, downloadPath, downloadRangePath, ProtocolType.HTTPS);

        // Mock listener to avoid NPE and verify callbacks
        downloadInfoListener = mock(DownloadInfoListener.class);
        httpsDownload.setDownloadInfoListener(downloadInfoListener);
    }

    @Test
    void testCallSuccessfulResponse() throws Exception {
        // Mock HttpsURLConnection and its behavior
        HttpsURLConnection connection = mock(HttpsURLConnection.class);

        // When url.openConnection() is called inside call(), return our mock connection
        URL spyUrl = spy(url);
        doReturn(connection).when(spyUrl).openConnection();

        // Replace the url in httpsDownload with the spy to intercept openConnection()
        httpsDownload.setUrl(spyUrl);

        when(connection.getResponseCode()).thenReturn(200);
        when(connection.getHeaderField("Content-Disposition")).thenReturn("attachment=downloadedFile.txt");
        when(connection.getContentLength()).thenReturn(1000);

        // Just verify no exception and state is as expected after call()
        httpsDownload.call();

        // Verify expected setters/fields were updated
        assertEquals(200, httpsDownload.getResponseCode());
        assertEquals("downloadedFile.txt", httpsDownload.getDownloadName());
        assertEquals(1000, httpsDownload.getSize());
        assertEquals(DownloadStatus.DOWNLOADING, httpsDownload.getStatus()); // status doesn't explicitly change here for success
        verify(connection).setRequestProperty("Range", "bytes=0-");
        verify(connection).setRequestMethod("HEAD");
        verify(connection).connect();
        verify(connection).disconnect();

        // Verify listener was notified
        verify(downloadInfoListener).newDownloadInfoGot(httpsDownload);
    }
}
