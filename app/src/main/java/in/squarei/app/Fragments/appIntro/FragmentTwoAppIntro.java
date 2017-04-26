package in.squarei.app.Fragments.appIntro;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.squarei.app.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTwoAppIntro extends Fragment {


    public FragmentTwoAppIntro() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_two_app_intro, container, false);
    }

}
