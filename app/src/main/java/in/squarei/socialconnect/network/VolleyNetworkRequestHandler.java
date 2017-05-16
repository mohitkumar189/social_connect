package in.squarei.socialconnect.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import in.squarei.socialconnect.R;
import in.squarei.socialconnect.socialConnectApplication.SocialConnectApplication;
import in.squarei.socialconnect.utils.Logger;

/**
 * Created by mohit kumar on 4/28/2017.
 */

public class VolleyNetworkRequestHandler {
    private static final String TAG = "VolleyNetworkRequestHandler";
    private static Context mctx;
    private static VolleyNetworkRequestHandler volleyNetworkRequestHandler = new VolleyNetworkRequestHandler();
    private static UrlResponseListener urlResponseListener;
    private static ProgressDialog pdialog;

    private VolleyNetworkRequestHandler() {

    }

    public static VolleyNetworkRequestHandler getInstance(Context context, UrlResponseListener urlResponseListener) {

        pdialog = new ProgressDialog(context);
        pdialog.setMessage("Please wait...");
        pdialog.setCancelable(true);
        //   if (VolleyNetworkRequestHandler.mctx == null) {
        VolleyNetworkRequestHandler.mctx = context;
        //    }
        if (volleyNetworkRequestHandler == null) {
            volleyNetworkRequestHandler = new VolleyNetworkRequestHandler();
        }
        //    if (VolleyNetworkRequestHandler.urlResponseListener == null) {
        VolleyNetworkRequestHandler.urlResponseListener = urlResponseListener;
        //  }
        return volleyNetworkRequestHandler;
    }

    public void getStringData(final String requestUrl, final ApiURLS.ApiId apiId, int requestMethod, final Map<String, String> postParams, final Map<String, String> headerParams) {
        Logger.info(TAG, "======================On Network Request=================url is======" + requestUrl + "====request Method===" + requestMethod);
        //  pdialog.setTitle(mctx.getResources().getString(R.string.data_downloading_message));
        pdialog.show();
        // CommonUtils.showprogressDialog(mctx, null, mctx.getResources().getString(R.string.data_downloading_message), false, false);
        final StringRequest stringRequest = new StringRequest(requestMethod, requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Logger.info(TAG, "======Network response====" + response);
                //  CommonUtils.cancelProgressDialog();
                pdialog.dismiss();
                try {
                    if (response != null) {
                        if (VolleyNetworkRequestHandler.urlResponseListener != null) {
                            Logger.info(TAG, "===Object not null===");
                            urlResponseListener.onResponseReceived(apiId, response);
                        }

                    } else {
                        Logger.info(TAG, "===Object null error===");
                        if (VolleyNetworkRequestHandler.urlResponseListener != null)
                            // listener.onPostRequestFailed(method, "Null data from server.");
                            Toast.makeText(mctx,
                                    mctx.getResources().getString(R.string.app_name),
                                    Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //  System.out.println("Data downloaded==== " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //CommonUtils.cancelProgressDialog();
                pdialog.dismiss();
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
                            if (VolleyNetworkRequestHandler.urlResponseListener != null) {
                                Logger.info(TAG, "===Object not null on error block===");
                                urlResponseListener.onErrorResponse(apiId, messageBody, responseCode);
                            } else {
                                Logger.info(TAG, "===Object null on error block===");
                            }
                        } else {
                            if (VolleyNetworkRequestHandler.urlResponseListener != null) {
                                Logger.info(TAG, "===Object not null on error block===");
                                urlResponseListener.onErrorResponse(apiId, messageBody, responseCode);
                            } else {
                                Logger.info(TAG, "===Object null on error block===");
                            }
                        }
                    }
                }
/*
                Log.d("abd", "Error: " + error
                        + ">>" + error.networkResponse.statusCode
                        + ">>" + error.networkResponse.data.toString()
                        + ">>" + error.getCause()
                        + ">>" + error.getMessage() +
                        ">>" + error.networkResponse.toString());*/
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                if (postParams != null) {
                    Logger.info(TAG, "==============Data for sending=========" + postParams.toString());

                    params.putAll(postParams);
                }
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                if (headerParams != null) {
                    Logger.info(TAG, "==============Data for Headers=========" + headerParams.toString());

                    //     params.put("Content-Type", "application/json; charset=UTF-8");
                    // params.put("client-id", "a34a80616d17600d32c78473bb1ef4c5");

                    for (Map.Entry<String, String> entry : headerParams.entrySet()) {
                        //    params.put(entry.getKey(), entry.getValue());
                        //     System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                    }
                    params.putAll(headerParams);
                }
                return params;
            }
        };
        // new VolleyNetworkRequestHandler.UrlVerifier().verify(requestUrl, null);
        SocialConnectApplication.getInstance().addToRequestQueue(stringRequest);

    }

    public void getJsonData(final String requestUrl, final ApiURLS.ApiId apiId, int requestMethod, final Map<String, String> postParams, final Map<String, String> headerParams) {
        Logger.info(TAG, "======================On Network Request=====================");
        pdialog.setTitle(mctx.getResources().getString(R.string.data_downloading_message));
        //   CommonUtils.showprogressDialog(mctx, null, mctx.getResources().getString(R.string.data_downloading_message), false, false);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(requestMethod, requestUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // CommonUtils.cancelProgressDialog();
                pdialog.dismiss();
                Logger.info(TAG, "=======Network Response=====" + response.toString());
                try {
                    if (response != null) {

                        if (VolleyNetworkRequestHandler.urlResponseListener != null)
                            urlResponseListener.onResponseReceived(apiId, response);
                    } else {
                        if (VolleyNetworkRequestHandler.urlResponseListener != null)
                            // listener.onPostRequestFailed(method, "Null data from server.");
                            Toast.makeText(mctx,
                                    mctx.getResources().getString(R.string.app_name),
                                    Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  CommonUtils.cancelProgressDialog();
                pdialog.dismiss();
                String messageBody = null; // message received in the error
                int responseCode = error.networkResponse.statusCode; // to get response code

                if (error.networkResponse.data != null) {
                    try {
                        messageBody = new String(error.networkResponse.data, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    urlResponseListener.onErrorResponse(apiId, messageBody, responseCode);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Logger.info(TAG, "==============Input  data=========" + postParams.toString());
                if (postParams != null) {
                    params.putAll(postParams);
                }
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                if (headerParams != null) {
                    params.putAll(headerParams);
                }
                return params;
            }
        };

        SocialConnectApplication.getInstance().addToRequestQueue(jsonObjReq);
    }

}
