package in.squarei.socialconnect.network;

import org.json.JSONObject;

/**
 * Created by mohit kumar on 4/27/2017.
 */

public interface ApiResponse {

    void onPostFail(int method, String response);
    void onPostSuccess(int method, JSONObject response);
}
