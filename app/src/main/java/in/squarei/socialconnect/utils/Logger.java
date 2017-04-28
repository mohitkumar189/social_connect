package in.squarei.socialconnect.utils;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by mohit kumar on 4/27/2017.
 */

public class Logger {

    public static final String LOG_TAG_FATAL = "Fatal";
    public static final String LOG_TAG_ERROR = "Error";
    public static final String LOG_TAG_WARN = "Warn";
    public static final String LOG_TAG_INFO = "Info";
    public static final String LOG_TAG_DEBUG = "Debug";
    public static final String LOG_TAG_TRACE = "Trace";
    private static final String LOG_FILTER_STRING = "SquareiApps";
    public static boolean loggingEnabled = true;
    private static boolean errorLoggingEnabled = true;

    public static void fatal(String message) {
        if (errorLoggingEnabled) {
            Log.e(LOG_FILTER_STRING + ":" + LOG_TAG_FATAL, message);
        }
    }

    public static void error(String TAG, String message) {
        if (loggingEnabled) {
            Log.e(LOG_FILTER_STRING + ":" + TAG, message);
        }
    }

    public static void error(Throwable e) {
        if (loggingEnabled) {
            Log.e(LOG_FILTER_STRING + ":" + LOG_TAG_ERROR, e.getMessage(), e);
        }
    }

    public static void error(String message, Exception e) {
        if (errorLoggingEnabled) {
            Log.e(LOG_FILTER_STRING + ":" + LOG_TAG_ERROR, message, e);
        }
    }

    public static void warn(String TAG, String message) {
        if (loggingEnabled) {
            Log.w(LOG_FILTER_STRING + ":" + TAG, message);
        }
    }

    public static void info(String TAG, String message) {
        if (loggingEnabled) {
            Log.i(LOG_FILTER_STRING + ":" + TAG, message);
        }
    }

    public static void debug(String TAG, String message) {
        if (loggingEnabled) {
            Log.d(LOG_FILTER_STRING + ":" + TAG, message);
        }
    }

    public static void trace(String tag, String message) {
        if (loggingEnabled) {
            Log.v(tag, message);
        }
    }

    public static void showCallStack() {
        if (loggingEnabled) {
            try {
                throw new Exception();
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                Logger.debug("Stack Trace: ", sw.toString());
            }
        }

    }


}