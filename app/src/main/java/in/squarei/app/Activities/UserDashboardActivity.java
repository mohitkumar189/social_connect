package in.squarei.app.Activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import in.squarei.app.R;

public class UserDashboardActivity extends SocialConnectBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initContext() {

    }

    @Override
    protected void initListners() {

    }

    @Override
    protected boolean isActionBar() {
        return true;
    }

    @Override
    protected boolean isHomeButton() {
        return true;
    }

    @Override
    protected boolean isNavigationView() {
        return true;
    }

    @Override
    protected boolean isTabs() {
        return true;
    }

    @Override
    protected boolean isFab() {
        return true;
    }

    @Override
    protected boolean isDrawerListener() {
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }
}
