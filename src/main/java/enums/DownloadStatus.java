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

package enums;

import java.util.Objects;

/**
 * @author <a href="kayvanfar.sj@gmail.com">Saeed Kayvanfar</a> 9/10/2015
 */
public enum DownloadStatus {
    // These are the status names.
    DOWNLOADING(0, "Downloading"),// 0
    PAUSED(1, "Paused"),  // 1
    COMPLETE(2, "Complete"), // 2
    CANCELLED(3, "canceled"), // 3
    ERROR(4, "Error"), // 4
    DISCONNECTING(5, "Disconnecting"),
    DISCONNECTED(6, "Disconnected"); // when server not respond

    private Integer value;
    private String desc;

    private DownloadStatus(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }


    // static factory method
    public static DownloadStatus valueOf(Integer type) {
        for (DownloadStatus code : DownloadStatus.values()) {
            if (Objects.equals(type, code.getValue())) {
                return code;
            }
        }
        return null;
    }
}
