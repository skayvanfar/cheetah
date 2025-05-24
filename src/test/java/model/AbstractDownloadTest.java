package model;

import gui.listener.DownloadInfoListener;
import gui.listener.DownloadStatusListener;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import enums.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;

class AbstractDownloadTest {

    private AbstractDownload download;

    private static class TestDownload extends AbstractDownload {
        public TestDownload(int id, URL url, String downloadName, int partCount, File downloadPath, File downloadRangePath, ProtocolType protocolType) {
            super(id, url, downloadName, partCount, downloadPath, downloadRangePath, protocolType);
        }

        @Override
        public Void performDownload() {
            return null; // no-op
        }

        @Override
        public void createDownloadRanges() {
            // no-op for tests
        }
    }

    private URL url = new URL("http://example.com/file");
    private File downloadPath = new File("testDownload");
    private File downloadRangePath = new File("testRange");

    AbstractDownloadTest() throws MalformedURLException {
    }

    @BeforeEach
    void setUp() throws Exception {
        download = new TestDownload(1, url, "file", 1, downloadPath, downloadRangePath, ProtocolType.HTTP);
    }

    @Test
    void testGettersSetters() {
        download.setId(5);
        assertEquals(5, download.getId());

        download.setDownloadName("newName");
        assertEquals("newName", download.getDownloadName());

        download.setSize(1000);
        assertEquals(1000, download.getSize());

        download.setDownloaded(200);
        assertEquals(200, download.getDownloaded());

        download.setStatus(DownloadStatus.PAUSED);
        assertEquals(DownloadStatus.PAUSED, download.getStatus());

        download.setDescription("desc");
        assertEquals("desc", download.getDescription());

        download.setProtocolType(ProtocolType.HTTPS);
        assertEquals(ProtocolType.HTTPS, download.getProtocolType());

        download.setConnectTimeout(2000);
        assertEquals(2000, download.getConnectTimeout());

        download.setReadTimeout(3000);
        assertEquals(3000, download.getReadTimeout());

        download.setDownloadPath(new File("path2"));
        assertEquals("path2", download.getDownloadPath().getPath());
    }

    @Test
    void testAddAndRemoveDownloadStatusListener() {
        DownloadStatusListener listener = mock(DownloadStatusListener.class);

        download.addDownloadStatusListener(listener);
        download.notifyStatusChanged();

        // The listener should receive callback
        verify(listener, timeout(1000)).downloadStatusChanged(download);

        download.deleteDownloadStatusListener(listener);
        download.notifyStatusChanged();
        // After removal, listener should not get called again (timeout means fail if no call)
        verifyNoMoreInteractions(listener);
    }

    @Test
    void testAddDownloadRange() {
        DownloadRange range = mock(DownloadRange.class);
        DownloadInfoListener infoListener = mock(DownloadInfoListener.class);

        download.setDownloadInfoListener(infoListener);
        download.addDownloadRange(range);

        assertTrue(download.getDownloadRangeList().contains(range));
        verify(range).addDownloadRangeStatusListener(download);
        verify(infoListener).newDownloadRangeEventOccured(range);

        // Adding same range again does not duplicate
        download.addDownloadRange(range);
        assertEquals(1, download.getDownloadRangeList().size());
    }

    @Test
    void testPauseResumesStatusAndCallsRanges() {
        DownloadRange range = mock(DownloadRange.class);
        download.setDownloadRangeList(Collections.singletonList(range));

        download.pause();
        assertEquals(DownloadStatus.DISCONNECTING, download.getStatus());
        verify(range).disConnect();

        reset(range);
        download.resume();
        assertEquals(DownloadStatus.DOWNLOADING, download.getStatus());
        verify(range).resume();
    }

    @Test
    void testResetData() {
        DownloadRange range = mock(DownloadRange.class);
        download.setDownloadRangeList(Collections.singletonList(range));
        download.setDownloaded(100);
        download.setStatus(DownloadStatus.PAUSED);

        download.resetData();

        assertEquals(0, download.getDownloaded());
        assertEquals(DownloadStatus.DOWNLOADING, download.getStatus());
        verify(range).resetData();
    }

    @Test
    void testGetProgress() {
        download.setSize(1000);
        download.setDownloaded(500);

        assertEquals(0.5f, download.getProgress());
    }

    @Test
    void testEqualsAndHashCode() {
        TestDownload another = new TestDownload(1, url, "file", 1, downloadPath, downloadRangePath, ProtocolType.HTTP);
        assertEquals(download, another);
        assertEquals(download.hashCode(), another.hashCode());

        another.setId(2);
        assertNotEquals(download, another);
    }

    // More tests can be written for updateInfo, endOfDownload, error, etc.
}
