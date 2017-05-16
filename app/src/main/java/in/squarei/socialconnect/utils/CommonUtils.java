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

import in.squarei.socialconnect.R;

/**
 * Created by mohit kumar on 4/26/2017.
 */

public class CommonUtils {
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
