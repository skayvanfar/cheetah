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

package service.impl;

import service.ProxyService;

import java.util.Enumeration;
import java.util.Properties;

/**
 * @author <a href="kayvanfar.sj@gmail.com">Saeed Kayvanfar</a> 11/5/2015
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
