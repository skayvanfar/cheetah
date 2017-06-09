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

import enums.ConnectionType;
import enums.ProxyType;
import service.ProxyService;

import java.util.Enumeration;
import java.util.Properties;

/**
 * @author <a href="kayvanfar.sj@gmail.com">Saeed Kayvanfar</a> 11/5/2015
 */
public class ProxyServiceImpl implements ProxyService {

    @Override
    public void setNoProxy() {
        // TODO: 6/9/17 must implemented.
//        System.getProperties().put("proxySet", "false");
//        System.setProperty("http.proxyHost", null);
//        System.setProperty("https.proxyHost", null);
//        System.setProperty("ftp.proxyHost", null);
    }

    @Override
    public void setUseSystemProxy() {
        System.setProperty("java.net.useSystemProxies", "true");
    }

    public void setProxy(ProxyType proxyType, String urlText, int port, String userName, String password) {
        if (!urlText.equals("") && port != 0) {
            System.setProperty(proxyType.getDesc() + ".proxyHost", urlText);
            System.setProperty(proxyType.getDesc() + ".proxyPort", String.valueOf(port));
            System.setProperty(proxyType.getDesc() + ".nonProxyHosts", "localhost|127.0.0.1");
        }

        if (!userName.equals("") && !password.equals("")) {
            System.setProperty(proxyType.getDesc() + ".proxyUser", userName);
            System.setProperty(proxyType.getDesc() + ".proxyPassword", password);
        }
    }

}
