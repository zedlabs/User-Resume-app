package tk.zedlabs.sidb.POJO;

import android.media.Image;

public class Personal {
/**
 *  "skills": "pop,slo,ss",
 *         "image": null,
 *         "uid": 1,
 *         "mobile_no": "78922",
 *         "name": "kj",
 *         "links": "www.com",
 *         "location": "lucknow",
 *         "id": 1,
 *         "email": "lol@est.com"
 */
    private String skills;
    private Image image;
    private Integer uid;
    private String mobile_no;
    private String name;
    private String links;
    private String location;
    private Integer id;
    private String email;

    public Personal(String skills, String mobile_no, String name, String links, String location, String email) {
        this.skills = skills;
        this.mobile_no = mobile_no;
        this.name = name;
        this.links = links;
        this.location = location;
        this.email = email;
    }

    public String getSkills() {
        return skills;
    }

    public Image getImage() {
        return image;
    }

    public Integer getUid() {
        return uid;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public String getName() {
        return name;
    }

    public String getLinks() {
        return links;
    }

    public String getLocation() {
        return location;
    }

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
