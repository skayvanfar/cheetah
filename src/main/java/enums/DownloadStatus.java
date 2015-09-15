package enums;

/**
 * Created by Saeed on 9/10/2015.
 */
public enum DownloadStatus {
    // These are the status names.
    DOWNLOADING(0, "Downloading"),// 0
    PAUSED(0, "Paused"),  // 1
    COMPLETE(0, "Complete"), // 2
    CANCELLED(0, "canceled"), // 3
    ERROR(0, "Error"); // 4

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
            if (type == code.getValue()) {
                return code;
            }
        }
        return null;
    }
}
