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

import enums.DownloadStatus;
import enums.ProtocolType;
import gui.listener.DownloadInfoListener;
import gui.listener.DownloadStatusListener;

import java.io.File;
import java.net.URL;
import java.util.List;

/**
 * @author <a href="kayvanfar.sj@gmail.com">Saeed Kayvanfar</a> 10/17/2015
 */
public interface Download {

    int getId();

    void setId(int id);

    URL getUrl();

    void setUrl(URL url);

    String getDownloadName();

    void setDownloadName(String downloadName);

    int getSize();

    void setSize(int size);

    DownloadStatus getStatus();

    void setStatus(DownloadStatus status);

    String getTransferRate();

    ProtocolType getProtocolType();

    void setProtocolType(ProtocolType protocolType);

    String getDescription();

    void setDescription(String description);

    int getDownloaded();

    void setDownloaded(int downloaded);

    File getDownloadPath();

    void setDownloadPath(File downloadPath);

    File getDownloadRangePath();

    void setDownloadRangePath(File downloadRangePath);

    int getResponseCode();

    boolean isResumeCapability();

    // Get this download's size.
    String getFormattedSize();

    // Get this download's progress.
    float getProgress();

    int getConnectTimeout();

    void setConnectTimeout(int connectTimeout);

    int getReadTimeout();

    void setReadTimeout(int readTimeout);

    /**
     * Adds an DownloadStatusListener to the set of downloadStatusListeners for this object, provided
     * that it is not the same as some DownloadStatusListener already in the set.
     * The order in which notifications will be delivered to multiple
     * DownloadDialogListeners is not specified. See the class comment.
     *
     * @param   downloadStatusListener   an DownloadStatusListener to be added.
     * @throws NullPointerException   if the parameter o is null.
     */
    void addDownloadStatusListener(DownloadStatusListener downloadStatusListener);

    /**
     * Deletes an DownloadStatusListener from the set of downloadStatusListeners of this object.
     * Passing <CODE>null</CODE> to this method will have no effect.
     * @param   downloadStatusListener   the DownloadStatusListener to be deleted.
     */
    void deleteDownloadStatusListener(DownloadStatusListener downloadStatusListener);

    List<DownloadRange> getDownloadRangeList();

    void setDownloadRangeList(List<DownloadRange> downloadRangeList);

    void setDownloadInfoListener(DownloadInfoListener downloadInfoListener);

    void removeDownloadInfo(DownloadInfoListener downloadInfoListener);

    // Pause this download.
    void pause();

    // Resume this download.
    void resume();

    void createDownloadRanges();

    // add a new downloadRange if not in downloadRangeList
    void addDownloadRange(DownloadRange downloadRange);

    // reset data of download for redownload
    void resetData();

    void startTransferRate();
}
