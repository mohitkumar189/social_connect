package in.squarei.socialconnect.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mohit kumar on 5/22/2017.
 */

public class JsonParserClass {

    private JsonParserClass jsonParserClass;
    private JSONObject commandResults = null;
    private String message = null;
    private JSONArray dataArray = null;
    private JSONArray inputArray = null;
    private int success = -1;
    private boolean error = false;
    private String response = null;
    private JSONObject jsonResponse;
    private static final String TAG = "JsonParserClass";

    private static final String KEY_ERROR = "error";
    private static final String KEY_COMMAND_RESULTS = "commandResult";
    private static final String KEY_DATA = "data";
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_INPUTS = "input";
    private static final String KEY_MESSAGE = "message";

    private JsonParserClass(String response) {
        this.response = response;
        try {
            jsonResponse = new JSONObject(response);
            processJson();
        } catch (JSONException e) {
            Logger.error(TAG, "==========Exception in json response=============" + e.toString());
        }
    }

    private void processJson() {
        try {
            commandResults = new JSONObject(jsonResponse.getString(KEY_COMMAND_RESULTS));
            setError(jsonResponse.getBoolean(KEY_ERROR));
            setSuccess(commandResults.getInt(KEY_SUCCESS));
            setDataArray(new JSONArray(commandResults.getString(KEY_DATA)));
            setInputArray(new JSONArray(jsonResponse.getString(KEY_INPUTS)));
            setMessage(commandResults.getString(KEY_MESSAGE));

    /*   -----------------------------------------------------------
            error = jsonResponse.getBoolean(KEY_ERROR);
            success = commandResults.getInt(KEY_SUCCESS);
            message = commandResults.getString(KEY_MESSAGE);
            dataArray = new JSONArray(commandResults.getString(KEY_DATA));
            inputArray = new JSONArray(jsonResponse.getString(KEY_INPUTS));
         -------------------------------------------------------------
     */
            Logger.info(TAG, "error====>" + error);
            Logger.info(TAG, "commandResults====>" + commandResults);
            Logger.info(TAG, "success====>" + success);
            Logger.info(TAG, "dataArray====>" + dataArray);
            Logger.info(TAG, "inputArray====>" + inputArray);

        } catch (JSONException e) {
            Logger.error(TAG, "==========Exception while extracting json=============" + e.toString());
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JSONArray getDataArray() {
        return dataArray;
    }

    public void setDataArray(JSONArray dataArray) {
        this.dataArray = dataArray;
    }

    public JSONArray getInputArray() {
        return inputArray;
    }

    public void setInputArray(JSONArray inputArray) {
        this.inputArray = inputArray;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public static JsonParserClass getInstance(String response) {
        return new JsonParserClass(response);
    }

    public String getMessgae() {
        return message;
    }
}
