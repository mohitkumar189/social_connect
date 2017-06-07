package in.squarei.socialconnect.modals;

/**
 * Created by mohit kumar on 6/6/2017.
 */

public class Users {
    private int id, owner_id;
    private String full_name;

    public Users(int id, String full_name, int owner_id) {
        this.id = id;
        this.full_name = full_name;
        this.owner_id = owner_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }
}
