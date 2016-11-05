package com.freeme.discovery.ui.adapter;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.freeme.discovery.R;
import com.freeme.discovery.common.AsyncImageCache;
import com.freeme.discovery.models.AppInfo;
import com.freeme.discovery.models.ShopInfo;
import com.freeme.discovery.utils.CommonUtils;
import com.freeme.discovery.view.ContentTempleteView;
import com.freeme.discovery.view.IndicatorTextView;

import java.text.DecimalFormat;
import java.util.Random;

/**
 * Created by server on 16-11-4.
 */

public class AppAdapter extends BaseAdapter<AppInfo>{

    private int random;
    private int radian;
    private int radius;
    private int distance;

    private final static int RADIAN_0 = 5;// 0 cicir show 5
    private final static int RADIAN_1 = 8;//
    private final static int RADIAN_2 = 13;//
    private final static int RADIAN_3 = 16;//
    private final static int RADIAN_4 = 18;//

    private Handler mHandler = new Handler();

    public AppAdapter(Context context){
        super(context);
    }

    public int getRadian(){
        random = (int) (Math.random()* CommonUtils.APP_LIMI);
        radian = (int) mapList.get(random);
        while (radian == 1000){
            random = (int) (Math.random()* CommonUtils.APP_LIMI);
            radian = (int) mapList.get(random);
        }
        mapList.set(random, 1000);

        return radian;
    };

    public int getRadius(){
        if(random < RADIAN_0){
            radius = CommonUtils.dip2px(mContext,CommonUtils.RADIUS[0]);
            distance = (int) (300 + Math.random()* 500);
        }else if(random > RADIAN_0 - 1 && random < RADIAN_1 + RADIAN_0){
            radius = CommonUtils.dip2px(mContext,CommonUtils.RADIUS[1]);
            distance = (int) (900 + Math.random()* 1100);
        }else if(random > RADIAN_1 + RADIAN_0 - 1 && random < RADIAN_2 + RADIAN_1 + RADIAN_0){
            radius = CommonUtils.dip2px(mContext,CommonUtils.RADIUS[2]);
            distance = (int) (2100 + Math.random()* 700);
        }else if(random > RADIAN_2 + RADIAN_1 + RADIAN_0 - 1 && random < RADIAN_3 + RADIAN_2 + RADIAN_1 + RADIAN_0){
            radius = CommonUtils.dip2px(mContext,CommonUtils.RADIUS[3]);
            distance = (int) (2900 + Math.random()*  1500);
        }else if(random > RADIAN_3 + RADIAN_2 + RADIAN_1 + RADIAN_0 - 1 && random < RADIAN_4 + RADIAN_3 + RADIAN_2 + RADIAN_1 + RADIAN_0){
            radius = CommonUtils.dip2px(mContext,CommonUtils.RADIUS[4]);
            distance = (int) (4500 + Math.random()* 2000);
        }
        return radius;
    }

    public int getDistance(){
        return distance;
    }


    public ContentTempleteView CreateInfoView(AppInfo info){
        final ContentTempleteView infoView =
                (ContentTempleteView) LayoutInflater.from(mContext).inflate(R.layout.iconlayout,null);
        ImageView icon = (ImageView) infoView.findViewById(R.id.hot_app_icon);
        mAsyncImageCache.displayImage(
                icon,48,48,
                new AsyncImageCache.NetworkImageGenerator(info.getIconurl(),
                        info.getIconurl()), 10);
        TextView title = (TextView)infoView.findViewById(R.id.hot_app_text);
        title.setText(info.getSname());
        IndicatorTextView statusTextView = (IndicatorTextView)infoView.findViewById(R.id.status_textview);
        statusTextView.setVisibility(View.GONE);
        ImageView iconbg = (ImageView)infoView.findViewById(R.id.icon_bg);
        ImageView imageView = (ImageView)infoView.findViewById(R.id.animg);
        imageView.setVisibility(View.GONE);

        infoView.setCircleAniImage(imageView);
        infoView.setTag("app");
        infoView.setMainType(info.getMainType());
        infoView.setAttentionType(new Random().nextInt(15));
        infoView.setUrl("http://m.zhuoyi.com/detail.php?apk_id="+info.getDocid());
        infoView.setIconViewSex(new Random().nextInt(2));
        infoView.setIndicatorTextView(statusTextView);
        infoView.setUsed(100);

        infoView.setRadian(getRadian());
        infoView.setRadius(getRadius());
        infoView.setDistance(getDistance());

        if (iconbg != null){
                iconbg.setImageDrawable(
                        infoView.getIconViewSex() == 0?
                                mContext.getResources().getDrawable(R.drawable.discovery_radar_icon_bg_women):
                                mContext.getResources().getDrawable(R.drawable.discovery_radar_icon_bg));
        }

        return infoView;
    }

    public void showAttentionAnim(final ContentTempleteView view){
        int randomstatus = (int) (10 + Math.random()* 50);
        if (view.getIshowattention()) {
            return;
        }
        final ImageView imageView = view.getCircleAniImage();
        final IndicatorTextView statusView = view.getIndicatorTextView();
        final int sex = view.getIconViewSex();
        if (randomstatus > 40) {
            statusView.setText(mContext.getResources().getString(R.string.discovery_radar_icon_corner_subscription));
        } else if (randomstatus > 30 && randomstatus < 40) {
            String used = String.format(mContext.getResources().getString(R.string.has_userd), view.getUsed());
            if(view.getUsed() > 10000){
                statusView.setText("10000+" + mContext.getResources().getString(R.string.has_users));
            }else{
                statusView.setText(used);
            }
        } else if (randomstatus > 20 && randomstatus < 30) {
            String distance;
            if (view.getDistance() > 1000) {
                float dis = (float) view.getDistance() / 1000;
                DecimalFormat decimalFormat = new DecimalFormat(".00");
                distance = decimalFormat.format(dis);
                statusView.setText(distance + mContext.getResources().getString(R.string.distance_km));
            } else {
                distance = String.format(mContext.getResources().getString(R.string.distance_mi),
                        view.getDistance());
                statusView.setText(distance);
            }
        } else {
            statusView.setText(sex == 0 ? mContext.getResources().getString(R.string.app_status_discrible_female) :
                    mContext.getResources().getString(R.string.app_status_discrible_male));
        }

        statusView.setSingleLine();
        statusView.forceLayout();
        statusView.setVisibility(View.VISIBLE);
        if (imageView != null && statusView != null) {
            Animation roateani = AnimationUtils.loadAnimation(mContext, R.anim.rotateimg);
            LinearInterpolator lin = new LinearInterpolator();
            roateani.setInterpolator(lin);
            roateani.setRepeatMode(Animation.RESTART);
            roateani.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }
                @Override
                public void onAnimationEnd(Animation animation) {
                    imageView.setVisibility(View.GONE);
                    statusView.setVisibility(View.GONE);
                    view.setIshowattention(false);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            imageView.setAnimation(roateani);
            imageView.setVisibility(View.VISIBLE);
            view.setIshowattention(true);

        }
    }

}
