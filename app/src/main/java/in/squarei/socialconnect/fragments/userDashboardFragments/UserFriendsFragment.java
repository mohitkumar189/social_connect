package in.squarei.socialconnect.fragments.userDashboardFragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import in.squarei.socialconnect.adapters.UserFriendsSuggestionAdapter;
import in.squarei.socialconnect.fragments.SocialConnectBaseFragment;
import in.squarei.socialconnect.interfaces.AppConstants;
import in.squarei.socialconnect.interfaces.ItemClickListener;
import in.squarei.socialconnect.modals.UsersListData;
import in.squarei.socialconnect.network.ApiURLS;
import in.squarei.socialconnect.network.UrlResponseListener;
import in.squarei.socialconnect.network.VolleyNetworkRequestHandler;
import in.squarei.socialconnect.utils.Logger;
import in.squarei.socialconnect.utils.SharedPreferenceUtils;
import in.squarei.socialconnect.utils.Validator;

import static in.squarei.socialconnect.network.ApiURLS.ApiId.ACCEPT_FRIEND;
import static in.squarei.socialconnect.network.ApiURLS.ApiId.GET_FRIENDS_REQUESTS;
import static in.squarei.socialconnect.network.ApiURLS.ApiId.REJECT_FRIEND;
import static in.squarei.socialconnect.network.ApiURLS.ApiId.SEND_FRIEND_REQUEST;


/**
 * Created by mohit kumar on 5/8/2017.
 */
public class UserFriendsFragment extends SocialConnectBaseFragment implements UrlResponseListener, ItemClickListener {

    private static final String TAG = "UserFriendsFragment";
    UserFriendsSuggestionAdapter userSuggestionFriendListAdapter;
    private RecyclerView recyclerViewFriendsList, recyclerViewSuggestedFriendsList;
    private List<UsersListData> usersListData;
    private UserFriendsListAdapter userFriendListAdapter;
    private List<UsersListData> usersSuggestionListData;
    private String clientiD;
    private int adapterPosition;
    private int suggestionPosition = -1;

    public UserFriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public void alertOkClicked() {

    }

    @Override
    protected void initViews() {
        usersListData = new ArrayList<>();
        usersSuggestionListData = new ArrayList<>();
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

        fetchAllFriendsData();
    }

    private void fetchAllFriendsData() {

        Map<String, String> headerParams = new HashMap<>();
        clientiD = SharedPreferenceUtils.getInstance(context).getString(AppConstants.API_KEY);

        Logger.info(TAG, "===================client id==========" + clientiD);
        headerParams.put("client-id", clientiD);// 8887e71887f2f2b8dc191ff238ad5a4f

        if (Validator.getInstance().isNetworkAvailable(context)) {
            VolleyNetworkRequestHandler volleyNetworkRequestHolder = VolleyNetworkRequestHandler.getInstance(context, this);
            volleyNetworkRequestHolder.getStringData(ApiURLS.USER_FRIENDS_LIST, ApiURLS.ApiId.USER_FRIENDS_LIST, ApiURLS.REQUEST_GET, null, headerParams);
            volleyNetworkRequestHolder.getStringData(ApiURLS.USER_SUGGESTION_FRIENDS_LIST, ApiURLS.ApiId.USER_FRIEND_SUGGESTIONS, ApiURLS.REQUEST_GET, null, headerParams);
            volleyNetworkRequestHolder.getStringData(ApiURLS.GET_FRIENDS_REQUESTS, ApiURLS.ApiId.GET_FRIENDS_REQUESTS, ApiURLS.REQUEST_GET, null, headerParams);
        } else {
            toast(context, currentActivity.getResources().getString(R.string.network_error));
        }

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
        } else if (apiId == ApiURLS.ApiId.USER_FRIEND_SUGGESTIONS) {
            updateUsersSuggestionList(stringResponse);
        } else if (apiId == GET_FRIENDS_REQUESTS) {
            updateUserFriendsRequestsList(stringResponse);
        } else if (apiId == ACCEPT_FRIEND) {
            updateAdapter(stringResponse, true);
        } else if (apiId == REJECT_FRIEND) {
            updateAdapter(stringResponse, false);
        } else if (apiId == SEND_FRIEND_REQUEST) {
            updateSuggestionADapter(stringResponse);
        }
    }

    private void updateSuggestionADapter(String stringResponse) {
        Logger.info(TAG, "================updateSuggestionADapter()=============");
        try {
            JSONObject jsonObject = new JSONObject(stringResponse);
            boolean error = jsonObject.getBoolean("error");
            if (!error) {
                JSONObject commandResult = jsonObject.getJSONObject("commandResult");
                String message = commandResult.getString("message");
                int success = commandResult.getInt("success");
                if (success == 1) {
                    toast(context, message);
                    usersSuggestionListData.remove(suggestionPosition);
                    userSuggestionFriendListAdapter.notifyDataSetChanged();
                } else {
                    toast(context, message);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateAdapter(String response, boolean b) {
        Logger.info(TAG, "================updateAdapter()=============");
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.getBoolean("error");
            if (!error) {
                JSONObject commandResult = jsonObject.getJSONObject("commandResult");
                String message = commandResult.getString("message");
                int success = commandResult.getInt("success");
                if (success == 1) {
                    if (b) {
                        usersListData.remove(adapterPosition);
                    } else {
                        usersListData.remove(adapterPosition);
                    }
                    userFriendListAdapter.notifyDataSetChanged();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateUserFriendsRequestsList(String stringResponse) {
        try {
            JSONObject jsonObject = new JSONObject(stringResponse);
            boolean error = jsonObject.getBoolean("error");
            if (!error) {
                JSONObject commandResult = jsonObject.getJSONObject("commandResult");
                String message = commandResult.getString("message");
                int success = commandResult.getInt("success");
                if (success == 1) {
                    String requestId = null;
                    String firstName = null;
                    String lastName = null;
                    String profilePic = null;
                    String shared = null;
                    String connectedOn = null;
                    String updatedOn = null;// 0==>friend request, 1==>friend
                    JSONArray data = commandResult.getJSONArray("data");
                    if (data.length() > 0) {
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject userData = data.getJSONObject(i);
                            requestId = userData.getString("id");
                            firstName = userData.getString("firstName");
                            lastName = userData.getString("lastName");
                            profilePic = userData.getString("profilePic");
                            updatedOn = userData.getString("updatedOn");
                            connectedOn = userData.getString("connectedOn");
                            usersListData.add(new UsersListData(requestId, firstName + " " + lastName, null, profilePic, null, false, null));
                        }
                        userFriendListAdapter.notifyDataSetChanged();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
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
                            usersSuggestionListData.add(new UsersListData(userid, firstName + " " + lastName, prof_status, profilePic, "123", false, null));
                        }
                        userSuggestionFriendListAdapter = new UserFriendsSuggestionAdapter(usersSuggestionListData, context, this);
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
                    String user_type = null;// 0==>friend request, 1==>friend
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
                            usersListData.add(new UsersListData(userid, firstName + " " + lastName, prof_status, profilePic, user_type, true, null));
/*
                            if (user_type.equals("0")) {
                                usersListData.add(new UsersListData(userid, firstName + " " + lastName, prof_status, profilePic, user_type, false));
                            } else {
                                usersListData.add(new UsersListData(userid, firstName + " " + lastName, prof_status, profilePic, user_type, true));
                            }*/
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
        adapterPosition = position;
        //   toast(context, "item clicked " + position);
        Intent intent = new Intent(currentActivity, FriendProfileActivity.class);
        switch (flag) {
            case 0:
                Logger.info(TAG, "==================user id to be passed===========" + usersListData.get(position).getUserId());
                intent.putExtra("userId", usersListData.get(position).getUserId());
                intent.putExtra("userType", flag);
                startActivity(intent);
                break;
            case 1:
                Logger.info(TAG, "==================user id to be passed===========" + usersSuggestionListData.get(position).getUserId());
                intent.putExtra("userId", usersSuggestionListData.get(position).getUserId());
                intent.putExtra("userType", flag);
                startActivity(intent);
                break;
            case 2:
                //  toast(context, "add");
                acceptFriendRequest(usersListData.get(position).getUserId());
                break;
            case 3:
                //  toast(context, "remove");
                rejectFriendRequest(usersListData.get(position).getUserId());
                break;
            case 5:
                // toast(context, "send request");
                suggestionPosition = position;
                sendFriendRequest(usersSuggestionListData.get(position).getUserId());
                break;
            case 6:
                //   toast(context, "remove suggestion");
                usersSuggestionListData.remove(position);
                userSuggestionFriendListAdapter.notifyDataSetChanged();
                //   rejectFriendRequest(usersSuggestionListData.get(position).getUserId());
                break;
        }
    }

    private void acceptFriendRequest(String conncetionId) {
        Map<String, String> headerParams = new HashMap<>();
        Map<String, String> postParams = new HashMap<>();
        String clientiD = SharedPreferenceUtils.getInstance(context).getString(AppConstants.API_KEY);
        Logger.info(TAG, "===================client id==========" + clientiD + "=============userid=====" + conncetionId);
        headerParams.put("client-id", clientiD);// 8887e71887f2f2b8dc191ff238ad5a4f
        postParams.put("id", conncetionId);
        postParams.put("action", "1");
        postParams.put("status", "1");

        if (Validator.getInstance().isNetworkAvailable(context)) {
            VolleyNetworkRequestHandler volleyNetworkRequestHolder = VolleyNetworkRequestHandler.getInstance(context, this);
            volleyNetworkRequestHolder.getStringData(ApiURLS.UPDATE_FRIEND_STATUS, ACCEPT_FRIEND, ApiURLS.REQUEST_PUT, postParams, headerParams);
        } else {
            toast(context, currentActivity.getResources().getString(R.string.network_error));
        }

    }

    private void sendFriendRequest(String conncetionId) {
        Map<String, String> headerParams = new HashMap<>();
        Map<String, String> postParams = new HashMap<>();
        String clientiD = SharedPreferenceUtils.getInstance(context).getString(AppConstants.API_KEY);
        Logger.info(TAG, "===================client id==========" + clientiD + "=============userid=====" + conncetionId);
        headerParams.put("client-id", clientiD);// 8887e71887f2f2b8dc191ff238ad5a4f
        postParams.put("connection_id", conncetionId);

        if (Validator.getInstance().isNetworkAvailable(context)) {
            VolleyNetworkRequestHandler volleyNetworkRequestHolder = VolleyNetworkRequestHandler.getInstance(context, this);
            volleyNetworkRequestHolder.getStringData(ApiURLS.SEND_FRIEND_REQUEST, SEND_FRIEND_REQUEST, ApiURLS.REQUEST_POST, postParams, headerParams);
        } else {
            toast(context, currentActivity.getResources().getString(R.string.network_error));
        }

    }

    private void rejectFriendRequest(String conncetionId) {
        Map<String, String> headerParams = new HashMap<>();
        Map<String, String> postParams = new HashMap<>();
        Logger.info(TAG, "===================client id==========" + clientiD + "=============userid=====" + conncetionId);
        headerParams.put("client-id", clientiD);// 8887e71887f2f2b8dc191ff238ad5a4f
        postParams.put("id", conncetionId);
        postParams.put("action", "2");
        postParams.put("status", "0");

        if (Validator.getInstance().isNetworkAvailable(context)) {
            VolleyNetworkRequestHandler volleyNetworkRequestHolder = VolleyNetworkRequestHandler.getInstance(context, this);
            volleyNetworkRequestHolder.getStringData(ApiURLS.UPDATE_FRIEND_STATUS, REJECT_FRIEND, ApiURLS.REQUEST_PUT, postParams, headerParams);
        } else {
            toast(context, currentActivity.getResources().getString(R.string.network_error));
        }

    }
}
