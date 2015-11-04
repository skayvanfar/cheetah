package model.dto;

import java.io.Serializable;

/**
 * Created by sad.keyvanfar on 11/3/2015.
 */
public class PreferencesGeneralDTO implements Serializable {

    private boolean launchOnsStartup;

    public PreferencesGeneralDTO(boolean launchOnsStartup) {
        this.launchOnsStartup = launchOnsStartup;
    }

    public boolean isLaunchOnsStartup() {
        return launchOnsStartup;
    }

    public void setLaunchOnsStartup(boolean launchOnsStartup) {
        this.launchOnsStartup = launchOnsStartup;
    }
}
