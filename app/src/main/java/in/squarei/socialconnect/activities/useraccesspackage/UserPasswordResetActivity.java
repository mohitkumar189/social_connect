package in.squarei.socialconnect.activities.useraccesspackage;

import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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

public class UserPasswordResetActivity extends SocialConnectBaseActivity implements UrlResponseListener {

    private static final String TAG = "UserPasswordResetActivity";
    private EditText editUserId;
    private TextView tvForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_password_reset);
    }

    @Override
    protected void initViews() {
        editUserId = (EditText) findViewById(R.id.editUserId);
        tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
    }

    @Override
    protected void initContext() {
        context = UserPasswordResetActivity.this;
        currentActivity = UserPasswordResetActivity.this;
    }

    @Override
    protected void initListners() {
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
        switch (v.getId()) {
            case R.id.tvForgotPassword:
                validateEmail();
                break;
        }
    }

    private void validateEmail() {
        Map<String, String> map = new HashMap<>();
        String emailValidationMessage = Validator.getInstance().validateEmail(context, editUserId.getText().toString());
        if (emailValidationMessage.length() > 0) {
            toast(emailValidationMessage, false);
            return;
        } else {
            map.put("user", editUserId.getText().toString().toLowerCase().trim());
            getForgotPasswordResult(map);
        }
    }

    private void getForgotPasswordResult(Map<String, String> map) {
        VolleyNetworkRequestHandler.getInstance(context, this).getStringData(ApiURLS.RESET_PASSWORD_URL, ApiURLS.ApiId.FORGOT, ApiURLS.REQUEST_POST, map, null);

    }

    @Override
    public void onResponseReceived(ApiURLS.ApiId apiId, JSONObject jsonObjectResponse) {

    }

    @Override
    public void onResponseReceived(ApiURLS.ApiId apiId, String stringResponse) {
        Logger.info(TAG, "===============Received response for API :" + apiId + " " + stringResponse);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(stringResponse);
            boolean error = jsonObject.getBoolean("error");
            if (!error) {
                JSONObject commandResult = jsonObject.getJSONObject("commandResult");
                int success = commandResult.getInt("success");
                String message = commandResult.getString("message");
                if (success == 1) {
                    toast(message, false);
                    finish();
                } else {
                    toast(message, false);
                }
            } else toast("Something went wrong...", false);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onErrorResponse(ApiURLS.ApiId apiId, String errorData, int responseCode) {
        Logger.info(TAG, "===============Error response for API :" + apiId + " " + errorData);
        toast("Something went wrong...", false);
    }
}
