package in.squarei.socialconnect.activities.useraccesspackage;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.squarei.socialconnect.activities.SocialConnectBaseActivity;
import in.squarei.socialconnect.R;
import in.squarei.socialconnect.activities.UserDashboardActivity;
import in.squarei.socialconnect.interfaces.AppConstants;
import in.squarei.socialconnect.network.ApiURLS;
import in.squarei.socialconnect.network.UrlResponseListener;
import in.squarei.socialconnect.network.VolleyNetworkRequestHandler;
import in.squarei.socialconnect.utils.Logger;
import in.squarei.socialconnect.utils.SharedPreferenceUtils;
import in.squarei.socialconnect.utils.Validator;

import static in.squarei.socialconnect.interfaces.AppConstants.USER_PIN;

public class UserLoginActivity extends SocialConnectBaseActivity implements UrlResponseListener {

    private static final String TAG = "UserLoginActivity";
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

        }
    };
    AlertDialog deleteDialog;
    private EditText editLoginId, editLoginPassword;
    private Button buttonLogin;
    private TextView tvSignupUser, tvForgotPassword;
    private TextView tvResendOtp;
    private String apiKey;
    private String userId;
    private String userpinPassword;
    // These are for dialog to enter OTP///
    private EditText editPassDigitOne, editPassDigitTwo, editPassDigitThree, editPassDigitFour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        Logger.info(TAG, "======onCreate()======" + "setContentView()");
    }

    @Override
    protected void initViews() {
        editLoginId = (EditText) findViewById(R.id.editLoginId);
        editLoginPassword = (EditText) findViewById(R.id.editLoginPassword);
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
                finish();
                break;
            case R.id.tvForgotPassword:
                startActivity(currentActivity, UserPasswordResetActivity.class);
                break;
            case R.id.tvResendOtp:
                toast("otp sent", false);
                break;
        }
    }

    private void getLoginResult(Map<String, String> map) {
        VolleyNetworkRequestHandler.getInstance(context, this).getStringData(ApiURLS.LOGIN_URL, ApiURLS.ApiId.LOGIN, ApiURLS.REQUEST_POST, map, null);
    }

    private void onLoginSuccess(Intent intent) {
        startActivity(intent);
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
                        Logger.info(TAG, "===========================================USER PIN" + userpinPassword);
                        toast(message, false);
                        if (userpinPassword.length() == 0) {
                            intent.putExtra("actionType", AppConstants.IntentTypes.SET_USER_PIN);
                            intent.putExtra("userPin", userpinPassword);
                            intent.putExtra("apiKey", apiKey);
                            Logger.info(TAG, "================API KEY SENT====" + apiKey);
                        } else {
                            intent.putExtra("actionType", AppConstants.IntentTypes.ENTER_USER_PIN);
                            intent.putExtra("userPin", userpinPassword);
                            intent.putExtra("apiKey", apiKey);
                            Logger.info(TAG, "================API KEY SENT====" + apiKey);
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
        String passwordValidatorMessage = Validator.getInstance().validatePassword(context, editLoginPassword.getText().toString());

        if (emailValidationMessage.length() > 0) {
            toast(emailValidationMessage, false);
            return;
        } else if (passwordValidatorMessage.length() > 0) {
            toast(passwordValidatorMessage, false);
            return;
        } else {
            map.put("email", editLoginId.getText().toString().toLowerCase().trim());
            map.put("password", editLoginPassword.getText().toString().toLowerCase().trim());
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
