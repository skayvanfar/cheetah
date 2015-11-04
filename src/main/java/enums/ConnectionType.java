package enums;

import java.util.Objects;

/**
 * Created by Saeed on 11/3/2015.
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
