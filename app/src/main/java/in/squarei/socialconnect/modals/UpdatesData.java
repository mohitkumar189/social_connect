package in.squarei.socialconnect.modals;

/**
 * Created by mohit kumar on 5/23/2017.
 */

public class UpdatesData {
    private String imageUrl, location, title;

    public UpdatesData(String imageUrl, String location, String title) {
        this.imageUrl = imageUrl;
        this.location = location;
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
