package in.squarei.socialconnect.services;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import in.squarei.socialconnect.utils.Logger;

/**
 * Created by mohit kumar on 4/27/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService{
    private static final String TAG="MyFirebaseInstanceIDService";
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Logger.info(TAG,"============Token=========="+refreshedToken);
    }
}
