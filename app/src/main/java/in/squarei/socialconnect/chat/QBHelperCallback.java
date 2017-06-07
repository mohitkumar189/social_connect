package in.squarei.socialconnect.chat;

import com.quickblox.users.model.QBUser;

/**
 * Created by mohit kumar on 6/7/2017.
 */

public interface QBHelperCallback {

    void onSuccessResult(int id, boolean status, QBUser qbUser);

    void onFailureResult(int id, boolean status, QBUser qbUser);
}
