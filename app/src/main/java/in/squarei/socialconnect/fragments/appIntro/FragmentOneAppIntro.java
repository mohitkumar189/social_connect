package in.squarei.socialconnect.fragments.appIntro;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.squarei.socialconnect.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOneAppIntro extends Fragment {


    public FragmentOneAppIntro() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_one_app_intro, container, false);
    }

}
