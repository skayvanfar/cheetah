package model.dto;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by Saeed on 9/24/2015.
 */
public class PreferencesDirectoryCategoryDTO implements Serializable {

    private String DirectoryName;
    private String path;
    private String[] fileExtensions;
    private String iconPath;

    public PreferencesDirectoryCategoryDTO() {

    }

    public PreferencesDirectoryCategoryDTO(String directoryName, String path, String[] fileExtensions, String iconPath) {
        DirectoryName = directoryName;
        this.path = path;
        this.fileExtensions = fileExtensions;
        this.iconPath = iconPath;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDirectoryName() {
        return DirectoryName;
    }

    public void setDirectoryName(String directoryName) {
        DirectoryName = directoryName;
    }

    public String[] getFileExtensions() {
        return fileExtensions;
    }

    public void setFileExtensions(String[] fileExtensions) {
        this.fileExtensions = fileExtensions;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    @Override
    public String toString() {
        return DirectoryName;
    }

    public boolean isIncludeFileExtension(String fileExtension) {
        return Arrays.asList(fileExtensions).contains(fileExtension);
    }
}
