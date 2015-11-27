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
 * @author <a href="kayvanfar.sj@gmail.com">Saeed Kayvanfar</a> 9/14/2015
 */
public enum ConnectionStatus {

    // These are the status names.
    CONNECTING(0, "Connecting..."), // 0
    SEND_GET(1, "Send GET..."),// 1
    RECEIVING_DATA(2, "Receiving Data..."),  // 2
    DISCONNECTED(3, "disconnect"), // 3
    WAITING_RESPONSE(4, "Waiting Response..."), // 4
    ERROR(5, "Error"), // 5
    COMPLETED(6, "Completed"), // 6
    DISCONNECTING(7, "Disconnecting"); // 7

    private Integer value;
    private String desc;

    private ConnectionStatus(Integer value, String desc) {
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
    public static ConnectionStatus valueOf(Integer type) {
        for (ConnectionStatus code : ConnectionStatus.values()) {
            if (Objects.equals(type, code.getValue())) {
                return code;
            }
        }
        return null;
    }

}
