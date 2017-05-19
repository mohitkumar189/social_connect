package in.squarei.socialconnect.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import in.squarei.socialconnect.R;
import in.squarei.socialconnect.interfaces.AppConstants;
import in.squarei.socialconnect.socialConnectApplication.SocialConnectApplication;

import static in.squarei.socialconnect.network.ApiURLS.REQUEST_PUT;

/**
 * Created by mohit kumar on 4/26/2017.
 */

public class CommonUtils implements AppConstants {
    private static final String TAG = "CommonUtils";
    private static ProgressDialog pdialog;
    private Dialog customDialog;

    public static void showprogressDialog(Context context, String title, String message, boolean cancelable, boolean isTitle) {
 /*       if(((Activity) context).isFinishing())
        {
            return;
        }*/
        Logger.info(TAG, "==============initContext=======" + context);
        //   if (pdialog == null) {
        pdialog = new ProgressDialog(context);
        //  }

        if (isTitle) {
            pdialog.setTitle(title);
        }

        pdialog.setMessage(message);

        if (!cancelable) {
            pdialog.setCancelable(false);
        }
        if (pdialog != null && !pdialog.isShowing()) {
            Log.e("show_dialog", "showing");
            pdialog.show();
        }

    }

    public static void cancelProgressDialog() {
        if (pdialog != null)
            pdialog.dismiss();
    }

    public static void saveDeviceToken(String url, final String deviceToken, final String clientId, final Context context) {
        final StringRequest stringRequest = new StringRequest(REQUEST_PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Logger.info(TAG, "======Network response====" + response);
                try {
                    if (response != null) {
                            Logger.info(TAG, "===Object not null===");
                            JSONObject jsonObject = new JSONObject(response);
                            Boolean error = jsonObject.getBoolean("error");
                            if (!error) {
                                JSONObject commandResult = new JSONObject(jsonObject.getString("commandResult"));
                                int success = commandResult.getInt("success");
                                String message = commandResult.getString("message");
                                if (success == 1) {
                                    SharedPreferenceUtils.getInstance(context).putBoolean(FIREBASE_STATUS,true);
                                }

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String messageBody = null; // message received in the error
                int responseCode = 0;
                if (error != null) {
                    if (error.networkResponse != null) {
                        responseCode = error.networkResponse.statusCode; // to get response code
                        if (error.networkResponse.data != null) {
                            try {
                                messageBody = new String(error.networkResponse.data, "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("deviceID", deviceToken);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                if (clientId != null)
                    params.put("client-id", clientId);

                return params;
            }
        };
        // new VolleyNetworkRequestHandler.UrlVerifier().verify(requestUrl, null);
        SocialConnectApplication.getInstance().addToRequestQueue(stringRequest);

    }

    protected Dialog creatingDialog(Context context, boolean isCancelableBack, boolean isCancelableoutside, View view, int height, int width) {
        customDialog = new Dialog(context, R.style.dialogTheme);
        //  dialog.setCancelable(isCancelableBack);
        if (view.getParent() != null) {
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            viewGroup.removeView(view);
        }
        customDialog.setCanceledOnTouchOutside(isCancelableoutside);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


      /*  WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = Helper.toPixels(context, 200);
        layoutParams.height = Helper.toPixels(context, 200);
        dialog.getWindow().setAttributes(layoutParams);*/
        customDialog.setContentView(view);
        WindowManager.LayoutParams wmlp = customDialog.getWindow().getAttributes();
        customDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        wmlp.gravity = Gravity.CENTER;
      /*  wmlp.x = 50;   //x position
        wmlp.y = -100;*/
        customDialog.show();

        customDialog.getWindow().setLayout(Helper.getPixels(context, width), Helper.getPixels(context, height));
        customDialog.getWindow().setAttributes(wmlp);
        return customDialog;

    }

    protected void cancelCustomDialog() {
        if (customDialog != null) {
            customDialog.cancel();
        }

    }
}
