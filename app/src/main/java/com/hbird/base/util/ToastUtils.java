package com.hbird.base.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Liul on 2018/6/27.
 */

public class ToastUtils {
    private static Toast toast = null;

    public ToastUtils() {
    }

    public static void ShowMessage(Context context, String msg) {
        if(toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), msg, 0);
        } else {
            toast.setText(msg);
        }

        toast.show();
    }

    public static void ShowToast_long(Context context, String msg) {
        if(toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), msg, 1);
        } else {
            toast.setText(msg);
        }

        toast.show();
    }
}
