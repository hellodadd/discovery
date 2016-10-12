package com.freeme.discovery.utils;

import android.content.Context;

/**
 * Created by zwb on 2016/10/13.
 */

public class CommonUtils {

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
