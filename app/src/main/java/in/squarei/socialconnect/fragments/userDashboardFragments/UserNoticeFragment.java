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
import in.squarei.socialconnect.adapters.NotificationsListAdapter;
import in.squarei.socialconnect.interfaces.ItemClickListener;
import in.squarei.socialconnect.modals.NotificationData;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserNoticeFragment extends Fragment implements ItemClickListener {
    private RecyclerView recyclerView;
    private List<NotificationData> notificationData;
    private NotificationsListAdapter notificationsListAdapter;

    public UserNoticeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_notice, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        notificationData = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        prepareData();

    }

    private void prepareData() {
        notificationData.add(null);
        notificationData.add(null);
        notificationData.add(null);
        notificationData.add(null);
        notificationData.add(null);
        notificationData.add(null);
        notificationsListAdapter = new NotificationsListAdapter(notificationData, getActivity(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(notificationsListAdapter);

    }

    @Override
    public void onItemClickCallback(int position, int flag) {

    }
}
