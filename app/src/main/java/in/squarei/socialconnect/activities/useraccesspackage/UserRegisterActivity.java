package in.squarei.socialconnect.activities.useraccesspackage;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.squarei.socialconnect.R;
import in.squarei.socialconnect.activities.SocialConnectBaseActivity;
import in.squarei.socialconnect.network.ApiURLS;
import in.squarei.socialconnect.network.UrlResponseListener;
import in.squarei.socialconnect.network.VolleyNetworkRequestHandler;
import in.squarei.socialconnect.utils.Logger;
import in.squarei.socialconnect.utils.Validator;

public class UserRegisterActivity extends SocialConnectBaseActivity implements UrlResponseListener {

    private static final String TAG = "UserRegisterActivity";
    private EditText editUserName, editUserEmail, editUserMobileNumber;
    private Button buttonSignup;
    private TextView tvLoginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
    }

    @Override
    protected void initViews() {

        editUserName = (EditText) findViewById(R.id.editUserName);
        editUserEmail = (EditText) findViewById(R.id.editUserEmail);
        editUserMobileNumber = (EditText) findViewById(R.id.editUserMobileNumber);
        tvLoginLink = (TextView) findViewById(R.id.tvLoginLink);
        buttonSignup = (Button) findViewById(R.id.buttonSignup);
    }

    @Override
    protected void initContext() {
        context = UserRegisterActivity.this;
        currentActivity = UserRegisterActivity.this;
    }

    @Override
    protected void initListners() {
        tvLoginLink.setOnClickListener(this);
        buttonSignup.setOnClickListener(this);
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
            case R.id.buttonSignup:
                validateSignupDetails();
                break;
            case R.id.tvLoginLink:
                break;
        }
    }

    private void getSIgnupResult(Map<String, String> map) {
        VolleyNetworkRequestHandler.getInstance(context, this).getStringData(ApiURLS.REGISTER_URL, ApiURLS.ApiId.REGISTER, ApiURLS.REQUEST_POST, map, null);
    }

    private void validateSignupDetails() {
        Map<String, String> map = new HashMap<>();
        String name = editUserName.getText().toString();
        String emailValidationMessage = Validator.getInstance().validateEmail(context, editUserEmail.getText().toString());
        String mobileValidateMessage = Validator.getInstance().validateNumber(context, editUserMobileNumber.getText().toString());

        if (name.length() < 3) {
            toast("too short name", false);
        } else if (emailValidationMessage.length() > 0) {
            toast(emailValidationMessage, false);
            return;
        } else if (mobileValidateMessage.length() > 0) {
            toast(mobileValidateMessage, false);
            return;
        } else {
            map.put("name", editUserName.getText().toString().toLowerCase().trim());
            map.put("email", editUserEmail.getText().toString().toLowerCase().trim());
            map.put("mobile", editUserMobileNumber.getText().toString().toLowerCase().trim());
            Logger.info(TAG, "==============Input  data=========" + map.toString());
            getSIgnupResult(map);
        }
    }

    private void onSignupSuccess() {
        toast("You are successfully registered...", false);
        startActivity(currentActivity, UserLoginActivity.class);
        finish();
    }

    @Override
    public void onResponseReceived(ApiURLS.ApiId apiId, JSONObject jsonObjectResponse) {

    }

    @Override
    public void onResponseReceived(ApiURLS.ApiId apiId, String stringResponse) {
        if (apiId == ApiURLS.ApiId.REGISTER) {
            Logger.info(TAG, "Response received============" + stringResponse);
            try {
                JSONObject jsonObjectResponse = new JSONObject(stringResponse);
                boolean error = jsonObjectResponse.getBoolean("error");
                if (error) {
                    JSONObject jsonObject = jsonObjectResponse.getJSONObject("commandResult");
                    Logger.info(TAG, "Command result====" + jsonObject);
                    String message = jsonObject.getString("message");
                    toast(message, false);
                } else {
                    onSignupSuccess(); /// go to onSignup success
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onErrorResponse(ApiURLS.ApiId apiId, String errorData, int responseCode) {
        if (apiId == ApiURLS.ApiId.REGISTER) {
            Logger.info(TAG, "Error Response received============" + errorData);
        }
    }
}
