package in.squarei.socialconnect.modals;

/**
 * Created by mohit kumar on 5/3/2017.
 */

public class UserProfiledata {
    private String userName, userMobile, userAddress, userCity, userZipcode, userState, userLandmark, userCountry, userProfilePic, userFirstName, userLastName, userEmailAddress, userGender, userProfilePolicy,userMobilePolicy;

    public UserProfiledata(String userName, String userMobile, String userAddress, String userCity, String userZipcode, String userState, String userLandmark, String userCountry, String userProfilePic, String userFirstName, String userLastName, String userEmailAddress, String userGender, String userProfilePolicy, String userMobilePolicy) {
        this.userName = userName;
        this.userMobile = userMobile;
        this.userAddress = userAddress;
        this.userCity = userCity;
        this.userZipcode = userZipcode;
        this.userState = userState;
        this.userLandmark = userLandmark;
        this.userCountry = userCountry;
        this.userProfilePic = userProfilePic;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userEmailAddress = userEmailAddress;
        this.userGender = userGender;
        this.userProfilePolicy=userProfilePolicy;
        this.userMobilePolicy=userMobilePolicy;

    }

    public String getUserMobilePolicy() {
        return userMobilePolicy;
    }

    public void setUserMobilePolicy(String userMobilePolicy) {
        this.userMobilePolicy = userMobilePolicy;
    }

    public String getUserProfilePolicy() {
        return userProfilePolicy;
    }

    public void setUserProfilePolicy(String userProfilePolicy) {
        this.userProfilePolicy = userProfilePolicy;
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

    public String getUserProfilePic() {
        return userProfilePic;
    }

    public void setUserProfilePic(String userProfilePic) {
        this.userProfilePic = userProfilePic;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserEmailAddress() {
        return userEmailAddress;
    }

    public void setUserEmailAddress(String userEmailAddress) {
        this.userEmailAddress = userEmailAddress;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }
}