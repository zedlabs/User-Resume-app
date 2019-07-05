package tk.zedlabs.sidb.POJO;

import com.google.gson.annotations.SerializedName;

import tk.zedlabs.sidb.POJO.Data;

public class DataResponse {

    @SerializedName("data")
    private Data data;

    public Data getAuthData() {
        return data;
    }

}
