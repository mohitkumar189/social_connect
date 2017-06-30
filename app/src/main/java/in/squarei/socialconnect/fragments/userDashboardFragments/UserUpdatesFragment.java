package in.squarei.socialconnect.fragments.userDashboardFragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import in.squarei.socialconnect.R;
import in.squarei.socialconnect.adapters.UserUpdatesAdapter;
import in.squarei.socialconnect.interfaces.ItemClickListener;
import in.squarei.socialconnect.modals.UpdatesData;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserUpdatesFragment extends Fragment implements ItemClickListener {

    private RecyclerView recyclerView;
    private List<UpdatesData> updatesData;
    private UserUpdatesAdapter userUpdatesAdapter;

    public UserUpdatesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_updates, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updatesData = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        prepareData();
    }

    private void prepareData() {
        updatesData.add(null);
        updatesData.add(null);
        updatesData.add(null);
        updatesData.add(null);
        updatesData.add(null);

        userUpdatesAdapter = new UserUpdatesAdapter(updatesData, getActivity(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(userUpdatesAdapter);

    }

    @Override
    public void onItemClickCallback(int position, int flag) {

    }
}
