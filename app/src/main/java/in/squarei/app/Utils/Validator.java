package in.squarei.app.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import in.squarei.app.R;

/**
 * Created by mohit kumar on 4/26/2017.
 */

public class Validator {
    private static Validator validator;

    private Validator() {

    }

    public static Validator getInstance() {
        if (validator == null) {
            validator = new Validator();
        }
        return validator;
    }

    public static String isValidEmail(Context context, CharSequence target) {
        if (target == null) {
            return context.getString(R.string.error_empty_email_id);

        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()) {
            return context.getString(R.string.error_invalid_email_id);
        }
        return "";
    }

    public String validateNumber(Context context, String s) {

        if (s.length() != 10) {
            return context.getString(R.string.error_phone_no_invalid);
        }
        if (s.charAt(0) == '0') {
            return context.getString(R.string.error_additional_zero);
        }
        return "";
    }

    public final static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
