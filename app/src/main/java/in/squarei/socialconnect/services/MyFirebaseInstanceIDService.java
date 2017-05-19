package in.squarei.socialconnect.services;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import in.squarei.socialconnect.interfaces.AppConstants;
import in.squarei.socialconnect.utils.CommonUtils;
import in.squarei.socialconnect.utils.Logger;
import in.squarei.socialconnect.utils.SharedPreferenceUtils;

import static in.squarei.socialconnect.network.ApiURLS.USER_UPDATE;

/**
 * Created by mohit kumar on 4/27/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseInstanceIDService";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Logger.info(TAG, "============Token==========" + refreshedToken);
        String clientiD = SharedPreferenceUtils.getInstance(getApplicationContext()
        ).getString(AppConstants.API_KEY);
        if (clientiD != null) {
            if (refreshedToken != null)
                CommonUtils.saveDeviceToken(USER_UPDATE, refreshedToken, clientiD, getApplicationContext());
        }
    }
}
