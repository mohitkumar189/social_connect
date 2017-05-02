package in.squarei.socialconnect.interfaces;

import com.android.volley.Request;

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
    String API_KEY = "apiKey";

    //Menu IDs
    int MENU_PROFILE_ID = 2000;

    enum IntentTypes {
        SET_USER_PIN, ENTER_USER_PIN
    }
}
