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
public class UserChatsFragment extends Fragment {


    public UserChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_chats, container, false);
    }

}
