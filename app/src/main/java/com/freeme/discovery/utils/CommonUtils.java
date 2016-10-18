package com.freeme.discovery.utils;

import android.content.Context;
import android.widget.TextView;

/**
 * Created by zwb on 2016/10/13.
 */

public class CommonUtils {

    public static int RADIUS[] = {250, 350, 400, 450, 500};

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static double acos(double a, double b, double c){
        return Math.acos((a * a + b * b - c * c) / (b * (2.0D * a)));
    }

    public static void setTextShadow(TextView textView, int color, int shadowcolor)
    {
        int i = dip2px(textView.getContext(), 1.0F);
        int j = dip2px(textView.getContext(), 2.0F);
        textView.getPaint().setShadowLayer(i, 0.0F, j, shadowcolor);
        textView.setTextColor(color);
    }
}
