package in.squarei.socialconnect.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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

public class FriendProfileActivity extends SocialConnectBaseActivity implements UrlResponseListener {
    private static final String TAG = "FriendProfileActivity";
    private String userId = null;
    private ImageView ivUserProfilePic, ivUserFriendShowImage;
    private TextView tvUserName, tvUserStatus, tvUserMobileNumber, tvUserEmailAddress, tvUserForAction, tvUserAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");

        Map<String, String> headerParams = new HashMap<>();
        String clientiD = SharedPreferenceUtils.getInstance(context).getString(AppConstants.API_KEY);

        Logger.info(TAG, "===================client id==========" + clientiD);
        headerParams.put("client-id", clientiD);// 8887e71887f2f2b8dc191ff238ad5a4f
        Logger.info(TAG, "==================user id Received===========" + userId);
        VolleyNetworkRequestHandler volleyNetworkRequestHolder = VolleyNetworkRequestHandler.getInstance(context, this);
        volleyNetworkRequestHolder.getStringData(ApiURLS.OTHER_USER_PROFILE + userId, ApiURLS.ApiId.OTHER_USER_PROFILE, ApiURLS.REQUEST_GET, null, headerParams);
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
    }

    @Override
    protected void initContext() {
        context = FriendProfileActivity.this;
        currentActivity = FriendProfileActivity.this;
    }

    @Override
    protected void initListners() {

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

    }

    @Override
    public void onResponseReceived(ApiURLS.ApiId apiId, JSONObject jsonObjectResponse) {

    }

    @Override
    public void onResponseReceived(ApiURLS.ApiId apiId, String stringResponse) {
        Logger.info(TAG, "==================onResponseReceived Received===========" + stringResponse);
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
                    String phone = profileData.getString("phone");
                    String profileStatus = profileData.getString("prof_status");
                    String privacy = profileData.getString("privacy");
                    String email = profileData.getString("alternateEmail");

                    tvUserName.setText(firstName + " " + lastName);
                    tvUserStatus.setText(profileStatus);
                    tvUserAddress.setText(address);
                    tvUserMobileNumber.setText(phone);
                    tvUserEmailAddress.setText(email);
                    Picasso.with(context)
                            .load(profilePic)
                            .placeholder(context.getResources().getDrawable(R.drawable.user)) //this is optional the image to display while the url image is downloading
                            .error(context.getResources().getDrawable(R.drawable.man))         //this is also optional if some error has occurred in downloading the image this image would be displayed
                            .into(ivUserProfilePic);
                } else {

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onErrorResponse(ApiURLS.ApiId apiId, String errorData, int responseCode) {
        Logger.info(TAG, "==================onErrorResponse Received===========" + errorData);
    }
}
