package model.dto;

import javax.swing.*;
import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by Saeed on 9/24/2015.
 */
public class PreferencesDirectoryCategoryDTO implements Serializable {

    private String DirectoryName;
    private String path;
    private String[] fileExtensions;
  //  private Icon icon; Todo for future

    public PreferencesDirectoryCategoryDTO() {

    }

    public PreferencesDirectoryCategoryDTO(String directoryName, String path, String[] fileExtensions) {
        DirectoryName = directoryName;
        this.path = path;
        this.fileExtensions = fileExtensions;
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

    @Override
    public String toString() {
        return DirectoryName;
    }

    public boolean isIncludeFileExtension(String fileExtension) {
        return Arrays.asList(fileExtensions).contains(fileExtension);
    }
}
