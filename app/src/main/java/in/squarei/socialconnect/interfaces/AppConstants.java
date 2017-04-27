package in.squarei.socialconnect.interfaces;

import com.android.volley.Request;

/**
 * Created by mohit kumar on 4/26/2017.
 */

public interface AppConstants {

    String APP_NAME = "social_connect";
    int APP_SPLASH_TIME=3000;

    /* REQUEST METHODS  */
    int REQUEST_POST= Request.Method.POST;
    int REQUEST_GET=Request.Method.GET;
    int REQUEST_PUT=Request.Method.PUT;
    int REQUEST_DELETE=Request.Method.DELETE;

    /* animation type*/
    int ANIMATION_SLIDE_UP = 0x01;
    int ANIMATION_SLIDE_LEFT = 0x02;

    // User Status Details
    String IS_LOGIN="islogin";
    String PASS_KEY="passKey";
    String USER_KEY="userKey";
}
