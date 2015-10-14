package gui.listener;

import enums.DownloadCategory;

import java.util.List;

/**
 * Created by saeed on 10/13/15.
 */
public interface CategoryPanelListener {
    public void categoryNodeSelected(List<String> fileExtensions, DownloadCategory downloadCategory);
}
