package com.yp.fastpayment.view;

import android.content.Context;
import android.widget.Toast;

public class CustomToast {
    public static Toast mToast;


    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, String message) {
        if (mToast == null) {
            mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(message);
        }
        mToast.show();
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, CharSequence message) {
        if (mToast == null) {
            mToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        } else {
            mToast.setText(message);
        }
        mToast.show();
    }

}
