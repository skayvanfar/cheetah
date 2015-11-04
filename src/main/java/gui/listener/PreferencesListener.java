package gui.listener;

import model.dto.PreferencesDTO;

/**
 * Created by Saeed on 9/13/2015.
 */
public interface PreferencesListener {
    public void preferencesSet(PreferencesDTO preferenceDTO);
    public void preferenceReset();
    public void preferenceDefaults();
}
