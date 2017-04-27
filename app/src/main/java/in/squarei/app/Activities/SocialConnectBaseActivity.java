package in.squarei.app.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import in.squarei.app.Interfaces.AppConstants;
import in.squarei.app.R;

public abstract class SocialConnectBaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    Activity currentActivity;
    public Bundle bundle;
    Context context;
    Toolbar toolbar;
    FloatingActionButton fab;
    DrawerLayout drawer;
    NavigationView navigationView;
    TabLayout tabLayout;

    protected abstract void initViews();

    protected abstract void initContext();

    protected abstract void initListners();

    protected abstract boolean isActionBar();

    protected abstract boolean isHomeButton();

    protected abstract boolean isNavigationView();

    protected abstract boolean isTabs();

    protected abstract boolean isFab();

    protected abstract boolean isDrawerListener();


    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View viewHolder = (View) getLayoutInflater().inflate(R.layout.activity_social_connect_base, null);

        FrameLayout activityLayout = (FrameLayout) viewHolder.findViewById(R.id.activity_container);
        getLayoutInflater().inflate(layoutResID, activityLayout, true);

        super.setContentView(viewHolder);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

// Setting Action Bar///
        if (isActionBar()) {
            setSupportActionBar(toolbar);
        } else {
            toolbar.setVisibility(View.GONE);
        }
// Setting HomeButton//
        if (isHomeButton()) {
            settingHomeButton();
        }
// Setting Tabs View//
        if (isTabs()) {
            //setSupportActionBar(toolbar);
        } else {
            tabLayout.setVisibility(View.GONE);
        }
// Setting Navigation View//
        if (isNavigationView()) {
            navigationView.setNavigationItemSelectedListener(this);
        } else {
            navigationView.setVisibility(View.GONE);
        }

        if (isDrawerListener()) {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
        }
// Setting Floating Action View//
        if (isFab()) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                /*    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();*/
                }
            });
        } else {
            fab.setVisibility(View.GONE);
        }
        initContext();
        initViews();
        initListners();

    }

    protected void toHideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void settingHomeButton() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        //    getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_btn);
    }

    public void settingTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public void removingHomeButton() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
    }

    public void toast(String text, boolean isLengthLong) {
        int time;
        if (isLengthLong) {
            time = Toast.LENGTH_LONG;
        } else {
            time = Toast.LENGTH_SHORT;
        }
        Toast.makeText(currentActivity, text, time).show();
    }

    public void toastTesting(String text, boolean isLengthLong) {
        int time;
        if (isLengthLong) {
            time = Toast.LENGTH_LONG;
        } else {
            time = Toast.LENGTH_SHORT;
        }
        Toast.makeText(currentActivity, text, time).show();
    }

    public static void log(String key, String value, int LogType) {
        if (LogType == Log.ASSERT) {
            Log.wtf(key, value);
        } else if (LogType == Log.DEBUG) {
            Log.d(key, value);
        } else if (LogType == Log.ERROR) {
            Log.e(key, value);
        } else if (LogType == Log.INFO) {
            Log.i(key, value);
        } else if (LogType == Log.VERBOSE) {
            Log.v(key, value);
        } else if (LogType == Log.WARN) {
            Log.w(key, value);
        }

    }


    public void logTesting(String key, String value, int LogType) {
        if (LogType == Log.ASSERT) {
            Log.wtf(key, value);
        } else if (LogType == Log.DEBUG) {
            Log.d(key, value);
        } else if (LogType == Log.ERROR) {
            Log.e(key, value);
        } else if (LogType == Log.INFO) {
            Log.i(key, value);
        } else if (LogType == Log.VERBOSE) {
            Log.v(key, value);
        } else if (LogType == Log.WARN) {
            Log.w(key, value);
        }

    }


    public void switchContent(Fragment fragment, boolean addToBackStack,
                              boolean add, String tag) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (!add) {

            ft.replace(R.id.activity_container, fragment, tag);
        } else {
            ft.add(R.id.activity_container, fragment, tag);
        }
        if (addToBackStack) {
            ft.addToBackStack(tag);
        }
        ft.commit();
    }

    public void popBackStack(String tag) {
        if (getSupportFragmentManager().getBackStackEntryCount() >= 0) {
            getSupportFragmentManager().popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

    }

    public void startActivity(Activity activity, Class newclass) {
        Intent intent = new Intent(activity, newclass);
        startActivity(intent);
    }

    public void startActivity(Activity activity, Class newclass, Bundle bundle, boolean isResult, int requestCode, boolean animationRequired, int animationType) {
        Intent intent = new Intent(activity, newclass);
        if (bundle != null)

            intent.putExtras(bundle);
        if (!isResult && !animationRequired)
            startActivity(intent);
        else if (!isResult && animationRequired) {
            startActivity(intent);
            if (animationType == AppConstants.ANIMATION_SLIDE_LEFT) {
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
            } else if (animationType == AppConstants.ANIMATION_SLIDE_UP) {
                overridePendingTransition(R.anim.anim_slide_up, R.anim.anim_stopping_exiting_activity);
            }

        } else if (isResult && animationRequired) {
            startActivityForResult(intent, requestCode);
            if (animationType == AppConstants.ANIMATION_SLIDE_LEFT) {
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
            } else if (animationType == AppConstants.ANIMATION_SLIDE_UP) {
                overridePendingTransition(R.anim.anim_slide_up, R.anim.anim_stopping_exiting_activity);
            }
        } else
            startActivityForResult(intent, requestCode);
    }

    /*
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);


     */
/*       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*//*


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

 */
/*       NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*//*

    }

*/
/*    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.social_connect_base, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {

            finish();
            overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

/*    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/

public void askForPermission(int permissionRequestCode, String... permissions ){

}
}
