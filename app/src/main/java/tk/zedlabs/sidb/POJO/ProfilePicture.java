package tk.zedlabs.sidb.POJO;

import android.util.Base64;

public class ProfilePicture {

    private String photo;
    private Integer uid;

    public ProfilePicture(String photo, Integer uid) {
        this.photo = photo;
        this.uid = uid;
    }

    public String getImage() {
        return photo;
    }
    public Integer getId() {
        return uid;
    }


}
