package in.squarei.socialconnect.activities.useraccesspackage;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.squarei.socialconnect.R;
import in.squarei.socialconnect.activities.SocialConnectBaseActivity;
import in.squarei.socialconnect.activities.UserDashboardActivity;
import in.squarei.socialconnect.interfaces.AppConstants;
import in.squarei.socialconnect.network.ApiURLS;
import in.squarei.socialconnect.network.UrlResponseListener;
import in.squarei.socialconnect.network.VolleyNetworkRequestHandler;
import in.squarei.socialconnect.utils.CommonUtils;
import in.squarei.socialconnect.utils.Logger;
import in.squarei.socialconnect.utils.SharedPreferenceUtils;
import in.squarei.socialconnect.utils.Validator;

import static in.squarei.socialconnect.interfaces.AppConstants.COMMUNITY_ID;
import static in.squarei.socialconnect.interfaces.AppConstants.COMMUNITY_NAME;
import static in.squarei.socialconnect.interfaces.AppConstants.COMMUNITY_STATUS;
import static in.squarei.socialconnect.interfaces.AppConstants.FIREBASE_STATUS;
import static in.squarei.socialconnect.interfaces.AppConstants.PROFILE_STATUS;
import static in.squarei.socialconnect.interfaces.AppConstants.USER_FIRST_NAME;
import static in.squarei.socialconnect.interfaces.AppConstants.USER_LAST_NAME;
import static in.squarei.socialconnect.interfaces.AppConstants.USER_PIN;
import static in.squarei.socialconnect.network.ApiURLS.USER_UPDATE;

public class UserLoginActivity extends SocialConnectBaseActivity implements UrlResponseListener {

    private static final String TAG = "UserLoginActivity";
    private AlertDialog deleteDialog;
    private String[] PERMISSIONS = {Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private int PERMISSION_ALL = 1;
    private EditText editLoginId, editLoginPassword;
    private Button buttonLogin;
    private TextView tvSignupUser, tvForgotPassword;
    private TextView tvResendOtp;
    private String apiKey;
    private String userId;
    private String userpinPassword;
    private String userFirstName;
    private String userLastName;
    private String communityName;
    private String communityId;
    // These are for dialog to enter OTP///
    private EditText editPassDigitOne, editPassDigitTwo, editPassDigitThree, editPassDigitFour;

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        Logger.info(TAG, "======onCreate()======" + "setContentView()");
    }

    @Override
    protected void initViews() {
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        editLoginId = (EditText) findViewById(R.id.editLoginId);
        // editLoginPassword = (EditText) findViewById(R.id.editLoginPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        tvSignupUser = (TextView) findViewById(R.id.tvSignupUser);
        tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        Logger.info(TAG, "======initViews()======" + "views found");
    }

    @Override
    protected void initContext() {
        context = UserLoginActivity.this;
        currentActivity = UserLoginActivity.this;
        SharedPreferenceUtils sharedPreferenceUtils = SharedPreferenceUtils.getInstance(context);
        String id = sharedPreferenceUtils.getString(USER_PIN);
        Logger.info(TAG, "=========================user pin===========" + id);
    }

    @Override
    protected void initListners() {
        buttonLogin.setOnClickListener(this);
        tvSignupUser.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);
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
        Logger.info(TAG, "======onClick()======" + "something clicked----" + v.getId());
        switch (v.getId()) {
            case R.id.buttonLogin:
                validateLoginDetails();
                break;
            case R.id.tvSignupUser:
                startActivity(currentActivity, UserRegisterActivity.class);
                //    finish();
                break;
            case R.id.tvForgotPassword:
                startActivity(currentActivity, UserPasswordResetActivity.class);
                break;
            case R.id.tvResendOtp:
                //  toast("otp sent", false);
                break;
        }
    }

    private void getLoginResult(Map<String, String> map) {
        VolleyNetworkRequestHandler.getInstance(context, this).getStringData(ApiURLS.LOGIN_URL, ApiURLS.ApiId.LOGIN, ApiURLS.REQUEST_POST, map, null);
    }

    private void onLoginSuccess(Intent intent) {
        if (!SharedPreferenceUtils.getInstance(context).getBoolean(FIREBASE_STATUS)) {
            if (apiKey != null && apiKey != "null") {
                String token = FirebaseInstanceId.getInstance().getToken();
                if (token != null)
                    CommonUtils.saveDeviceToken(USER_UPDATE, token, apiKey, context);
            }
        }
        startActivity(intent);
        finishAffinity();
        //toast("Login success", false);
        //showDialogForComment();
        //   creatingDialog(context,true,true,null,200,400);
    }

    @Override
    public void onResponseReceived(ApiURLS.ApiId apiId, JSONObject jsonObjectResponse) {
        Logger.info(TAG, "on response received===" + jsonObjectResponse);

    }

    @Override
    public void onResponseReceived(ApiURLS.ApiId apiId, String stringResponse) {
        Intent intent = new Intent(UserLoginActivity.this, UserPassActivity.class);
        Logger.info(TAG, "on response received===");
        if (apiId == ApiURLS.ApiId.LOGIN) {
            try {
                JSONObject jsonObjectResponse = new JSONObject(stringResponse);
                boolean error = jsonObjectResponse.getBoolean("error");
                if (!error) {
                    JSONObject jsonObject = jsonObjectResponse.getJSONObject("commandResult");
                    int success = jsonObject.getInt("success");
                    String message = jsonObject.getString("message");
                    Logger.info(TAG, "Command result====" + jsonObject);
                    if (success == 1) {
                        JSONObject jsonDataObject = jsonObject.getJSONObject("data");
                        userId = jsonDataObject.getString("id");
                        apiKey = jsonDataObject.getString("apiKey");
                        userpinPassword = jsonDataObject.getString("pinPassword");
                        userFirstName = jsonDataObject.getString("firstName");
                        userLastName = jsonDataObject.getString("lastName");
                        communityId = jsonDataObject.getString("communityID");
                        communityName = jsonDataObject.getString("community");
                        Logger.info(TAG, "===========================================USER PIN" + userpinPassword);
                        toast(message, false);
                        if (userpinPassword == "null" || userpinPassword.length() == 0) {
                            intent.putExtra("actionType", AppConstants.IntentTypes.SET_USER_PIN);
                            intent.putExtra("userPin", userpinPassword);
                            intent.putExtra("apiKey", apiKey);
                            Logger.info(TAG, "================API KEY SENT====" + apiKey);
                        } else {
                            intent.putExtra("actionType", AppConstants.IntentTypes.ENTER_USER_PIN);
                            intent.putExtra("userPin", userpinPassword);
                            intent.putExtra("apiKey", apiKey);
      /*                      Intent pinEnterIntent = new Intent(currentActivity, UserPinEnterActivity.class);
                            pinEnterIntent.putExtra("userPin", userpinPassword);
                            pinEnterIntent.putExtra("apiKey", apiKey);*/
                            //    startActivity(pinEnterIntent);
                            //   onLoginSuccess(pinEnterIntent);
                            Logger.info(TAG, "================API KEY SENT====" + apiKey);
                        }
                        if (userFirstName == "null" || userFirstName.length() == 0) {
                            SharedPreferenceUtils.getInstance(context).putBoolean(PROFILE_STATUS, false);
                        } else {
                            SharedPreferenceUtils.getInstance(context).putString(USER_FIRST_NAME, userFirstName);
                            SharedPreferenceUtils.getInstance(context).putBoolean(PROFILE_STATUS, true);
                            if (communityId != "null" && communityId.length() != 0) {
                                SharedPreferenceUtils.getInstance(context).putString(COMMUNITY_ID, communityId);
                                if (communityName != "null" && communityName.length() != 0) {
                                    SharedPreferenceUtils.getInstance(context).putString(COMMUNITY_NAME, communityName);
                                    SharedPreferenceUtils.getInstance(context).putBoolean(COMMUNITY_STATUS, true);
                                    Logger.info(TAG, "===========Community details are saved============");
                                }
                            }
                            if (userLastName != "null" || userLastName.length() != 0) {
                                SharedPreferenceUtils.getInstance(context).putString(USER_LAST_NAME, userLastName);
                            }
                        }
                        //  intent.putExtra("actionType", AppConstants.IntentTypes.SET_USER_PIN);
                        onLoginSuccess(intent); /// go to onlogin success and save required properties
                    } else {
                        toast(message, false);
                    }

                } else {
                    toast("Something went wrong...", false);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onErrorResponse(ApiURLS.ApiId apiId, String errorData, int responseCode) {
        Logger.info(TAG, "on error received===" + errorData);
    }

    private void validateLoginDetails() {
        Map<String, String> map = new HashMap<>();
        String emailValidationMessage = Validator.getInstance().validateEmail(context, editLoginId.getText().toString());
//        String passwordValidatorMessage = Validator.getInstance().validatePassword(context, editLoginPassword.getText().toString());

        if (emailValidationMessage.length() > 0) {
            toast(emailValidationMessage, false);
            return;
        }/* else if (passwordValidatorMessage.length() > 0) {
            toast(passwordValidatorMessage, false);
            return;
        }*/ else {
            map.put("email", editLoginId.getText().toString().toLowerCase().trim());
            // map.put("password", editLoginPassword.getText().toString().toLowerCase().trim());
            Logger.info(TAG, "==============Input  data=========" + map.toString());
            getLoginResult(map);
        }
    }

    private void showDialogForComment() {

        LayoutInflater factory = LayoutInflater.from(this);
        final View dialogView = factory.inflate(R.layout.dialog_enter_otp, null);
        deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.setView(dialogView);

        //     creatingDialog(context, false, false, dialogView, 200, 200);
        editPassDigitOne = (EditText) dialogView.findViewById(R.id.editPassDigitOne);
        editPassDigitTwo = (EditText) dialogView.findViewById(R.id.editPassDigitTwo);
        editPassDigitThree = (EditText) dialogView.findViewById(R.id.editPassDigitThree);
        editPassDigitFour = (EditText) dialogView.findViewById(R.id.editPassDigitFour);
        tvResendOtp = (TextView) dialogView.findViewById(R.id.tvResendOtp);
        editPassDigitOne.addTextChangedListener(new MyTextWatcher(editPassDigitOne));
        editPassDigitTwo.addTextChangedListener(new MyTextWatcher(editPassDigitTwo));
        editPassDigitThree.addTextChangedListener(new MyTextWatcher(editPassDigitThree));
        editPassDigitFour.addTextChangedListener(new MyTextWatcher(editPassDigitFour));
        tvResendOtp.setOnClickListener(this);
/*
        deleteDialogView.findViewById(R.id.submitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //your business logic
                deleteDialog.dismiss();
            }
        });
        deleteDialogView.findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
            }
        });
*/
        deleteDialog.show();
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            switch (view.getId()) {
                case R.id.editPassDigitOne:
                    editPassDigitTwo.requestFocus();
                    break;
                case R.id.editPassDigitTwo:
                    editPassDigitThree.requestFocus();
                    break;
                case R.id.editPassDigitThree:
                    editPassDigitFour.requestFocus();
                    break;
                case R.id.editPassDigitFour:
                    if (text != null || text != "") {
                        String otp = editPassDigitOne.getText().toString() + editPassDigitTwo.getText().toString() + editPassDigitThree.getText().toString() + editPassDigitFour.getText().toString();
                        if (otp.equals("0000")) {
                            ////////////////////////go to the user dashboard///////////////////////
                            toast("otp correct", false);
                            startActivity(currentActivity, UserDashboardActivity.class);
                            deleteDialog.dismiss();
                        } else {
                            toast("otp incorrect", false);
                        }
                    }
                    break;
            }
        }
    }
}
