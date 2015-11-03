package model.dto;

import java.io.Serializable;

/**
 * Created by sad.keyvanfar on 11/3/2015.
 */
public class PreferenceGeneralDTO implements Serializable {

    private boolean launchOnsStartup;

    public PreferenceGeneralDTO(boolean launchOnsStartup) {
        this.launchOnsStartup = launchOnsStartup;
    }

    public boolean isLaunchOnsStartup() {
        return launchOnsStartup;
    }

    public void setLaunchOnsStartup(boolean launchOnsStartup) {
        this.launchOnsStartup = launchOnsStartup;
    }
}
