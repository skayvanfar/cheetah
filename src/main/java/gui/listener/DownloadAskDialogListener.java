package gui.listener;

/**
 * Created by Saeed on 10/9/15.
 */
public interface DownloadAskDialogListener {
    public void startDownloadEventOccured(String path);
    public void cancelDownloadEventOccured();
    public void laterDownloadEventOccured();
}
