package model.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Saeed on 9/24/2015.
 */
public class PreferencesSaveDTO implements Serializable {

    private List<PreferencesDirectoryCategoryDTO> preferencesDirectoryCategoryDTOs;

    public PreferencesSaveDTO() {
    }

    public List<PreferencesDirectoryCategoryDTO> getPreferencesDirectoryCategoryDTOs() {
        return preferencesDirectoryCategoryDTOs;
    }

    public void setPreferencesDirectoryCategoryDTOs(List<PreferencesDirectoryCategoryDTO> preferencesDirectoryCategoryDTOs) {
        this.preferencesDirectoryCategoryDTOs = preferencesDirectoryCategoryDTOs;
    }
}
