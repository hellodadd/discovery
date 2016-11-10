package com.freeme.discovery.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.freeme.discovery.R;
import com.freeme.discovery.models.AppInfo;
import com.freeme.discovery.models.ShopInfo;
import com.freeme.discovery.models.VideoInfo;
import com.freeme.discovery.ui.adapter.AppAdapter;
import com.freeme.discovery.ui.adapter.BaseAdapter;
import com.freeme.discovery.ui.adapter.ShopAdapter;
import com.freeme.discovery.ui.adapter.VideoAdapter;
import com.freeme.discovery.utils.CommonUtils;

import java.util.ArrayList;
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
    float lastDegrees;
    float lastX;
    float lastY;
    private TextView disdanceView[] = new TextView[4];
    private int mStarDotNum = 5;
    private ContentTempleteView mContentTempleteView[] = new ContentTempleteView[60];
    private onItemClickListener onItemClickListener;

    private BaseAdapter baseAdapter;

    private int mTouchMode = -1;
    private int mTouchSlop;
    private static int TOUCH_MODE_DOWN = 0;
    private static int TOUCH_MODE_UP = 1;
    private static int TOUCH_MODE_MOVE = 1;
    int mMotionX;
    int mMotionY;

    final static int MSG_RADAR_SCAN_ANI = 1;
    final static int MSG_DEFAULT_ROTATE_SPEED = 2;
    final static int MSG_UPDATE_STATUS = 3;

    Handler mHandler = new Handler(){
        public final void handleMessage(Message paramMessage){
            switch (paramMessage.what){
                case 0:
                    updateAngle(0.4f);
                    break;
                case MSG_RADAR_SCAN_ANI:
                    startRadarScanAni();
                    break;
                case MSG_DEFAULT_ROTATE_SPEED:
                    updateAngleDefault();
                    break;
                case MSG_UPDATE_STATUS:
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

    public RadarScene(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;

        LcdWidth = context.getResources().getDisplayMetrics().widthPixels;
        LcdHeight = context.getResources().getDisplayMetrics().heightPixels;

        final ViewConfiguration configuration = ViewConfiguration.get(mContext);
        mTouchSlop = configuration.getScaledTouchSlop();

        setWillNotDraw(false);

        mRadarScanView = new View(context);
        addView(mRadarScanView, 0, new FrameLayout.LayoutParams(100, 100));

        mBottomCicyleView = new ImageView(context);
        mBottomCicyleView.setScaleType(ImageView.ScaleType.FIT_XY);
        mBottomCicyleView.setBackgroundResource(R.drawable.discovery_radar_center_meter);
        int width = getDefaultWidth()/2 + 20;
        addView(mBottomCicyleView, 0, new FrameLayout.LayoutParams(width, width, 81));
        mBottomCicyleView.setPivotX(width / 2.0F);
        mBottomCicyleView.setPivotY(width / 2.0F);
        mBottomCicyleView.setTranslationY(width / 2.0F);

        startRadarScanAni();

        mHandler.sendEmptyMessageDelayed(MSG_DEFAULT_ROTATE_SPEED, 500);

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

        int startDisdance = CommonUtils.dip2px(mContext,40);
        for(int i = 0; i < 4; i++){
            disdanceView[i].layout(LcdWidth / 2 - CommonUtils.dip2px(mContext,20) , startDisdance,
                    LcdWidth / 2 + CommonUtils.dip2px(mContext,100), startDisdance + CommonUtils.dip2px(mContext,20) );

            startDisdance += CommonUtils.dip2px(mContext,120);
        }

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
                if(iconView.getMainType().equals(CommonUtils.SHOP_TYPE)) {
                    iconView.layout(l, t, l + CommonUtils.dip2px(mContext, 190), t + CommonUtils.dip2px(mContext, 120));
                }else{
                    iconView.layout(l, t, l + CommonUtils.dip2px(mContext, 140), t + CommonUtils.dip2px(mContext, 140));
                }
                iconView.setIconViewXY(l, t);
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
                stardot.layout(l, t, l + 30, t + 30);
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
        showAttentionAnim(new Random().nextInt(25));
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
            lastDegrees = angle;
            mBottomCicyleView.setRotation(-angle);

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
        int event = motionEvent.getActionMasked();
        boolean ret = false;
        switch (event){
            case MotionEvent.ACTION_DOWN:
                mMotionX = (int) motionEvent.getX();
                mMotionY = (int) motionEvent.getY();
                mTouchMode = TOUCH_MODE_DOWN;
                break;
            case MotionEvent.ACTION_MOVE:
                int x = (int) motionEvent.getX();
                int y = (int) motionEvent.getY();
                if(mTouchMode == TOUCH_MODE_DOWN){
                    final int deltaX = x - mMotionX;
                    final int deltaY = y - mMotionY;
                    lastX = x;
                    lastY = y;
                    if(Math.abs(deltaX) > mTouchSlop || Math.abs(deltaY) > mTouchSlop){
                        mTouchMode = TOUCH_MODE_MOVE;
                        ret = true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                mTouchMode = TOUCH_MODE_UP;
                break;
        }
        return ret;
    }

    public boolean onTouchEvent(MotionEvent motionEvent){
        boolean ret = true;

        float x = motionEvent.getX();
        float y = motionEvent.getY();

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
                mTouchMode = TOUCH_MODE_UP;
                mHandler.sendEmptyMessage(MSG_DEFAULT_ROTATE_SPEED);
        }
        return ret;
    }

    private float getDistance(float x, float y, float x1, float y1){
        float dx =  x - x1;
        float dy = y - y1;
        return  (float)Math.abs(Math.sqrt(dx * dx + dy * dy));
    }

    public void clearData(){
        for(int i = 0; i < mContentTempleteView.length; i++) {
           removeView(mContentTempleteView[i]);
            mContentTempleteView[i] = null;
        }
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

    private void showAttentionAnim(int type){
        for(int i = 0; i < mContentTempleteView.length; i++) {
            final ContentTempleteView view = mContentTempleteView[i];
            if (view != null && type == view.getAttentionType()) {
                baseAdapter.showAttentionAnim(view);
            }
        }
    }

    public void stopItemAttentionAni(){
        mHandler.removeMessages(MSG_RADAR_SCAN_ANI);
    }

    public void onResume(){
        mHandler.removeMessages(MSG_RADAR_SCAN_ANI);
        mHandler.sendEmptyMessage(MSG_RADAR_SCAN_ANI);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        onItemClickListener = listener;
    }

    public void setAdapter(BaseAdapter adapter){
        baseAdapter = adapter;
        adapter.initMapList();
        for(int i = 0; i < mContentTempleteView.length; i++) {
            removeView(mContentTempleteView[i]);
            mContentTempleteView[i] = null;
        }
        if(adapter instanceof VideoAdapter){
            ArrayList<VideoInfo> list = adapter.getInfoList();
            int index = 0;
            int delayShowTime = 0;
            for(VideoInfo videoInfo : list){
                final ContentTempleteView infoView = adapter.CreateInfoView(videoInfo);
                mContentTempleteView[index] = infoView;
                index += 1;
                infoView.setVisibility(GONE);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        infoView.setVisibility(VISIBLE);
                    }
                },delayShowTime);
                delayShowTime += 200;
                infoView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(infoView.getUrl());
                    }
                });
                addView(infoView, new FrameLayout.LayoutParams(-2, -2));
            }
        }else if(adapter instanceof ShopAdapter){
            ArrayList<ShopInfo> list = adapter.getInfoList();
            int index = 0;
            int delayShowTime = 0;
            for(ShopInfo shopInfo : list){
                final ContentTempleteView infoView = adapter.CreateInfoView(shopInfo);
                mContentTempleteView[index] = infoView;
                index += 1;
                infoView.setVisibility(GONE);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        infoView.setVisibility(VISIBLE);
                    }
                },delayShowTime);
                delayShowTime += 200;
                infoView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(infoView.getUrl());
                    }
                });
                addView(infoView, new FrameLayout.LayoutParams(-2, -2));
            }
        }else if(adapter instanceof AppAdapter){
            ArrayList<AppInfo> list = adapter.getInfoList();
            int index = 0;
            int delayShowTime = 0;
            for(AppInfo appInfo : list){
                final ContentTempleteView infoView = adapter.CreateInfoView(appInfo);
                mContentTempleteView[index] = infoView;
                index += 1;
                infoView.setVisibility(GONE);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        infoView.setVisibility(VISIBLE);
                    }
                },delayShowTime);
                delayShowTime += 200;
                infoView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(infoView.getUrl());
                    }
                });
                addView(infoView, new FrameLayout.LayoutParams(-2, -2));
            }
        }
    }

    public abstract interface onItemClickListener{
        public abstract void onItemClick(String url);
    }
}
