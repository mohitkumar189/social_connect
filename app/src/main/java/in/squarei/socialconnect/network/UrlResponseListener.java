package in.squarei.socialconnect.network;

import org.json.JSONObject;

/**
 * Created by mohit kumar on 4/28/2017.
 */

public interface UrlResponseListener {
    void onResponseReceived(ApiURLS.ApiId apiId, JSONObject jsonObjectResponse);
    void onResponseReceived(ApiURLS.ApiId apiId, String stringResponse);
    void onErrorResponse(ApiURLS.ApiId apiId, String errorData, int responseCode);
}
