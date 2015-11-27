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

package service;

/**
 * @author <a href="kayvanfar.sj@gmail.com">Saeed Kayvanfar</a> 11/5/2015
 */
public interface ProxyService {
    public void setNoProxy();
    public void setUseSystemProxy();
    public void setHttpProxy(String urlText, int port, String userName, String password);
    public void setHttpsProxy(String urlText, int port, String userName, String password);
    public void setFtpProxy(String urlText, int port, String userName, String password);
    public void setSocksProxy(String urlText, int port, String userName, String password);
}