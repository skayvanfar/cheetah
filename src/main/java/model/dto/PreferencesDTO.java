package model.dto;

import java.io.Serializable;

/**
 * Created by Saeed on 9/24/2015.
 */
public class PreferencesDTO implements Serializable {

    private PreferenceConnectionDTO preferenceConnectionDTO;
    private PreferencesSaveDTO preferencesSaveDTO;

    public PreferencesDTO() {

    }

    public PreferenceConnectionDTO getPreferenceConnectionDTO() {
        return preferenceConnectionDTO;
    }

    public void setPreferenceConnectionDTO(PreferenceConnectionDTO preferenceConnectionDTO) {
        this.preferenceConnectionDTO = preferenceConnectionDTO;
    }

    public PreferencesSaveDTO getPreferencesSaveDTO() {
        return preferencesSaveDTO;
    }

    public void setPreferencesSaveDTO(PreferencesSaveDTO preferencesSaveDTO) {
        this.preferencesSaveDTO = preferencesSaveDTO;
    }
}
