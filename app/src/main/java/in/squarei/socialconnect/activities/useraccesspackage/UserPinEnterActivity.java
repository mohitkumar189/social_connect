package in.squarei.socialconnect.activities.useraccesspackage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import in.squarei.socialconnect.R;
import in.squarei.socialconnect.activities.SocialConnectBaseActivity;
import in.squarei.socialconnect.activities.UserDashboardActivity;
import in.squarei.socialconnect.utils.Logger;
import in.squarei.socialconnect.utils.SharedPreferenceUtils;

import static in.squarei.socialconnect.interfaces.AppConstants.API_KEY;
import static in.squarei.socialconnect.interfaces.AppConstants.COMMUNITY_STATUS;
import static in.squarei.socialconnect.interfaces.AppConstants.PIN_STATUS;
import static in.squarei.socialconnect.interfaces.AppConstants.USER_PIN;

public class UserPinEnterActivity extends SocialConnectBaseActivity {
    private EditText editPassDigitOne, editPassDigitTwo, editPassDigitThree, editPassDigitFour;
    private TextView tvForgotPin;
    private static final String TAG = "UserPinEnterActivity";
    private String userPin;
    private String apiKey;
    private SharedPreferenceUtils sharedPreferenceUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pin_enter);
        Intent intent = getIntent();
        this.userPin = intent.getStringExtra("userPin");
        this.apiKey = intent.getStringExtra("apiKey");
        saveDataInSf(userPin, apiKey);
        Logger.info(TAG, "================USER PIN RECEIVED====" + userPin);
        Logger.info(TAG, "================API KEY RECEIVED====" + apiKey);
    }

    private void saveDataInSf(String userPin, String apiKey) {
        Logger.info(TAG, "==============saving data in preferences=============" + userPin + " " + apiKey);
        if (userPin != null) sharedPreferenceUtils.putString(USER_PIN, userPin);
        sharedPreferenceUtils.putBoolean(PIN_STATUS, true);
        if (apiKey != null) sharedPreferenceUtils.putString(API_KEY, apiKey);

    }

    @Override
    protected void initViews() {
        editPassDigitOne = (EditText) findViewById(R.id.editPassDigitOne);
        editPassDigitTwo = (EditText) findViewById(R.id.editPassDigitTwo);
        editPassDigitThree = (EditText) findViewById(R.id.editPassDigitThree);
        editPassDigitFour = (EditText) findViewById(R.id.editPassDigitFour);
        tvForgotPin = (TextView) findViewById(R.id.tvForgotPin);

    }

    @Override
    protected void initContext() {
        context = UserPinEnterActivity.this;
        currentActivity = UserPinEnterActivity.this;
        sharedPreferenceUtils = SharedPreferenceUtils.getInstance(context);
    }

    @Override
    protected void initListners() {
        tvForgotPin.setOnClickListener(this);
        editPassDigitOne.addTextChangedListener(new UserPinEnterActivity.MyTextWatcher(editPassDigitOne));
        editPassDigitTwo.addTextChangedListener(new UserPinEnterActivity.MyTextWatcher(editPassDigitTwo));
        editPassDigitThree.addTextChangedListener(new UserPinEnterActivity.MyTextWatcher(editPassDigitThree));
        editPassDigitFour.addTextChangedListener(new UserPinEnterActivity.MyTextWatcher(editPassDigitFour));
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
            case R.id.tvForgotPin:
                resetUserPin();
                break;
        }
    }

    private void resetUserPin() {

    }

    private void goToDashboardActivity() {
        Logger.info(TAG, "===================Dashboard Activity navigation=================");
        String digi1 = editPassDigitOne.getText().toString();
        String digi2 = editPassDigitTwo.getText().toString();
        String digi3 = editPassDigitThree.getText().toString();
        String digi4 = editPassDigitFour.getText().toString();
        String enteredUserPin = null;
        if (digi1 == null) {
            editPassDigitOne.requestFocus();
        } else if (digi2 == null) {
            editPassDigitTwo.requestFocus();
        } else if (digi3 == null) {
            editPassDigitThree.requestFocus();
        } else if (digi4 == null) {
            editPassDigitFour.requestFocus();
        } else {
            enteredUserPin = digi1 + digi2 + digi3 + digi4;
        }
        if (enteredUserPin.equals(sharedPreferenceUtils.getString(USER_PIN))) {
            // startActivity(currentActivity, UserDashboardActivity.class);/////////////////////////////////////////////Navigate to complete user profile
            if (!sharedPreferenceUtils.getBoolean(COMMUNITY_STATUS)) {
                startActivity(currentActivity, UserCompleteProfile.class);
            } else {
                startActivity(currentActivity, UserDashboardActivity.class);
            }

        } else {
            toast("Wrong Pin...", false);
        }
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
                    if (text != null || text != "")
                        editPassDigitTwo.requestFocus();
                    break;
                case R.id.editPassDigitTwo:
                    if (text != null || text != "")
                        editPassDigitThree.requestFocus();
                    break;
                case R.id.editPassDigitThree:
                    if (text != null || text != "")
                        editPassDigitFour.requestFocus();
                    break;
                case R.id.editPassDigitFour:
                    if (text != null || text != "")
                        goToDashboardActivity();///////////////////////////////////////////////////////////////////////////////////////////
                    break;

            }
        }
    }

}
