package in.squarei.socialconnect.activities;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;

import com.google.firebase.iid.FirebaseInstanceId;

import in.squarei.socialconnect.R;
import in.squarei.socialconnect.fragments.userDashboardFragments.UserChatsFragment;
import in.squarei.socialconnect.fragments.userDashboardFragments.UserFeedsFragment;
import in.squarei.socialconnect.fragments.userDashboardFragments.UserFriendsFragment;
import in.squarei.socialconnect.fragments.userDashboardFragments.UserNoticeFragment;
import in.squarei.socialconnect.fragments.userDashboardFragments.UserUpdatesFragment;
import in.squarei.socialconnect.interfaces.AppConstants;
import in.squarei.socialconnect.utils.Logger;

import static in.squarei.socialconnect.interfaces.AppConstants.MENU_PROFILE_ID;

public class UserDashboardActivity extends SocialConnectBaseActivity {
    private static final String TAG = "UserDashboardActivity";
    private boolean canExit = false;
    private FrameLayout fragment_container;
    private UserFeedsFragment userFeedsFragment;
    private UserChatsFragment userChatsFragment;
    private UserNoticeFragment userNoticeFragment;
    private UserFriendsFragment userFriendsFragment;
    private UserUpdatesFragment userUpdatesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        // settingNavigationView();
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
        switchContent(R.id.fragment_container, userFeedsFragment, true, false, "userFeedFragment");
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
        return true;
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
                toast("profile", false);
                startActivity(this, UserProfileActivity.class);
                break;
            case R.id.nav_user_feeds:
                drawer.closeDrawer(navigationView);
                switchContent(R.id.fragment_container, userFeedsFragment, true, false, "userFeedFragment");
                break;
            case R.id.nav_user_chat:
                drawer.closeDrawer(navigationView);
                switchContent(R.id.fragment_container, userChatsFragment, true, false, "userChatsFragment");
                break;
            case R.id.nav_user_friends:
                drawer.closeDrawer(navigationView);
                switchContent(R.id.fragment_container, userFriendsFragment, true, false, "userFriendsFragment");
                break;
            case R.id.nav_user_notice:
                drawer.closeDrawer(navigationView);
                switchContent(R.id.fragment_container, userNoticeFragment, true, false, "userNoticeFragment");
                break;
            case R.id.nav_user_updates:
                drawer.closeDrawer(navigationView);
                switchContent(R.id.fragment_container, userUpdatesFragment, true, false, "userUpdatesFragment");
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
            super.onBackPressed();
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
}
