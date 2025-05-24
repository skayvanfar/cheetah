package model;

import enums.ProtocolType;

import java.io.File;
import java.net.URL;

public class TestDownload extends AbstractDownload {
    public TestDownload(int id, URL url, String downloadName, int partCount, File downloadPath, File downloadRangePath, ProtocolType protocolType) {
        super(id, url, downloadName, partCount, downloadPath, downloadRangePath, protocolType);
    }

    @Override
    public Void call() {
        return null; // no-op
    }

    @Override
    public void createDownloadRanges() {
        // no-op for tests
    }
}
