/*
 * Cheetah - A Free Fast Downloader
 *
 * Copyright © 2015 Saeed Kayvanfar
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package model.dto;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="kayvanfar.sj@gmail.com">Saeed Kayvanfar</a> 9/24/2015
 */
public class PreferencesSaveDTO implements Serializable {

    private List<PreferencesDirectoryCategoryDTO> preferencesDirectoryCategoryDTOs;
    private String tempDirectory;
    private String databasePath;
    private String logPath;

    public PreferencesSaveDTO() {
    }

    public List<PreferencesDirectoryCategoryDTO> getPreferencesDirectoryCategoryDTOs() {
        if (preferencesDirectoryCategoryDTOs == null)
            preferencesDirectoryCategoryDTOs = new ArrayList<>();
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

    public String getDatabasePath() {
        return databasePath;
    }

    public void setDatabasePath(String databasePath) {
        this.databasePath = databasePath;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public String getPathByFileExtension(String fileExtension) {
        for (PreferencesDirectoryCategoryDTO preferencesDirectoryCategoryDTO : preferencesDirectoryCategoryDTOs) {
            if (preferencesDirectoryCategoryDTO.isIncludeFileExtension(fileExtension))
                return preferencesDirectoryCategoryDTO.getPath();
        }
        return (new File(preferencesDirectoryCategoryDTOs.get(0).getPath())).getParentFile().getPath();
    }

    @Override
    public String toString() {
        return "PreferencesSaveDTO{" +
                "preferencesDirectoryCategoryDTOs=" + preferencesDirectoryCategoryDTOs +
                ", tempDirectory='" + tempDirectory + '\'' +
                ", databasePath='" + databasePath + '\'' +
                ", logPath='" + logPath + '\'' +
                '}';
    }
}
