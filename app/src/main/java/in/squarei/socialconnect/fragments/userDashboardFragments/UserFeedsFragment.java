package in.squarei.socialconnect.fragments.userDashboardFragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.squarei.socialconnect.R;
import in.squarei.socialconnect.activities.UploadPostActivity;
import in.squarei.socialconnect.adapters.PostCommentsAdapter;
import in.squarei.socialconnect.adapters.UserFeedsAdapter;
import in.squarei.socialconnect.fragments.SocialConnectBaseFragment;
import in.squarei.socialconnect.interfaces.AppConstants;
import in.squarei.socialconnect.interfaces.ItemClickListener;
import in.squarei.socialconnect.modals.PostCommentsData;
import in.squarei.socialconnect.modals.UserFeedsData;
import in.squarei.socialconnect.network.ApiURLS;
import in.squarei.socialconnect.network.UrlResponseListener;
import in.squarei.socialconnect.network.VolleyNetworkRequestHandler;
import in.squarei.socialconnect.utils.Logger;
import in.squarei.socialconnect.utils.SharedPreferenceUtils;
import in.squarei.socialconnect.utils.Validator;

import static android.app.Activity.RESULT_OK;
import static in.squarei.socialconnect.network.ApiURLS.ApiId.FRIENDS_POST;
import static in.squarei.socialconnect.network.ApiURLS.ApiId.LIKE_POST;
import static in.squarei.socialconnect.network.ApiURLS.ApiId.POST_COMMENTS;
import static in.squarei.socialconnect.network.ApiURLS.ApiId.To_POST_COMMENT;

public class UserFeedsFragment extends SocialConnectBaseFragment implements UrlResponseListener, ItemClickListener {

    private static final String TAG = "UserFeedsFragment";
    private static final int UPLOAD_ACTIVITY = 100;
    String postIdd;
    private RecyclerView recyclerViewUserFeeds;
    private List<UserFeedsData> userFeedData;
    private List<PostCommentsData> postCommentsData;
    private boolean isDismissable = false;
    private AlertDialog b;
    private String clientiD;
    private SwipeRefreshLayout swiperefresh;
    private UserFeedsAdapter userFeedAdapter;
    private LinearLayout linearWritePost, linearPostUpdate, linearSharePost;
    private int adapterPosition = -1;
    private boolean loading = false;

    public UserFeedsFragment() {
        // Required empty public constructor
    }

    @Override
    public void alertOkClicked() {

    }

    @Override
    protected void initViews() {
        recyclerViewUserFeeds = (RecyclerView) currentActivity.findViewById(R.id.recyclerViewUserFeeds);
        swiperefresh = (SwipeRefreshLayout) currentActivity.findViewById(R.id.swiperefresh);
        linearWritePost = (LinearLayout) currentActivity.findViewById(R.id.linearWritePost);
        linearPostUpdate = (LinearLayout) currentActivity.findViewById(R.id.linearPostUpdate);
        linearSharePost = (LinearLayout) currentActivity.findViewById(R.id.linearSharePost);
    }

    @Override
    protected void initContext() {
        currentActivity = getActivity();
        context = getActivity();
        userFeedData = new ArrayList<>();
    }

    @Override
    protected void initListners() {
        linearWritePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UploadPostActivity.class);
                startActivityForResult(intent, UPLOAD_ACTIVITY);
            }
        });
        linearSharePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UploadPostActivity.class);
                startActivityForResult(intent, UPLOAD_ACTIVITY);
            }
        });
        swiperefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                swiperefresh.setRefreshing(false);
                                updateFeedsListOnRefresh();
                            }
                        }, 500);

                    }
                });
        linearPostUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UploadPostActivity.class);
                startActivityForResult(intent, UPLOAD_ACTIVITY);
            }
        });
    }

    private void updateFeedsListOnRefresh() {
        if (userFeedData != null) {
            userFeedData.clear();
            userFeedAdapter.notifyDataSetChanged();
        }
        loadDataForFeeds();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Logger.info(TAG, "==============onCreateView================");
        return inflater.inflate(R.layout.fragment_user_feeds, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Logger.info(TAG, "==============onActivityCreated================");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Logger.info(TAG, "==============onAttach================");
        clientiD = SharedPreferenceUtils.getInstance(context).getString(AppConstants.API_KEY);
        Logger.info(TAG, "===================client id==========" + clientiD);
        loadDataForFeeds();
    }

    private void loadDataForFeeds() {
        Map<String, String> headerParams = new HashMap<>();
        headerParams.put("client-id", clientiD);// 8887e71887f2f2b8dc191ff238ad5a4f

        if (Validator.getInstance().isNetworkAvailable(context)) {
            VolleyNetworkRequestHandler volleyNetworkRequestHolder = VolleyNetworkRequestHandler.getInstance(context, this);
            volleyNetworkRequestHolder.getStringData(ApiURLS.FRIENDS_POSTS, FRIENDS_POST, ApiURLS.REQUEST_GET, null, headerParams);
        } else {
            toast(context, currentActivity.getResources().getString(R.string.network_error));
        }

    }

    @Override
    public void onResponseReceived(ApiURLS.ApiId apiId, JSONObject jsonObjectResponse) {

    }

    @Override
    public void onResponseReceived(ApiURLS.ApiId apiId, String stringResponse) {
        Logger.info(TAG, "===================onResponseReceived==========" + stringResponse);
        if (apiId == LIKE_POST) {
            try {
                JSONObject jsonObject = new JSONObject(stringResponse);
                Boolean error = jsonObject.getBoolean("error");
                if (!error) {
                    JSONObject commandResult = jsonObject.getJSONObject("commandResult");
                    String message = commandResult.getString("message");
                    switch (message) {
                        case "Liked":
                            break;
                        case "Disliked":
                            break;
                        default:
                            break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (apiId == To_POST_COMMENT) {
            try {
                JSONObject jsonObject = new JSONObject(stringResponse);
                Boolean error = jsonObject.getBoolean("error");
                if (!error) {
                    JSONObject commandResult = jsonObject.getJSONObject("commandResult");
                    int success = commandResult.getInt("success");
                    String message = commandResult.getString("message");
                    if (success == 1) {
                        toast(context, message);
                        b.dismiss();
                    } else {
                        toast(context, message);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (apiId == POST_COMMENTS) {
            try {
                JSONObject jsonObject = new JSONObject(stringResponse);
                Boolean error = jsonObject.getBoolean("error");
                if (!error) {
                    JSONObject commandResult = jsonObject.getJSONObject("commandResult");
                    int success = commandResult.getInt("success");
                    postCommentsData = new ArrayList<>();
                    if (success == 1) {
                        JSONArray data = new JSONArray(commandResult.getString("data"));
                        // JSONArray data = commandResult.getJSONArray("data");
                        Logger.info(TAG, "================length=============" + data.length() + " ====data===" + data);
                        if (data.length() > 0) {
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject comment = data.getJSONObject(i);
                                String postId = comment.getString("id");
                                String postComment = comment.getString("comment");
                                String commentOn = comment.getString("commentOn");
                                String commentBy = comment.getString("commentBy");
                                String profilePic = comment.getString("profilePic");
                                String commentByName = comment.getString("firstName") + " " + comment.getString("lastName");
                                postCommentsData.add(new PostCommentsData(postId, postComment, commentOn, commentBy, profilePic, commentByName));
                            }
                            showUserCommentsDialog();

                        } else {
                            toast(context, "No Records Found");
                            showUserCommentsDialog();
                        }
                    } else {

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (apiId == FRIENDS_POST) {

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
                            String postLikes = jsonFeed.getString("likes");
                            String postComments = jsonFeed.getString("comments");
                            // String postShares = String.valueOf(jsonFeed.getLong("shares"));
                            userFeedData.add(new UserFeedsData(postedBy, postTitle, postLikes, postComments, postImageUrl, postId));
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            setUpAdapter(userFeedData);

        }
    }

    private void setUpAdapter(List<UserFeedsData> userFeedsData) {
        userFeedAdapter = new UserFeedsAdapter(userFeedsData, context, this);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerViewUserFeeds.setLayoutManager(mLayoutManager);
        recyclerViewUserFeeds.setHasFixedSize(false);
        recyclerViewUserFeeds.setAdapter(userFeedAdapter);
        recyclerViewUserFeeds.setNestedScrollingEnabled(false);

        recyclerViewUserFeeds.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastvisibleitemposition = mLayoutManager.findLastVisibleItemPosition();
                if (lastvisibleitemposition == userFeedAdapter.getItemCount() - 1) {

                    if (!loading) {
                        loading = true;
                        // Increment the pagecount everytime we scroll to fetch data from the next pageR
                        // make loading = false once the data is loaded
                        // call mAdapter.notifyDataSetChanged() to refresh the Adapter and Layout
                    }
                }
            }
        });
    }

    @Override
    public void onErrorResponse(ApiURLS.ApiId apiId, String errorData, int responseCode) {
        Logger.info(TAG, "===================onErrorResponse==========" + errorData);
        if (errorData != null) toast(context, "Something went wrong");
    }

    @Override
    public void onItemClickCallback(int position, int flag) {
        switch (flag) {
            case 1:
                getPostComments(position);
                break;
            case 2:
                likeUserPost(position);
                break;
            default:
                break;
        }
    }

    private void likeUserPost(int position) {
        adapterPosition = position;
        Map<String, String> headerParams = new HashMap<>();
        headerParams.put("client-id", clientiD);
        String postId = userFeedData.get(position).getPostId();
        if (Validator.getInstance().isNetworkAvailable(context)) {
            VolleyNetworkRequestHandler volleyNetworkRequestHolder = VolleyNetworkRequestHandler.getInstance(context, this);
            volleyNetworkRequestHolder.getStringData(ApiURLS.TO_LIKE_POST + postId, LIKE_POST, ApiURLS.REQUEST_POST, null, headerParams);
        } else {
            toast(context, currentActivity.getResources().getString(R.string.network_error));
        }

    }

    private void getPostComments(int position) {
        Map<String, String> headerParams = new HashMap<>();
        String postId = userFeedData.get(position).getPostId();
        postIdd = postId;
        Logger.info(TAG, "===================client id==========" + clientiD);
        headerParams.put("client-id", clientiD);// 8887e71887f2f2b8dc191ff238ad5a4f

        if (Validator.getInstance().isNetworkAvailable(context)) {
            VolleyNetworkRequestHandler volleyNetworkRequestHolder = VolleyNetworkRequestHandler.getInstance(context, this);
            volleyNetworkRequestHolder.getStringData(ApiURLS.POST_COMMENTS + postId + "/comments", POST_COMMENTS, ApiURLS.REQUEST_GET, null, headerParams);
        } else {
            toast(context, currentActivity.getResources().getString(R.string.network_error));
        }


    }

    private void showUserCommentsDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = currentActivity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_post_comments, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(true);
        dialogBuilder.setTitle("Comments");
        b = dialogBuilder.create();
        RecyclerView recyclerUserComments = (RecyclerView) dialogView.findViewById(R.id.recyclerUserComments);
        final EditText editEnteredComment = (EditText) dialogView.findViewById(R.id.editEnteredComment);
        ImageView ivCommentSend = (ImageView) dialogView.findViewById(R.id.ivCommentSend);

        if (postCommentsData.size() > 0) {
            PostCommentsAdapter postCommentsAdapter = new PostCommentsAdapter(postCommentsData, context, this);
            recyclerUserComments.setLayoutManager(new LinearLayoutManager(context));
            recyclerUserComments.setAdapter(postCommentsAdapter);
        }
        b.show();

        ivCommentSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = editEnteredComment.getText().toString();
                postComment(comment);
            }
        });
    }

    private void postComment(String comment) {
        Map<String, String> headerParams = new HashMap<>();
        Map<String, String> postParams = new HashMap<>();
        postParams.put("comment", comment);
        postParams.put("post_id", postIdd);
        Logger.info(TAG, "===================client id==========" + clientiD);
        headerParams.put("client-id", clientiD);// 8887e71887f2f2b8dc191ff238ad5a4f
        if (Validator.getInstance().isNetworkAvailable(context)) {
            VolleyNetworkRequestHandler volleyNetworkRequestHolder = VolleyNetworkRequestHandler.getInstance(context, this);
            volleyNetworkRequestHolder.getStringData(ApiURLS.To_POST_COMMENT, To_POST_COMMENT, ApiURLS.REQUEST_POST, postParams, headerParams);
        } else {
            toast(context, currentActivity.getResources().getString(R.string.network_error));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Logger.info(TAG, "==============onAttach================");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Logger.info(TAG, "===============onDetach===============");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == UPLOAD_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                updateFeedsListOnRefresh();
            }
        }
    }
}
