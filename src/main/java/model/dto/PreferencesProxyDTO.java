package model.dto;

import java.io.Serializable;

/**
 * Created by sad.keyvanfar on 11/3/2015.
 */
public class PreferencesProxyDTO implements Serializable {

    private int proxySettingType;

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

    public int getProxySettingType() {
        return proxySettingType;
    }

    public void setProxySettingType(int proxySettingType) {
        this.proxySettingType = proxySettingType;
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
}
