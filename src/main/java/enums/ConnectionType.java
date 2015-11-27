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
 * @author <a href="kayvanfar.sj@gmail.com">Saeed Kayvanfar</a> 11/3/2015
 */
public enum ConnectionType {
    NotSet(0, "I don't know"),
    LOW(1, "Low speed: Dial Up modem / ISDN / Bluetooth / Mobile Edge / IrDA"),
    MEDIUM(2, "Medium speed: ADSL / DSL /  Mobile 3G / Wi-Fi / Bluetooth 3.0 / other"),
    HIGH(3, "High speed: Direct connection (Ethernet/Cable) / Wi-Fi / Mobile 4G / other");

    private Integer value;
    private String desc;

    private ConnectionType(Integer value, String desc) {
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
    public static ConnectionType valueOf(Integer type) {
        for (ConnectionType code : ConnectionType.values()) {
            if (Objects.equals(type, code.getValue())) {
                return code;
            }
        }
        return null;
    }

    // static factory method
    public static ConnectionType valueOfByDesc(String desc) {
        for (ConnectionType code : ConnectionType.values()) {
            if (desc.equals(code.getDesc())) {
                return code;
            }
        }
        return null;
    }
}
