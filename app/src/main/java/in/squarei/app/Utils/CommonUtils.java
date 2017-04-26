package in.squarei.app.Utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import in.squarei.app.R;

/**
 * Created by mohit kumar on 4/26/2017.
 */

public class CommonUtils {
    Dialog customDialog;
    ProgressDialog pdialog;

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

    public void progressDialog(Context context, String title, String message, boolean cancelable, boolean isTitle) {
        if (pdialog == null) {
            pdialog = new ProgressDialog(context);
        }

        if (isTitle) {
            pdialog.setTitle(title);
        }

        pdialog.setMessage(message);

        if (!cancelable) {
            pdialog.setCancelable(false);
        }

        if (!pdialog.isShowing()) {
            pdialog.show();

        }

    }

    public void cancelProgressDialog() {
        pdialog.cancel();
    }
}
