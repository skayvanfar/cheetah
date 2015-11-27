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

    public int getId();

    public void setId(int id);

    public URL getUrl();

    public void setUrl(URL url);

    public String getDownloadName();

    public void setDownloadName(String downloadName);

    public int getSize();

    public void setSize(int size);

    public DownloadStatus getStatus();

    public void setStatus(DownloadStatus status);

    public String getTransferRate();

    public ProtocolType getProtocolType();

    public void setProtocolType(ProtocolType protocolType);

    public String getDescription();

    public void setDescription(String description);

    public int getDownloaded();

    public void setDownloaded(int downloaded);

    public File getDownloadPath();

    public void setDownloadPath(File downloadPath);

    public File getDownloadRangePath();

    public void setDownloadRangePath(File downloadRangePath);

    public int getResponseCode();

    public boolean isResumeCapability();

    // Get this download's size.
    public String getFormattedSize();

    // Get this download's progress.
    public float getProgress();

    public int getConnectTimeout();

    public void setConnectTimeout(int connectTimeout);

    public int getReadTimeout();

    public void setReadTimeout(int readTimeout);

    /**
     * Adds an DownloadStatusListener to the set of downloadStatusListeners for this object, provided
     * that it is not the same as some DownloadStatusListener already in the set.
     * The order in which notifications will be delivered to multiple
     * DownloadDialogListeners is not specified. See the class comment.
     *
     * @param   downloadStatusListener   an DownloadStatusListener to be added.
     * @throws NullPointerException   if the parameter o is null.
     */
    public void addDownloadStatusListener(DownloadStatusListener downloadStatusListener);

    /**
     * Deletes an DownloadStatusListener from the set of downloadStatusListeners of this object.
     * Passing <CODE>null</CODE> to this method will have no effect.
     * @param   downloadStatusListener   the DownloadStatusListener to be deleted.
     */
    public void deleteDownloadStatusListener(DownloadStatusListener downloadStatusListener);

    public List<DownloadRange> getDownloadRangeList();

    public void setDownloadRangeList(List<DownloadRange> downloadRangeList);

    public void setDownloadInfoListener(DownloadInfoListener downloadInfoListener);

    public void removeDownloadInfo(DownloadInfoListener downloadInfoListener);

    // Pause this download.
    public void pause();

    // Resume this download.
    public void resume();

    public void createDownloadRanges();

    // add a new downloadRange if not in downloadRangeList
    public void addDownloadRange(DownloadRange downloadRange);

    // reset data of download for redownload
    public void resetData();
}
