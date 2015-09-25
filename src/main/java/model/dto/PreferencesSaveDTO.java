package model.dto;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Saeed on 9/24/2015.
 */
public class PreferencesSaveDTO implements Serializable {

    private List<PreferencesDirectoryCategoryDTO> preferencesDirectoryCategoryDTOs;
    private String tempDirectory;

    public PreferencesSaveDTO() {
    }

    public List<PreferencesDirectoryCategoryDTO> getPreferencesDirectoryCategoryDTOs() {
        return preferencesDirectoryCategoryDTOs;
    }

    public void setPreferencesDirectoryCategoryDTOs(List<PreferencesDirectoryCategoryDTO> preferencesDirectoryCategoryDTOs) {
        this.preferencesDirectoryCategoryDTOs = preferencesDirectoryCategoryDTOs;
    }

    public String getTempDirectory() {
        return tempDirectory;
    }

    public void setTempDirectory(String tempDirectory) {
        this.tempDirectory = tempDirectory;
    }

    public String getPathByFileExtension(String fileExtension) {
        for (PreferencesDirectoryCategoryDTO preferencesDirectoryCategoryDTO : preferencesDirectoryCategoryDTOs) {
            if (preferencesDirectoryCategoryDTO.isIncludeFileExtension(fileExtension))
                return preferencesDirectoryCategoryDTO.getPath();
        }
        return (new File(preferencesDirectoryCategoryDTOs.get(0).getPath())).getParentFile().getPath();
    }
}
