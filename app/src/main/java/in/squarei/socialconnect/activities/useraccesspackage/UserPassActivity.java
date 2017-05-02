package in.squarei.socialconnect.activities.useraccesspackage;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import in.squarei.socialconnect.utils.Logger;
import in.squarei.socialconnect.utils.SharedPreferenceUtils;

import static in.squarei.socialconnect.interfaces.AppConstants.API_KEY;
import static in.squarei.socialconnect.interfaces.AppConstants.PIN_STATUS;
import static in.squarei.socialconnect.interfaces.AppConstants.USER_PIN;

public class UserPassActivity extends SocialConnectBaseActivity implements UrlResponseListener {

    private static final String TAG = "UserPassActivity";
    private RelativeLayout container_user_pin_digits_reset;
    private LinearLayout container_user_pin_enter;
    private TextView tvSetUserPin;
    private EditText editPassDigitOne, editPassDigitTwo, editPassDigitThree, editPassDigitFour;
    private EditText editPassDigitOne2, editPassDigitTwo2, editPassDigitThree2, editPassDigitFour2;
    private String userPin;
    private String enteredUserPin;
    private String apiKey;
    private String pinToBeSaved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pass);
        Intent intent = getIntent();
        if (intent.getExtras().get("actionType").equals(AppConstants.IntentTypes.ENTER_USER_PIN)) {
            userPin = intent.getStringExtra("userPin");
            //apiKey = intent.getStringExtra("apiKey");
            Logger.info(TAG, "================USER PIN RECEIVED====" + userPin);
            if (container_user_pin_enter.getVisibility() == View.GONE) {
                container_user_pin_enter.setVisibility(View.VISIBLE);
                if (container_user_pin_digits_reset.getVisibility() == View.VISIBLE)
                    container_user_pin_digits_reset.setVisibility(View.GONE);
            }
        } else if (intent.getExtras().get("actionType").equals(AppConstants.IntentTypes.SET_USER_PIN)) {
            userPin = intent.getStringExtra("userPin");
            apiKey = intent.getStringExtra("apiKey");
            Logger.info(TAG, "================API KEY RECEIVED====" + apiKey);
            if (container_user_pin_digits_reset.getVisibility() == View.GONE) {
                container_user_pin_digits_reset.setVisibility(View.VISIBLE);
                if (container_user_pin_enter.getVisibility() == View.VISIBLE)
                    container_user_pin_enter.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void initViews() {
        container_user_pin_digits_reset = (RelativeLayout) findViewById(R.id.container_user_pin_digits_reset);
        container_user_pin_enter = (LinearLayout) findViewById(R.id.container_user_pin_enter);
        tvSetUserPin = (TextView) findViewById(R.id.tvSetUserPin);
        editPassDigitOne = (EditText) findViewById(R.id.editPassDigitOne);
        editPassDigitTwo = (EditText) findViewById(R.id.editPassDigitTwo);
        editPassDigitThree = (EditText) findViewById(R.id.editPassDigitThree);
        editPassDigitFour = (EditText) findViewById(R.id.editPassDigitFour);

        editPassDigitOne2 = (EditText) findViewById(R.id.editPassDigitOne2);
        editPassDigitTwo2 = (EditText) findViewById(R.id.editPassDigitTwo2);
        editPassDigitThree2 = (EditText) findViewById(R.id.editPassDigitThree2);
        editPassDigitFour2 = (EditText) findViewById(R.id.editPassDigitFour2);
    }

    @Override
    protected void initContext() {
        context = UserPassActivity.this;
        currentActivity = UserPassActivity.this;
    }

    @Override
    protected void initListners() {
        tvSetUserPin.setOnClickListener(this);
        editPassDigitOne.addTextChangedListener(new UserPassActivity.MyTextWatcher(editPassDigitOne));
        editPassDigitTwo.addTextChangedListener(new UserPassActivity.MyTextWatcher(editPassDigitTwo));
        editPassDigitThree.addTextChangedListener(new UserPassActivity.MyTextWatcher(editPassDigitThree));
        editPassDigitFour.addTextChangedListener(new UserPassActivity.MyTextWatcher(editPassDigitFour));

        editPassDigitOne2.addTextChangedListener(new UserPassActivity.MyTextWatcher(editPassDigitOne2));
        editPassDigitTwo2.addTextChangedListener(new UserPassActivity.MyTextWatcher(editPassDigitTwo2));
        editPassDigitThree2.addTextChangedListener(new UserPassActivity.MyTextWatcher(editPassDigitThree2));
        editPassDigitFour2.addTextChangedListener(new UserPassActivity.MyTextWatcher(editPassDigitFour2));
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
            case R.id.tvSetUserPin:
                if (enteredUserPin != null) {
                    Map<String, String> mapParams = new HashMap<>();
                    Map<String, String> mapHeader = new HashMap<>();
                    mapParams.put("pin", enteredUserPin);
                    //     mapParams.put("client-id", apiKey);
                    mapHeader.put("client-id", apiKey);
                    getPinUpdateResult(mapParams, mapHeader);
                }
                break;
            default:
                break;
        }
    }

    private void getPinUpdateResult(Map<String, String> mapParams, Map<String, String> mapHeader) {
        VolleyNetworkRequestHandler.getInstance(context, this).getStringData(ApiURLS.USER_PIN_UPDATE, ApiURLS.ApiId.PIN_UPDATE, ApiURLS.REQUEST_PUT, mapParams, mapHeader);
    }

    @Override
    public void onResponseReceived(ApiURLS.ApiId apiId, JSONObject jsonObjectResponse) {

    }

    @Override
    public void onResponseReceived(ApiURLS.ApiId apiId, String stringResponse) {
        Logger.info(TAG, "===================Received response==============" + stringResponse);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(stringResponse);
            boolean error = jsonObject.getBoolean("error");
            if (!error) {
                JSONObject commandResult = jsonObject.getJSONObject("commandResult");
                int success = commandResult.getInt("success");
                String message = commandResult.getString("message");
                pinToBeSaved = jsonObject.getJSONObject("input").getString("pin");
                if (success == 1) {
                    goToDashboardActivity();
                    toast(message, false);
                } else {
                    toast(message, false);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onErrorResponse(ApiURLS.ApiId apiId, String errorData, int responseCode) {
        Logger.info(TAG, "===================Error response==============" + errorData);
    }

    private void goToDashboardActivity() {
        Logger.info(TAG, "=========================user pin===========" + pinToBeSaved + " " + apiKey + " " + userPin);
        SharedPreferenceUtils sharedPreferenceUtils = SharedPreferenceUtils.getInstance(context);
        sharedPreferenceUtils.putString(USER_PIN, userPin);
        sharedPreferenceUtils.putBoolean(PIN_STATUS, true);
        sharedPreferenceUtils.putString(API_KEY, apiKey);
        startActivity(currentActivity, UserDashboardActivity.class);
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
                        enteredUserPin = otp;
                        if (enteredUserPin.length() != 0) {
                            if (enteredUserPin.equals(userPin)) {
                                goToDashboardActivity();
                            }
                        } else {
                            toast("Enter a valid pin", false);
                        }
                    }
                    break;

                case R.id.editPassDigitOne2:
                    editPassDigitTwo2.requestFocus();
                    break;
                case R.id.editPassDigitTwo2:
                    editPassDigitThree2.requestFocus();
                    break;
                case R.id.editPassDigitThree2:
                    editPassDigitFour2.requestFocus();
                    break;
                case R.id.editPassDigitFour2:
                    if (text != null || text != "") {
                        toast("From reset pin", false);
                        String otp = editPassDigitOne2.getText().toString() + editPassDigitTwo2.getText().toString() + editPassDigitThree2.getText().toString() + editPassDigitFour2.getText().toString();
                        enteredUserPin = otp;
                    }
                    break;
            }
        }
    }
}
