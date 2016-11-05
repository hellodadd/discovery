package com.freeme.discovery.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.TextView;

/**
 * Created by zwb on 2016/10/13.
 */

public class CommonUtils {

    public static int RADIUS[] = {200, 270, 350, 430, 510};
    public static int RADIUS_VIDEO[] = {220, 320, 420, 520, 610};
    public static int RADIUS_SHOP[] = {280, 400, 520, 600, 610};

    public static int APP_LIMI = 60;
    public static int VIDEO_LIMI = 40;
    public static int SHOP_LIMI = 20;

    public static String VIDEO_TYPE = "video";
    public static String SHOP_TYPE = "shop";

    public static int DEFAULT_MENU = 0;

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


    //*/network
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }
    //*/
}
