package model;

import enums.ConnectionStatus;
import gui.listener.DownloadRangeStatusListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class AbstractDownloadRangeTest {

    private AbstractDownloadRange downloadRange;

    // Concrete subclass for testing
    private static class TestDownloadRange extends AbstractDownloadRange {
        public TestDownloadRange(int number, URL url, File downloadRangeFile, int startRange, int endRange) {
            super(number, url, downloadRangeFile, startRange, endRange);
        }

        @Override
        public Void call() {
            // Dummy implementation for test
            return null;
        }
    }

    private URL testUrl;
    private File testFile;

    @BeforeEach
    void setUp() throws MalformedURLException {
        testUrl = new URL("http://example.com/file");
        testFile = new File("testfile.part");
        downloadRange = new TestDownloadRange(1, testUrl, testFile, 0, 1000);
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals(1, downloadRange.getNumber());
        assertEquals(testUrl, downloadRange.getUrl());
        assertEquals(0, downloadRange.getStartRange());
        assertEquals(1000, downloadRange.getEndRange());
        assertEquals(testFile, downloadRange.getDownloadRangeFile());
        assertEquals(ConnectionStatus.CONNECTING, downloadRange.getConnectionStatus());
        assertEquals(-1, downloadRange.getRangeSize());
        assertEquals(0, downloadRange.getRangeDownloaded());
    }

    @Test
    void testSetters() throws MalformedURLException {
        URL newUrl = new URL("http://example.org/otherfile");
        File newFile = new File("otherfile.part");

        downloadRange.setId(42);
        assertEquals(42, downloadRange.getId());

        downloadRange.setUrl(newUrl);
        assertEquals(newUrl, downloadRange.getUrl());

        downloadRange.setNumber(10);
        assertEquals(10, downloadRange.getNumber());

        downloadRange.setRangeSize(500);
        assertEquals(500, downloadRange.getRangeSize());

        downloadRange.setRangeDownloaded(250);
        assertEquals(250, downloadRange.getRangeDownloaded());

        downloadRange.setConnectionStatus(ConnectionStatus.RECEIVING_DATA);
        assertEquals(ConnectionStatus.RECEIVING_DATA, downloadRange.getConnectionStatus());

        downloadRange.setStartRange(50);
        assertEquals(50, downloadRange.getStartRange());

        downloadRange.setEndRange(1500);
        assertEquals(1500, downloadRange.getEndRange());

        downloadRange.setDownloadRangeFile(newFile);
        assertEquals(newFile, downloadRange.getDownloadRangeFile());

        downloadRange.setConnectTimeout(3000);
        assertEquals(3000, downloadRange.getConnectTimeout());

        downloadRange.setReadTimeout(5000);
        assertEquals(5000, downloadRange.getReadTimeout());
    }

    @Test
    void testAddAndDeleteDownloadRangeStatusListener() {
        AtomicBoolean called = new AtomicBoolean(false);
        DownloadRangeStatusListener listener = (range, readed) -> called.set(true);

        downloadRange.addDownloadRangeStatusListener(listener);
        assertThrows(NullPointerException.class, () -> downloadRange.addDownloadRangeStatusListener(null));

        // Trigger state change
        downloadRange.stateChanged(123);
        assertTrue(called.get());

        called.set(false);
        downloadRange.deleteDownloadRangeStatusListener(listener);

        // After removal, listener shouldn't be called
        downloadRange.stateChanged(456);
        assertFalse(called.get());

        assertThrows(NullPointerException.class, () -> downloadRange.deleteDownloadRangeStatusListener(null));
    }

    @Test
    void testDisConnect() {
        downloadRange.setConnectionStatus(ConnectionStatus.CONNECTING);
        downloadRange.disConnect();
        assertEquals(ConnectionStatus.DISCONNECTING, downloadRange.getConnectionStatus());
        // stop flag should be true (can't directly check private, but can test via resume behavior)
    }

    @Test
    void testResumeWhenNotCompleted() {
        downloadRange.setConnectionStatus(ConnectionStatus.RECEIVING_DATA);
        downloadRange.resume();
        // Can't easily test internal submit, but no exception and stop flag cleared
        assertFalse(downloadRange.stop);
    }

    @Test
    void testResumeWhenCompleted() {
        downloadRange.setConnectionStatus(ConnectionStatus.COMPLETED);
        downloadRange.resume();
        // Since it's completed, resume should do nothing, stop flag stays unchanged (default false)
        assertFalse(downloadRange.stop);
    }

    @Test
    void testResetData() {
        downloadRange.setRangeSize(1000);
        downloadRange.setRangeDownloaded(500);
        downloadRange.setConnectionStatus(ConnectionStatus.ERROR);

        downloadRange.resetData();

        assertEquals(-1, downloadRange.getRangeSize());
        assertEquals(0, downloadRange.getRangeDownloaded());
        assertEquals(ConnectionStatus.CONNECTING, downloadRange.getConnectionStatus());
    }

    @Test
    void testEqualsAndHashCode() throws MalformedURLException {
        AbstractDownloadRange other = new TestDownloadRange(1, testUrl, testFile, 0, 1000);
        other.setId(downloadRange.getId());
        other.setRangeSize(downloadRange.getRangeSize());
        other.setRangeDownloaded(downloadRange.getRangeDownloaded());
        other.setConnectionStatus(downloadRange.getConnectionStatus());

        assertEquals(downloadRange, other);
        assertEquals(downloadRange.hashCode(), other.hashCode());

        other.setNumber(2);
        assertNotEquals(downloadRange, other);
    }

    @Test
    void testToStringContainsFields() {
        String s = downloadRange.toString();
        assertTrue(s.contains("number=1"));
        assertTrue(s.contains("url=http://example.com/file"));
        assertTrue(s.contains("rangeSize=-1"));
    }
}
