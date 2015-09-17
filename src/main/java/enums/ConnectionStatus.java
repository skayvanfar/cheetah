package enums;

/**
 * Created by Saeed on 9/14/2015.
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
            if (type == code.getValue()) {
                return code;
            }
        }
        return null;
    }

}
