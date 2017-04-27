package in.squarei.socialconnect.activities.useraccesspackage;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import in.squarei.socialconnect.activities.SocialConnectBaseActivity;
import in.squarei.socialconnect.network.ApiResponse;
import in.squarei.socialconnect.network.NetworkRequestHandler;
import in.squarei.socialconnect.R;
import in.squarei.socialconnect.utils.Logger;

public class UserLoginActivity extends SocialConnectBaseActivity implements ApiResponse {

    private static final String TAG = "UserLoginActivity";
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

        }
    };
    private EditText editLoginId, editLoginPassword;
    private Button buttonLogin;
    private TextView tvSignupUser, tvForgotPassword;

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
                getLoginResult();
                break;
            case R.id.tvSignupUser:
                break;
            case R.id.tvForgotPassword:
                break;
        }
    }

    private void getLoginResult() {
        String url = "http://www.google.com";
       NetworkRequestHandler.getInstance().getquery(url);
       // NetworkRequestHandler networkRequestHandler = new NetworkRequestHandler(AppConstants.REQUEST_GET, context, this);
      //  networkRequestHandler.getquery(url);

/*        RequestQueue queue = SocialConnectApplication.getInstance().getRequestQueue();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Logger.info(TAG,response);
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);*/
    }

    @Override
    public void onPostFail(int method, String response) {
        System.out.println("error is received  ===============" + response.toString());
    }

    @Override
    public void onPostSuccess(int method, JSONObject response) {

        System.out.println("response is received  ===============" + response.toString());
    }
}
