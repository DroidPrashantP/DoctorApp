package com.midoconline.app.Util;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;

import com.midoconline.app.R;

/**
 * Created by Prashant on 6/10/15.
 */
public class Utils {

    static Dialog progressDialog;
    /**
     * call method to show progress dialog
     */
    public static void showProgress(Context context) {
        if (context != null) {
            progressDialog = new Dialog(context);
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.setContentView(R.layout.layout_progress);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.findViewById(R.id.emptyTextView).setVisibility(View.GONE);
            progressDialog.show();
        }
    }

    /**
     * call method to close progress dialog
     */
    public static void closeProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
