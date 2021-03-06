package in.squarei.socialconnect.network;

import com.android.volley.Request;

/**
 * Created by mohit kumar on 4/27/2017.
 */

public class ApiURLS {

    /* REQUEST METHODS  */
    public static final int REQUEST_POST = Request.Method.POST;
    public static final int REQUEST_GET = Request.Method.GET;
    public static final int REQUEST_PUT = Request.Method.PUT;
    public static final int REQUEST_DELETE = Request.Method.DELETE;

    public static final String BASE_URL = "http://www.squarei.in/api/v1/";

    public static final String LOGIN_URL = BASE_URL + "login"; //RequestMethod===>POST
    public static final String REGISTER_URL = BASE_URL + "register ";
    public static final String RESET_PASSWORD_URL = BASE_URL + "forgot";
    public static final String USER_PIN_UPDATE = BASE_URL + "pin";
    public static final String USER_PROFILE = BASE_URL + "profile";
    public static final String OTHER_USER_PROFILE = BASE_URL + "profile/";
    public static final String FRIENDS_POSTS = BASE_URL + "friend/posts";
    public static final String USER_FRIENDS_LIST = BASE_URL + "friends";
    public static final String USER_SUGGESTION_FRIENDS_LIST = BASE_URL + "friend/suggestions";
    public static final String POST_COMMENTS = BASE_URL + "friend/posts/";
    public static final String To_POST_COMMENT = BASE_URL + "friend/posts/comment";
    public static final String UPDATE_FRIEND_STATUS = BASE_URL + "friend/request/update";
    public static final String TO_LIKE_POST = BASE_URL + "friend/posts/like/";
    public static final String GET_FRIENDS_REQUESTS = BASE_URL + "friend/requests";
    public static final String SEND_FRIEND_REQUEST = BASE_URL + "friend/request";
    public static final String UPLOAD_POST = BASE_URL + "friend/posts";
    public static final String COMMUNITIES_LIST = BASE_URL + "communities";
    public static final String COMMUNITY_ADD = BASE_URL + "communities/add";
    public static final String USER_UPDATE = BASE_URL + "user/update";

    public enum ApiId {
        LOGIN, REGISTER, FORGOT, PIN_UPDATE, USER_PROFILE, USER_PROFILE_UPDATE, FRIENDS_POST, USER_FRIENDS_LIST,
        OTHER_USER_PROFILE, USER_FRIEND_SUGGESTIONS, POST_COMMENTS, To_POST_COMMENT, ACCEPT_FRIEND,
        REJECT_FRIEND, LIKE_POST, GET_FRIENDS_REQUESTS, SEND_FRIEND_REQUEST, UPLOAD_POST, COMMUNITIES_LIST, COMMUNITY_ADD, USER_UPDATE
    }

}