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
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.chat.utils.DialogUtils;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import org.jivesoftware.smack.SmackException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.squarei.socialconnect.R;
import in.squarei.socialconnect.adapters.UserChatADapter;
import in.squarei.socialconnect.chat.MessageListener;
import in.squarei.socialconnect.chat.QBHelper;
import in.squarei.socialconnect.chat.QBHelperCallback;
import in.squarei.socialconnect.interfaces.AppConstants;
import in.squarei.socialconnect.modals.Users;
import in.squarei.socialconnect.utils.Logger;
import in.squarei.socialconnect.utils.SharedPreferenceUtils;

public class ChatDetailActivity extends SocialConnectBaseActivity implements QBHelperCallback, MessageListener {

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
    private String message = null;
    private QBIncomingMessagesManager incomingMessagesManager;
    private final String PROPERTY_SAVE_TO_HISTORY = "save_to_history";


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


   /*     if (!QBHelper.getInstance(context).checkQBUserLogin()) {
            Logger.info(TAG, "===attempting chat login again==");
            QBHelper.getInstance(context).connectToChat(qbUser, this);
        } else {
            Logger.info(TAG, "===attempting chat dialog creation==");
            if (qbChatDialog == null)
                createChatDialog(chatId);
        }*/
/*
        if (!QBHelper.getInstance(context).checkQBUserLogin()) {
            if (qbUser != null)
                QBHelper.getInstance(context).signin(qbUser, this);
        } else {
            QBHelper.getInstance(context).connectToChat(this.qbUser, this);
        }*/
        init();
    }

    private void createDialog() {
        QBHelper.getInstance(context).registerMessageListener(this);
        ArrayList<Integer> occupantIdsList = new ArrayList<Integer>();
        occupantIdsList.add(chatId);
        occupantIdsList.add(SharedPreferenceUtils.getInstance(context).getInteger(AppConstants.CHAT_ID));
/*
        QBChatDialog dialog = new QBChatDialog();
        dialog.setType(QBDialogType.PRIVATE);
        dialog.setOccupantsIds(occupantIdsList);*/

        //or just use DialogUtils
        //for creating PRIVATE dialog
        //QBChatDialog dialog = DialogUtils.buildPrivateDialog(recipientId);

        //for creating GROUP dialog

        QBChatDialog dialog = DialogUtils.buildDialog("Chat with Garry and John", QBDialogType.PRIVATE, occupantIdsList);

        QBRestChatService.createChatDialog(dialog).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog result, Bundle params) {
                Logger.info(TAG, "===========chat dialog created successfully======");
            }

            @Override
            public void onError(QBResponseException responseException) {
                Logger.info(TAG, "===========chat dialog create error======");
            }
        });
    }

    private void createChatDialog(int chatId) {
        qbChatDialog = QBHelper.getInstance(context).startChat(SharedPreferenceUtils.getInstance(context).getInteger(AppConstants.CHAT_ID), chatId);
        if (qbChatDialog != null) {
            Logger.info(TAG, "===========chat dialog created successfully======");
            QBHelper.getInstance(context).registerMessageListener(this);
        }
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
        loginUser(qbUser);

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
                createDialog();
            }

            @Override
            public void onError(QBResponseException error) {
                // errror
                Logger.info(TAG, "login error to the chat====>");
            }
        });
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
        switch (v.getId()) {
            case R.id.btnSend:
                message = editMessage.getText().toString();
                if (message.length() != 0)
                    chatData.add(new Users(0, message, 0));
                chatADapter.notifyDataSetChanged();
                editMessage.setText("");
                if (qbChatDialog != null) {
                    Logger.info(TAG, "=========attempting  message======" + qbChatDialog);
                    //  QBHelper.getInstance(context).sendMessage(qbChatDialog, message, this);
                    sendMessage(qbChatDialog, message);
                } else {
                    createChatDialog(chatId);
                    Logger.info(TAG, "=========dialog is not available to send the message======");
                }

                break;
        }
    }

    public void sendMessage(QBChatDialog d, final String message) {

/*        QBChatMessage chatMessage = new QBChatMessage();
        chatMessage.setBody(message);
        chatMessage.setProperty(PROPERTY_SAVE_TO_HISTORY, "1");
        chatMessage.setDateSent(new Date().getTime() / 1000);
        Logger.info(TAG, "message to be sent===" + chatMessage);

        try {
            d.sendMessage(chatMessage);
            Logger.info(TAG, "===========message Sent======");
        } catch (SmackException.NotConnectedException e) {
            Logger.info(TAG, "===========message not sent======" + e);
            //  messageListener.onFailed();
        }*/
        QBRestChatService.createChatDialog(d).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog result, Bundle params) {
                QBChatMessage chatMessage = new QBChatMessage();
                chatMessage.setBody(message);
                chatMessage.setProperty(PROPERTY_SAVE_TO_HISTORY, "1");
                chatMessage.setDateSent(new Date().getTime() / 1000);
                Logger.info(TAG, "message to be sent===" + chatMessage);

                try {
                    result.sendMessage(chatMessage);
                    Logger.info(TAG, "===========message Sent======");
                } catch (SmackException.NotConnectedException e) {
                    Logger.info(TAG, "===========message not sent======" + e);
                    //  messageListener.onFailed();
                }
            }

            @Override
            public void onError(QBResponseException responseException) {
                Logger.info(TAG, "===========QBResponseException======" + responseException);
            }
        });
    }


    @Override
    public void onSuccessResult(int id, boolean status, QBUser qbUser) {
        if (id == QBHelper.LOGIN) {
            Logger.info(TAG, "===========Login success======");
            if (status) {

              /*  if (QBHelper.getInstance(context).checkQBUserLogin()) {
                    if (this.qbUser != null)
                        QBHelper.getInstance(context).connectToChat(this.qbUser, this);
                }*/
            }
        } else if (id == QBHelper.CHAT_RESULT) {
            Logger.info(TAG, "===========chat connect success======");
            createChatDialog(chatId);

        }
    }

    @Override
    public void onFailureResult(int id, boolean status, QBUser qbUser) {
        if (id == QBHelper.CHAT_RESULT) {
            Logger.info(TAG, "===========chat connect failed======");
        }
    }

    @Override
    public void onSent() {
        Logger.info(TAG, "===========message onSent======");
        // 1 for successfully sent, 0 for not sent , 2 for received message

    }

    @Override
    public void onReceived(String message) {
        Logger.info(TAG, "===========message onReceived======");
        if (message != null && message.length() != 0 && message != "")
            chatData.add(new Users(0, message, 0));
    }

    @Override
    public void onError() {
        Logger.info(TAG, "===========message onError======");
    }
}
