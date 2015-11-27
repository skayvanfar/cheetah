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
 * @author <a href="kayvanfar.sj@gmail.com">Saeed Kayvanfar</a> 11/3/2015
 */
public class PreferencesProxyDTO implements Serializable {

    private int proxySettingType;

    private boolean useProxyNotSocks;

    // http
    private String httpProxyAddress;
    private int httpProxyPort;
    private String httpProxyUserName;
    private String httpProxyPassword;

    // https
    private String httpsProxyAddress;
    private int httpsProxyPort;
    private String httpsProxyUserName;
    private String httpsProxyPassword;

    // ftp
    private String ftpProxyAddress;
    private int ftpProxyPort;
    private String ftpProxyUserName;
    private String ftpProxyPassword;

    // socks
    private String socksProxyAddress;
    private int socksProxyPort;
    private String socksProxyUserName;
    private String socksProxyPassword;

    public int getProxySettingType() {
        return proxySettingType;
    }

    public void setProxySettingType(int proxySettingType) {
        this.proxySettingType = proxySettingType;
    }

    public boolean isUseProxyNotSocks() {
        return useProxyNotSocks;
    }

    public void setUseProxyNotSocks(boolean useProxyNotSocks) {
        this.useProxyNotSocks = useProxyNotSocks;
    }

    public String getHttpProxyAddress() {
        return httpProxyAddress;
    }

    public void setHttpProxyAddress(String httpProxyAddress) {
        this.httpProxyAddress = httpProxyAddress;
    }

    public int getHttpProxyPort() {
        return httpProxyPort;
    }

    public void setHttpProxyPort(int httpProxyPort) {
        this.httpProxyPort = httpProxyPort;
    }

    public String getHttpProxyUserName() {
        return httpProxyUserName;
    }

    public void setHttpProxyUserName(String httpProxyUserName) {
        this.httpProxyUserName = httpProxyUserName;
    }

    public String getHttpProxyPassword() {
        return httpProxyPassword;
    }

    public void setHttpProxyPassword(String httpProxyPassword) {
        this.httpProxyPassword = httpProxyPassword;
    }

    public String getHttpsProxyAddress() {
        return httpsProxyAddress;
    }

    public void setHttpsProxyAddress(String httpsProxyAddress) {
        this.httpsProxyAddress = httpsProxyAddress;
    }

    public int getHttpsProxyPort() {
        return httpsProxyPort;
    }

    public void setHttpsProxyPort(int httpsProxyPort) {
        this.httpsProxyPort = httpsProxyPort;
    }

    public String getHttpsProxyUserName() {
        return httpsProxyUserName;
    }

    public void setHttpsProxyUserName(String httpsProxyUserName) {
        this.httpsProxyUserName = httpsProxyUserName;
    }

    public String getHttpsProxyPassword() {
        return httpsProxyPassword;
    }

    public void setHttpsProxyPassword(String httpsProxyPassword) {
        this.httpsProxyPassword = httpsProxyPassword;
    }

    public String getFtpProxyAddress() {
        return ftpProxyAddress;
    }

    public void setFtpProxyAddress(String ftpProxyAddress) {
        this.ftpProxyAddress = ftpProxyAddress;
    }

    public int getFtpProxyPort() {
        return ftpProxyPort;
    }

    public void setFtpProxyPort(int ftpProxyPort) {
        this.ftpProxyPort = ftpProxyPort;
    }

    public String getFtpProxyUserName() {
        return ftpProxyUserName;
    }

    public void setFtpProxyUserName(String ftpProxyUserName) {
        this.ftpProxyUserName = ftpProxyUserName;
    }

    public String getFtpProxyPassword() {
        return ftpProxyPassword;
    }

    public void setFtpProxyPassword(String ftpProxyPassword) {
        this.ftpProxyPassword = ftpProxyPassword;
    }

    public String getSocksProxyAddress() {
        return socksProxyAddress;
    }

    public void setSocksProxyAddress(String socksProxyAddress) {
        this.socksProxyAddress = socksProxyAddress;
    }

    public int getSocksProxyPort() {
        return socksProxyPort;
    }

    public void setSocksProxyPort(int socksProxyPort) {
        this.socksProxyPort = socksProxyPort;
    }

    public String getSocksProxyUserName() {
        return socksProxyUserName;
    }

    public void setSocksProxyUserName(String socksProxyUserName) {
        this.socksProxyUserName = socksProxyUserName;
    }

    public String getSocksProxyPassword() {
        return socksProxyPassword;
    }

    public void setSocksProxyPassword(String socksProxyPassword) {
        this.socksProxyPassword = socksProxyPassword;
    }
}
