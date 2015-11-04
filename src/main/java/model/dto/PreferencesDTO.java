package model.dto;

import java.io.Serializable;

/**
 * Created by Saeed on 9/24/2015.
 */
public class PreferencesDTO implements Serializable {

    private PreferencesGeneralDTO preferencesGeneralDTO;
    private PreferencesConnectionDTO preferencesConnectionDTO;
    private PreferencesSaveDTO preferencesSaveDTO;
    private PreferencesProxyDTO preferencesProxyDTO;
    private PreferencesInterfaceDTO  preferencesInterfaceDTO;

    public PreferencesDTO() {

    }

    public PreferencesGeneralDTO getPreferencesGeneralDTO() {
        return preferencesGeneralDTO;
    }

    public void setPreferencesGeneralDTO(PreferencesGeneralDTO preferencesGeneralDTO) {
        this.preferencesGeneralDTO = preferencesGeneralDTO;
    }

    public PreferencesConnectionDTO getPreferencesConnectionDTO() {
        return preferencesConnectionDTO;
    }

    public void setPreferencesConnectionDTO(PreferencesConnectionDTO preferencesConnectionDTO) {
        this.preferencesConnectionDTO = preferencesConnectionDTO;
    }

    public PreferencesSaveDTO getPreferencesSaveDTO() {
        return preferencesSaveDTO;
    }

    public void setPreferencesSaveDTO(PreferencesSaveDTO preferencesSaveDTO) {
        this.preferencesSaveDTO = preferencesSaveDTO;
    }

    public PreferencesProxyDTO getPreferencesProxyDTO() {
        return preferencesProxyDTO;
    }

    public void setPreferencesProxyDTO(PreferencesProxyDTO preferencesProxyDTO) {
        this.preferencesProxyDTO = preferencesProxyDTO;
    }

    public PreferencesInterfaceDTO getPreferencesInterfaceDTO() {
        return preferencesInterfaceDTO;
    }

    public void setPreferencesInterfaceDTO(PreferencesInterfaceDTO preferencesInterfaceDTO) {
        this.preferencesInterfaceDTO = preferencesInterfaceDTO;
    }
}
