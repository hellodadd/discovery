package com.freeme.discovery.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.freeme.discovery.R;
import com.freeme.discovery.common.AsyncImageCache;
import com.freeme.discovery.models.AppInfo;
import com.freeme.discovery.models.VideoInfo;
import com.freeme.discovery.utils.CommonUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class RadarScene extends FrameLayout {
    private View mRadarScanView;
    private ImageView mBottomCicyleView;

    private float mViewWidth = 0f;
    private float mViewHeight;
    private float mViewCenterX;

    private ObjectAnimator mRadarScanAni;

    private Context mContext;

    private int LcdWidth;
    private int LcdHeight;

    private float mRotateAngle = 0.0f;
    private double mStartAngle = 0;
    private float mRotateAngleDefault;

    float lastX;
    float lastY;

    private TextView disdanceView[] = new TextView[4];

    private int mStarDotNum = 5;


    private ContentTempleteView mContentTempleteView[] = new ContentTempleteView[60];

    private int mItemCount = 0;

    private onItemClickListener onItemClickListener;

    private final static int RADIAN_0 = 5;// 0 cicir show 5
    private final static int RADIAN_1 = 8;//
    private final static int RADIAN_2 = 13;//
    private final static int RADIAN_3 = 16;//
    private final static int RADIAN_4 = 18;//

    private List mapList = new ArrayList();

    final static int MSG_RADAR_SCAN_ANI = 1;
    final static int MSG_DEFAULT_ROTATE_SPEED = 2;
    final static int MSG_UPDATE_STATUS = 3;


    private AsyncImageCache mAsyncImageCache = null;

    Handler mHandler = new Handler(){
        public final void handleMessage(Message paramMessage){
            switch (paramMessage.what){
                case 0:
                    updateAngle(0.4f);
                    break;
                case MSG_RADAR_SCAN_ANI:
                    startRadarScanAni();
                    //updateAngle();
                    break;
                case MSG_DEFAULT_ROTATE_SPEED:
                    updateAngleDefault();
                    break;
                case MSG_UPDATE_STATUS:
                    Log.i("zccc", "    MSG_UPDATE_STATUS   ");
                    for(int i = 0; i < 5; i++) {
                        //updateContentViewSataus();
                    }
                    break;

            }
        }
    };


    public RadarScene(Context context){
        this(context,null);
    }

    public RadarScene(Context context, AttributeSet attrs){
        this(context,attrs,0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RadarScene(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;

        initMapList();

        LcdWidth = context.getResources().getDisplayMetrics().widthPixels;
        LcdHeight = context.getResources().getDisplayMetrics().heightPixels;

        setWillNotDraw(false);

        mRadarScanView = new View(context);
        addView(mRadarScanView, 0, new FrameLayout.LayoutParams(100, 100));

        mBottomCicyleView = new ImageView(context);
        mBottomCicyleView.setScaleType(ImageView.ScaleType.FIT_XY);
        mBottomCicyleView.setBackground(context.getDrawable(R.drawable.discovery_radar_center_meter));
        int width = getDefaultWidth()/2 + 20;
        addView(mBottomCicyleView, 0, new FrameLayout.LayoutParams(width, width, 81));
        mBottomCicyleView.setPivotX(width / 2.0F);
        mBottomCicyleView.setPivotY(width / 2.0F);
        mBottomCicyleView.setTranslationY(width / 2.0F);

        startRadarScanAni();

        mHandler.sendEmptyMessageDelayed(MSG_DEFAULT_ROTATE_SPEED, 500);

        mAsyncImageCache = AsyncImageCache.from(mContext);

        showDisdanceOnBackground();

        showStarDot();
    }

    protected void onDetachedFromWindow(){
        super.onDetachedFromWindow();
        mHandler.removeMessages(MSG_DEFAULT_ROTATE_SPEED);
        mHandler.removeMessages(MSG_RADAR_SCAN_ANI);
    }


    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);

        mViewWidth = getMeasuredWidth();
        mViewHeight = getMeasuredHeight();
        mViewCenterX = (mViewWidth / 2.0F);

        if (mRadarScanView != null){
            mRadarScanView.setBackgroundDrawable(/*new BitmapDrawable(getResources(), bg)*/
            mContext.getResources().getDrawable(R.drawable.discovery_wave));
            int width = (int)(mViewWidth / 0.33F);//(int)(bg.getWidth() / 0.33F);
            int height = (int)(mViewHeight / 0.33F);//(int)(bg.getHeight() / 0.33F);
            FrameLayout.LayoutParams layoutParams =
                    (FrameLayout.LayoutParams)mRadarScanView.getLayoutParams();
            layoutParams.width = width;
            layoutParams.height = height;
            mRadarScanView.setLayoutParams(layoutParams);
            mRadarScanView.setMinimumWidth(width);
            mRadarScanView.setMinimumHeight(height);
            int margin = CommonUtils.dip2px(mContext, 3.0F);
            mRadarScanView.setTranslationX(mViewCenterX - width + margin);
            mRadarScanView.setTranslationY(mViewHeight - height + CommonUtils.dip2px(mContext, 2.0F));
            mRadarScanView.setPivotX(width - margin);
            mRadarScanView.setPivotY(height);
        }
    }

    private int getDefaultWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return Math.min(outMetrics.widthPixels, outMetrics.heightPixels);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom){
        super.onLayout(changed, left, top, right, bottom);

        final int childCount = getChildCount();

        int startDisdance = CommonUtils.dip2px(mContext,50);
        for(int i = 0; i < 4; i++){
            disdanceView[i].layout(LcdWidth / 2 - CommonUtils.dip2px(mContext,20) , startDisdance,
                    LcdWidth / 2 + CommonUtils.dip2px(mContext,100), startDisdance + CommonUtils.dip2px(mContext,50) );

            startDisdance += CommonUtils.dip2px(mContext,100);
        }

        mItemCount = 0;
        for(int i = 0; i < childCount; i++){
            View view = getChildAt(i);
            if("app".equals(view.getTag())){
                ContentTempleteView iconView = (ContentTempleteView)view;
                double radian = iconView.getRadian();
                radian %= 360;
                int x, y;
                float tmp = iconView.getRadius();
                x = (int) Math.round(tmp
                        * Math.cos(Math.toRadians(radian)));
                y = (int) Math.round(tmp
                        * Math.sin(Math.toRadians(radian)));
                int l = LcdWidth / 2 + x;
                int t = LcdHeight -y;
                iconView.layout(l, t, l + CommonUtils.dip2px(mContext,140), t + CommonUtils.dip2px(mContext,140));
                iconView.setIconViewXY(l, t);
                mItemCount +=1;
            }

            if("star_dot".equals(view.getTag())){
                RadarScanDotView stardot = (RadarScanDotView) view;
                float radian = stardot.getRadian();
                radian %= 360;
                int x, y;
                float tmp = stardot.getRadius();
                x = (int) Math.round(tmp
                        * Math.cos(Math.toRadians(radian)));
                y = (int) Math.round(tmp
                        * Math.sin(Math.toRadians(radian)));
                int l = LcdWidth / 2 + x;
                int t = LcdHeight -y;
                stardot.layout(l, t, l + 50, t + 50);
                stardot.setOrginX(l);
                stardot.setOrginY(t);
                stardot.setLayouted(true);
            }
        }

    }

    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
    }

    private void showDisdanceOnBackground(){
        for(int i = 0; i < 4; i++){
            disdanceView[i] = new TextView(mContext);
        }
        disdanceView[0].setText("- " + "6.57 KM");
        disdanceView[0].setTextSize(12);
        disdanceView[0].setTextColor(0x4400fdfa);
        disdanceView[1].setText("- " + "4.53 KM");
        disdanceView[1].setTextSize(12);
        disdanceView[1].setTextColor(0x4400fdfa);
        disdanceView[2].setText("- " + "2.84 KM");
        disdanceView[2].setTextSize(12);
        disdanceView[2].setTextColor(0x4400fdfa);
        disdanceView[3].setText("- " + "1.53 KM");
        disdanceView[3].setTextSize(12);
        disdanceView[3].setTextColor(0x4400fdfa);
        for(TextView view : disdanceView){
            FrameLayout.LayoutParams p =  new FrameLayout.LayoutParams(-2,-2);
            p.gravity = Gravity.CENTER_VERTICAL;
            addView(view, p);
        }
    }

    private void startRadarScanAni(){
        if(mRadarScanAni == null){
            mRadarScanAni = ObjectAnimator.ofFloat(mRadarScanView, "rotation", new float[] { -95.0F, 160.0F });
        }
        mRadarScanAni.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
            }
        });
        mRadarScanAni.setDuration(3000L);
        mRadarScanAni.start();
        mHandler.removeMessages(MSG_RADAR_SCAN_ANI);
        mHandler.sendEmptyMessageDelayed(MSG_RADAR_SCAN_ANI, 3500L);

        mHandler.removeMessages(MSG_UPDATE_STATUS);
        updateContentViewSataus(new Random().nextInt(5));
        updateVideoTypeAttentionAni(new Random().nextInt(5));
    }

    private void updateAngleDefault(){
        mRotateAngle += 0.04f;
        updateAngle(mRotateAngle);
        mHandler.removeMessages(MSG_DEFAULT_ROTATE_SPEED);
        mHandler.sendEmptyMessage(MSG_DEFAULT_ROTATE_SPEED);
        mHandler.removeMessages(MSG_UPDATE_STATUS);
    }

    private void updateAngle(float angle){
            mRotateAngle = angle;
            mBottomCicyleView.setRotation(-angle);
            mStartAngle = angle;

            for(int i = 0; i < getChildCount(); i++){
                View view = getChildAt(i);
                if("app".equals(view.getTag())){
                    ContentTempleteView iconView = (ContentTempleteView) view;
                    double radian = iconView.getRadian() + angle;
                    radian %= 360;
                    int x, y;
                    float tmp = iconView.getRadius();
                    x = (int) Math.round(tmp
                            * Math.cos(Math.toRadians(radian)));
                    y = (int) Math.round(tmp
                            * Math.sin(Math.toRadians(radian)));
                    int l = LcdWidth / 2 + x;
                    int t = LcdHeight -y;
                    iconView.setTranslationX(l - iconView.getIconViewX());
                    iconView.setTranslationY(t - iconView.getIconViewY());
                }

                if("star_dot".equals(view.getTag())){
                    RadarScanDotView stardot = (RadarScanDotView) view;
                    float rad = stardot.getRadian() + angle;
                    rad %= 360;
                    int x, y;
                    float tmp = stardot.getRadius();
                    x = (int) Math.round(tmp
                            * Math.cos(Math.toRadians(rad)));
                    y = (int) Math.round(tmp
                            * Math.sin(Math.toRadians(rad)));
                    int l = LcdWidth / 2 + x;
                    int t = LcdHeight -y;
                    stardot.setTranslationX(l - stardot.getOrginX());
                    stardot.setTranslationY(t - stardot.getOrginY());
                }

            }
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent){
        if(motionEvent.getAction() == MotionEvent.ACTION_MOVE){
            return true;
        }
        return false;
    }

    float lastDegrees;
    public boolean onTouchEvent(MotionEvent motionEvent){
        boolean ret = false;
        float x = motionEvent.getX();
        float y = motionEvent.getY();

        lastDegrees = mRotateAngle;

        int event = motionEvent.getActionMasked();
        switch (event){
            case MotionEvent.ACTION_DOWN:
                lastX = motionEvent.getX();
                lastY = motionEvent.getY();
                mHandler.removeMessages(MSG_DEFAULT_ROTATE_SPEED);
                break;
            case MotionEvent.ACTION_MOVE:
                float d = getDistance(lastX, lastY, x, y);
                float d2 = getDistance(mViewCenterX, getMeasuredHeight(), x, y);
                float d3 = getDistance(mViewCenterX, mViewHeight, lastX, lastY);
                float degrees = (float)Math.toDegrees(Math.acos((d3 * d3 +
                        d2 * d2 - d * d) / (2.0D * d3 * d2)));
                if(Float.isNaN(degrees)){
                    degrees = 0.0f;
                }
                if (lastX - x < 0) {
                    degrees = -degrees;
                }
                lastX = x;
                lastY = y;
                updateAngle((360.0F + (degrees * 1.0F + lastDegrees)) % 360.0F);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mHandler.sendEmptyMessage(MSG_DEFAULT_ROTATE_SPEED);
        }
        return true;
    }

    private float getDistance(float x, float y, float x1, float y1){
        float dx =  x - x1;
        float dy = y - y1;
        return  (float)Math.abs(Math.sqrt(dx * dx + dy * dy));
    }

    private void initMapList(){
        int radianStep = 0;
        for(int i = 0; i < RADIAN_0; i++) {
            mapList.add(radianStep);
            radianStep += 70;
        }
        radianStep = 0;
        for(int i = RADIAN_0; i < RADIAN_1 + RADIAN_0; i++) {
            mapList.add(radianStep);
            radianStep += 45;
        }
        radianStep = 0;
        for(int i = RADIAN_1 + RADIAN_0; i < RADIAN_2 + RADIAN_1 + RADIAN_0; i++) {
            mapList.add(radianStep);
            radianStep += 27;
        }
        radianStep = 0;
        for(int i = RADIAN_2 + RADIAN_1 + RADIAN_0; i < RADIAN_3 + RADIAN_2 + RADIAN_1 + RADIAN_0; i++) {
            mapList.add(radianStep);
            radianStep += 360/RADIAN_3;
        }
        radianStep = 0;
        for(int i = RADIAN_3 + RADIAN_2 + RADIAN_1 + RADIAN_0; i < RADIAN_4 + RADIAN_3 + RADIAN_2 + RADIAN_1 + RADIAN_0; i++) {
            mapList.add(radianStep);
            radianStep += 360/RADIAN_4;
        }
    }

    private  int count = 0;
    public void updateData(ArrayList<AppInfo> appInfosList){
        mapList.clear();
        initMapList();
        long delay = 0;
        if(appInfosList != null && appInfosList.size() > 0){
            for(AppInfo appInfo : appInfosList){
                String mainType = appInfo.getMainType();

                int resourceType;

                if(mainType.equals(CommonUtils.VIDEO_TYPE)){
                    resourceType = R.layout.video_temp;
                }else{
                    resourceType = R.layout.iconlayout;
                }

                final ContentTempleteView view = (ContentTempleteView) LayoutInflater.from(mContext)
                        .inflate(resourceType, null);

                view.setMainType(mainType);

                ImageView icon = (ImageView) view.findViewById(R.id.hot_app_icon);

                mAsyncImageCache.displayImage(
                        icon,48,48,
                        new AsyncImageCache.NetworkImageGenerator(appInfo.getIconurl(),
                                appInfo.getIconurl()), 10);

                view.setTag("app");


                int random = (int) (Math.random()* mapList.size());
                int radian = (int) mapList.get(random);
                while (radian == 1000){
                    random = (int) (Math.random()* mapList.size());
                    radian = (int) mapList.get(random);
                }
                mapList.set(random, 1000);

                view.setRadian(radian);

                int radius = 0;
                int distance = 0;
                long used;
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

                used = appInfo.getDownloadnum();

                view.setRadius(radius);

                view.setDistance(distance);

                view.setUsed(used);

                view.setAttentionType(new Random().nextInt(5));

                view.setUrl("http://m.zhuoyi.com/detail.php?apk_id="+appInfo.getDocid());
                //*/

                TextView textView = (TextView)view.findViewById(R.id.hot_app_text);
                textView.setText(appInfo.getSname());

                ImageView imageView = (ImageView)view.findViewById(R.id.animg);
                imageView.setVisibility(GONE);
                //*/

                ImageView iconbg = (ImageView)view.findViewById(R.id.icon_bg);

                view.setCircleAniImage(imageView);

                int sex = new Random().nextInt(2);

                view.setIconViewSex(sex);

                if(sex == 0) {
                    if (iconbg != null){
                        iconbg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.discovery_radar_icon_bg_women));
                    }
                }

                IndicatorTextView statusTextView = (IndicatorTextView)view.findViewById(R.id.status_textview);
                statusTextView.setVisibility(GONE);
                view.setIndicatorTextView(statusTextView);

                if(mainType.equals(CommonUtils.VIDEO_TYPE)) {
                    ImageView videoh = (ImageView) view.findViewById(R.id.video_h_ani_t);
                    ImageView videoh_b = (ImageView) view.findViewById(R.id.video_h_ani_b);
                    ImageView videov = (ImageView) view.findViewById(R.id.video_v_ani_l);
                    ImageView videov_r = (ImageView) view.findViewById(R.id.video_v_ani_r);

                    ImageView indline = (ImageView)view.findViewById(R.id.discovery_ind_line);

                    ImageView sexImage = (ImageView)view.findViewById(R.id.icon_button_sex);


                    videoh.setVisibility(GONE);
                    videoh_b.setVisibility(GONE);
                    videov.setVisibility(GONE);
                    videov_r.setVisibility(GONE);

                    indline.setVisibility(GONE);

                    view.setVideo_h_t(videoh);
                    view.setVideo_h_b(videoh_b);
                    view.setVideo_v_l(videov);
                    view.setVideo_v_r(videov_r);
                    view.setIndline(indline);

                    if(sexImage != null){
                        if(view.getIconViewSex() == 0){
                            sexImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.discovery_woman));
                        }else{
                            sexImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.discovery_man));
                        }
                    }
                }

                mContentTempleteView[count] = view;

                view.setVisibility(GONE);

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setVisibility(VISIBLE);
                    }
                }, delay);

                delay += 200;

                icon.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(view.getUrl());
                    }
                });

                addView(view, new FrameLayout.LayoutParams(-2, -2));

                count += 1;

            }
        }
    }

    public void updateVideoData(ArrayList<VideoInfo> videoInfoList){
        long delay = 0;
        mapList.clear();
        initMapList();
        if(videoInfoList != null && videoInfoList.size() > 0){
            for(VideoInfo videoInfo : videoInfoList){
                String mainType = videoInfo.getMainType();

                int resourceType;

                if(mainType.equals(CommonUtils.VIDEO_TYPE)){
                    resourceType = R.layout.video_temp;
                }else{
                    resourceType = R.layout.iconlayout;
                }

                final ContentTempleteView view = (ContentTempleteView) LayoutInflater.from(mContext)
                        .inflate(resourceType, null);

                view.setMainType(mainType);

                ImageView icon = (ImageView) view.findViewById(R.id.hot_app_icon);

                mAsyncImageCache.displayImage(
                        icon,214,144,
                        new AsyncImageCache.NetworkImageGenerator(videoInfo.getIconurl(),
                                videoInfo.getIconurl()), 10);

                view.setTag("app");


                int random = (int) (Math.random()* CommonUtils.VIDEO_LIMI);
                int radian = (int) mapList.get(random);
                while (radian == 1000){
                    random = (int) (Math.random()* CommonUtils.VIDEO_LIMI);
                    radian = (int) mapList.get(random);
                }
                mapList.set(random, 1000);

                view.setRadian(radian);

                int radius = 0;
                int distance = 0;
                long used;
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

                used = 100;//appInfo.getDownloadnum();

                view.setRadius(radius);

                view.setDistance(distance);

                view.setUsed(used);

                view.setAttentionType(new Random().nextInt(5));

                view.setUrl(videoInfo.getUrl());
                //*/

                TextView textView = (TextView)view.findViewById(R.id.hot_app_text);
                textView.setText(videoInfo.getSname());

                ImageView imageView = (ImageView)view.findViewById(R.id.animg);
                imageView.setVisibility(GONE);
                //*/

                ImageView iconbg = (ImageView)view.findViewById(R.id.icon_bg);

                view.setCircleAniImage(imageView);

                int sex = new Random().nextInt(2);

                view.setIconViewSex(sex);

                if(sex == 0) {
                    if (iconbg != null){
                        //iconbg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.discovery_radar_icon_bg_women));
                    }
                }

                IndicatorTextView statusTextView = (IndicatorTextView)view.findViewById(R.id.status_textview);
                statusTextView.setVisibility(GONE);
                view.setIndicatorTextView(statusTextView);

                if(mainType.equals(CommonUtils.VIDEO_TYPE)) {
                    ImageView videoh = (ImageView) view.findViewById(R.id.video_h_ani_t);
                    ImageView videoh_b = (ImageView) view.findViewById(R.id.video_h_ani_b);
                    ImageView videov = (ImageView) view.findViewById(R.id.video_v_ani_l);
                    ImageView videov_r = (ImageView) view.findViewById(R.id.video_v_ani_r);

                    ImageView indline = (ImageView)view.findViewById(R.id.discovery_ind_line);

                    ImageView sexImage = (ImageView)view.findViewById(R.id.icon_button_sex);


                    videoh.setVisibility(GONE);
                    videoh_b.setVisibility(GONE);
                    videov.setVisibility(GONE);
                    videov_r.setVisibility(GONE);

                    indline.setVisibility(GONE);

                    view.setVideo_h_t(videoh);
                    view.setVideo_h_b(videoh_b);
                    view.setVideo_v_l(videov);
                    view.setVideo_v_r(videov_r);
                    view.setIndline(indline);

                    if(sexImage != null){
                        if(view.getIconViewSex() == 0){
                            sexImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.discovery_woman));
                        }else{
                            sexImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.discovery_man));
                        }
                    }
                }

                mContentTempleteView[count] = view;

                view.setVisibility(GONE);

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setVisibility(VISIBLE);
                    }
                }, delay);

                delay += 200;

                icon.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(view.getUrl());
                    }
                });

                addView(view, new FrameLayout.LayoutParams(-2, -2));

                count += 1;

            }
        }
    }

    public void clearData(){
        for(int i = 0; i < mContentTempleteView.length; i++) {
           removeView(mContentTempleteView[i]);
        }
        mapList.clear();
        initMapList();
        count = 0;
    }

    public void stopRadarScan(){
        mHandler.removeMessages(MSG_DEFAULT_ROTATE_SPEED);
    }

    public void startRadarScan(){
        mHandler.sendEmptyMessage(MSG_DEFAULT_ROTATE_SPEED);
    }

    private void showStarDot(){
        int radian = 0;
        for(int i = 0; i < mStarDotNum; i++){
            RadarScanDotView view = new RadarScanDotView(mContext);
            view.setImageDrawable(mContext.getResources().getDrawable(R.drawable.start_dot));
            view.setTag("star_dot");
            view.setRadius(CommonUtils.dip2px(mContext,CommonUtils.RADIUS[i]));
            view.setRadian(radian);
            radian += 70;

            Animation alpha = AnimationUtils.loadAnimation(mContext,R.anim.dotviewani);
            LinearInterpolator linearInterpolator = new LinearInterpolator();
            alpha.setInterpolator(linearInterpolator);
            view.setAnimation(alpha);

            addView(view,new FrameLayout.LayoutParams(-2, -2));
        }
    }


    private void updateContentViewSataus(int type){
        int randomstatus = (int) (10 + Math.random()* 50);
        for(int i = 0; i < mContentTempleteView.length; i++) {
            final ContentTempleteView view = mContentTempleteView[i];
            if (view != null && view.getAttentionType() == type
                    && !view.getMainType().equals(CommonUtils.VIDEO_TYPE)) {
                //Log.i("ded", "   i = ----- " + i + "  type  ----- " + type);
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
                    statusView.setText(used);
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
                statusView.setVisibility(VISIBLE);
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
                            imageView.setVisibility(GONE);
                            statusView.setVisibility(GONE);
                            view.setIshowattention(false);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    imageView.setAnimation(roateani);
                    imageView.setVisibility(VISIBLE);
                    view.setIshowattention(true);

                }
            }
        }
    }

    private void updateVideoTypeAttentionAni(int type){
        int randomstatus = (int) (10 + Math.random()* 50);

        for(int i = 0; i < mContentTempleteView.length; i++) {
            final ContentTempleteView view = mContentTempleteView[i];
            if(view != null && view.getAttentionType() == type
                    && view.getMainType().equals(CommonUtils.VIDEO_TYPE)) {
                final ImageView video_h_t = view.getVideo_h_t();
                final ImageView video_h_b = view.getVideo_h_b();
                final ImageView video_v_l = view.getVideo_v_l();
                final ImageView video_v_r = view.getVideo_v_r();

                final IndicatorTextView statusView = view.getIndicatorTextView();
                final int sex = view.getIconViewSex();
                if (randomstatus > 40) {
                    statusView.setText(mContext.getResources().getString(R.string.discovery_radar_icon_corner_subscription));
                } else if (randomstatus > 30 && randomstatus < 40) {
                    String used = String.format(mContext.getResources().getString(R.string.has_userd), view.getUsed());
                    statusView.setText(used);
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
                statusView.setVisibility(VISIBLE);

                final ImageView indline = view.getIndline();
                indline.setVisibility(VISIBLE);

                LinearInterpolator lin = new LinearInterpolator();

                Animation h_t = AnimationUtils.loadAnimation(mContext, R.anim.discovery_video_h);


                h_t.setInterpolator(lin);

                Animation h_b = AnimationUtils.loadAnimation(mContext, R.anim.discovery_video_h_b);

                h_b.setInterpolator(lin);

                Animation v_l = AnimationUtils.loadAnimation(mContext, R.anim.discovery_video_v);

                v_l.setInterpolator(lin);

                Animation v_r = AnimationUtils.loadAnimation(mContext, R.anim.discovery_video_v_r);

                v_r.setInterpolator(lin);
                //v_r.setRepeatMode(Animation.REVERSE);
                v_r.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        video_h_t.setVisibility(GONE);
                        video_h_b.setVisibility(GONE);
                        video_v_l.setVisibility(GONE);
                        video_v_r.setVisibility(GONE);

                        statusView.setVisibility(GONE);
                        indline.setVisibility(GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });



                video_h_t.setAnimation(h_t);
                video_h_b.setAnimation(h_b);
                video_v_l.setAnimation(v_l);
                video_v_r.setAnimation(v_r);
                video_v_r.setTranslationY(0);

                video_h_t.setVisibility(VISIBLE);
                video_h_b.setVisibility(VISIBLE);
                video_v_l.setVisibility(VISIBLE);
                video_v_r.setVisibility(VISIBLE);
            }
        }
    }

    public void stopItemAttentionAni(){
        for(int i = 0; i < mContentTempleteView.length; i++){
            if(mContentTempleteView[i] != null){
                mContentTempleteView[i].getCircleAniImage().setVisibility(GONE);;
                mContentTempleteView[i].getIndicatorTextView().setVisibility(GONE);
            }
        }
        mHandler.removeMessages(MSG_RADAR_SCAN_ANI);
    }

    public void onResume(){
        mHandler.removeMessages(MSG_RADAR_SCAN_ANI);
        mHandler.sendEmptyMessage(MSG_RADAR_SCAN_ANI);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        onItemClickListener = listener;
    }

    public abstract interface onItemClickListener{
        public abstract void onItemClick(String url);
    }
}
