package model.dto;

import enums.ConnectionType;

import java.io.Serializable;

/**
 * Created by Saeed on 9/24/2015.
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
