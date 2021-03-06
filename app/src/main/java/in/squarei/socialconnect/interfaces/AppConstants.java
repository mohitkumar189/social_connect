package in.squarei.socialconnect.interfaces;

/**
 * Created by mohit kumar on 4/26/2017.
 */

public interface AppConstants {

    String APP_NAME = "social_connect";
    int APP_SPLASH_TIME = 3000;

    /* animation type*/
    int ANIMATION_SLIDE_UP = 0x01;
    int ANIMATION_SLIDE_LEFT = 0x02;

    // User Status Details
    String IS_LOGIN = "islogin";
    String PASS_KEY = "passKey";
    String USER_KEY = "userKey";
    String USER_PIN = "userPin";
    String PIN_STATUS = "pinStatus"; // true if set, false is not set
    String IS_INTRO_COMPLETED = "introStatus";
    String PROFILE_STATUS = "profileStatus";
    String USER_FIRST_NAME = "userFirstName";
    String USER_LAST_NAME = "userLasttName";
    String API_KEY = "apiKey";
    String COMMUNITY_STATUS = "communitySet";
    String COMMUNITY_ID = "communityId";
    String COMMUNITY_NAME = "communityName";
    String EMAIL = "email";
    String FIREBASE_TOKEN = "firebasetoken";
    String FIREBASE_STATUS = "firebasestatus";
    String CHAT_ID_STATUS = "chatidstatus";
    String CHAT_ID = "chatid";

    //Menu IDs
    int MENU_PROFILE_ID = 2000;
    long BACK_EXIT_TIME = 3000;

    enum IntentTypes {
        SET_USER_PIN, ENTER_USER_PIN
    }
}
