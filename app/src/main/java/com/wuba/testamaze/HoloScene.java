package com.wuba.testamaze;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by server on 16-9-18.
 */

public class HoloScene extends FrameLayout {


    private View mRadarScanView;
    private ImageView mBottomCicyleView;

    private final float H;
    private float I;
    private int J;
    private int K;


    private boolean u;
    private float v = 0f;
    private float w;
    private float x;
    private float y;

    private ObjectAnimator mRadarScanAni;

    private Context mContext;

    private int LcdWidth;
    private int LcdHeight;

    private float mRotateAngle = 0.0f;
    private double mStartAngle = 0;
    private float mRotateAngleDefault;

    float lastX;
    float lastY;

    private final ValueAnimator ac = new ValueAnimator();

    final static int MSG_RADAR_SCAN_ANI = 1;
    final static int MSG_DEFAULT_ROTATE_SPEED = 2;

    private MyAdapter mMyAdapter;

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

            }
        }
    };


    public HoloScene(Context context){
        this(context,null);
    }

    public HoloScene(Context context, AttributeSet attrs){
        this(context,attrs,0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HoloScene(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;

        LcdWidth = context.getResources().getDisplayMetrics().widthPixels;
        LcdHeight = context.getResources().getDisplayMetrics().heightPixels;

        setWillNotDraw(false);

        H = getDroiHight(context, 100.0F)*40;
        K = getDroiHight(context, 12.0F);
        J = getDroiHight(context, 3.0F);

        mRadarScanView = new View(context);
        addView(mRadarScanView, 0, new FrameLayout.LayoutParams(100, 100));

        mBottomCicyleView = new ImageView(context);
        mBottomCicyleView.setScaleType(ImageView.ScaleType.FIT_XY);
        mBottomCicyleView.setBackground(context.getDrawable(R.drawable.discovery_radar_center_meter));
        int l = 1080;
        addView(mBottomCicyleView, 0, new FrameLayout.LayoutParams(l, l, 81));
        mBottomCicyleView.setPivotX(l / 2.0F);
        mBottomCicyleView.setPivotY(l / 2.0F);
        mBottomCicyleView.setTranslationY(l / 2.0F);

        addAppView();

        startRadarScanAni();

        ac.setInterpolator(new DecelerateInterpolator());
        ac.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        ac.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //updateAngle();
            }
        });

        float f3 = 0.06F;
        float f4 = 100.0F * f3;
        float[] arrayOfFloat = new float[2];
        arrayOfFloat[0] = mRotateAngle;
        arrayOfFloat[1] = (f4 + mRotateAngle);
        //ac.setFloatValues(arrayOfFloat);
        //ac.setDuration((int)(800.0F * Math.abs(f3)));
        //ac.start();

        //updateAngle();
        mHandler.sendEmptyMessageDelayed(MSG_DEFAULT_ROTATE_SPEED, 500);

        mAsyncImageCache = AsyncImageCache.from(mContext);
    }

    protected void onDetachedFromWindow(){
        super.onDetachedFromWindow();
        mHandler.removeMessages(MSG_DEFAULT_ROTATE_SPEED);
        mHandler.removeMessages(MSG_RADAR_SCAN_ANI);
    }


    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);

        this.v = getMeasuredWidth();
        this.w = getMeasuredHeight();
        this.x = this.w;
        this.y = (this.v / 2.0F);
        this.I = (this.y + this.K);

        if (mRadarScanView != null){
            Bitmap bg = getRadarIndicator();
            mRadarScanView.setBackgroundDrawable(new BitmapDrawable(getResources(), bg));
            int i1 = (int)(bg.getWidth() / 0.33F);
            int i2 = (int)(bg.getHeight() / 0.33F);
            FrameLayout.LayoutParams localLayoutParams = (FrameLayout.LayoutParams)mRadarScanView.getLayoutParams();
            localLayoutParams.width = i1;
            localLayoutParams.height = i2;
            mRadarScanView.setLayoutParams(localLayoutParams);
            mRadarScanView.setMinimumWidth(i1);
            mRadarScanView.setMinimumHeight(i2);
            int i3 = getDroiHight(mContext, 3.0F);
            mRadarScanView.setTranslationX(this.y - i1 + i3);
            mRadarScanView.setTranslationY(this.x - i2 + getDroiHight(mContext, 2.0F));
            mRadarScanView.setPivotX(i1 - i3);
            mRadarScanView.setPivotY(i2);
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
        float angleDelay = 360 / (20 - 1);
        int layoutRadius = 800;

        for(int i = 0; i < childCount; i++){



            View view = getChildAt(i);
            Log.d("zwb", "zhangwuba ---------ddd- = " +view.getTag());
            if("app".equals(view.getTag())){
                mStartAngle %= 360;
                int x, y;
                float tmp = 800;
                x = (int) Math.round(tmp
                        * Math.cos(Math.toRadians(mStartAngle)));
                y = (int) Math.round(tmp
                        * Math.sin(Math.toRadians(mStartAngle)));
                int l = LcdWidth / 2 + x;
                int t = LcdHeight -y;
                view.layout(l, t, l + 200, t + 200);
                IconView iconView = (IconView)view;
                iconView.setIconViewXY(l, t);
                mStartAngle += angleDelay;
            }
        }

        float angleDelay2 = 360 / (20 - 1);

        for(int i = 0; i < childCount; i++){

            mStartAngle %= 360;

            View view = getChildAt(i);
            Log.d("zwb", "zhangwuba ---------ddd- = " +view.getTag());
            if("app2".equals(view.getTag())){
                int x, y;
                float tmp = 1200;
                x = (int) Math.round(tmp
                        * Math.cos(Math.toRadians(mStartAngle)));
                y = (int) Math.round(tmp
                        * Math.sin(Math.toRadians(mStartAngle)));
                int l = LcdWidth / 2 + x;
                int t = LcdHeight -y;
                view.layout(l, t, l + 200, t + 200);
                IconView iconView = (IconView)view;
                iconView.setIconViewXY(l, t);
                mStartAngle += angleDelay2;
            }
        }



    }

    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        drawGridBackground(mContext, canvas);
    }

    private void drawGridBackground(Context context, Canvas canvas){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap grid = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.discovery_grid, options);
        BitmapShader gridShader = new BitmapShader(grid, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        int gridWidth = grid.getWidth();
        int gridHeight = grid.getHeight();
        float halfCanvasW = canvas.getWidth() / 2.0f;
        float halfCanvasH = canvas.getHeight() / 2.0f;
        float dx = (halfCanvasW / gridWidth - (int)(halfCanvasW / gridWidth) - 0.5F - 1.0F) * gridWidth;
        float dy = (halfCanvasH / gridHeight - (int)(halfCanvasH / gridHeight) - 0.5F - 1.0F) * gridHeight;

        Matrix matrix = new Matrix();
        matrix.postTranslate(dx, dy);
        gridShader.setLocalMatrix(matrix);
        Paint paint = new Paint();
        paint.setShader(gridShader);

        canvas.drawRect(0.0f, 0.0f, (float) LcdWidth, (float) LcdHeight, paint);
    }

    private void drawIconView(Context context, Canvas canvas){

    }

    public int getDroiHight(Context paramContext, float paramFloat) {
        return (int)(0.5F + paramFloat * paramContext.getResources().getDisplayMetrics().density);
    }

    private Bitmap getRadarIndicator(){
        int k = (int)v;
        int l = (int)(1.0F * x);
        float f1 = 0.33F * (int)(10.0D + Math.sqrt(l * l + k / 2.0F * (k / 2.0F)));
        int i1 = (int)f1;
        Bitmap localBitmap1 = Bitmap.createBitmap(i1, i1, Bitmap.Config.ARGB_8888);
        Canvas localCanvas2 = new Canvas(localBitmap1);

        float f2 = 0.33F * getDroiHight(mContext, 10.0F);
        float f3 = 0.33F * getDroiHight(mContext, 1.5F);
        float f4 = f2 / 2.0F;

        localCanvas2.rotate(-90.0F, 0.0F, f1);
        localCanvas2.translate(0.0F, f4);

        int i2 = mContext.getResources().getColor(R.color.discovery_radar_indicator);
        int i3 = mContext.getResources().getColor(R.color.discovery_radar_wave);

        int i4 = i2 & 0x70FFFFFF;
        int i5 = 0xFFFFFF & i4;

        SweepGradient localSweepGradient = new SweepGradient(0.0F, f1,
                new int[] { i4, i5, i5, i5, i5, i5, i5, i5, i5 },
                new float[] { 0.0F, 0.1246F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F });

        Paint localPaint1 = new Paint();
        localPaint1.setAntiAlias(true);
        localPaint1.setShader(localSweepGradient);
        localCanvas2.drawCircle(0.0F, f1, f1, localPaint1);
        localCanvas2.translate(0.0F, -f4);
        Paint localPaint2 = new Paint();
        localPaint2.setAntiAlias(true);
        localPaint2.setColor(0xFF000000 | i3);
        localPaint2.setStrokeWidth(f3);
        localCanvas2.drawLine(0.0F, f1 + f4, f1, f4 + f1, localPaint2);
        Paint localPaint3 = new Paint(1);
        localPaint3.setColor(i3);
        int i6 = (int)(0.33F * getDroiHight(mContext, 1.0F));
        localPaint3.setStrokeWidth(i6);
        localCanvas2.drawLine(0.0F, f1, 0.0F, 0.0F, localPaint3);
        for (int i7 = 0; i7 < 5; ++i7)
        {
            localCanvas2.drawLine(0.0F, f1, f1, i7 * -i6, localPaint3);
            localCanvas2.drawLine(0.0F, f1, f1, i6 * i7, localPaint3);
        }
        Matrix localMatrix = new Matrix();
        localMatrix.postScale(-1.0F, 1.0F);
        Bitmap localBitmap2 = Bitmap.createBitmap(localBitmap1, 0, 0, localBitmap1.getWidth(), localBitmap1.getHeight(), localMatrix, true);
        if (localBitmap2 != localBitmap1) {
            localBitmap1.recycle();
        }
        return localBitmap2;
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

        //mRotateAngle += 1.0f;

        //mBottomCicyleView.setRotation(mRotateAngle);
    }

    private void updateAngleDefault(){
        mRotateAngle += 0.02f;
        updateAngle(mRotateAngle);
        mHandler.removeMessages(MSG_DEFAULT_ROTATE_SPEED);
        mHandler.sendEmptyMessage(MSG_DEFAULT_ROTATE_SPEED);
    }

    private void updateAngle(float angle){
        //Log.d("zwb", "  --------------updateAngle-------angle-------  = " + angle);
        if(ac != null){
            //mRotateAngle -= angle; //(360.0F + ((Float)ac.getAnimatedValue()).floatValue()) % 360.0F;
            //Log.d("zwb", "  --------------updateAngle--------------  " + mRotateAngle);
            mRotateAngle = angle;
            mBottomCicyleView.setRotation(angle);


            // mHandler.removeMessages(2);
            // mHandler.sendEmptyMessage(2);

            mStartAngle = -angle;
            float angleDelay = 360 / (20 - 1);

            for(int i = 0; i < getChildCount(); i++){



                View view = getChildAt(i);
                if("app".equals(view.getTag())){
                    mStartAngle %= 360;
                    int x, y;
                    float tmp = 800;
                    x = (int) Math.round(tmp
                            * Math.cos(Math.toRadians(mStartAngle)));
                    y = (int) Math.round(tmp
                            * Math.sin(Math.toRadians(mStartAngle)));
                    int l = LcdWidth / 2 + x;
                    int t = LcdHeight -y;
                    IconView iconView = (IconView) view;
                    iconView.setTranslationX(l - iconView.getIconViewX());
                    iconView.setTranslationY(t - iconView.getIconViewY());
                    //view.setScrollX(100);
                    //view.setScrollY(100);
                    //view.setTranslationY(t);
                    //invalidate();
                    invalidate();
                    mStartAngle += angleDelay;
                }

            }
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent){

        return false;
    }

    float lastDegrees;
    public boolean onTouchEvent(MotionEvent motionEvent){

        float x = motionEvent.getX();
        float y = motionEvent.getY();

        int event = motionEvent.getActionMasked();
        switch (event){
            case MotionEvent.ACTION_DOWN:
                lastX = motionEvent.getX();
                lastY = motionEvent.getY();
                lastDegrees = mRotateAngle;
                mHandler.removeMessages(MSG_DEFAULT_ROTATE_SPEED);
                break;
            case MotionEvent.ACTION_MOVE:
                float d = getDistance(lastX, lastY, x, y);
                float d2 = getDistance(this.y, getMeasuredHeight(), x, y);
                float d3 = getDistance(this.y, this.w, lastX, lastY);
                float degrees = (float)Math.toDegrees(Math.acos((d3 * d3 +
                        d2 * d2 - d * d) / (2.0D * d3 * d2)));
                //mHandler.sendEmptyMessage(0);
                if(Float.isNaN(degrees)){
                    degrees = 0.0f;
                }

                if (x - lastX < 0) {
                    degrees = -degrees;
                }

                updateAngle((360.0F + (degrees * 1.0F + lastDegrees)) % 360.0F);

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                //mHandler.removeMessages(0);
                mHandler.sendEmptyMessage(MSG_DEFAULT_ROTATE_SPEED);
                break;


        }
        return true;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void addAppView(){
        for(int i = 0; i < 20; i++){
            IconView view = new IconView(mContext);
            view.setBackground(mContext.getResources().getDrawable(R.drawable.discovery_radar_icon_bg));
            view.setTag("app");
            addView(view,new FrameLayout.LayoutParams(-2, -2));
        }

        for(int i = 0; i < 20; i++){
            IconView view = new IconView(mContext);
            view.setBackground(mContext.getResources().getDrawable(R.drawable.discovery_radar_icon_bg));
            view.setTag("app2");
            addView(view,new FrameLayout.LayoutParams(-2, -2));
        }

        // postDelayed(new AutoRotateRunnable(30.0f), 500);
    }

    private float getDistance(float x, float y, float x1, float y1){
        float dx = x - x1;
        float dy = y - y1;
        return  (float)Math.abs(Math.sqrt(dx * dx + dy * dy));
    }

    private class AutoRotateRunnable implements Runnable {

        private float angelPerSecond;

        public AutoRotateRunnable(float velocity){
            this.angelPerSecond = velocity;
        }

        public void run() {
            mStartAngle += 0.1;//(angelPerSecond / 30);
            post(this);
            //requestLayout();
            //invalidate();
        }
    }

    public void setAdapter(MyAdapter adapter){
        mMyAdapter = adapter;
    }

    public void updateData(ArrayList<HotApp> hotAppsInfo){
        if(hotAppsInfo != null && hotAppsInfo.size() > 0){
            for(HotApp hotApp : hotAppsInfo){
                IconView view = (IconView) LayoutInflater.from(mContext).inflate(R.layout.iconlayout, null);
                ImageView icon = (ImageView) view.findViewById(R.id.hot_app_icon);
                mAsyncImageCache.displayImage(
                        icon,
                        R.drawable.newspage_default_icon,
                        new AsyncImageCache.NetworkImageGenerator(hotApp
                                .getIconUrl(), hotApp.getIconUrl()), 0);
                view.setTag("app");


                addView(view,new FrameLayout.LayoutParams(-2, -2));
            }
        }

        //requestLayout();

    }
}
