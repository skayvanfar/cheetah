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

package model;

import enums.ConnectionStatus;
import gui.listener.DownloadRangeStatusListener;

import java.io.File;
import java.net.URL;

/**
 * @author <a href="kayvanfar.sj@gmail.com">Saeed Kayvanfar</a> 10/17/2015
 */
public interface DownloadRange {

    int getId();

    void setId(int id);

    URL getUrl();

    void setUrl(URL url);

    int getNumber();

    void setNumber(int number);

    int getRangeSize();

    void setRangeSize(int rangeSize);

    int getRangeDownloaded();

    void setRangeDownloaded(int rangeDownloaded);

    ConnectionStatus getConnectionStatus();

    void setConnectionStatus(ConnectionStatus connectionStatus);

    int getStartRange();

    void setStartRange(int startRange);

    int getEndRange();

    void setEndRange(int endRange);

    File getDownloadRangeFile();

    void setDownloadRangeFile(File downloadRangeFile);

    int getConnectTimeout();

    void setConnectTimeout(int connectTimeout);

    int getReadTimeout();

    void setReadTimeout(int readTimeout);

    /**
     * Adds an DownloadRangeStatusListener to the set of downloadRangeStatusListeners for this object, provided
     * that it is not the same as some DownloadRangeStatusListener already in the set.
     * The order in which notifications will be delivered to multiple
     * downloadRangeStatusListeners is not specified. See the class comment.
     *
     * @param   downloadRangeStatusListener   an DownloadRangeStatusListener to be added.
     * @throws NullPointerException   if the parameter is null.
     */
    void addDownloadRangeStatusListener(DownloadRangeStatusListener downloadRangeStatusListener);

    /**
     * Deletes an DownloadRangeStatusListener from the set of downloadRangeStatusListeners of this object.
     * Passing <CODE>null</CODE> to this method will have no effect.
     * @param   downloadRangeStatusListener   the DownloadRangeStatusListener to be deleted.
     */
    void deleteDownloadRangeStatusListener(DownloadRangeStatusListener downloadRangeStatusListener);

    // Pause this download.
    void disConnect();

    // Resume this download.
    void resume();

    // Reset data
    void resetData();
}
