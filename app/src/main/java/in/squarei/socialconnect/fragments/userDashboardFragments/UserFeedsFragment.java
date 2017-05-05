package in.squarei.socialconnect.fragments.userDashboardFragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.squarei.socialconnect.R;
import in.squarei.socialconnect.adapters.UserFeedsAdapter;
import in.squarei.socialconnect.fragments.SocialConnectBaseFragment;
import in.squarei.socialconnect.interfaces.AppConstants;
import in.squarei.socialconnect.modals.UserFeedsData;
import in.squarei.socialconnect.network.ApiURLS;
import in.squarei.socialconnect.network.UrlResponseListener;
import in.squarei.socialconnect.network.VolleyNetworkRequestHandler;
import in.squarei.socialconnect.utils.Logger;
import in.squarei.socialconnect.utils.SharedPreferenceUtils;

public class UserFeedsFragment extends SocialConnectBaseFragment implements UrlResponseListener {

    private static final String TAG = "UserFeedsFragment";
    private RecyclerView recyclerViewUserFeeds;
    private List<UserFeedsData> userFeedData;

    public UserFeedsFragment() {
        // Required empty public constructor
    }

    @Override
    public void alertOkClicked() {

    }

    @Override
    protected void initViews() {
        recyclerViewUserFeeds = (RecyclerView) currentActivity.findViewById(R.id.recyclerViewUserFeeds);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_feeds, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Map<String, String> headerParams = new HashMap<>();
        String clientiD = SharedPreferenceUtils.getInstance(context).getString(AppConstants.API_KEY);

        Logger.info(TAG, "===================client id==========" + clientiD);
        headerParams.put("client-id", clientiD);// 8887e71887f2f2b8dc191ff238ad5a4f
        VolleyNetworkRequestHandler volleyNetworkRequestHolder = VolleyNetworkRequestHandler.getInstance(context, this);
        volleyNetworkRequestHolder.getStringData(ApiURLS.FRIENDS_POSTS, ApiURLS.ApiId.FRIENDS_POST, ApiURLS.REQUEST_GET, null, headerParams);
    }

    @Override
    public void onResponseReceived(ApiURLS.ApiId apiId, JSONObject jsonObjectResponse) {

    }

    @Override
    public void onResponseReceived(ApiURLS.ApiId apiId, String stringResponse) {
        userFeedData = new ArrayList<>();
        Logger.info(TAG, "===================onResponseReceived==========" + stringResponse);
        try {
            JSONObject jsonObject = new JSONObject(stringResponse);
            Boolean error = jsonObject.getBoolean("error");
            if (!error) {
                JSONObject commandResult = jsonObject.getJSONObject("commandResult");
                int success = commandResult.getInt("success");
                if (success == 1) {
                    JSONArray data = commandResult.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jsonFeed = data.getJSONObject(i);
                        String postId = jsonFeed.getString("id");
                        String postedBy = "Posted By " + jsonFeed.getString("postedBy");
                        String postTitle = jsonFeed.getString("title");
                        String postImageUrl = jsonFeed.getString("postURL");
                        String postDescription = jsonFeed.getString("description");
                        String userId = jsonFeed.getString("user_id");
                        String postTime = jsonFeed.getString("posted_on");
                        String postType = jsonFeed.getString("post_type");
                        String postLikes = String.valueOf(jsonFeed.getLong("likes"));
                        String postComments = String.valueOf(jsonFeed.getLong("comments"));
                        // String postShares = String.valueOf(jsonFeed.getLong("shares"));
                        userFeedData.add(new UserFeedsData(postedBy, postTitle, postLikes, postComments, postImageUrl));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UserFeedsAdapter userFeedAdapter = new UserFeedsAdapter(userFeedData, context);
        recyclerViewUserFeeds.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewUserFeeds.setAdapter(userFeedAdapter);
        recyclerViewUserFeeds.setNestedScrollingEnabled(false);
    }

    @Override
    public void onErrorResponse(ApiURLS.ApiId apiId, String errorData, int responseCode) {
        Logger.info(TAG, "===================onErrorResponse==========" + errorData);
        if (errorData != null) toast(context, "Something went wrong");
    }
}
