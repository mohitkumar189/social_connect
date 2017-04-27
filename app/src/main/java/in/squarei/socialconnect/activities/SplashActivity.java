package in.squarei.socialconnect.activities;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import in.squarei.socialconnect.activities.useraccesspackage.UserLoginActivity;
import in.squarei.socialconnect.broadcastreceivers.SmsListener;
import in.squarei.socialconnect.interfaces.AppConstants;
import in.squarei.socialconnect.R;
import in.squarei.socialconnect.utils.Logger;

import static java.lang.Thread.sleep;

public class SplashActivity extends SocialConnectBaseActivity implements SmsListener.OnSmsReceivedListener {

    private static final String TAG = "SplashActivity";
    private SmsListener receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.info(TAG, "======onCreate()======");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // hiding status bar
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Logger.info(TAG, "======Timer finished======");
                navigateUser();
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
        try
        {

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void initContext() {
        currentActivity = SplashActivity.this;
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
