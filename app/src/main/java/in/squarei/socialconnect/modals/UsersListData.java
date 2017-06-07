package in.squarei.socialconnect.modals;

/**
 * Created by mohit kumar on 5/8/2017.
 */

public class UsersListData {
    private String userId, userName, userStatus, userProfilePic, userType, userChatId;
    private boolean isFriend;

    public UsersListData(String userId, String userName, String userStatus, String userProfilePic, String userType, boolean isFriend, String userChatId) {
        this.userId = userId;
        this.userName = userName;
        this.userStatus = userStatus;
        this.userProfilePic = userProfilePic;
        this.userType = userType;
        this.isFriend = isFriend;
        this.userChatId = userChatId;
    }

    public UsersListData() {

    }

    public String getUserChatId() {
        return userChatId;
    }

    public void setUserChatId(String userChatId) {
        this.userChatId = userChatId;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
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
