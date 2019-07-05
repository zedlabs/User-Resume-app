package tk.zedlabs.sidb.POJO;

import com.google.gson.annotations.SerializedName;

public class Test {

    private String status;

    @SerializedName("server_name")
    private String serverName;

    private String url;

    public String getStatus() {
        return status;
    }

    public String getServerName() {
        return serverName;
    }

    public String getUrl() {
        return url;
    }
}
