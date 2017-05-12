package in.squarei.socialconnect.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.squarei.socialconnect.R;
import in.squarei.socialconnect.fragments.userDashboardFragments.UserChatsFragment;
import in.squarei.socialconnect.fragments.userDashboardFragments.UserFeedsFragment;
import in.squarei.socialconnect.fragments.userDashboardFragments.UserFriendsFragment;
import in.squarei.socialconnect.fragments.userDashboardFragments.UserNoticeFragment;
import in.squarei.socialconnect.fragments.userDashboardFragments.UserUpdatesFragment;
import in.squarei.socialconnect.interfaces.AppConstants;
import in.squarei.socialconnect.network.ApiURLS;
import in.squarei.socialconnect.network.UrlResponseListener;
import in.squarei.socialconnect.network.VolleyNetworkRequestHandler;
import in.squarei.socialconnect.utils.Logger;
import in.squarei.socialconnect.utils.SharedPreferenceUtils;

import static in.squarei.socialconnect.interfaces.AppConstants.MENU_PROFILE_ID;
import static in.squarei.socialconnect.interfaces.AppConstants.PROFILE_STATUS;
import static in.squarei.socialconnect.interfaces.AppConstants.USER_FIRST_NAME;
import static in.squarei.socialconnect.interfaces.AppConstants.USER_LAST_NAME;

public class UserDashboardActivity extends SocialConnectBaseActivity implements UrlResponseListener {

    private static final String TAG = "UserDashboardActivity";
    String[] PERMISSIONS = {Manifest.permission.READ_PHONE_STATE
            , Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    int PERMISSION_ALL = 1;
    private boolean canExit = false;
    private FrameLayout fragment_container;
    private UserFeedsFragment userFeedsFragment;
    private UserChatsFragment userChatsFragment;
    private UserNoticeFragment userNoticeFragment;
    private UserFriendsFragment userFriendsFragment;
    private UserUpdatesFragment userUpdatesFragment;
    private AlertDialog b;
    private String firstName, lastName;

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        // settingNavigationView();
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        switchContent(R.id.fragment_container, userFeedsFragment, true, false, "userFeedFragment");
        navigationView.setCheckedItem(R.id.nav_user_feeds);
        settingTitle(getResources().getString(R.string.feed_fragment));
        checkForProfileCompleteDialog();
    }

    private void checkForProfileCompleteDialog() {
        if (SharedPreferenceUtils.getInstance(context).getBoolean(PROFILE_STATUS)) {
            updateUserName();
        } else {
            showUserProfileDialog();
        }
    }

    private void showUserProfileDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = currentActivity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_profile_update, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(true);
        dialogBuilder.setTitle("Please enter profile details");
        b = dialogBuilder.create();
        final EditText editUserFirstName = (EditText) dialogView.findViewById(R.id.editUserFirstName);
        final EditText editUserLastName = (EditText) dialogView.findViewById(R.id.editUserLastName);
        final TextView tvUserNameUpdate = (TextView) dialogView.findViewById(R.id.tvUserNameUpdate);
        b.show();

        tvUserNameUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstName = editUserFirstName.getText().toString();
                lastName = editUserLastName.getText().toString();

                Map<String, String> paramPost = new HashMap<>();
                if (firstName != null) paramPost.put("firstName", firstName);

                if (lastName != null) paramPost.put("lastName", lastName);

                String clientiD = SharedPreferenceUtils.getInstance(context).getString(AppConstants.API_KEY);
                Map<String, String> headerParams = new HashMap<>();
                headerParams.put("client-id", clientiD);
                VolleyNetworkRequestHandler.getInstance(context, UserDashboardActivity.this).getStringData(ApiURLS.USER_PROFILE, ApiURLS.ApiId.USER_PROFILE_UPDATE, ApiURLS.REQUEST_PUT, paramPost, headerParams);
            }
        });
    }


    private void settingNavigationView() {
        navigationView.getMenu().clear();
        //  navigationView.removeAllViews();
        final Menu menu = navigationView.getMenu();
        //    menu.add("Profile");
        menu.add(1, MENU_PROFILE_ID, 100, "Profile"); //(int groupId,int itemId, int, int order, CharSequence title )
        menu.add(1, 2, 200, "Profile1");
        menu.add(1, 3, 300, "Profile2");
/*        for (int i = 1; i <= 3; i++) {
            menu.add("Runtime item " + i);
            menu.add(1,1,100,"1");
        }*/
        LayoutInflater factory = LayoutInflater.from(context);
        final View deleteDialogView = factory.inflate(R.layout.nav_header_social_connect_base, null);
        navigationView.addHeaderView(deleteDialogView);


        // adding a section and items into it
 /*       final SubMenu subMenu = menu.addSubMenu("SubMenu Title");
        for (int i = 1; i <= 2; i++) {
            subMenu.add("SubMenu Item " + i);
        }*/
        for (int i = 0, count = navigationView.getChildCount(); i < count; i++) {
            final View child = navigationView.getChildAt(i);
            if (child != null && child instanceof ListView) {
                final ListView menuView = (ListView) child;
                final HeaderViewListAdapter adapter = (HeaderViewListAdapter) menuView.getAdapter();
                final BaseAdapter wrapped = (BaseAdapter) adapter.getWrappedAdapter();
                wrapped.notifyDataSetChanged();
            }
        }
        //  navigationView.addView();
    }

    @Override
    protected void initViews() {
        String token = FirebaseInstanceId.getInstance().getToken();
        fragment_container = (FrameLayout) findViewById(R.id.fragment_container);
        Logger.info(TAG, "============Token==========" + token);
    }

    @Override
    protected void initContext() {
        context = UserDashboardActivity.this;
        currentActivity = UserDashboardActivity.this;
        initObjects();
    }

    private void initObjects() {
        if (userFeedsFragment == null)
            userFeedsFragment = new UserFeedsFragment();

        if (userChatsFragment == null)
            userChatsFragment = new UserChatsFragment();

        if (userNoticeFragment == null)
            userNoticeFragment = new UserNoticeFragment();

        if (userFriendsFragment == null)
            userFriendsFragment = new UserFriendsFragment();

        if (userUpdatesFragment == null)
            userUpdatesFragment = new UserUpdatesFragment();
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
        return false;
    }

    @Override
    protected boolean isNavigationView() {
        return true;
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
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_user_profile:
                drawer.closeDrawer(navigationView);
                startActivity(this, UserProfileActivity.class);
                break;
            case R.id.nav_user_feeds:
                drawer.closeDrawer(navigationView);
                switchContent(R.id.fragment_container, userFeedsFragment, false, false, "userFeedFragment");
                settingTitle(getResources().getString(R.string.feed_fragment));
                break;
            case R.id.nav_user_chat:
                drawer.closeDrawer(navigationView);
                switchContent(R.id.fragment_container, userChatsFragment, false, false, "userChatsFragment");
                settingTitle(getResources().getString(R.string.chat_fragment));
                break;
            case R.id.nav_user_friends:
                drawer.closeDrawer(navigationView);
                switchContent(R.id.fragment_container, userFriendsFragment, false, false, "userFriendsFragment");
                settingTitle(getResources().getString(R.string.friends_fragment));
                break;
            case R.id.nav_user_notice:
                drawer.closeDrawer(navigationView);
                switchContent(R.id.fragment_container, userNoticeFragment, false, false, "userNoticeFragment");
                settingTitle(getResources().getString(R.string.notice_fragment));
                break;
            case R.id.nav_user_updates:
                drawer.closeDrawer(navigationView);
                switchContent(R.id.fragment_container, userUpdatesFragment, false, false, "userUpdatesFragment");
                settingTitle(getResources().getString(R.string.update_fragment));
                break;
            case R.id.nav_logout:
                SharedPreferenceUtils.getInstance(context).clearAll();
                startActivity(currentActivity, SplashActivity.class);
                finish();
                break;
            default:
                drawer.closeDrawer(navigationView);
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onBackPressed() {

        if (canExit) {
            // super.onBackPressed();
            finish();
            return;
        }
        canExit = true;
        toast("Press back again", false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Logger.info(TAG, "======Timer finished======");
                canExit = false;
            }
        }, AppConstants.BACK_EXIT_TIME);
    }

    @Override
    public void onResponseReceived(ApiURLS.ApiId apiId, JSONObject jsonObjectResponse) {

    }

    @Override
    public void onResponseReceived(ApiURLS.ApiId apiId, String stringResponse) {
        Logger.info(TAG, "======onResponseReceived======" + stringResponse);
        if (apiId == ApiURLS.ApiId.USER_PROFILE_UPDATE) {

            try {
                JSONObject jsonObject = new JSONObject(stringResponse);
                boolean error = jsonObject.getBoolean("error");
                if (!error) {
                    String data = jsonObject.getString("commandResult");
                    JSONObject jsonObject1 = new JSONObject(data);
                    int success = jsonObject1.getInt("success");
                    String message = jsonObject1.getString("message");
                    if (success == 1) {
                        JSONObject input = new JSONObject(jsonObject.getString("input"));
                        if (firstName != null) {
                            SharedPreferenceUtils.getInstance(context).putString(USER_FIRST_NAME, input.getString("firstName"));
                            SharedPreferenceUtils.getInstance(context).putBoolean(PROFILE_STATUS, true);
                            if (lastName != null) {
                                SharedPreferenceUtils.getInstance(context).putString(USER_LAST_NAME, input.getString("lastName"));
                            }
                            updateUserName();
                        }

                        toast(message, false);
                        b.dismiss();
                    } else {
                        toast(message, false);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onErrorResponse(ApiURLS.ApiId apiId, String errorData, int responseCode) {
        Logger.info(TAG, "======onErrorResponse======" + errorData);
    }

    private void updateUserName() {
        View header = navigationView.getHeaderView(0);

        //  TextView tvNavUserName = (TextView) navigationView.inflateHeaderView(R.layout.nav_header_social_connect_base).findViewById(R.id.tvNavUserName);
        TextView tvNavUserName = (TextView) header.findViewById(R.id.tvNavUserName);
        String fullname = null;
        String firstName = SharedPreferenceUtils.getInstance(context).getString(USER_FIRST_NAME);
        String lastName = SharedPreferenceUtils.getInstance(context).getString(USER_LAST_NAME);
        if (firstName != null) {
            fullname = firstName;
            if (lastName != null) {
                fullname = firstName + " " + lastName;
            }
        }
        if (fullname != null) {
            tvNavUserName.setText(fullname);
        }
    }
}
