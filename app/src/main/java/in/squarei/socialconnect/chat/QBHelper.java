package in.squarei.socialconnect.chat;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.quickblox.auth.session.QBSession;
import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.auth.session.QBSessionParameters;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.utils.DialogUtils;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import org.jivesoftware.smack.SmackException;

import java.util.Date;

import in.squarei.socialconnect.utils.Logger;

/**
 * Created by mohit kumar on 6/7/2017.
 */

public class QBHelper {
    private static final String TAG = "QBHelper";
    public static final int REGISTER = 1;
    public static final int LOGIN = 2;
    public static final int SESSION = 3;
    public static final int CHAT_RESULT = 4;
    private static QBHelper mInstance;
    private QBChatDialog chatDialog;
    private QBIncomingMessagesManager incomingMessagesManager;
    private QBChatService chatService;
    private final String PROPERTY_SAVE_TO_HISTORY = "save_to_history";
    public static final String CHAT_PASSWORD = "squareiapps";
    public static final String CHAT_NAME = "Unknown";
    private Context context;
    private ProgressDialog progressDialog;
    private static final String MESSAGE = "Please wait...";

    private QBHelper() {

    }

    public static QBHelper getInstance(Context context) {
        if (mInstance == null)
            mInstance = new QBHelper();

        mInstance.context = context;
        mInstance.progressDialog = new ProgressDialog(context);
        mInstance.progressDialog.setMessage(MESSAGE);
        return mInstance;
    }

    public String getQBToken() {
        return QBChatService.getInstance().getToken();
    }

    public boolean checkQBUserLogin() {
        return QBSessionManager.getInstance().getSessionParameters() != null;
    }

    public void logoutChat(final QBChatService cService) {
        try {
            cService.logout(new QBEntityCallback() {

                @Override
                public void onSuccess(Object o, Bundle bundle) {
                    cService.destroy();

                }

                @Override
                public void onError(QBResponseException errors) {

                }
            });
        } catch (Exception e) {

        }
    }

    public void registerQBUser(QBUser user, final QBHelperCallback qbHelperCallback) {
        Logger.info(TAG, "===registerQBUser===");
        //  progressDialog.show();
        QBUsers.signUp(user).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser user, Bundle args) {
                Logger.info(TAG, "===registerQBUser===onSuccess" + user);
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                qbHelperCallback.onSuccessResult(REGISTER, true, user);

            }

            @Override
            public void onError(QBResponseException error) {
                Logger.info(TAG, "===registerQBUser onError===" + error);
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                // error
                qbHelperCallback.onFailureResult(REGISTER, false, null);
            }
        });
    }

    public void loginAndSessionCreate(QBUser qbUser, final QBHelperCallback qbHelperCallback) {
        Logger.info(TAG, "===loginAndSessionCreate===");

        //  progressDialog.show();
        QBUsers.signIn(qbUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser user, Bundle args) {
                Logger.info(TAG, "===loginAndSessionCreate===onSuccess" + user + "args " + args);
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                // success
                qbHelperCallback.onSuccessResult(LOGIN, true, user);
            }

            @Override
            public void onError(QBResponseException error) {
                Logger.info(TAG, "===loginAndSessionCreate===onError" + error);
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                // error
                qbHelperCallback.onFailureResult(LOGIN, false, null);
            }
        });
    }

    public QBChatDialog startChat(int idMe, int idOther) {
        if (chatDialog == null) {
            chatDialog = DialogUtils.buildPrivateDialog(idOther);
        }
        if (chatService != null) {
            incomingMessagesManager = chatService.getIncomingMessagesManager();
            registerMessageListener();
        } else {

        }
        return chatDialog;
    }

    public void connectToChat(final QBUser qbUser, final QBHelperCallback qbHelperCallback) {
        Logger.info(TAG, "===connectToChat===");
        //    progressDialog.show();
        // initialize Chat service
        chatService = QBChatService.getInstance();
        chatService.login(qbUser, new QBEntityCallback() {
            @Override
            public void onSuccess(Object o, Bundle bundle) {
                Logger.info(TAG, "===connectToChat===onError" + o + "args :" + bundle);
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                qbHelperCallback.onSuccessResult(CHAT_RESULT, true, qbUser);
            }

            @Override
            public void onError(QBResponseException e) {
                Logger.info(TAG, "===connectToChat===onError" + e);
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                qbHelperCallback.onFailureResult(CHAT_RESULT, true, qbUser);
            }
        });
    }

    private void registerMessageListener() {
        incomingMessagesManager.addDialogMessageListener(
                new QBChatDialogMessageListener() {
                    @Override
                    public void processMessage(String dialogId, QBChatMessage message, Integer senderId) {
                    }

                    @Override
                    public void processError(String dialogId, QBChatException exception, QBChatMessage message, Integer senderId) {

                    }
                });
    }

    private void sendMessage(QBChatDialog d, final String message, final MessageListener messageListener) {

        QBRestChatService.createChatDialog(d).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog result, Bundle params) {
                QBChatMessage chatMessage = new QBChatMessage();
                chatMessage.setBody(message);
                chatMessage.setProperty(PROPERTY_SAVE_TO_HISTORY, "1");
                chatMessage.setDateSent(new Date().getTime() / 1000);

                try {
                    result.sendMessage(chatMessage);
                    messageListener.onSent();

                } catch (SmackException.NotConnectedException e) {
                    messageListener.onFailed();
                }
            }

            @Override
            public void onError(QBResponseException responseException) {
                messageListener.onError();
            }
        });
    }

    public void monitorSession(final QBUser qbUser) {
        QBSessionManager.getInstance().addListener(new QBSessionManager.QBSessionListener() {
            @Override
            public void onSessionCreated(QBSession session) {
                //calls when session was created firstly or after it has been expired
                Log.i("tag:", "onSessionCreated()");
                //    connectingChat();
            }

            @Override
            public void onSessionUpdated(QBSessionParameters sessionParameters) {
                //calls when user signed in or signed up
                //QBSessionParameters stores information about signed in user.
                Log.i("tag:", "onSessionUpdated()");
            }

            @Override
            public void onSessionDeleted() {
                //calls when user signed Out or session was deleted
                Log.i("tag:", "onSessionDeleted()");
            }

            @Override
            public void onSessionRestored(QBSession session) {
                //calls when session was restored from local storage
                Log.i("tag:", "onSessionRestored()");
                // connectingChat();
            }

            @Override
            public void onSessionExpired() {
                //calls when session is expired
                Log.i("tag:", "onSessionExpired()");
                QBHelper.getInstance(context).loginAndSessionCreate(qbUser, null);
            }
        });
    }
}
