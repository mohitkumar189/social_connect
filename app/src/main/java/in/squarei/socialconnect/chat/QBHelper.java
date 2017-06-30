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

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

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
        Logger.info(TAG, "===QBHelper() constructor====");
        chatService = QBChatService.getInstance();
        chatService.setUseStreamManagement(true);
        if (chatService != null) {
            incomingMessagesManager = chatService.getIncomingMessagesManager();
        }

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

    public static QBUser getCurrentUser() {
        return QBChatService.getInstance().getUser();
    }

    public void addConnectionListener(ConnectionListener listener) {
        chatService.addConnectionListener(listener);
    }

    public void removeConnectionListener(ConnectionListener listener) {
        chatService.removeConnectionListener(listener);
    }

    public void leaveChatDialog(QBChatDialog chatDialog) throws XMPPException, SmackException.NotConnectedException {
        chatDialog.leave();
    }

    public void destroy() {
        chatService.destroy();
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

    public void signin(QBUser qbUser, final QBHelperCallback qbHelperCallback) {
        Logger.info(TAG, "===signin and session===");
        monitorSession(qbUser);
        //  progressDialog.show();
        QBUsers.signIn(qbUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser user, Bundle args) {
                Logger.info(TAG, "===session created successfully===onSuccess" + user + "args " + args);
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                // success
                qbHelperCallback.onSuccessResult(LOGIN, true, user);
            }

            @Override
            public void onError(QBResponseException error) {
                Logger.info(TAG, "===session failed===onError" + error);
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                // error
                qbHelperCallback.onFailureResult(LOGIN, false, null);
            }
        });
    }

    public QBChatDialog startChat(int idMe, int idOther) {

/*
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
                    messageListener.onSent();

                    Logger.info(TAG, "===========message Sent======");
                } catch (SmackException.NotConnectedException e) {
                    Logger.info(TAG, "===========message not sent======" + e);
                    //  messageListener.onFailed();
                }
            }

            @Override
            public void onError(QBResponseException responseException) {
                messageListener.onError();
                Logger.info(TAG, "===========QBResponseException======" + responseException.getMessage());
            }
        });*/


        if (chatDialog == null) {
            chatDialog = DialogUtils.buildPrivateDialog(idOther);
            Logger.info(TAG, "===startChat dialog===" + idOther);

        }
        return chatDialog;
    }

    public void connectToChat(final QBUser qbUser, final QBHelperCallback qbHelperCallback) {
        Logger.info(TAG, "===connectToChat method()===" + qbUser);
        //    progressDialog.show();
        // initialize Chat service
        chatService = QBChatService.getInstance();
        if (!checkQBUserLogin()) {
            chatService.login(qbUser, new QBEntityCallback() {
                @Override
                public void onSuccess(Object o, Bundle bundle) {
                    Logger.info(TAG, "===connectToChat method()===onError" + o + "args :" + bundle);
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    qbHelperCallback.onSuccessResult(CHAT_RESULT, true, qbUser);
                }

                @Override
                public void onError(QBResponseException e) {
                    Logger.info(TAG, "===connectToChat method()===onError" + e);
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    qbHelperCallback.onFailureResult(CHAT_RESULT, true, qbUser);
                }
            });
        }
    }

    public void registerMessageListener(final MessageListener messageListener) {
        Logger.info(TAG, "=======Registering message listener====");
        if (incomingMessagesManager != null) {
            incomingMessagesManager.addDialogMessageListener(
                    new QBChatDialogMessageListener() {
                        @Override
                        public void processMessage(String dialogId, QBChatMessage message, Integer senderId) {
                            messageListener.onReceived(message.getBody());
                        }

                        @Override
                        public void processError(String dialogId, QBChatException exception, QBChatMessage message, Integer senderId) {

                        }
                    });
        }
        Logger.info(TAG, "=======Registered message listener====");
    }

    public void sendMessage(QBChatDialog d, final String message, final MessageListener messageListener) {

/*        QBChatMessage chatMessage = new QBChatMessage();
        chatMessage.setBody(message);
        chatMessage.setProperty(PROPERTY_SAVE_TO_HISTORY, "1");
        chatMessage.setDateSent(new Date().getTime() / 1000);
        Logger.info(TAG, "message to be sent===" + chatMessage);

        try {
            d.sendMessage(chatMessage);
            messageListener.onSent();
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
                    messageListener.onSent();

                    Logger.info(TAG, "===========message Sent======");
                } catch (SmackException.NotConnectedException e) {
                    Logger.info(TAG, "===========message not sent======" + e);
                    //  messageListener.onFailed();
                }
            }

            @Override
            public void onError(QBResponseException responseException) {
                messageListener.onError();
                Logger.info(TAG, "===========QBResponseException======" + responseException);
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
                signin(qbUser, null);
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
                signin(qbUser, null);
            }
        });
    }
}
