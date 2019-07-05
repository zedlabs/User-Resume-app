package tk.zedlabs.sidb.POJO;

public class Professional {
    private String end_date;
    private int uid;
    private String organisation;
    private String designation;
    private int id;
    private String start_date;

    public Professional(String end_date, String organisation, String designation, String start_date) {
        this.end_date = end_date;
        this.organisation = organisation;
        this.designation = designation;
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public int getUid() {
        return uid;
    }

    public String getOrganisation() {
        return organisation;
    }

    public String getDesignation() {
        return designation;
    }

    public int getId() {
        return id;
    }

    public String getStart_date() {
        return start_date;
    }
}
