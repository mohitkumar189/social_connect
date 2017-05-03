package in.squarei.socialconnect.modals;

/**
 * Created by mohit kumar on 5/3/2017.
 */

public class UserProfiledata {
    private String userName, userMobile, userAddress, userCity, userZipcode, userState, userLandmark, userCountry, userProfilePic;

    public UserProfiledata(String userName, String userMobile, String userAddress, String userCity, String userZipcode, String userState, String userLandmark, String userCountry, String userProfilePic) {
        this.userName = userName;
        this.userMobile = userMobile;
        this.userAddress = userAddress;
        this.userCity = userCity;
        this.userZipcode = userZipcode;
        this.userState = userState;
        this.userLandmark = userLandmark;
        this.userCountry = userCountry;
        this.userProfilePic = userProfilePic;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserCity() {
        return userCity;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }

    public String getUserZipcode() {
        return userZipcode;
    }

    public void setUserZipcode(String userZipcode) {
        this.userZipcode = userZipcode;
    }

    public String getUserState() {
        return userState;
    }

    public void setUserState(String userState) {
        this.userState = userState;
    }

    public String getUserLandmark() {
        return userLandmark;
    }

    public void setUserLandmark(String userLandmark) {
        this.userLandmark = userLandmark;
    }

    public String getUserCountry() {
        return userCountry;
    }

    public void setUserCountry(String userCountry) {
        this.userCountry = userCountry;
    }
}
