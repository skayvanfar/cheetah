package model.dto;

import java.io.Serializable;

/**
 * Created by Saeed on 11/4/2015.
 */
public class PreferencesInterfaceDTO implements Serializable {

    private String lookAndFeelName;

    public String getLookAndFeelName() {
        return lookAndFeelName;
    }

    public void setLookAndFeelName(String lookAndFeelName) {
        this.lookAndFeelName = lookAndFeelName;
    }
}
