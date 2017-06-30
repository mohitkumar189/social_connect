package in.squarei.socialconnect.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;

import in.squarei.socialconnect.R;
import in.squarei.socialconnect.activities.useraccesspackage.UserLoginActivity;
import in.squarei.socialconnect.broadcastreceivers.SmsListener;
import in.squarei.socialconnect.interfaces.AppConstants;
import in.squarei.socialconnect.utils.Logger;
import in.squarei.socialconnect.utils.SharedPreferenceUtils;

import static in.squarei.socialconnect.interfaces.AppConstants.IS_INTRO_COMPLETED;
import static java.lang.Thread.sleep;

public class SplashActivity extends SocialConnectBaseActivity implements SmsListener.OnSmsReceivedListener {

    private static final String TAG = "SplashActivity";
    private SmsListener receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.info(TAG, "======onCreate()======");
        hideStatusBar();
        setContentView(R.layout.activity_splash);
        SharedPreferenceUtils.getInstance(context).putString(AppConstants.API_KEY, "8887e71887f2f2b8dc191ff238ad5a4f");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Logger.info(TAG, "======Timer finished======");
                SharedPreferenceUtils sharedPreferenceUtils = SharedPreferenceUtils.getInstance(context);
                if (sharedPreferenceUtils.getBoolean(IS_INTRO_COMPLETED)) {
/*                    if (sharedPreferenceUtils.getBoolean(PIN_STATUS)) {
                        // open activity to enter the pin
                        Intent intent = new Intent(currentActivity, UserDashboardActivity.class);
                        intent.putExtra("actionType", AppConstants.IntentTypes.ENTER_USER_PIN);
                        intent.putExtra("userPin", sharedPreferenceUtils.getString(USER_PIN));
                        startActivity(intent);
                    } else {
                        // open Login activity
                        startActivity(currentActivity, UserLoginActivity.class);

                    }*/
                    startActivity(currentActivity, UserDashboardActivity.class);
                } else {
                    startActivity(currentActivity, UserDashboardActivity.class);
                }
                //  navigateUser();
            }
        }, AppConstants.APP_SPLASH_TIME);

    }

    private void navigateUser() {
        Logger.info(TAG, "======Navigating User======");
        startActivity(currentActivity, UserLoginActivity.class);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(3000);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(currentActivity, UserLoginActivity.class);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        //   thread.start();
    }

    @Override
    protected void initViews() {

        receiver = new SmsListener();
        receiver.setOnSmsReceivedListener(this);
    }

    ////////////////////////////Receiving Message //////////////////////
    @Override
    public void onSmsReceived(String otp) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initContext() {
        currentActivity = SplashActivity.this;
        context = SplashActivity.this;
    }

    @Override
    protected void initListners() {

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
        Logger.info(TAG, "======onClick()======for id=====" + v.getId());
    }

}
