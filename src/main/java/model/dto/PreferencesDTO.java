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

import java.io.Serializable;

/**
 * @author <a href="kayvanfar.sj@gmail.com">Saeed Kayvanfar</a> 9/24/2015
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
        if (preferencesGeneralDTO == null)
            preferencesGeneralDTO = new PreferencesGeneralDTO(false);
        return preferencesGeneralDTO;
    }

    public void setPreferencesGeneralDTO(PreferencesGeneralDTO preferencesGeneralDTO) {
        this.preferencesGeneralDTO = preferencesGeneralDTO;
    }

    public PreferencesConnectionDTO getPreferencesConnectionDTO() {
        if (preferencesConnectionDTO == null)
            preferencesConnectionDTO = new PreferencesConnectionDTO();
        return preferencesConnectionDTO;
    }

    public void setPreferencesConnectionDTO(PreferencesConnectionDTO preferencesConnectionDTO) {
        this.preferencesConnectionDTO = preferencesConnectionDTO;
    }

    public PreferencesSaveDTO getPreferencesSaveDTO() {
        if (preferencesSaveDTO == null)
            preferencesSaveDTO = new PreferencesSaveDTO();
        return preferencesSaveDTO;
    }

    public void setPreferencesSaveDTO(PreferencesSaveDTO preferencesSaveDTO) {
        this.preferencesSaveDTO = preferencesSaveDTO;
    }

    public PreferencesProxyDTO getPreferencesProxyDTO() {
        if (preferencesProxyDTO == null)
            preferencesProxyDTO = new PreferencesProxyDTO();
        return preferencesProxyDTO;
    }

    public void setPreferencesProxyDTO(PreferencesProxyDTO preferencesProxyDTO) {
        this.preferencesProxyDTO = preferencesProxyDTO;
    }

    public PreferencesInterfaceDTO getPreferencesInterfaceDTO() {
        if (preferencesInterfaceDTO == null)
            preferencesInterfaceDTO = new PreferencesInterfaceDTO();
        return preferencesInterfaceDTO;
    }

    public void setPreferencesInterfaceDTO(PreferencesInterfaceDTO preferencesInterfaceDTO) {
        this.preferencesInterfaceDTO = preferencesInterfaceDTO;
    }

    @Override
    public String toString() {
        return "PreferencesDTO{" +
                "preferencesGeneralDTO=" + preferencesGeneralDTO +
                ", preferencesConnectionDTO=" + preferencesConnectionDTO +
                ", preferencesSaveDTO=" + preferencesSaveDTO +
                ", preferencesProxyDTO=" + preferencesProxyDTO +
                ", preferencesInterfaceDTO=" + preferencesInterfaceDTO +
                '}';
    }
}
