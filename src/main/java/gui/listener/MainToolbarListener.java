package gui.listener;

/**
 * Created by Saeed on 9/10/2015.
 */
public interface MainToolbarListener {
    public void newDownloadEventOccured();
    public void pauseEventOccured();
    public void resumeEventOccured();
    public void cancelEventOccured();
    public void clearEventOccured();
    public void clearAllCompletedEventOccured();
    public void preferencesEventOccured();
}
