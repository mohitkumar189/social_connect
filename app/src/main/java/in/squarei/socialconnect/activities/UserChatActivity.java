package in.squarei.socialconnect.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.squarei.socialconnect.R;
import in.squarei.socialconnect.adapters.ChatAdapter;
import in.squarei.socialconnect.adapters.UserFriendsListAdapter;
import in.squarei.socialconnect.chat.QBHelper;
import in.squarei.socialconnect.chat.QBHelperCallback;
import in.squarei.socialconnect.interfaces.AppConstants;
import in.squarei.socialconnect.interfaces.ItemClickListener;
import in.squarei.socialconnect.modals.UsersListData;
import in.squarei.socialconnect.network.ApiURLS;
import in.squarei.socialconnect.network.UrlResponseListener;
import in.squarei.socialconnect.network.VolleyNetworkRequestHandler;
import in.squarei.socialconnect.utils.Logger;
import in.squarei.socialconnect.utils.SharedPreferenceUtils;
import in.squarei.socialconnect.utils.Validator;

public class UserChatActivity extends SocialConnectBaseActivity implements ItemClickListener, UrlResponseListener, QBHelperCallback {
    private RecyclerView recyclerView;
    private List<String> chatData;
    private ChatAdapter chatAdapter;
    private List<UsersListData> usersListData;
    private UserFriendsListAdapter userFriendListAdapter;
    private String clientiD;
    private final String TAG = "UserChatActivity";
    private QBUser qbUser = new QBUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chat);
        //    prepareData();
        setTitle("Chat");
        fetchAllFriendsData();
        if (!checkSignIn())
            loginUser(null);
    }

    private boolean checkSignIn() {
        return QBSessionManager.getInstance().getSessionParameters() != null;
    }

    @Override
    protected void initViews() {
        chatData = new ArrayList<>();
        usersListData = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }

    private void fetchAllFriendsData() {

        Map<String, String> headerParams = new HashMap<>();
        clientiD = SharedPreferenceUtils.getInstance(context).getString(AppConstants.API_KEY);

        Logger.info(TAG, "===================client id==========" + clientiD);
        headerParams.put("client-id", clientiD);// 8887e71887f2f2b8dc191ff238ad5a4f

        if (Validator.getInstance().isNetworkAvailable(context)) {
            VolleyNetworkRequestHandler volleyNetworkRequestHolder = VolleyNetworkRequestHandler.getInstance(context, this);
            volleyNetworkRequestHolder.getStringData(ApiURLS.USER_FRIENDS_LIST, ApiURLS.ApiId.USER_FRIENDS_LIST, ApiURLS.REQUEST_GET, null, headerParams);
        } else {
            toast(currentActivity.getResources().getString(R.string.network_error), false);
        }

    }

    @Override
    protected void initContext() {
        context = UserChatActivity.this;
        currentActivity = UserChatActivity.this;
    }

    @Override
    protected void initListners() {

    }

    @Override
    protected boolean isActionBar() {
        return true;
    }

    @Override
    protected boolean isHomeButton() {
        return true;
    }

    @Override
    protected boolean isNavigationView() {
        return false;
    }

    @Override
    protected boolean isTabs() {
        return false;
    }

    @Override
    protected boolean isFab() {
        return false;
    }

    @Override
    protected boolean isDrawerListener() {
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onClick(View v) {

    }

    private void prepareData() {
        chatData.add(null);
        chatData.add(null);
        chatData.add(null);
        chatData.add(null);
        chatData.add(null);
        chatData.add(null);
        chatAdapter = new ChatAdapter(chatData, context, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(chatAdapter);

    }

    @Override
    public void onItemClickCallback(int position, int flag) {
        if (flag == 0) {
            Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
            String chatId = usersListData.get(position).getUserChatId();
            Intent intent = new Intent(context, ChatDetailActivity.class);
            intent.putExtra("chatId", chatId);
            intent.putExtra("userName", usersListData.get(position).getUserName());
            Logger.info(TAG, "===chat id sent:==" + chatId);
            startActivity(intent);
        }
    }

    @Override
    public void onResponseReceived(ApiURLS.ApiId apiId, JSONObject jsonObjectResponse) {

    }

    @Override
    public void onResponseReceived(ApiURLS.ApiId apiId, String stringResponse) {
        if (apiId == ApiURLS.ApiId.USER_FRIENDS_LIST) {
            updateUsersList(stringResponse);
            // createSession();
        }
    }

    private void loginUser(QBUser user) {
        qbUser.setEmail(SharedPreferenceUtils.getInstance(context).getString(AppConstants.EMAIL));
        qbUser.setPassword(QBHelper.CHAT_PASSWORD);
        Logger.info(TAG, "============QBUSER============" + qbUser);
        QBUsers.signIn(qbUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser user, Bundle args) {
                // success
                Logger.info(TAG, "login success====>");
                loginToChat(qbUser);
            }

            @Override
            public void onError(QBResponseException error) {
                // error
                Logger.info(TAG, "login error====>");
            }
        });
    }

    private void loginToChat(QBUser user) {
        // initialize Chat service
        QBChatService chatService = QBChatService.getInstance();

        chatService.login(qbUser, new QBEntityCallback() {

            @Override
            public void onSuccess(Object o, Bundle bundle) {
                Logger.info(TAG, "login success to chat====>");
            }

            @Override
            public void onError(QBResponseException error) {
                // errror
                Logger.info(TAG, "login error to the chat====>");
            }
        });
    }

    private void createSession() {
        Logger.info(TAG, "============creating session============");
        qbUser.setEmail(SharedPreferenceUtils.getInstance(context).getString(AppConstants.EMAIL));
        qbUser.setPassword(QBHelper.CHAT_PASSWORD);
        Logger.info(TAG, "============QBUSER============" + qbUser);
        QBHelper.getInstance(context).signin(qbUser, this);
        //   QBHelper.getInstance(context).monitorSession(qbUser);
    }

    @Override
    public void onErrorResponse(ApiURLS.ApiId apiId, String errorData, int responseCode) {

    }


    private void updateUsersList(String stringResponse) {
        Logger.info(TAG, "================updateUsersList()=============");

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
                    String chatid = null;
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
                            chatid = userData.getString("chatid");
                            usersListData.add(new UsersListData(userid, firstName + " " + lastName, prof_status, profilePic, user_type, true, chatid));
                        }
                        userFriendListAdapter = new UserFriendsListAdapter(usersListData, context, this);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                        recyclerView.setAdapter(userFriendListAdapter);
                    }
                } else {

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccessResult(int id, boolean status, QBUser qbUser) {
        if (id == QBHelper.LOGIN) {
            // make login to chat service
            Logger.info(TAG, "=========successfully setup======");
            if (!QBHelper.getInstance(context).checkQBUserLogin()) {
                Logger.info(TAG, "=========attempting chat login======");
                QBHelper.getInstance(context).connectToChat(qbUser, this);
            } else {
                Logger.info(TAG, "=======already logged in chat======");
            }

        } else if (id == QBHelper.CHAT_RESULT) {
            Logger.info(TAG, "===========Login to chat success======");
        }
    }

    @Override
    public void onFailureResult(int id, boolean status, QBUser qbUser) {
        if (id == QBHelper.CHAT_RESULT) {
            Logger.info(TAG, "===========Login to chat failed======");
        } else if (id == QBHelper.LOGIN) {
            // make login to chat service
            Logger.info(TAG, "=======signin success======");
        }
    }
}
