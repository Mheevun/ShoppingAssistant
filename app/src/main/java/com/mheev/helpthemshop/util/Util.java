package com.mheev.helpthemshop.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.widget.Toast;

/**
 * Created by mheev on 9/18/2016.
 */
@TargetApi(Build.VERSION_CODES.N)
public class Util {
    public static void showToast(CharSequence toastText, Context context) {
        Toast mToast = null;
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(context, toastText, Toast.LENGTH_SHORT);
        mToast.show();
    }



}
