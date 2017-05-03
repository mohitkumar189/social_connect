package in.squarei.socialconnect.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.squarei.socialconnect.R;
import in.squarei.socialconnect.interfaces.AppConstants;
import in.squarei.socialconnect.modals.UserProfiledata;
import in.squarei.socialconnect.network.ApiURLS;
import in.squarei.socialconnect.network.UrlResponseListener;
import in.squarei.socialconnect.network.VolleyNetworkRequestHandler;
import in.squarei.socialconnect.utils.Logger;
import in.squarei.socialconnect.utils.SharedPreferenceUtils;

import static in.squarei.socialconnect.network.ApiURLS.REQUEST_GET;


public class UserProfileActivity extends SocialConnectBaseActivity implements UrlResponseListener {

    private static final String TAG = "UserProfileActivity";
    private Button btnEditProfile;
    private EditText editUserAddress, editUserLandmark, editUserCity, editUserState, editUserCountry, editUserZipcode, editUserMobilenumber;
    private UserProfiledata userProfiledata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
    }

    @Override
    protected void initViews() {
        btnEditProfile = (Button) findViewById(R.id.btnEditProfile);
        editUserAddress = (EditText) findViewById(R.id.editUserAddress);
        editUserLandmark = (EditText) findViewById(R.id.editUserLandmark);
        editUserCity = (EditText) findViewById(R.id.editUserCity);
        editUserState = (EditText) findViewById(R.id.editUserState);
        editUserCountry = (EditText) findViewById(R.id.editUserCountry);
        editUserZipcode = (EditText) findViewById(R.id.editUserZipcode);
        editUserMobilenumber = (EditText) findViewById(R.id.editUserMobilenumber);
    }

    @Override
    protected void initContext() {
        context = UserProfileActivity.this;
        currentActivity = UserProfileActivity.this;
        //toHideKeyboard();
    }

    @Override
    protected void initListners() {
        btnEditProfile.setOnClickListener(this);
        VolleyNetworkRequestHandler volleyNetworkRequestHandler = VolleyNetworkRequestHandler.getInstance(context, this);
        Map<String, String> map = new HashMap<>();
        String clientiD = SharedPreferenceUtils.getInstance(context).getString(AppConstants.API_KEY);

        Logger.info(TAG, "===================client id==========" + clientiD);
        map.put("client-id", "8887e71887f2f2b8dc191ff238ad5a4f");// 8887e71887f2f2b8dc191ff238ad5a4f
        volleyNetworkRequestHandler.getStringData(ApiURLS.USER_PROFILE, ApiURLS.ApiId.USER_PROFILE, REQUEST_GET, null, map);
    }

    @Override
    protected boolean isActionBar() {
        return false;
    }

    @Override
    protected boolean isHomeButton() {
        return false;
    }

    @Override
    protected boolean isNavigationView() {
        return false;
    }

    @Override
    protected boolean isTabs() {
        return false;
    }

    @Override
    protected boolean isFab() {
        return false;
    }

    @Override
    protected boolean isDrawerListener() {
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnEditProfile:
                enableAllEditBoxes();
                break;
        }
/*        VolleyNetworkRequestHandler volleyNetworkRequestHandler = VolleyNetworkRequestHandler.getInstance(context, this);
        Map<String, String> map = new HashMap<>();
        String clientiD = SharedPreferenceUtils.getInstance(context).getString(AppConstants.API_KEY);

        Logger.info(TAG, "===================client id==========" + clientiD);
        map.put("client-id", "8887e71887f2f2b8dc191ff238ad5a4f");// 8887e71887f2f2b8dc191ff238ad5a4f
        volleyNetworkRequestHandler.getStringData(ApiURLS.USER_PROFILE, ApiURLS.ApiId.USER_PROFILE, REQUEST_GET, null, map);*/
    }

    @Override
    public void onResponseReceived(ApiURLS.ApiId apiId, JSONObject jsonObjectResponse) {
        Logger.info(TAG, "================received response=============" + jsonObjectResponse);
    }

    @Override
    public void onResponseReceived(ApiURLS.ApiId apiId, String stringResponse) {
        Logger.info(TAG, "================received response=============" + stringResponse);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(stringResponse);
            boolean error = jsonObject.getBoolean("error");
            if (!error) {
                JSONObject commandResult = jsonObject.getJSONObject("commandResult");
                int success = commandResult.getInt("success");
                String message = commandResult.getString("message");
                if (success == 1) {
                    String dataString = commandResult.getString("data");
                    JSONObject data = new JSONObject(dataString);
                    Logger.info(TAG, "================received data=============" + data);
                    String userId = data.getString("userID");
                    String userFirstName = data.getString("firstName");
                    String phoneString = data.getString("phone"); //json object
                    JSONObject phone = new JSONObject(phoneString);
                    String userMobileNumber = phone.getString("ph"); //mobile number
                    String privacy = phone.getString("policy"); // policy
                    String userLastName = data.getString("lastName");
                    String userAddress = data.getString("address");
                    String userLandmark = data.getString("landmark");
                    String userCity = data.getString("city");
                    String userState = data.getString("state");
                    String userCountry = data.getString("country");
                    String userZipcode = data.getString("zipCode");
                    String userProfilePic = data.getString("proficPic");
                    userProfiledata = new UserProfiledata(userFirstName + " " + userLastName, userMobileNumber, userAddress, userCity, userZipcode, userState, userLandmark, userCountry, userProfilePic);
                    setUserDetails(userProfiledata);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onErrorResponse(ApiURLS.ApiId apiId, String errorData, int responseCode) {
        Logger.info(TAG, "================received error=============" + errorData);
    }

    private void setUserDetails(UserProfiledata userDetails) {
        editUserAddress.setText(userDetails.getUserAddress());
        editUserLandmark.setText(userDetails.getUserLandmark());
        editUserCity.setText(userDetails.getUserCity());
        editUserState.setText(userDetails.getUserState());
        editUserCountry.setText(userDetails.getUserCountry());
        editUserZipcode.setText(userDetails.getUserZipcode());
        editUserMobilenumber.setText(userDetails.getUserMobile());
    }

    private void enableAllEditBoxes() {
        editUserAddress.setEnabled(true);
        editUserAddress.setEnabled(true);
        editUserLandmark.setEnabled(true);
        editUserCity.setEnabled(true);
        editUserState.setEnabled(true);
        editUserCountry.setEnabled(true);
        editUserZipcode.setEnabled(true);
        editUserMobilenumber.setEnabled(true);
    }
}