package tk.zedlabs.sidb.POJO;

import com.google.gson.annotations.SerializedName;

public class DataResponsePersonal {

    @SerializedName("data")
    private Personal personalData;

    public Personal getPersonalData(){ return personalData;}
}
