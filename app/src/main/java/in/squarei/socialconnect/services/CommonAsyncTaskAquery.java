package in.squarei.socialconnect.services;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONObject;

import java.util.HashMap;

import in.squarei.socialconnect.network.UrlResponseListener;

/**
 * Created by Prince on 11/7/2016.
 */
public class CommonAsyncTaskAquery {
    int method;
    private ProgressDialog pd;
    private AQuery aq;
    private Context context;
    private UrlResponseListener listener;

    public CommonAsyncTaskAquery(int method, Context context, UrlResponseListener response) {
        aq = new AQuery(context);
        this.context = context;
        listener = response;
        this.method = method;
        pd = new ProgressDialog(context);
        pd.setMessage("Please wait ... ");
        pd.setCancelable(false);

    }

    public static AQuery getAqueryObj(Context context) {
        return new AQuery(context);
    }


    public void getquery(String url, HashMap<String, Object> hm) {
        // String url = context.getResources().getString(R.string.base_url) + addurl;

        Log.e("request", ": " + url + hm.toString());
        aq.progress(pd).ajax(url, hm, JSONObject.class, new AjaxCallback<JSONObject>() {

                    @Override
                    public void callback(String url, JSONObject json, AjaxStatus status) {
                        super.callback(url, json, status);
                        Log.e("response", "** " + json);
                        if (json != null) {

                            //successful ajax call, show status code and json content
                            Toast.makeText(aq.getContext(), status.getCode() + ":" + json.toString(), Toast.LENGTH_LONG).show();

                        } else {

                            //ajax error, show error code
                            Toast.makeText(aq.getContext(), "Error:" + status.getCode(), Toast.LENGTH_LONG).show();
                        }
               /*         if (obj != null) {
                            try {
                                if (listener != null) {
                                    listener.onResponseReceived(null, obj);

                                } else {
                                    if (listener != null) {
                                        listener.onResponseReceived(null, "Null data from server.");
                                    }
                                 *//*       // listener.onPostRequestFailed(method, "Null data from server.");
                                        Toast.makeText(context,
                                                context.getResources().getString(R.string.problem_server),
                                                Toast.LENGTH_LONG).show();*//*
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {

                        }*/
                        //  Toast.makeText(context, context.getString(R.string.message_problem), Toast.LENGTH_SHORT).show();
                        //   Alerts.okAlert(context, context.getString(R.string.something_went_wrong));
                    }
                }

        );
    }


}
