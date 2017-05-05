package in.squarei.socialconnect.fragments.userDashboardFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.squarei.socialconnect.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFriendsFragment extends Fragment {


    public UserFriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_friends, container, false);
    }

}
