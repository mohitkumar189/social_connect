package in.squarei.socialconnect.activities;

import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;

import com.google.firebase.iid.FirebaseInstanceId;

import in.squarei.socialconnect.R;
import in.squarei.socialconnect.utils.Logger;

import static in.squarei.socialconnect.interfaces.AppConstants.MENU_PROFILE_ID;

public class UserDashboardActivity extends SocialConnectBaseActivity {
    private static final String TAG = "UserDashboardActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        settingNavigationView();
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
        Logger.info(TAG, "============Token==========" + token);
    }

    @Override
    protected void initContext() {
        context = UserDashboardActivity.this;
        currentActivity = UserDashboardActivity.this;
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
        switch (item.getItemId()) {
            case MENU_PROFILE_ID:
                drawer.closeDrawer(navigationView);
                toast("profile", false);
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
}
