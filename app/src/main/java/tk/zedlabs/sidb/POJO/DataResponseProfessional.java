package tk.zedlabs.sidb.POJO;

import com.google.gson.annotations.SerializedName;

public class DataResponseProfessional {

    @SerializedName("data")
    private Professional professional;

    public Professional getProfessional() {
        return professional;
    }

}
