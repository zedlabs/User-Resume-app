package tk.zedlabs.sidb.POJO;

public class Education {
    private int uid;
    private String image_path;
    private String start_year;
    private String degree;
    private String organisation;
    private String location;
    private int id;
    private String end_year;


    public Education(String start_year, String degree, String organisation, String location, String end_year) {
        this.start_year = start_year;
        this.degree = degree;
        this.organisation = organisation;
        this.location = location;
        this.end_year = end_year;
    }

    public int getUid() {
        return uid;
    }

    public String getImage_path() {
        return image_path;
    }

    public String getStart_year() {
        return start_year;
    }

    public String getDegree() {
        return degree;
    }

    public String getOrganisation() {
        return organisation;
    }

    public String getLocation() {
        return location;
    }

    public int getId() {
        return id;
    }

    public String getEnd_year() {
        return end_year;
    }
}
