package in.squarei.socialconnect.chat;

/**
 * Created by mohit kumar on 6/7/2017.
 */

public interface MessageListener {
    void onSent();
    void onFailed();
    void onError();
}
