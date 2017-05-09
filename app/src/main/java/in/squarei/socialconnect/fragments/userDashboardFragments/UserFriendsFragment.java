package in.squarei.socialconnect.fragments.userDashboardFragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.squarei.socialconnect.R;
import in.squarei.socialconnect.activities.FriendProfileActivity;
import in.squarei.socialconnect.adapters.UserFriendsListAdapter;
import in.squarei.socialconnect.fragments.SocialConnectBaseFragment;
import in.squarei.socialconnect.interfaces.AppConstants;
import in.squarei.socialconnect.interfaces.ItemClickListener;
import in.squarei.socialconnect.modals.UsersListData;
import in.squarei.socialconnect.network.ApiURLS;
import in.squarei.socialconnect.network.UrlResponseListener;
import in.squarei.socialconnect.network.VolleyNetworkRequestHandler;
import in.squarei.socialconnect.utils.Logger;
import in.squarei.socialconnect.utils.SharedPreferenceUtils;


/**
 * Created by mohit kumar on 5/8/2017.
 */
public class UserFriendsFragment extends SocialConnectBaseFragment implements UrlResponseListener, ItemClickListener {

    private static final String TAG = "UserFriendsFragment";
    private RecyclerView recyclerViewFriendsList, recyclerViewSuggestedFriendsList;
    private List<UsersListData> usersListData;
    private UserFriendsListAdapter userFriendListAdapter;
    private UserFriendsListAdapter userSuggestionFriendListAdapter;
    private List<UsersListData> usersSuggestionListData;

    public UserFriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public void alertOkClicked() {

    }

    @Override
    protected void initViews() {
        recyclerViewFriendsList = (RecyclerView) currentActivity.findViewById(R.id.recyclerViewFriendsList);
        recyclerViewSuggestedFriendsList = (RecyclerView) currentActivity.findViewById(R.id.recyclerViewSuggestedFriendsList);
    }

    @Override
    protected void initContext() {
        currentActivity = getActivity();
        context = getActivity();
    }

    @Override
    protected void initListners() {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        usersListData = new ArrayList<>();
        usersSuggestionListData = new ArrayList<>();
        Map<String, String> headerParams = new HashMap<>();
        String clientiD = SharedPreferenceUtils.getInstance(context).getString(AppConstants.API_KEY);

        Logger.info(TAG, "===================client id==========" + clientiD);
        headerParams.put("client-id", clientiD);// 8887e71887f2f2b8dc191ff238ad5a4f
        VolleyNetworkRequestHandler volleyNetworkRequestHolder = VolleyNetworkRequestHandler.getInstance(context, this);
        volleyNetworkRequestHolder.getStringData(ApiURLS.USER_FRIENDS_LIST, ApiURLS.ApiId.USER_FRIENDS_LIST, ApiURLS.REQUEST_GET, null, headerParams);
        volleyNetworkRequestHolder.getStringData(ApiURLS.USER_SUGGESTION_FRIENDS_LIST, ApiURLS.ApiId.USER_FRIEND_SUGGESTIONS, ApiURLS.REQUEST_GET, null, headerParams);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_friends, container, false);
    }

    @Override
    public void onResponseReceived(ApiURLS.ApiId apiId, JSONObject jsonObjectResponse) {

    }

    @Override
    public void onResponseReceived(ApiURLS.ApiId apiId, String stringResponse) {
        Logger.info(TAG, "===================Response Received==========" + stringResponse);

        if (apiId == ApiURLS.ApiId.USER_FRIENDS_LIST) {
            updateUsersList(stringResponse);
        }
        if (apiId == ApiURLS.ApiId.USER_FRIEND_SUGGESTIONS) {
            updateUsersSuggestionList(stringResponse);
        }

    }

    private void updateUsersSuggestionList(String stringResponse) {
        Logger.info(TAG, "================updateUsersSuggestionList()=============");
        //userSuggestionFriendListAdapter;
        try {
            JSONObject jsonObject = new JSONObject(stringResponse);
            boolean error = jsonObject.getBoolean("error");
            if (!error) {
                JSONObject commandResult = jsonObject.getJSONObject("commandResult");
                String message = commandResult.getString("message");
                int success = commandResult.getInt("success");
                if (success == 1) {
                    String userid = null;
                    String firstName = null;
                    String lastName = null;
                    String profilePic = null;
                    String shared = null;
                    String prof_status = null;
                    String user_type = null;// 0==>individual, 1==>group
                    JSONArray data = commandResult.getJSONArray("data");
                    if (data.length() > 0) {
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject userData = data.getJSONObject(i);
                            userid = userData.getString("userid");
                            firstName = userData.getString("firstName");
                            lastName = userData.getString("lastName");
                            profilePic = userData.getString("profilePic");
                            //        shared = userData.getString("shared");
                            prof_status = userData.getString("prof_status");
                            //    user_type = userData.getString("user_type");
                            usersSuggestionListData.add(new UsersListData(userid, firstName + " " + lastName, prof_status, profilePic, user_type));
                        }
                        userSuggestionFriendListAdapter = new UserFriendsListAdapter(usersSuggestionListData, context, this);
                        recyclerViewSuggestedFriendsList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                        recyclerViewSuggestedFriendsList.setAdapter(userSuggestionFriendListAdapter);
                        //   updateUsersList();
                    }
                } else {

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void updateUsersList(String stringData) {
        Logger.info(TAG, "================updateUsersList()=============");

        try {
            JSONObject jsonObject = new JSONObject(stringData);
            boolean error = jsonObject.getBoolean("error");
            if (!error) {
                JSONObject commandResult = jsonObject.getJSONObject("commandResult");
                String message = commandResult.getString("message");
                int success = commandResult.getInt("success");
                if (success == 1) {
                    String userid = null;
                    String firstName = null;
                    String lastName = null;
                    String profilePic = null;
                    String shared = null;
                    String prof_status = null;
                    String user_type = null;// 0==>individual, 1==>group
                    JSONArray data = commandResult.getJSONArray("data");
                    if (data.length() > 0) {
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject userData = data.getJSONObject(i);
                            userid = userData.getString("userid");
                            firstName = userData.getString("firstName");
                            lastName = userData.getString("lastName");
                            profilePic = userData.getString("profilePic");
                            shared = userData.getString("shared");
                            prof_status = userData.getString("prof_status");
                            user_type = userData.getString("user_type");
                            usersListData.add(new UsersListData(userid, firstName + " " + lastName, prof_status, profilePic, user_type));
                        }
                        //   updateUsersList();
                        userFriendListAdapter = new UserFriendsListAdapter(usersListData, context, this);
                        recyclerViewFriendsList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                        recyclerViewFriendsList.setAdapter(userFriendListAdapter);
                    }
                } else {

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(ApiURLS.ApiId apiId, String errorData, int responseCode) {
        Logger.info(TAG, "================Error received=============" + errorData);
    }

    @Override
    public void onItemClickCallback(int position, int flag) {
        toast(context, "item clicked " + position);
        Intent intent = new Intent(currentActivity, FriendProfileActivity.class);
        intent.putExtra("userId", usersListData.get(position).getUserId());
        startActivity(intent);
        Logger.info(TAG, "==================user id to be passed===========" + usersListData.get(position).getUserId());
    }
}
