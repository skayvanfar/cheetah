package controller;

import javax.swing.*;

/**
 * Created by Saeed on 9/13/2015.
 */
public abstract class PausableSwingWorker<K, V> extends SwingWorker<K, V> {
    @Override
    protected K doInBackground() throws Exception {
        return null;
    }

    private volatile boolean isPaused;

    public final void pause() {
        if (!isPaused() && !isDone()) {
            isPaused = true;
            firePropertyChange("paused", false, true);
        }
    }

    public final void resume() {
        if (isPaused() && !isDone()) {
            isPaused = false;
            firePropertyChange("paused", true, false);
        }
    }

    public final boolean isPaused() {
        return isPaused;
    }
}
