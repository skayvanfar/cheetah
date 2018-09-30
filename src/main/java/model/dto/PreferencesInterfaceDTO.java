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
 * @author <a href="kayvanfar.sj@gmail.com">Saeed Kayvanfar</a> 11/4/2015
 */
public class PreferencesInterfaceDTO implements Serializable {

    private String lookAndFeelName;
    private String localName;

    public String getLookAndFeelName() {
        return lookAndFeelName;
    }

    public void setLookAndFeelName(String lookAndFeelName) {
        this.lookAndFeelName = lookAndFeelName;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    @Override
    public String toString() {
        return "PreferencesInterfaceDTO{" +
                "lookAndFeelName='" + lookAndFeelName + '\'' +
                ", localName='" + localName + '\'' +
                '}';
    }
}
