package in.squarei.socialconnect.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.Random;

import in.squarei.socialconnect.interfaces.AppConstants;
import in.squarei.socialconnect.socialConnectApplication.SocialConnectApplication;

/**
 * Created by mohit kumar on 4/26/2017.
 */

public class Helper {

    public static byte[] getImageBytes(Bitmap bmp) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return imageBytes;
    }


    public static long generateRandomNoTimeMillis() {

        int max = 9999;
        int min = 1000;
        Date date = new Date();


        Random random = new Random();


        Log.e("random no", "" + date.getTime() + random.nextInt((max - min) + 1) + min);

        return date.getTime() + random.nextInt((max - min) + 1) + min;

    }

    public static long generateRandomPassword() {

        int max = 99999;
        int min = 10000;
        Date date = new Date();


        Random random = new Random();


        Log.e("random no", "" + date.getTime() + random.nextInt((max - min) + 1) + min);

        return date.getTime() + random.nextInt((max - min) + 1) + min;

    }

    public static float getDeviceDensity() {
        Context context = SocialConnectApplication.getInstance().getApplicationContext();
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return (metrics.densityDpi / 160f);

    }

    public static int convertDpToPixel(float dp) {
        Context context = SocialConnectApplication.getInstance().getApplicationContext();
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Float px = dp * (metrics.densityDpi / 160f);
        return px.intValue();
    }

    public static float convertPixelsToDp(float px) {
        Context context = SocialConnectApplication.getInstance().getApplicationContext();
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    public static int getPixels(Context context, int dp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }

    public static boolean isLogin(Context context) {

        return SharedPreferenceUtils.getInstance(context).getSharedPreferences().getBoolean(AppConstants.IS_LOGIN, false);
    }

    public static Integer getInteger(String val) {
        Integer integer = null;
        try {
            integer = Integer.parseInt(val);
        } catch (Exception e) {

        }
        return integer;
    }

    public static Boolean getBoolean(String val) {
        try {
            return Boolean.parseBoolean(val);
        } catch (Exception e) {

        }
        return null;
    }

    public static Double getDouble(String val) {
        try {
            return Double.parseDouble(val);
        } catch (Exception e) {

        }
        return null;
    }

    public static Long getLong(String val) {
        try {
            return Long.parseLong(val);
        } catch (Exception e) {

        }
        return null;
    }

    public static boolean isValidString(String val) {
        return (val != null && !"".equals(val) && !"null".equals(val.toLowerCase()));
    }

    public static boolean isStringSame(String str1, String str2) {
        if (str1 == null && str2 == null) return true;
        if (str1 != null && str1.equals(str2)) return true;
        return str2 != null && str2.equals(str1);
    }

    public static boolean isExternalURL(String str) {
        if(str == null) return false;
        return str.indexOf( "http://" ) == 0 || str.indexOf( "https://" ) == 0 || str.indexOf( "www." ) == 0;
    }

    public static void openURL(Activity activity, String url) {
        Intent webIntent = new Intent( Intent.ACTION_VIEW, Uri.parse( url ) );
        activity.startActivity( webIntent );
    }
    

}
