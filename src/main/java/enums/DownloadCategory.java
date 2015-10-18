package enums;

/**
 * Created by saeed on 10/14/15.
 */
public enum DownloadCategory {

    ALL(0, "All Downloads"),
    UNFINISHED(1, "Unfinished"),
    FINISHED(2, "Finished");

    private Integer value;
    private String desc;

    private DownloadCategory(Integer value, String desc) {
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
    public static DownloadCategory valueOf(Integer type) {
        for (DownloadCategory code : DownloadCategory.values()) {
            if (type == code.getValue()) {
                return code;
            }
        }
        return null;
    }

    // static factory method
    public static DownloadCategory valueOfByDesc(String desc) {
        for (DownloadCategory code : DownloadCategory.values()) {
            if (desc == code.getDesc()) {
                return code;
            }
        }
        return null;
    }
}