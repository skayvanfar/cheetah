package service.impl;

import service.ProxyService;

import java.util.Enumeration;
import java.util.Properties;

/**
 * Created by Saeed on 11/5/2015.
 */
public class ProxyServiceImpl implements ProxyService {

    @Override
    public void setNoProxy() {
//        System.getProperties().put("proxySet", "false");
//        System.setProperty("http.proxyHost", null);
//        System.setProperty("https.proxyHost", null);
//        System.setProperty("ftp.proxyHost", null);
    }

    @Override
    public void setUseSystemProxy() {

    }

    @Override
    public void setHttpProxy(String urlText, int port, String userName, String password) {
        if (urlText != "" && port != 0) {
            System.getProperties().put("proxySet", "true");
            System.setProperty("http.proxyHost", urlText);
            System.setProperty("http.proxyPort", String.valueOf(port));
        }

        if (userName != "" && password != "") {
            System.setProperty("http.proxyUser", userName);
            System.setProperty("http.proxyPassword", password);
        }
        System.setProperty("http.nonProxyHosts", "localhost|127.0.0.1");
        Properties probs = System.getProperties();
        Enumeration keys = probs.keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            System.out.println("key: " + key + " value: " + probs.get(key));
        }
    }

    @Override
    public void setHttpsProxy(String urlText, int port, String userName, String password) {
        if (urlText != "" && port != 0) {
            System.setProperty("https.proxyHost", urlText);
            System.setProperty("https.proxyPort", String.valueOf(port));
            System.setProperty("http.nonProxyHosts", "localhost|127.0.0.1");
        }

        if (userName != "" && password != "") {
            System.setProperty("https.proxyUser", userName);
            System.setProperty("https.proxyPassword", password);
        }
    }

    @Override
    public void setFtpProxy(String urlText, int port, String userName, String password) {
        if (urlText != "" && port != 0) {
            System.setProperty("ftp.proxyHost", urlText);
            System.setProperty("ftp.proxyPort", String.valueOf(port));
            System.setProperty("http.nonProxyHosts", "localhost|127.0.0.1");
        }

        if (userName != "" && password != "") {
            System.setProperty("ftp.proxyUser", userName);
            System.setProperty("ftp.proxyPassword", password);
        }
    }

    @Override
    public void setSocksProxy(String urlText, int port, String userName, String password) {
        if (urlText != "" && port != 0) {
            System.setProperty("socks.proxyHost", urlText);
            System.setProperty("socks.proxyPort", String.valueOf(port));
            System.setProperty("http.nonProxyHosts", "localhost|127.0.0.1");
        }

        if (userName != null && password != null) {
            System.setProperty("socks.proxyUser", userName);
            System.setProperty("socks.proxyPassword", password);
        }
    }
}
