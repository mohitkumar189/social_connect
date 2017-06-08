package in.squarei.socialconnect.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.List;

import in.squarei.socialconnect.R;
import in.squarei.socialconnect.adapters.UserChatADapter;
import in.squarei.socialconnect.chat.QBHelper;
import in.squarei.socialconnect.chat.QBHelperCallback;
import in.squarei.socialconnect.interfaces.AppConstants;
import in.squarei.socialconnect.modals.Users;
import in.squarei.socialconnect.utils.Logger;
import in.squarei.socialconnect.utils.SharedPreferenceUtils;

public class ChatDetailActivity extends SocialConnectBaseActivity implements QBHelperCallback {

    private final String TAG = "ChatDetailActivity";
    private int chatId;
    private String userName;
    private RecyclerView recyclerView;
    private TextView tvName;
    private EditText editMessage;
    private Button btnSend;
    private UserChatADapter chatADapter;
    private List<Users> chatData;
    private QBChatService chatService = QBChatService.getInstance();
    private QBUser qbUser = new QBUser();
    private QBChatDialog qbChatDialog = null;
    ;
    private QBIncomingMessagesManager incomingMessagesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);
        Intent intent = getIntent();
        String id = intent.getStringExtra("chatId");
        if (id != null && !id.isEmpty()) {
            chatId = Integer.parseInt(id);
            if (SharedPreferenceUtils.getInstance(context).getBoolean(AppConstants.CHAT_ID_STATUS)) {
                qbUser.setId(SharedPreferenceUtils.getInstance(context).getInteger(AppConstants.CHAT_ID));
                qbUser.setPassword(QBHelper.CHAT_PASSWORD);
                qbUser.setEmail(SharedPreferenceUtils.getInstance(context).getString(AppConstants.EMAIL));
            }

        }

        userName = getIntent().getStringExtra("userName");
        Logger.info(TAG, "===chat id received:==" + chatId);

        if (!QBHelper.getInstance(context).checkQBUserLogin()) {
            if (qbUser != null)
                QBHelper.getInstance(context).loginAndSessionCreate(qbUser, this);
        } else {
            QBHelper.getInstance(context).connectToChat(this.qbUser, this);
        }
        init();
    }

    @Override
    protected void initViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        tvName = (TextView) findViewById(R.id.tvName);
        editMessage = (EditText) findViewById(R.id.editMessage);
        btnSend = (Button) findViewById(R.id.btnSend);
    }

    private void init() {
        chatData = new ArrayList<>();
        chatADapter = new UserChatADapter(this, chatData);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatADapter);

    }

    @Override
    protected void initContext() {
        context = this;
        currentActivity = this;
    }

    @Override
    protected void initListners() {
        btnSend.setOnClickListener(this);
    }

    @Override
    protected boolean isActionBar() {
        return false;
    }

    @Override
    protected boolean isHomeButton() {
        return false;
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
        chatData.add(new Users(0, editMessage.getText().toString(), 0));
        chatADapter.notifyDataSetChanged();
    }


    @Override
    public void onSuccessResult(int id, boolean status, QBUser qbUser) {
        if (id == QBHelper.LOGIN) {
            Logger.info(TAG, "===========Login success======");
            if (status) {
                if (QBHelper.getInstance(context).checkQBUserLogin()) {
                    if (this.qbUser != null)
                        QBHelper.getInstance(context).connectToChat(this.qbUser, this);
                }
            }
        } else if (id == QBHelper.CHAT_RESULT) {
            Logger.info(TAG, "===========chat connect success======");
            qbChatDialog = QBHelper.getInstance(context).startChat(SharedPreferenceUtils.getInstance(context).getInteger(AppConstants.CHAT_ID), chatId);
        }
    }

    @Override
    public void onFailureResult(int id, boolean status, QBUser qbUser) {

    }
}
