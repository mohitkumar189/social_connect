package in.squarei.socialconnect.modals;

/**
 * Created by mohit kumar on 5/8/2017.
 */

public class UsersListData {
    private String userId, userName, userStatus, userProfilePic, userType;

    public UsersListData(String userId, String userName, String userStatus, String userProfilePic, String userType) {
        this.userId = userId;
        this.userName = userName;
        this.userStatus = userStatus;
        this.userProfilePic = userProfilePic;
        this.userType = userType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserProfilePic() {
        return userProfilePic;
    }

    public void setUserProfilePic(String userProfilePic) {
        this.userProfilePic = userProfilePic;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
