package com.midoconline.app.Util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import com.midoconline.app.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

    /**
     * show snackbar
     * @param view
     * @param msg
     */
    public static void ShowSnackBar(View view, String msg){
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
    }


    public  static void ShowDialog(String msg,Context context){
        AlertDialog.Builder diaBuilder = new AlertDialog.Builder(context);
        diaBuilder.setTitle("Error");
        diaBuilder.setMessage(msg);
        diaBuilder.show();
        diaBuilder.setCancelable(true);
    }


    public static String GetHashKey(Context context){
        // Add code to print out the key hash
        String hashKey = "";
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    "com.facebook.samples.hellofacebook",
                    PackageManager.GET_SIGNATURES);
            MessageDigest md;
            for (Signature signature : info.signatures) {
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                hashKey = Base64.encodeToString(md.digest(), Base64.DEFAULT);
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        return hashKey;
    }
}
