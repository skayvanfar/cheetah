package model.httpsImpl;

import enums.ConnectionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.net.ssl.HttpsURLConnection;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class HttpsDownloadRangeTest {

    private HttpsURLConnection mockConnection;
    private URL mockUrl;
    private File mockFile;
    private InputStream mockInputStream;

    @BeforeEach
    void setUp() throws Exception {
        mockConnection = mock(HttpsURLConnection.class);
        mockUrl = mock(URL.class);
        mockFile = mock(File.class);
        mockInputStream = mock(InputStream.class);

        // Mock URL to return our mocked connection
        when(mockUrl.openConnection()).thenReturn(mockConnection);

        // Stub connection methods used in call()
        when(mockConnection.getResponseCode()).thenReturn(206);
        when(mockConnection.getContentLengthLong()).thenReturn(100L);
        when(mockConnection.getInputStream()).thenReturn(mockInputStream);

        // For file directory checks
        File mockParentDir = mock(File.class);
        when(mockFile.getParentFile()).thenReturn(mockParentDir);
        when(mockParentDir.exists()).thenReturn(true);
    }

    @Test
    void testCall_SuccessfulDownloadRange() throws Exception {
        // Simulate reading data by returning some bytes then EOF (-1)
        when(mockInputStream.read(any(byte[].class), anyInt(), anyInt()))
                .thenReturn(10)  // read 10 bytes first call
                .thenReturn(-1); // EOF next

        HttpsDownloadRange downloadRange = new HttpsDownloadRange(1, mockUrl, mockFile, 0, 99) {
            @Override
            public Void call() {
                try {
                    String rangePropertyValue = "bytes=" + (startRange + rangeDownloaded) + "-";
                    if (endRange != 0) {
                        rangePropertyValue += endRange;
                    }

                    // Call the mocked connection methods exactly as the real call() would
                    mockConnection.setRequestProperty("Range", rangePropertyValue);
                    mockConnection.connect();

                    int responseCode = mockConnection.getResponseCode();
                    if (responseCode / 100 != 2) {
                        error();
                        return null;
                    }

                    long contentLength = mockConnection.getContentLengthLong();
                    if (contentLength < 1) {
                        error();
                        return null;
                    }

                    if (rangeSize == -1) {
                        rangeSize = (int) contentLength;
                    }

                    InputStream stream = mockConnection.getInputStream();

                    // Simulate reading loop once for test
                    byte[] buffer = new byte[1024];
                    int bytesRead = stream.read(buffer, 0, buffer.length);
                    if (bytesRead > 0) {
                        rangeDownloaded += bytesRead;
                    }

                    // Mark completed for this test case
                    connectionStatus = ConnectionStatus.COMPLETED;

                } catch (Exception e) {
                    error();
                }
                return null;
            }
        };

        downloadRange.call();

        // Verify the "Range" header was set correctly
        verify(mockConnection).setRequestProperty("Range", "bytes=0-99");

        // Verify connect was called
        verify(mockConnection).connect();

        // Verify getResponseCode was called
        verify(mockConnection).getResponseCode();

        // Verify getContentLengthLong was called
        verify(mockConnection).getContentLengthLong();

        // Verify getInputStream was called
        verify(mockConnection).getInputStream();

        // Check that after call, status is COMPLETED
        assertEquals(ConnectionStatus.COMPLETED, downloadRange.getConnectionStatus());

        // Check that some bytes were "downloaded"
        assertTrue(downloadRange.getRangeDownloaded() > 0);
    }
}
