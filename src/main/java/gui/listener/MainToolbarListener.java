package gui.listener;

/**
 * Created by Saeed on 9/10/2015.
 */
public interface MainToolbarListener {
    public void newDownloadEventOccured();
    public void pauseEventOccured();
    public void resumeEventOccured();
    public void pauseAllEventOccured();
    public void clearEventOccured();
    public void clearAllCompletedEventOccured();
    public void reJoinEventOccured();
    public void reDownloadEventOccured();
    public void propertiesEventOccured();
    public void preferencesEventOccured();
}
