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
import com.freeme.discovery.models.VideoInfo;
import com.freeme.discovery.utils.CommonUtils;
import com.freeme.discovery.view.ContentTempleteView;
import com.freeme.discovery.view.IndicatorTextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by server on 16-11-4.
 */

public class VideoAdapter extends BaseAdapter<VideoInfo>{

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

    public VideoAdapter(Context context){
        super(context);
    }

    public int getRadian(){
        random = (int) (Math.random()* CommonUtils.VIDEO_LIMI);
        radian = (int) mapList.get(random);
        while (radian == 1000){
            random = (int) (Math.random()* CommonUtils.VIDEO_LIMI);
            radian = (int) mapList.get(random);
        }
        mapList.set(random, 1000);

        return radian;
    };

    public int getRadius(){
        if(random < RADIAN_0){
            radius = CommonUtils.dip2px(mContext,CommonUtils.RADIUS_VIDEO[0]);
            distance = (int) (300 + Math.random()* 500);
        }else if(random > RADIAN_0 - 1 && random < RADIAN_1 + RADIAN_0){
            radius = CommonUtils.dip2px(mContext,CommonUtils.RADIUS_VIDEO[1]);
            distance = (int) (900 + Math.random()* 1100);
        }else if(random > RADIAN_1 + RADIAN_0 - 1 && random < RADIAN_2 + RADIAN_1 + RADIAN_0){
            radius = CommonUtils.dip2px(mContext,CommonUtils.RADIUS_VIDEO[2]);
            distance = (int) (2100 + Math.random()* 700);
        }else if(random > RADIAN_2 + RADIAN_1 + RADIAN_0 - 1 && random < RADIAN_3 + RADIAN_2 + RADIAN_1 + RADIAN_0){
            radius = CommonUtils.dip2px(mContext,CommonUtils.RADIUS_VIDEO[3]);
            distance = (int) (2900 + Math.random()*  1500);
        }else if(random > RADIAN_3 + RADIAN_2 + RADIAN_1 + RADIAN_0 - 1 && random < RADIAN_4 + RADIAN_3 + RADIAN_2 + RADIAN_1 + RADIAN_0){
            radius = CommonUtils.dip2px(mContext,CommonUtils.RADIUS_VIDEO[4]);
            distance = (int) (4500 + Math.random()* 2000);
        }
        return radius;
    }

    public int getDistance(){
        return distance;
    }


    public ContentTempleteView CreateInfoView(VideoInfo info){
        final ContentTempleteView infoView =
                (ContentTempleteView) LayoutInflater.from(mContext).inflate(R.layout.video_temp,null);
        ImageView icon = (ImageView) infoView.findViewById(R.id.hot_app_icon);
        mAsyncImageCache.displayImage(
                icon,214,144,
                new AsyncImageCache.NetworkImageGenerator(info.getIconurl(),
                        info.getIconurl()), 4);
        TextView title = (TextView)infoView.findViewById(R.id.hot_app_text);
        title.setText(info.getSname());
        IndicatorTextView statusTextView = (IndicatorTextView)infoView.findViewById(R.id.status_textview);
        statusTextView.setVisibility(View.GONE);
        ImageView videoh = (ImageView) infoView.findViewById(R.id.video_h_ani_t);
        ImageView videoh_b = (ImageView) infoView.findViewById(R.id.video_h_ani_b);
        ImageView videov = (ImageView) infoView.findViewById(R.id.video_v_ani_l);
        ImageView videov_r = (ImageView) infoView.findViewById(R.id.video_v_ani_r);
        ImageView indline = (ImageView)infoView.findViewById(R.id.discovery_ind_line);
        ImageView sexImage = (ImageView)infoView.findViewById(R.id.icon_button_sex);
        videoh.setVisibility(View.GONE);
        videoh_b.setVisibility(View.GONE);
        videov.setVisibility(View.GONE);
        videov_r.setVisibility(View.GONE);
        indline.setVisibility(View.GONE);

        View animLayout = infoView.findViewById(R.id.animLayout);
        if(animLayout != null){
            infoView.setAnimlayout(animLayout);
        }

        infoView.setTag("app");
        infoView.setMainType(info.getMainType());
        infoView.setAttentionType(new Random().nextInt(15));
        infoView.setUrl(info.getUrl());
        infoView.setIconViewSex(new Random().nextInt(2));
        infoView.setVideo_h_t(videoh);
        infoView.setVideo_h_b(videoh_b);
        infoView.setVideo_v_l(videov);
        infoView.setVideo_v_r(videov_r);
        infoView.setIndline(indline);
        if(sexImage != null){
            if(infoView.getIconViewSex() == 0){
                sexImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.discovery_woman));
            }else{
                sexImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.discovery_man));
            }
        }
        infoView.setIndicatorTextView(statusTextView);
        infoView.setUsed(100);

        infoView.setRadian(getRadian());
        infoView.setRadius(getRadius());
        infoView.setDistance(getDistance());

        return infoView;
    }

    public void showAttentionAnim(final ContentTempleteView view){
        final ImageView video_h_t = view.getVideo_h_t();
        final ImageView video_h_b = view.getVideo_h_b();
        final ImageView video_v_l = view.getVideo_v_l();
        final ImageView video_v_r = view.getVideo_v_r();

        int randomstatus = (int) (10 + Math.random()* 50);

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

        final ImageView indline = view.getIndline();
        indline.setVisibility(View.VISIBLE);

        LinearInterpolator lin = new LinearInterpolator();

        Animation h_t = AnimationUtils.loadAnimation(mContext, R.anim.discovery_video_h);
        h_t.setInterpolator(lin);
        Animation h_b = AnimationUtils.loadAnimation(mContext, R.anim.discovery_video_h_b);
        h_b.setInterpolator(lin);
        Animation v_l = AnimationUtils.loadAnimation(mContext, R.anim.discovery_video_v);
        v_l.setInterpolator(lin);
        Animation v_r = AnimationUtils.loadAnimation(mContext, R.anim.discovery_video_v_r);
        v_r.setInterpolator(lin);
        v_r.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }
            @Override
            public void onAnimationEnd(Animation animation) {
                video_h_t.setVisibility(View.GONE);
                video_h_b.setVisibility(View.GONE);
                video_v_l.setVisibility(View.GONE);
                video_v_r.setVisibility(View.GONE);
                statusView.setVisibility(View.GONE);
                indline.setVisibility(View.GONE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
                //Log.i("zwb","  repeat ----");
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            video_h_t.setAnimation(h_t);
            video_h_b.setAnimation(h_b);
            video_v_l.setAnimation(v_l);
            video_v_r.setAnimation(v_r);

            video_h_t.setVisibility(View.VISIBLE);
            video_h_b.setVisibility(View.VISIBLE);
            video_v_l.setVisibility(View.VISIBLE);
            video_v_r.setVisibility(View.VISIBLE);
        }else{
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    statusView.setVisibility(View.GONE);
                    indline.setVisibility(View.GONE);
                }
            },3000);
        }
    }
}
