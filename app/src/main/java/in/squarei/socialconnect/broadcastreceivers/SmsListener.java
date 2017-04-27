package in.squarei.socialconnect.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

/**
 * Created by mohit kumar on 4/27/2017.
 */

public class SmsListener extends BroadcastReceiver {
    public OnSmsReceivedListener listener = null;
    public Context context;

    public SmsListener() {

    }

    public void setOnSmsReceivedListener(Context context) {
        this.listener = (OnSmsReceivedListener) context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            String msg_from;
            if (bundle != null) {
                //---retrieve the SMS message received---
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        String msgBody = msgs[i].getMessageBody();

                        String otpSMS = msgBody.substring(36, 36 + 4);

                        if (listener != null) {
                            listener.onSmsReceived(otpSMS);
                        } else {
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    public interface OnSmsReceivedListener {
        void onSmsReceived(String otp);
    }

}
