package in.squarei.socialconnect.modals;

/**
 * Created by mohit kumar on 5/16/2017.
 */

public class CommunitiesData {
    String id, title, brief_desc, full_desc, status, admin_id, admin_fname, admin_lname;

    public CommunitiesData(String id, String title, String status, String admin_id, String admin_fname, String admin_lname) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.admin_id = admin_id;
        this.admin_fname = admin_fname;
        this.admin_lname = admin_lname;
    }

    public CommunitiesData(String id, String title, String status, String admin_id) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.admin_id = admin_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrief_desc() {
        return brief_desc;
    }

    public void setBrief_desc(String brief_desc) {
        this.brief_desc = brief_desc;
    }

    public String getFull_desc() {
        return full_desc;
    }

    public void setFull_desc(String full_desc) {
        this.full_desc = full_desc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(String admin_id) {
        this.admin_id = admin_id;
    }

    public String getAdmin_fname() {
        return admin_fname;
    }

    public void setAdmin_fname(String admin_fname) {
        this.admin_fname = admin_fname;
    }

    public String getAdmin_lname() {
        return admin_lname;
    }

    public void setAdmin_lname(String admin_lname) {
        this.admin_lname = admin_lname;
    }
}
