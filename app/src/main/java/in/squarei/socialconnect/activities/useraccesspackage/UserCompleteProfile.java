package in.squarei.socialconnect.activities.useraccesspackage;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.squarei.socialconnect.R;
import in.squarei.socialconnect.activities.SocialConnectBaseActivity;
import in.squarei.socialconnect.interfaces.AppConstants;
import in.squarei.socialconnect.modals.CommunitiesData;
import in.squarei.socialconnect.network.ApiURLS;
import in.squarei.socialconnect.network.UrlResponseListener;
import in.squarei.socialconnect.network.VolleyNetworkRequestHandler;
import in.squarei.socialconnect.utils.Logger;
import in.squarei.socialconnect.utils.SharedPreferenceUtils;
import in.squarei.socialconnect.utils.Validator;

import static in.squarei.socialconnect.network.ApiURLS.ApiId.COMMUNITIES_LIST;
import static in.squarei.socialconnect.network.ApiURLS.ApiId.COMMUNITY_ADD;

public class UserCompleteProfile extends SocialConnectBaseActivity implements RadioGroup.OnCheckedChangeListener, UrlResponseListener, View.OnFocusChangeListener {

    private static final String TAG = "UserCompleteProfile";
    private ImageView ivImageUpload;
    private EditText editEmailAddress;
    private EditText editFirstName;
    private EditText editLastName;
    private EditText editUserMobilenumber;
    private EditText editCommunityName;
    private EditText editUserAddress;
    private EditText editUserLandmark;
    private EditText editUserCity;
    private EditText editUserZipcode;
    private EditText editUserState;
    private EditText editUserCountry;
    private TextView btnSubmitProfile;
    private Button btnCommunityList;
    private Button btnAddCommunity;
    private RadioButton radioBtnCreate;
    private RadioButton radioBtnAdd;
    private RadioButton radioBtnMale;
    private RadioButton radioBtnFemale;
    private RadioButton radioBtnPublic;
    private RadioButton radioBtnPrivate;
    private RadioButton radioBtnOnlyMe;
    private RadioGroup radioGroupUserCommunity;
    private RadioGroup radioGroupUserGender;
    private RadioGroup radioGroupUserPrivacy;
    private String clientiD;
    private Switch switchPhonePolicy;
    private boolean communitiesFetched = false;
    private List<CommunitiesData> communitiesData;
    private AlertDialog b;
    private ListView communitiesList;
    private String[] communities;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_complete_profile);
    }

    @Override
    protected void initViews() {
        ivImageUpload = (ImageView) findViewById(R.id.ivImageUpload);
        editEmailAddress = (EditText) findViewById(R.id.editEmailAddress);
        editFirstName = (EditText) findViewById(R.id.editFirstName);
        editLastName = (EditText) findViewById(R.id.editLastName);
        editUserMobilenumber = (EditText) findViewById(R.id.editUserMobilenumber);
        editCommunityName = (EditText) findViewById(R.id.editCommunityName);
        editUserAddress = (EditText) findViewById(R.id.editUserAddress);
        editUserLandmark = (EditText) findViewById(R.id.editUserLandmark);
        editUserCity = (EditText) findViewById(R.id.editUserCity);
        editUserZipcode = (EditText) findViewById(R.id.editUserZipcode);
        editUserState = (EditText) findViewById(R.id.editUserState);
        editUserCountry = (EditText) findViewById(R.id.editUserCountry);
        btnSubmitProfile = (TextView) findViewById(R.id.btnSubmitProfile);
        btnCommunityList = (Button) findViewById(R.id.btnCommunityList);
        btnAddCommunity = (Button) findViewById(R.id.btnAddCommunity);
        radioBtnCreate = (RadioButton) findViewById(R.id.radioBtnCreate);
        radioBtnAdd = (RadioButton) findViewById(R.id.radioBtnAdd);
        radioBtnMale = (RadioButton) findViewById(R.id.radioBtnMale);
        radioBtnFemale = (RadioButton) findViewById(R.id.radioBtnFemale);
        radioBtnPublic = (RadioButton) findViewById(R.id.radioBtnPublic);
        radioBtnPrivate = (RadioButton) findViewById(R.id.radioBtnPrivate);
        radioBtnOnlyMe = (RadioButton) findViewById(R.id.radioBtnOnlyMe);
        radioGroupUserCommunity = (RadioGroup) findViewById(R.id.radioGroupUserCommunity);
        radioGroupUserGender = (RadioGroup) findViewById(R.id.radioGroupUserGender);
        radioGroupUserPrivacy = (RadioGroup) findViewById(R.id.radioGroupUserPrivacy);
        switchPhonePolicy = (Switch) findViewById(R.id.switchPhonePolicy);
    }

    @Override
    protected void initContext() {
        context = UserCompleteProfile.this;
        currentActivity = UserCompleteProfile.this;
    }

    @Override
    protected void initListners() {
        ivImageUpload.setOnClickListener(this);
        btnSubmitProfile.setOnClickListener(this);
        btnCommunityList.setOnClickListener(this);
        btnAddCommunity.setOnClickListener(this);
        radioGroupUserCommunity.setOnCheckedChangeListener(this);
        radioGroupUserGender.setOnCheckedChangeListener(this);
        radioGroupUserPrivacy.setOnCheckedChangeListener(this);
        editCommunityName.setOnFocusChangeListener(this);
        clientiD = SharedPreferenceUtils.getInstance(context).getString(AppConstants.API_KEY);
        getUserProfile();
    }

    private void getUserProfile() {
        Map<String, String> headerParams = new HashMap<>();
        headerParams.put("client-id", clientiD);// 8887e71887f2f2b8dc191ff238ad5a4f
        if (Validator.getInstance().isNetworkAvailable(context)) {
            VolleyNetworkRequestHandler.getInstance(context, this).getStringData(ApiURLS.USER_PROFILE, ApiURLS.ApiId.USER_PROFILE, ApiURLS.REQUEST_GET, null, headerParams);
        } else {
            toast(getResources().getString(R.string.network_error), false);
        }
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
            case R.id.ivImageUpload:
                uploadProfilePic();
                break;
            case R.id.btnSubmitProfile:
                updateProfile();
                break;
            case R.id.btnCommunityList:
                getListOfCommunities();
                break;
            case R.id.btnAddCommunity:
                addCommunityName();
                break;
        }
    }

    private void addCommunityName() {
        String communityName = editCommunityName.getText().toString().trim();
        if (communityName != null && communityName.length() > 3) {
            Map<String, String> headerParams = new HashMap<>();
            Map<String, String> postParms = new HashMap<>();
            headerParams.put("client-id", clientiD);// 8887e71887f2f2b8dc191ff238ad5a4f
            postParms.put("title", communityName);
            if (Validator.getInstance().isNetworkAvailable(context)) {
                VolleyNetworkRequestHandler.getInstance(context, this).getStringData(ApiURLS.COMMUNITY_ADD, COMMUNITY_ADD, ApiURLS.REQUEST_POST, postParms, headerParams);
            } else {
                toast(getResources().getString(R.string.network_error), false);
            }
        } else {
            toast("Community name length must be 4", false);
        }

    }

    private void updateProfile() {
    }

    private void getListOfCommunities() {
        if (!communitiesFetched) {
            Map<String, String> headerParams = new HashMap<>();
            headerParams.put("client-id", clientiD);// 8887e71887f2f2b8dc191ff238ad5a4f
            if (Validator.getInstance().isNetworkAvailable(context)) {
                VolleyNetworkRequestHandler.getInstance(context, this).getStringData(ApiURLS.COMMUNITIES_LIST, COMMUNITIES_LIST, ApiURLS.REQUEST_GET, null, headerParams);
            } else {
                toast(getResources().getString(R.string.network_error), false);
            }
        } else {
            showDialogForCommunities(communitiesData);
        }
    }

    private void uploadProfilePic() {
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (group.getId()) {
            case R.id.radioGroupUserCommunity:
                switch (checkedId) {
                    case R.id.radioBtnCreate:
                        if (btnAddCommunity.getVisibility() == View.GONE) {
                            btnAddCommunity.setVisibility(View.VISIBLE);
                        }
                        editCommunityName.requestFocus();
                        break;
                    case R.id.radioBtnAdd:
                        if (btnAddCommunity.getVisibility() == View.VISIBLE) {
                            btnAddCommunity.setVisibility(View.GONE);
                        }
                        getListOfCommunities();
                        break;
                }
                break;
            case R.id.radioGroupUserGender:
                switch (checkedId) {
                    case R.id.radioBtnMale:
                        break;
                    case R.id.radioBtnFemale:
                        break;
                }
                break;
            case R.id.radioGroupUserPrivacy:
                switch (checkedId) {
                    case R.id.radioBtnPublic:
                        break;
                    case R.id.radioBtnPrivate:
                        break;
                    case R.id.radioBtnOnlyMe:
                        break;
                }
                break;
        }
    }

    @Override
    public void onResponseReceived(ApiURLS.ApiId apiId, JSONObject jsonObjectResponse) {

    }

    @Override
    public void onResponseReceived(ApiURLS.ApiId apiId, String stringResponse) {
        Logger.info(TAG, "================onResponseReceived================" + stringResponse + "===========api id==========" + apiId);
        JSONObject jsonObject = null;
        if (apiId == ApiURLS.ApiId.USER_PROFILE) {
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
                        setupUserDetail(data);
                    } else {
                        toast(message, false);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (apiId == COMMUNITIES_LIST) {
            try {
                jsonObject = new JSONObject(stringResponse);
                boolean error = jsonObject.getBoolean("error");
                if (!error) {
                    JSONObject commandResult = jsonObject.getJSONObject("commandResult");
                    int success = commandResult.getInt("success");
                    String message = commandResult.getString("message");
                    if (success == 1) {
                        JSONArray jsonDataArray = commandResult.getJSONArray("data");
                        setCommunitiesModal(jsonDataArray);
                    } else {
                        toast(message, false);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (apiId == COMMUNITY_ADD) {
            try {
                jsonObject = new JSONObject(stringResponse);
                boolean error = jsonObject.getBoolean("error");
                if (!error) {
                    JSONObject commandResult = jsonObject.getJSONObject("commandResult");
                    int success = commandResult.getInt("success");
                    String message = commandResult.getString("message");
                    if (success == 1) {
                        toast(message, false);
                        editCommunityName.setEnabled(false);
                    } else {
                        toast(message, false);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setCommunitiesModal(JSONArray jsonDataArray) {
        communitiesData = new ArrayList<>();
        communities = new String[jsonDataArray.length()];
        for (int i = 0; i < jsonDataArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonDataArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String title = jsonObject.getString("title");
                String status = jsonObject.getString("status");
                String admin_id = jsonObject.getString("admin_id");
                communities[i] = title;
                communitiesData.add(new CommunitiesData(id, title, status, admin_id));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        communitiesFetched = true;
        showDialogForCommunities(communitiesData);
    }

    private void showDialogForCommunities(final List<CommunitiesData> communitiesData) {
        if (communitiesData != null) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            LayoutInflater inflater = currentActivity.getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.dialog_for_communities_list, null);
            dialogBuilder.setView(dialogView);
            dialogBuilder.setCancelable(true);
            dialogBuilder.setTitle("Communities");
            b = dialogBuilder.create();
            communitiesList = (ListView) dialogView.findViewById(R.id.listView);
            ArrayAdapter communitiesListAdapter = new ArrayAdapter(this,
                    android.R.layout.simple_spinner_dropdown_item,
                    communities);
            communitiesList.setAdapter(communitiesListAdapter);
            communitiesList.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    editCommunityName.setText(communitiesData.get(position).getTitle());
                    b.dismiss();
                }
            });
            b.show();
        }
    }

    private void setupUserDetail(JSONObject data) {
        Logger.info(TAG, "================received data=============" + data);
        try {
            String userId = data.getString("userID").trim();
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

            ///////////////setting the details/////////////
            editEmailAddress.setText(userEmailAddress);
            editFirstName.setText(userFirstName);
            editLastName.setText(userLastName);
            editUserCity.setText(userCity);
            editUserCountry.setText(userCountry);
            editUserState.setText(userState);
            editUserZipcode.setText(userZipcode);
            editUserAddress.setText(userAddress);
            editUserLandmark.setText(userLandmark);
            editUserMobilenumber.setText(userMobileNumber);
            // setting up the details(radio groups)//
            switch (userGender) {
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

            switch (profilePolicy) {
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

            switch (mobilePolicy) {
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
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onErrorResponse(ApiURLS.ApiId apiId, String errorData, int responseCode) {
        Logger.info(TAG, "================onErrorResponse================" + errorData);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.editCommunityName:
                if (radioBtnAdd.isChecked()) {
                    getListOfCommunities();
                }
                break;
        }
    }
}
