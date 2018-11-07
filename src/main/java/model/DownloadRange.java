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
 * An abstract representation of a part of download file.
 *
 * @author <a href="kayvanfar.sj@gmail.com">Saeed Kayvanfar</a> 10/17/2015
 * @see model.Download
 */
public interface DownloadRange {

    /**
     * Gets the id of this {@code Download}.
     *
     * @return the id of this {@code Download}
     */
    int getId();

    void setId(int id);

    /**
     * Gets the url of this {@code Download}.
     *
     * @return the url of this {@code Download}
     */
    URL getUrl();

    void setUrl(URL url);

    /**
     * Gets the number of this {@code Download}.
     *
     * @return the number of this {@code Download}
     */
    int getNumber();

    void setNumber(int number);

    /**
     * Gets the size of this {@code Download}.
     *
     * @return the size of this {@code Download}
     */
    int getRangeSize();

    void setRangeSize(int rangeSize);

    /**
     * Gets amount of downloaded of this {@code Download}.
     *
     * @return amount of downloaded of this {@code Download}
     */
    int getRangeDownloaded();

    void setRangeDownloaded(int rangeDownloaded);

    /**
     * Gets the connectionStatus of this {@code Download}.
     *
     * @return the connectionStatus of this {@code Download}
     */
    ConnectionStatus getConnectionStatus();

    void setConnectionStatus(ConnectionStatus connectionStatus);

    /**
     * Gets the start of this {@code Download}.
     *
     * @return the start of this {@code Download}
     */
    int getStartRange();

    void setStartRange(int startRange);

    /**
     * Gets the end of this {@code Download}.
     *
     * @return the end of this {@code Download}
     */
    int getEndRange();

    void setEndRange(int endRange);

    /**
     * Gets the downloadRangeFile of this {@code Download}.
     *
     * @return the downloadRangeFile of this {@code Download}
     */
    File getDownloadRangeFile();

    void setDownloadRangeFile(File downloadRangeFile);

    /**
     * Gets the connectTimeout of this {@code Download}.
     *
     * @return the connectTimeout of this {@code Download}
     */
    int getConnectTimeout();

    void setConnectTimeout(int connectTimeout);

    /**
     * Gets the url of this {@code Download}.
     *
     * @return the url of this {@code Download}
     */
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

    /**
     * Pause this download.
     */
    void disConnect();

    /**
     * Resume this download.
     */
    void resume();

    /**
     * Reset data
     */
    void resetData();
}
