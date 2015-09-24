package model.dto;

import java.io.Serializable;

/**
 * Created by Saeed on 9/24/2015.
 */
public class PreferenceConnectionDTO implements Serializable {

    private int maxConnectionNumber;

    public PreferenceConnectionDTO() {
    }

    public PreferenceConnectionDTO(int maxConnectionNumber) {
        this.maxConnectionNumber = maxConnectionNumber;
    }

    public int getMaxConnectionNumber() {
        return maxConnectionNumber;
    }

    public void setMaxConnectionNumber(int maxConnectionNumber) {
        this.maxConnectionNumber = maxConnectionNumber;
    }
}
