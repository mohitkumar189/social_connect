package in.squarei.socialconnect.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.squarei.socialconnect.R;
import in.squarei.socialconnect.adapters.ViewPagerAdapter;
import in.squarei.socialconnect.fragments.userDashboardFragments.UserChatsFragment;
import in.squarei.socialconnect.fragments.userDashboardFragments.UserFeedsFragment;
import in.squarei.socialconnect.fragments.userDashboardFragments.UserFriendsFragment;
import in.squarei.socialconnect.fragments.userDashboardFragments.UserNoticeFragment;
import in.squarei.socialconnect.fragments.userDashboardFragments.UserUpdatesFragment;
import in.squarei.socialconnect.interfaces.AppConstants;
import in.squarei.socialconnect.network.ApiURLS;
import in.squarei.socialconnect.network.UrlResponseListener;
import in.squarei.socialconnect.network.VolleyNetworkRequestHandler;
import in.squarei.socialconnect.utils.CommonUtils;
import in.squarei.socialconnect.utils.CustomTypefaceSpan;
import in.squarei.socialconnect.utils.Logger;
import in.squarei.socialconnect.utils.SharedPreferenceUtils;

import static in.squarei.socialconnect.interfaces.AppConstants.COMMUNITY_NAME;
import static in.squarei.socialconnect.interfaces.AppConstants.COMMUNITY_STATUS;
import static in.squarei.socialconnect.interfaces.AppConstants.FIREBASE_STATUS;
import static in.squarei.socialconnect.interfaces.AppConstants.IS_LOGIN;
import static in.squarei.socialconnect.interfaces.AppConstants.MENU_PROFILE_ID;
import static in.squarei.socialconnect.interfaces.AppConstants.PROFILE_STATUS;
import static in.squarei.socialconnect.interfaces.AppConstants.USER_FIRST_NAME;
import static in.squarei.socialconnect.interfaces.AppConstants.USER_LAST_NAME;
import static in.squarei.socialconnect.network.ApiURLS.USER_UPDATE;

public class UserDashboardActivity extends SocialConnectBaseActivity implements UrlResponseListener, TabLayout.OnTabSelectedListener {

    private static final String TAG = "UserDashboardActivity";
    String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    int PERMISSION_ALL = 1;
    boolean isVisible = true;
    private boolean canExit = false;
    private FrameLayout fragment_container;
    private UserFeedsFragment userFeedsFragment;
    private UserChatsFragment userChatsFragment;
    private UserNoticeFragment userNoticeFragment;
    private UserFriendsFragment userFriendsFragment;
    private UserUpdatesFragment userUpdatesFragment;
    private AlertDialog b;
    private String firstName, lastName;
    private ImageView image_show;
    private NestedScrollView scrollview;
    private TextView tvNavUserCommunityName;
    private TabLayout tabs;
    private ViewPager viewpager;
    private LinearLayout ll_profile, ll_updates, ll_notice, ll_friends, ll_chat, ll_home;

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

    private void changeFonts() {
        Menu m = navigationView.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }
    }

    private void applyFontToMenuItem(MenuItem mi1) {
        Typeface font = Typeface.createFromAsset(context.getAssets(), "gotham.ttf");
        SpannableString mNewTitle = new SpannableString(mi1.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi1.setTitle(mNewTitle);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        // settingNavigationView();
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        //  switchContent(R.id.fragment_container, userFeedsFragment, true, false, "userFeedFragment");
        navigationView.setCheckedItem(R.id.nav_user_feeds);
      //  settingTitle(getResources().getString(R.string.feed_fragment));
        settingTitle("LANDMARK CITY");
        //checkForProfileCompleteDialog();
        changeFonts();
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
        tabs = (TabLayout) findViewById(R.id.tabs);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        tabs.setupWithViewPager(viewpager);
        ll_profile = (LinearLayout) findViewById(R.id.ll_profile);
        ll_updates = (LinearLayout) findViewById(R.id.ll_updates);
        ll_notice = (LinearLayout) findViewById(R.id.ll_notice);
        ll_friends = (LinearLayout) findViewById(R.id.ll_friends);
        ll_chat = (LinearLayout) findViewById(R.id.ll_chat);
        ll_home = (LinearLayout) findViewById(R.id.ll_home);

        String token = FirebaseInstanceId.getInstance().getToken();
        //fragment_container = (FrameLayout) findViewById(R.id.fragment_container);
        Logger.info(TAG, "============Token==========" + token);
        scrollview = (NestedScrollView) findViewById(R.id.scrollview);
        image_show = (ImageView) findViewById(R.id.image_show);

        image_show.setOnClickListener(this);

        if (!SharedPreferenceUtils.getInstance(context).getBoolean(FIREBASE_STATUS)) {
            String clientiD = SharedPreferenceUtils.getInstance(context).getString(AppConstants.API_KEY);
            if (clientiD != null) {
                if (token != null)
                    CommonUtils.saveDeviceToken(USER_UPDATE, token, clientiD, context);
            }
        }

        //  image_show.bringToFront();
        // scrollview.bringToFront();
        setupViewPager();

        tabs.addOnTabSelectedListener(this);
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new UserFeedsFragment(), "LANDMARK CITY");
        adapter.addFragment(new UserChatsFragment(), "CHATS");
        adapter.addFragment(new UserFriendsFragment(), "FRIENDS");
        adapter.addFragment(new UserNoticeFragment(), "NOTICES");
        adapter.addFragment(new UserUpdatesFragment(), "UPDATES");
        viewpager.setAdapter(adapter);
    }

    @Override
    protected void initContext() {
        context = UserDashboardActivity.this;
        currentActivity = UserDashboardActivity.this;
        //  initObjects();
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
        ll_profile.setOnClickListener(this);
        ll_updates.setOnClickListener(this);
        ll_notice.setOnClickListener(this);
        ll_friends.setOnClickListener(this);
        ll_chat.setOnClickListener(this);
        ll_home.setOnClickListener(this);
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
        return true;
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
            case R.id.nav_user_feeds:
                drawer.closeDrawer(navigationView);
                viewpager.setCurrentItem(0);
                //   switchContent(R.id.fragment_container, userFeedsFragment, false, false, "userFeedFragment");
                settingTitle(getResources().getString(R.string.feed_fragment));
                break;
            case R.id.nav_user_chat:
                drawer.closeDrawer(navigationView);
                //  startActivity(currentActivity, UserChatActivity.class);
                viewpager.setCurrentItem(1);

                //  switchContent(R.id.fragment_container, userChatsFragment, false, false, "userChatsFragment");
                // settingTitle(getResources().getString(R.string.chat_fragment));
                break;
            case R.id.nav_user_friends:
                drawer.closeDrawer(navigationView);
                viewpager.setCurrentItem(2);
                //switchContent(R.id.fragment_container, userFriendsFragment, false, false, "userFriendsFragment");
                settingTitle(getResources().getString(R.string.friends_fragment));
                break;
            case R.id.nav_user_notice:
                drawer.closeDrawer(navigationView);
                //  startActivity(currentActivity, NotificationActivity.class);
                viewpager.setCurrentItem(3);
                //  switchContent(R.id.fragment_container, userNoticeFragment, false, false, "userNoticeFragment");
                //   settingTitle(getResources().getString(R.string.notice_fragment));
                break;
            case R.id.nav_user_updates:
                drawer.closeDrawer(navigationView);
                //  startActivity(currentActivity, UserUpdateActivity.class);
                viewpager.setCurrentItem(4);
                //   switchContent(R.id.fragment_container, userUpdatesFragment, false, false, "userUpdatesFragment");
                //  settingTitle(getResources().getString(R.string.update_fragment));
                break;
            case R.id.nav_user_profile:
                drawer.closeDrawer(navigationView);
                startActivity(this, UserProfileActivity.class);
                break;
            case R.id.nav_logout:
                SharedPreferenceUtils.getInstance(context).putBoolean(IS_LOGIN, false);
                //  SharedPreferenceUtils.getInstance(context).putBoolean(PROFILE_STATUS, false);
                // SharedPreferenceUtils.getInstance(context).putString(USER_FIRST_NAME, "");
                // SharedPreferenceUtils.getInstance(context).putString(USER_LAST_NAME, "");
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
        switch (v.getId()) {
            case R.id.image_show:
                hideHome();
                break;
            case R.id.ll_profile:
                startActivity(this, UserProfileActivity.class);
                hideHome();
                break;
            case R.id.ll_updates:
                viewpager.setCurrentItem(4);
                hideHome();
                break;
            case R.id.ll_notice:
                viewpager.setCurrentItem(3);
                hideHome();
                break;
            case R.id.ll_friends:
                viewpager.setCurrentItem(2);
                hideHome();
                break;
            case R.id.ll_chat:
                viewpager.setCurrentItem(1);
                hideHome();
                break;
            case R.id.ll_home:
                viewpager.setCurrentItem(0);
                hideHome();
                break;

        }
    }


    private void hideHome() {
        if (isVisible) {
            // scrollview.setVisibility(View.GONE);
            hideAnimation();
            isVisible = false;
        } else {
            //  scrollview.setVisibility(View.VISIBLE);
            showAnimation();
            isVisible = true;
        }
    }


    private void showAnimation() {
        Animation anim;
        anim = AnimationUtils.loadAnimation(context, R.anim.leftright);

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                scrollview.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        scrollview.startAnimation(anim);
        image_show.startAnimation(anim);
    }


    private void hideAnimation() {
        Animation anim;
        anim = AnimationUtils.loadAnimation(context, R.anim.righttoleft);

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                scrollview.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        scrollview.startAnimation(anim);
        image_show.startAnimation(anim);
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
                        if (firstName != "null" && firstName != "") {
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
        TextView tvNavUserCommunityName = (TextView) header.findViewById(R.id.tvNavUserCommunityName);
        String fullname = null;
        String firstName = SharedPreferenceUtils.getInstance(context).getString(USER_FIRST_NAME);
        String lastName = SharedPreferenceUtils.getInstance(context).getString(USER_LAST_NAME);
        if (SharedPreferenceUtils.getInstance(context).getBoolean(COMMUNITY_STATUS)) {
            String communityName = SharedPreferenceUtils.getInstance(context).getString(COMMUNITY_NAME);
            Logger.info(TAG, "==============Community name in SF============" + communityName);
            if (communityName != null && communityName != "") {
                tvNavUserCommunityName.setText(communityName);
            }
        }


        if (firstName != null && firstName.length() != 0) {
            fullname = firstName;
            if (lastName != null) {
                fullname = firstName + " " + lastName;
            }
        }
        if (fullname != null) {
            tvNavUserName.setText(fullname);
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        // tab.getPosition();
        toolbar.setTitle(tab.getText());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.lCity:
                //Toast.makeText(getApplicationContext(), "Item 1 Selected", Toast.LENGTH_LONG).show();
                startActivity(currentActivity, ActivityLandmarkCityDetail.class);
                return true;
            case R.id.dsr:
                //Toast.makeText(getApplicationContext(), "Item 1 Selected", Toast.LENGTH_LONG).show();
                startActivity(currentActivity, DsrActivity.class);
                return true;
            case R.id.crm:
                //Toast.makeText(getApplicationContext(), "Item 1 Selected", Toast.LENGTH_LONG).show();
                startActivity(currentActivity, CrmActivity.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
