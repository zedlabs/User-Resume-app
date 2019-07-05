package tk.zedlabs.sidb.POJO;

import com.google.gson.annotations.SerializedName;

public class DataResponseEducation {

    @SerializedName("data")
    private Education education;

    public Education getEducation() {
        return education;
    }
}
