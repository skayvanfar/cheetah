package enums;

import java.util.Objects;

/**
 * Created by Saeed on 10/17/2015.
 */
public enum ProtocolType {

    HTTP(0, "http"),
    FTP(1, "ftp"),
    HTTPS(2, "https");

    private Integer value;
    private String desc;

    private ProtocolType(Integer value, String desc) {
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
    public static ProtocolType valueOf(Integer type) {
        for (ProtocolType code : ProtocolType.values()) {
            if (Objects.equals(type, code.getValue())) {
                return code;
            }
        }
        return null;
    }

    // static factory method
    public static ProtocolType valueOfByDesc(String desc) {
        for (ProtocolType code : ProtocolType.values()) {
            if (desc.equals(code.getDesc())) {
                return code;
            }
        }
        return null;
    }
}
