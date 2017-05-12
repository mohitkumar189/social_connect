package in.squarei.socialconnect.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import in.squarei.socialconnect.R;
import in.squarei.socialconnect.interfaces.AppConstants;
import in.squarei.socialconnect.modals.UserProfiledata;
import in.squarei.socialconnect.network.ApiURLS;
import in.squarei.socialconnect.network.UrlResponseListener;
import in.squarei.socialconnect.network.VolleyNetworkRequestHandler;
import in.squarei.socialconnect.utils.Logger;
import in.squarei.socialconnect.utils.SharedPreferenceUtils;
import in.squarei.socialconnect.utils.Validator;

import static in.squarei.socialconnect.network.ApiURLS.REQUEST_GET;


public class UserProfileActivity extends SocialConnectBaseActivity implements UrlResponseListener, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "UserProfileActivity";
    private ImageView ivEditProfile;
    private TextView tvUserProfileName, tvUserStatus;
    private EditText editUserAddress, editUserLandmark, editUserCity, editUserState, editUserCountry, editUserZipcode, editUserMobilenumber, editFirstName, editLastName, editEmailAddress;
    private UserProfiledata userProfiledata;
    private Toolbar toolbar;
    private TextView btnSubmitProfile;
    private RadioButton radioBtnMale, radioBtnFemale;
    private RadioButton radioBtnPublic, radioBtnPrivate, radioBtnOnlyMe;
    private String checkedGender;
    private String checkedPrivacy;
    private String phonePolicy;
    private CircleImageView profile_image;
    private Switch switchPhonePolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
    }

    @Override
    protected void initViews() {
        ivEditProfile = (ImageView) findViewById(R.id.ivEditProfile);
        editUserAddress = (EditText) findViewById(R.id.editUserAddress);
        editUserLandmark = (EditText) findViewById(R.id.editUserLandmark);
        editUserCity = (EditText) findViewById(R.id.editUserCity);
        editUserState = (EditText) findViewById(R.id.editUserState);
        editUserCountry = (EditText) findViewById(R.id.editUserCountry);
        editUserZipcode = (EditText) findViewById(R.id.editUserZipcode);
        editUserMobilenumber = (EditText) findViewById(R.id.editUserMobilenumber);
        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        editFirstName = (EditText) findViewById(R.id.editFirstName);
        editLastName = (EditText) findViewById(R.id.editLastName);
        editEmailAddress = (EditText) findViewById(R.id.editEmailAddress);
        tvUserProfileName = (TextView) findViewById(R.id.tvUserProfileName);
        tvUserStatus = (TextView) findViewById(R.id.tvUserStatus);
        btnSubmitProfile = (TextView) findViewById(R.id.btnSubmitProfile);
        profile_image = (CircleImageView) findViewById(R.id.profile_image);

        radioBtnMale = (RadioButton) findViewById(R.id.radioBtnMale);
        radioBtnFemale = (RadioButton) findViewById(R.id.radioBtnFemale);

        radioBtnPublic = (RadioButton) findViewById(R.id.radioBtnPublic);
        radioBtnPrivate = (RadioButton) findViewById(R.id.radioBtnPrivate);
        radioBtnOnlyMe = (RadioButton) findViewById(R.id.radioBtnOnlyMe);

        switchPhonePolicy = (Switch) findViewById(R.id.switchPhonePolicy);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.proflile_activity));

    }

    @Override
    protected void initContext() {
        context = UserProfileActivity.this;
        currentActivity = UserProfileActivity.this;
        //toHideKeyboard();
    }

    @Override
    protected void initListners() {
        ivEditProfile.setOnClickListener(this);
        btnSubmitProfile.setOnClickListener(this);
        radioBtnMale.setOnClickListener(this);
        radioBtnFemale.setOnClickListener(this);

        radioBtnPublic.setOnClickListener(this);
        radioBtnPrivate.setOnClickListener(this);
        radioBtnOnlyMe.setOnClickListener(this);

        switchPhonePolicy.setOnCheckedChangeListener(this);

        String clientiD = SharedPreferenceUtils.getInstance(context).getString(AppConstants.API_KEY);
        Map<String, String> headerParams = new HashMap<>();
        headerParams.put("client-id", clientiD);// 8887e71887f2f2b8dc191ff238ad5a4f

        if (Validator.getInstance().isNetworkAvailable(context)) {
            VolleyNetworkRequestHandler volleyNetworkRequestHandler = VolleyNetworkRequestHandler.getInstance(context, this);
            volleyNetworkRequestHandler.getStringData(ApiURLS.USER_PROFILE, ApiURLS.ApiId.USER_PROFILE, REQUEST_GET, null, headerParams);
        } else {
            toast(getResources().getString(R.string.network_error), false);
        }


        Logger.info(TAG, "===================client id==========" + clientiD);

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
            case R.id.ivEditProfile:
                enableAllEditBoxes();
                break;
            case R.id.radioBtnMale:
                checkedGender = "Male";
                break;
            case R.id.radioBtnFemale:
                checkedGender = "Female";
                break;
            case R.id.radioBtnPublic:
                checkedPrivacy = "0";
                break;
            case R.id.radioBtnPrivate:
                checkedPrivacy = "1";
                break;
            case R.id.radioBtnOnlyMe:
                checkedPrivacy = "2";
                break;
            case R.id.btnSubmitProfile:
                String validateMessage = validateUserInputs();
                if (validateMessage == null) {
                    Map<String, String> paramPost = new HashMap<>();

                    //go to the request
                    String firstName = editFirstName.getText().toString();
                    String lastName = editLastName.getText().toString();
                    String address = editUserAddress.getText().toString();
                    String landmark = editUserLandmark.getText().toString();
                    String city = editUserCity.getText().toString();
                    String state = editUserState.getText().toString();
                    String country = editUserCountry.getText().toString();
                    String zipCode = editUserZipcode.getText().toString();
                    String phone = editUserMobilenumber.getText().toString();
                    //  String gender=editLastName.getText().toString();

                    if (firstName != null) {
                        paramPost.put("firstName", firstName);
                    }
                    if (lastName != null) {
                        paramPost.put("lastName", lastName);
                    }
                    if (address != null) {
                        paramPost.put("address", address);
                    }
                    if (landmark != null) {
                        paramPost.put("landmark", landmark);
                    }
                    if (city != null) {
                        paramPost.put("city", city);
                    }
                    if (state != null) {
                        paramPost.put("state", state);
                    }
                    if (country != null) {
                        paramPost.put("country", country);
                    }
                    if (zipCode != null) {
                        paramPost.put("zipCode", zipCode);
                    }
                    if (phone != null) {
                        String phoneData = "{" + "ph:" + phone + "," + "policy:" + phonePolicy + "}";
                        paramPost.put("phone", phoneData);
                    }
                    if (checkedGender != null) {
                        paramPost.put("gender", checkedGender);
                    }
                    if (checkedPrivacy != null) {
                        paramPost.put("privacy", checkedPrivacy);
                    }


                    String clientiD = SharedPreferenceUtils.getInstance(context).getString(AppConstants.API_KEY);
                    Map<String, String> headerParams = new HashMap<>();
                    headerParams.put("client-id", clientiD);// 8887e71887f2f2b8dc191ff238ad5a4f
                    if (Validator.getInstance().isNetworkAvailable(context)) {
                        VolleyNetworkRequestHandler.getInstance(context, this).getStringData(ApiURLS.USER_PROFILE, ApiURLS.ApiId.USER_PROFILE_UPDATE, ApiURLS.REQUEST_PUT, paramPost, headerParams);
                    } else {
                        toast(getResources().getString(R.string.network_error), false);
                    }
                } else {
                    toast(validateMessage, false);
                }
                validateUserInputs();
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
        if (apiId == ApiURLS.ApiId.USER_PROFILE_UPDATE) {

            try {
                JSONObject jsonObject = new JSONObject(stringResponse);
                boolean error = jsonObject.getBoolean("error");
                if (!error) {
                    String data = jsonObject.getString("commandResult");
                    JSONObject jsonObject1 = new JSONObject(data);
                    int success = jsonObject1.getInt("success");
                    String message = jsonObject1.getString("message");
                    if (success == 1) {
                        toast(message, false);
                        disableAllEditBoxes();// Disabling all the edit boxes
                    } else {

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (apiId == ApiURLS.ApiId.USER_PROFILE) {
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
                        String userFirstName = data.getString("firstName").trim();
                        String phoneString = data.getString("phone").trim(); //json object
                        JSONObject phone = new JSONObject(phoneString);
                        String userMobileNumber = phone.getString("ph").trim(); //mobile number
                        String mobilePolicy = phone.getString("policy").trim();
                        String profilePolicy = data.getString("privacy").trim();// policy---->public , private, onlyMe
                        String userLastName = data.getString("lastName").trim();
                        String userAddress = data.getString("address").trim();
                        String userLandmark = data.getString("landmark").trim();
                        String userCity = data.getString("city").trim();
                        String userState = data.getString("state").trim();
                        String userCountry = data.getString("country").trim();
                        String userZipcode = data.getString("zipCode");
                        String userProfilePic = data.getString("profilePic");
                        String userEmailAddress = data.getString("email").trim();
                        String userGender = data.getString("gender").trim();
                        String profileStatus = data.getString("prof_status").trim();
                        userProfiledata = new UserProfiledata(userFirstName + " " + userLastName, userMobileNumber, userAddress, userCity, userZipcode, userState, userLandmark, userCountry, userProfilePic,
                                userFirstName, userLastName, userEmailAddress, userGender, profilePolicy, mobilePolicy, profileStatus);
                        setUserDetails(userProfiledata);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
        editFirstName.setText(userDetails.getUserFirstName());
        editLastName.setText(userDetails.getUserLastName());
        editEmailAddress.setText(userDetails.getUserEmailAddress());
        tvUserProfileName.setText(userDetails.getUserName());
        tvUserStatus.setText(userDetails.getUserProfileStatus());

        switch (userDetails.getUserGender()) {
            case "Male":
                radioBtnMale.setChecked(true);
                break;
            case "Female":
                radioBtnFemale.setChecked(true);
                break;
            default:
                radioBtnFemale.setChecked(false);
                radioBtnFemale.setChecked(false);
                break;
        }

        switch (userDetails.getUserProfilePolicy()) {
            case "0":
                radioBtnPublic.setChecked(true);
                break;
            case "1":
                radioBtnPrivate.setChecked(true);
                break;
            case "2":
                radioBtnOnlyMe.setChecked(true);
                break;
            default:
                radioBtnPublic.setChecked(true);
                break;
        }

        switch (userDetails.getUserMobilePolicy()) {
            case "public":
                switchPhonePolicy.setChecked(true);
                switchPhonePolicy.setText("Public");
                break;
            case "private":
                switchPhonePolicy.setChecked(false);
                switchPhonePolicy.setText("Private");
                break;
            default:
        }
        String imageUrl = userDetails.getUserProfilePic();
        if (imageUrl != "null" || imageUrl != null || imageUrl.length() != 0) {
            Picasso.with(context)
                    .load(userDetails.getUserProfilePic())
                    .placeholder(context.getResources().getDrawable(R.drawable.user)) //this is optional the image to display while the url image is downloading
                    .error(context.getResources().getDrawable(R.drawable.user))         //this is also optional if some error has occurred in downloading the image this image would be displayed
                    .into(profile_image);
        }
    }

    private void enableAllEditBoxes() {
        editUserAddress.setEnabled(true);
        editUserLandmark.setEnabled(true);
        editUserCity.setEnabled(true);
        editUserState.setEnabled(true);
        editUserCountry.setEnabled(true);
        editUserZipcode.setEnabled(true);
        editUserMobilenumber.setEnabled(true);
        editFirstName.setEnabled(true);
        editLastName.setEnabled(true);
        radioBtnMale.setEnabled(true);
        radioBtnFemale.setEnabled(true);

        radioBtnPublic.setEnabled(true);
        radioBtnPrivate.setEnabled(true);
        radioBtnOnlyMe.setEnabled(true);

        switchPhonePolicy.setEnabled(true);
        editFirstName.requestFocus();
        if (btnSubmitProfile.getVisibility() == View.GONE)
            btnSubmitProfile.setVisibility(View.VISIBLE);
    }

    private void disableAllEditBoxes() {
        editUserAddress.setEnabled(false);
        editUserLandmark.setEnabled(false);
        editUserCity.setEnabled(false);
        editUserState.setEnabled(false);
        editUserCountry.setEnabled(false);
        editUserZipcode.setEnabled(false);
        editUserMobilenumber.setEnabled(false);
        editFirstName.setEnabled(false);
        editLastName.setEnabled(false);
        radioBtnMale.setEnabled(false);
        radioBtnFemale.setEnabled(false);
        switchPhonePolicy.setEnabled(false);


        radioBtnPublic.setEnabled(false);
        radioBtnPrivate.setEnabled(false);
        radioBtnOnlyMe.setEnabled(false);

        if (btnSubmitProfile.getVisibility() == View.VISIBLE)
            btnSubmitProfile.setVisibility(View.GONE);
    }

    private String validateUserInputs() {
        String returnMessage = null;
/*        if (!Helper.isValidString(editFirstName.getText().toString())) {
            returnMessage = "Please enter a valid first name";
            editFirstName.requestFocus();
            return returnMessage;
        } else if (!Helper.isValidString(editLastName.getText().toString())) {
            returnMessage = "Please enter a valid last name";
            editLastName.requestFocus();
            return returnMessage;
        } else if (!Helper.isValidString(editUserAddress.getText().toString())) {
            returnMessage = "Address is not valid";
            editUserAddress.requestFocus();
            return returnMessage;
        }else if(Validator.getInstance().validateNumber(context,editUserMobilenumber.getText().toString()) !=null){

        }*/

        return null;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.switchPhonePolicy:
                if (switchPhonePolicy.isChecked()) {
                    phonePolicy = "public";
                    switchPhonePolicy.setText("Public");
                } else {
                    phonePolicy = "private";
                    switchPhonePolicy.setText("Private");
                }

                break;
        }
    }
}