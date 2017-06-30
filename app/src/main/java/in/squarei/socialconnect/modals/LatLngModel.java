package in.squarei.socialconnect.modals;

/**
 * Created by mohit kumar on 6/28/2017.
 */

public class LatLngModel {
    private Double lat, lng;

    public LatLngModel(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
