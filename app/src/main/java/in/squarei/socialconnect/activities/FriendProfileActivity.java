package in.squarei.socialconnect.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.squarei.socialconnect.R;
import in.squarei.socialconnect.interfaces.AppConstants;
import in.squarei.socialconnect.network.ApiURLS;
import in.squarei.socialconnect.network.UrlResponseListener;
import in.squarei.socialconnect.network.VolleyNetworkRequestHandler;
import in.squarei.socialconnect.utils.Logger;
import in.squarei.socialconnect.utils.SharedPreferenceUtils;
import in.squarei.socialconnect.utils.Validator;

import static in.squarei.socialconnect.network.ApiURLS.ApiId.OTHER_USER_PROFILE;
import static in.squarei.socialconnect.network.ApiURLS.ApiId.SEND_FRIEND_REQUEST;

public class FriendProfileActivity extends SocialConnectBaseActivity implements UrlResponseListener {
    private static final String TAG = "FriendProfileActivity";
    private String userId = null;
    private int userType;
    private ImageView ivUserProfilePic, ivUserFriendShowImage;
    private TextView tvUserName, tvUserStatus, tvUserMobileNumber, tvUserEmailAddress, tvUserForAction, tvUserAddress;
    private LinearLayout linearUserStatusHolder;
    private String clientiD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        userType = intent.getIntExtra("userType", -1);
        setContentView(R.layout.activity_friend_profile);
        settingTitle(getResources().getString(R.string.friend_profile_activity));

        Map<String, String> headerParams = new HashMap<>();
        clientiD = SharedPreferenceUtils.getInstance(context).getString(AppConstants.API_KEY);

        Logger.info(TAG, "===================client id==========" + clientiD + "=============userid=====" + userId + "======userType=====" + userType);
        headerParams.put("client-id", clientiD);// 8887e71887f2f2b8dc191ff238ad5a4f

        if (Validator.getInstance().isNetworkAvailable(context)) {
            VolleyNetworkRequestHandler volleyNetworkRequestHolder = VolleyNetworkRequestHandler.getInstance(context, this);
            volleyNetworkRequestHolder.getStringData(ApiURLS.OTHER_USER_PROFILE + userId, OTHER_USER_PROFILE, ApiURLS.REQUEST_GET, null, headerParams);
        } else {
            toast(getResources().getString(R.string.network_error), false);
        }

    }

    @Override
    protected void initViews() {
        ivUserProfilePic = (ImageView) findViewById(R.id.ivUserProfilePic);
        ivUserFriendShowImage = (ImageView) findViewById(R.id.ivUserFriendShowImage);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvUserStatus = (TextView) findViewById(R.id.tvUserStatus);
        tvUserMobileNumber = (TextView) findViewById(R.id.tvUserMobileNumber);
        tvUserEmailAddress = (TextView) findViewById(R.id.tvUserEmailAddress);
        tvUserForAction = (TextView) findViewById(R.id.tvUserForAction);
        tvUserAddress = (TextView) findViewById(R.id.tvUserAddress);
        linearUserStatusHolder = (LinearLayout) findViewById(R.id.linearUserStatusHolder);
    }

    @Override
    protected void initContext() {
        context = FriendProfileActivity.this;
        currentActivity = FriendProfileActivity.this;
    }

    @Override
    protected void initListners() {
        linearUserStatusHolder.setOnClickListener(this);
        if (userType == 0) {
            //    toast("" + userType, false);
            tvUserForAction.setText(context.getResources().getString(R.string.is_friend_title));
            ivUserFriendShowImage.setImageDrawable(context.getResources().getDrawable(R.drawable.add_friend));

        } else if (userType == 1) {
            linearUserStatusHolder.setClickable(true);
            //  toast("" + userType, false);
            tvUserForAction.setText(context.getResources().getString(R.string.add_friend_title));
            ivUserFriendShowImage.setImageDrawable(context.getResources().getDrawable(R.drawable.remove_friend));
        }
    }

    @Override
    protected boolean isActionBar() {
        return true;
    }

    @Override
    protected boolean isHomeButton() {
        return true;
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
            case R.id.linearUserStatusHolder:
                if (userType == 1) {
                    // toast("image clicked", false);
                    sendFriendRequest(userId);
                }
                break;
            default:
                break;
        }
    }

    private void sendFriendRequest(String conncetionId) {
        Map<String, String> headerParams = new HashMap<>();
        Map<String, String> postParams = new HashMap<>();
        Logger.info(TAG, "===================client id==========" + clientiD + "=============userid=====" + userId + "======userType=====" + userType);
        headerParams.put("client-id", clientiD);// 8887e71887f2f2b8dc191ff238ad5a4f
        postParams.put("connection_id", conncetionId);

        if (Validator.getInstance().isNetworkAvailable(context)) {
            VolleyNetworkRequestHandler volleyNetworkRequestHolder = VolleyNetworkRequestHandler.getInstance(context, this);
            volleyNetworkRequestHolder.getStringData(ApiURLS.SEND_FRIEND_REQUEST, SEND_FRIEND_REQUEST, ApiURLS.REQUEST_POST, postParams, headerParams);
        } else {
            toast(getResources().getString(R.string.network_error), false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onResponseReceived(ApiURLS.ApiId apiId, JSONObject jsonObjectResponse) {

    }

    @Override
    public void onResponseReceived(ApiURLS.ApiId apiId, String stringResponse) {
        Logger.info(TAG, "==================onResponseReceived Received===========" + stringResponse);
        if (apiId == SEND_FRIEND_REQUEST) {
            try {
                JSONObject jsonObject = new JSONObject(stringResponse);
                boolean error = jsonObject.getBoolean("error");
                if (!error) {
                    JSONObject commandResult = jsonObject.getJSONObject("commandResult");
                    int success = commandResult.getInt("success");
                    String message = commandResult.getString("message");
                    if (success == 1) {
                        toast(message, false);
                    } else {
                        toast(message, false);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (apiId == OTHER_USER_PROFILE) {
            try {
                JSONObject jsonObject = new JSONObject(stringResponse);
                boolean error = jsonObject.getBoolean("error");
                if (!error) {
                    JSONObject commandResult = jsonObject.getJSONObject("commandResult");
                    int success = commandResult.getInt("success");
                    String message = commandResult.getString("message");
                    if (success == 1) {
                        JSONObject profileData = new JSONObject(commandResult.getString("data"));
                        String userID = profileData.getString("userID");
                        String firstName = profileData.getString("firstName");
                        String lastName = profileData.getString("lastName");
                        String address = profileData.getString("address");
                        String landmark = profileData.getString("landmark");
                        String city = profileData.getString("city");
                        String state = profileData.getString("state");
                        String country = profileData.getString("country");
                        String zipCode = profileData.getString("zipCode");
                        String profilePic = profileData.getString("profilePic");
                        //  String phone = profileData.getString("phone");
                        String phoneString = profileData.getString("phone"); //json object
                        JSONObject phone = new JSONObject(phoneString);
                        String userMobileNumber = phone.getString("ph"); //mobile number
                        String mobilePolicy = phone.getString("policy");
                        String profileStatus = profileData.getString("prof_status");
                        String privacy = profileData.getString("privacy");
                        String email = profileData.getString("email");

                        tvUserName.setText(firstName + " " + lastName);
                        if (profileStatus != null) tvUserStatus.setText(profileStatus);

                        if (address != null) tvUserAddress.setText(address);
                        if (!phone.equals("null") || phone.length() != 0 || phone != null)
                            tvUserMobileNumber.setText(userMobileNumber);
                        if (email != null) tvUserEmailAddress.setText(email);
                        Picasso.with(context)
                                .load(profilePic)
                                .placeholder(context.getResources().getDrawable(R.drawable.user)) //this is optional the image to display while the url image is downloading
                                .error(context.getResources().getDrawable(R.drawable.user))         //this is also optional if some error has occurred in downloading the image this image would be displayed
                                .into(ivUserProfilePic);
                    } else {

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void onErrorResponse(ApiURLS.ApiId apiId, String errorData, int responseCode) {
        Logger.info(TAG, "==================onErrorResponse Received===========" + errorData);
    }
}
