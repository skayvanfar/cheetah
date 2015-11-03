package model.dto;

import gui.preference.PreferenceGeneralPanel;

import java.io.Serializable;

/**
 * Created by Saeed on 9/24/2015.
 */
public class PreferencesDTO implements Serializable {

    private PreferenceGeneralDTO preferenceGeneralDTO;
    private PreferenceConnectionDTO preferenceConnectionDTO;
    private PreferencesSaveDTO preferencesSaveDTO;
    private PreferenceProxyDTO preferenceProxyDTO;

    public PreferencesDTO() {

    }

    public PreferenceGeneralDTO getPreferenceGeneralDTO() {
        return preferenceGeneralDTO;
    }

    public void setPreferenceGeneralDTO(PreferenceGeneralDTO preferenceGeneralDTO) {
        this.preferenceGeneralDTO = preferenceGeneralDTO;
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

    public PreferenceProxyDTO getPreferenceProxyDTO() {
        return preferenceProxyDTO;
    }

    public void setPreferenceProxyDTO(PreferenceProxyDTO preferenceProxyDTO) {
        this.preferenceProxyDTO = preferenceProxyDTO;
    }
}
