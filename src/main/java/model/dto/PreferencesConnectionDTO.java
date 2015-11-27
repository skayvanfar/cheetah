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

import enums.ConnectionType;

import java.io.Serializable;

/**
 * @author <a href="kayvanfar.sj@gmail.com">Saeed Kayvanfar</a> 9/24/2015
 */
public class PreferencesConnectionDTO implements Serializable {

    private ConnectionType connectionType;
    private int maxConnectionNumber;
    private int timeBetweenAttempts;
    private int maxNumberAttempts;
    private int connectionTimeOut;
    private int readTimeOut;


    public PreferencesConnectionDTO() {
    }

    public PreferencesConnectionDTO(ConnectionType connectionType, int maxConnectionNumber, int timeBetweenAttempts, int maxNumberAttempts, int connectionTimeOut, int readTimeOut) {
        this.connectionType = connectionType;
        this.maxConnectionNumber = maxConnectionNumber;
        this.timeBetweenAttempts = timeBetweenAttempts;
        this.maxNumberAttempts = maxNumberAttempts;
        this.connectionTimeOut = connectionTimeOut;
        this.readTimeOut = readTimeOut;
    }

    public PreferencesConnectionDTO(int maxConnectionNumber) {
        this.maxConnectionNumber = maxConnectionNumber;
    }

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(ConnectionType connectionType) {
        this.connectionType = connectionType;
    }

    public int getMaxConnectionNumber() {
        return maxConnectionNumber;
    }

    public void setMaxConnectionNumber(int maxConnectionNumber) {
        this.maxConnectionNumber = maxConnectionNumber;
    }

    public int getTimeBetweenAttempts() {
        return timeBetweenAttempts;
    }

    public void setTimeBetweenAttempts(int timeBetweenAttempts) {
        this.timeBetweenAttempts = timeBetweenAttempts;
    }

    public int getMaxNumberAttempts() {
        return maxNumberAttempts;
    }

    public void setMaxNumberAttempts(int maxNumberAttempts) {
        this.maxNumberAttempts = maxNumberAttempts;
    }

    public int getConnectionTimeOut() {
        return connectionTimeOut;
    }

    public void setConnectionTimeOut(int connectionTimeOut) {
        this.connectionTimeOut = connectionTimeOut;
    }

    public int getReadTimeOut() {
        return readTimeOut;
    }

    public void setReadTimeOut(int readTimeOut) {
        this.readTimeOut = readTimeOut;
    }
}
