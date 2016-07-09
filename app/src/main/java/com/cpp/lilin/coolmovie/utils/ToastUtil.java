package com.cpp.lilin.coolmovie.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by lilin on 2016/7/9.
 */
public class ToastUtil {

    private ToastUtil() {
    }

    private static Toast mToast = null;

    public static void show(Context context, int resId) {
        if (mToast == null) {
            mToast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(resId);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }
}
