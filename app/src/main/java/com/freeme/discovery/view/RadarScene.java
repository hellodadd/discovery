package com.freeme.discovery.view;


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
import android.widget.TextView;

import com.freeme.discovery.R;
import com.freeme.discovery.bean.apps.HotApp;
import com.freeme.discovery.common.AsyncImageCache;
import com.freeme.discovery.ui.adapter.MyAdapter;
import com.freeme.discovery.utils.CommonUtils;

import java.util.ArrayList;


public class RadarScene extends FrameLayout {


    private View mRadarScanView;
    private ImageView mBottomCicyleView;

    //private final float H;
    private float I;
    private int J;
    private int K;


    private boolean u;
    private float mViewWidth = 0f;
    private float mViewHeight;
    //private float x;
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

        LcdWidth = context.getResources().getDisplayMetrics().widthPixels;
        LcdHeight = context.getResources().getDisplayMetrics().heightPixels;

        setWillNotDraw(false);

        //H = getDroiHight(context, 100.0F)*40;
        K = CommonUtils.dip2px(context, 12.0F);
        J = CommonUtils.dip2px(context, 3.0F);

        mRadarScanView = new View(context);
        addView(mRadarScanView, 0, new FrameLayout.LayoutParams(100, 100));

        mBottomCicyleView = new ImageView(context);
        mBottomCicyleView.setScaleType(ImageView.ScaleType.FIT_XY);
        mBottomCicyleView.setBackground(context.getDrawable(R.drawable.discovery_radar_center_meter));
        int width = getDefaultWidth();
        addView(mBottomCicyleView, 0, new FrameLayout.LayoutParams(width, width, 81));
        mBottomCicyleView.setPivotX(width / 2.0F);
        mBottomCicyleView.setPivotY(width / 2.0F);
        mBottomCicyleView.setTranslationY(width / 2.0F);

        //addAppView();

        startRadarScanAni();


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

        mViewWidth = getMeasuredWidth();
        mViewHeight = getMeasuredHeight();
        //this.x = this.w;
        mViewCenterX = (mViewWidth / 2.0F);
        this.I = (mViewCenterX + this.K);

        if (mRadarScanView != null){
            Bitmap bg = getRadarBitmap();
            mRadarScanView.setBackgroundDrawable(new BitmapDrawable(getResources(), bg));
            int width = (int)(bg.getWidth() / 0.33F);
            int height = (int)(bg.getHeight() / 0.33F);
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

        Log.d("zwb", "zhangwuba ---------onLayout---------");

        final int childCount = getChildCount();
        float angleDelay = 360 / (20 - 1);

        for(int i = 0; i < childCount; i++){

            View view = getChildAt(i);
            if("app".equals(view.getTag())){
                IconView iconView = (IconView)view;
                mStartAngle %= 360;
                int x, y;
                float tmp = iconView.getRadius();
                x = (int) Math.round(tmp
                        * Math.cos(Math.toRadians(mStartAngle)));
                y = (int) Math.round(tmp
                        * Math.sin(Math.toRadians(mStartAngle)));
                int l = LcdWidth / 2 + x;
                int t = LcdHeight -y;
                iconView.layout(l, t, l + 200, t + 200);
                iconView.setIconViewXY(l, t);
                mStartAngle += angleDelay;
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

    private Bitmap getRadarBitmap(){
        int width = (int)mViewWidth;
        int height = (int)(1.0F * mViewHeight);
        float radius = 0.33F * (int)(10.0D + Math.sqrt(height * height + width / 2.0F * (width / 2.0F)));
        Bitmap radarBitmap = Bitmap.createBitmap((int)radius, (int) radius, Bitmap.Config.ARGB_8888);
        Canvas radarCanvas = new Canvas(radarBitmap);

        float mapWith = 0.33F * CommonUtils.dip2px(mContext, 10.0F);
        float strokeWidth = 0.33F * CommonUtils.dip2px(mContext, 1.5F);
        float yCoodinate = mapWith / 2.0F;

        radarCanvas.rotate(-90.0F, 0.0F, radius);
        radarCanvas.translate(0.0F, yCoodinate);

        int indicatorColor = mContext.getResources().getColor(R.color.discovery_radar_indicator);
        int waveColor = mContext.getResources().getColor(R.color.discovery_radar_wave);

        int lineColor = indicatorColor & 0x70FFFFFF;
        int gradientColor = 0xFFFFFF & indicatorColor;

        SweepGradient localSweepGradient = new SweepGradient(0.0F, radius,
                new int[] { lineColor, gradientColor, gradientColor,
                        gradientColor, gradientColor, gradientColor,
                        gradientColor, gradientColor, gradientColor },
                new float[] { 0.0F, 0.1246F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F });

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(localSweepGradient);
        radarCanvas.drawCircle(0.0F, radius, radius, paint);
        radarCanvas.translate(0.0F, -yCoodinate);
        Paint linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(0xFF000000 | waveColor);
        linePaint.setStrokeWidth(strokeWidth);
        radarCanvas.drawLine(0.0F, radius + yCoodinate, radius, yCoodinate + radius, linePaint);
        Paint wavePaint = new Paint(1);
        wavePaint.setColor(waveColor);
        int lineWidth = (int)(0.33F * CommonUtils.dip2px(mContext, 1.0F));
        wavePaint.setStrokeWidth(lineWidth);
        radarCanvas.drawLine(0.0F, radius, 0.0F, 0.0F, wavePaint);
        for (int i = 0; i < 5; ++i) {
            radarCanvas.drawLine(0.0F, radius, radius, i * -lineWidth, wavePaint);
            radarCanvas.drawLine(0.0F, radius, radius, lineWidth * i, wavePaint);
        }
        Matrix matrix = new Matrix();
        matrix.postScale(-1.0F, 1.0F);
        Bitmap bitmap = Bitmap.createBitmap(radarBitmap, 0, 0,
                radarBitmap.getWidth(), radarBitmap.getHeight(), matrix, true);
        if (bitmap != radarBitmap) {
            radarBitmap.recycle();
        }
        return bitmap;
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
            mBottomCicyleView.setRotation(-angle);


            // mHandler.removeMessages(2);
            // mHandler.sendEmptyMessage(2);

            mStartAngle = angle;
            float angleDelay = 360 / (20 - 1);

            for(int i = 0; i < getChildCount(); i++){



                View view = getChildAt(i);
                if("app".equals(view.getTag())){
                    IconView iconView = (IconView) view;
                    mStartAngle %= 360;
                    int x, y;
                    float tmp = iconView.getRadius();
                    x = (int) Math.round(tmp
                            * Math.cos(Math.toRadians(mStartAngle)));
                    y = (int) Math.round(tmp
                            * Math.sin(Math.toRadians(mStartAngle)));
                    int l = LcdWidth / 2 + x;
                    int t = LcdHeight -y;
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
                float d2 = getDistance(mViewCenterX, getMeasuredHeight(), x, y);
                float d3 = getDistance(mViewCenterX, mViewHeight, lastX, lastY);
                float degrees = (float)Math.toDegrees(Math.acos((d3 * d3 +
                        d2 * d2 - d * d) / (2.0D * d3 * d2)));
                //mHandler.sendEmptyMessage(0);
                if(Float.isNaN(degrees)){
                    degrees = 0.0f;
                }

                if (lastX - x < 0) {
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

                view.setRadius((int) (800 + Math.random()*400));

                TextView textView = (TextView)view.findViewById(R.id.hot_app_text);
                textView.setText(hotApp.getApkName());

                ImageView imageView = (ImageView)view.findViewById(R.id.animg);
                imageView.setPivotX(imageView.getWidth()/2);
                imageView.setPivotY(imageView.getWidth()/2);
                imageView.setTranslationY(imageView.getWidth() / 2.0F);

                ObjectAnimator ani = ObjectAnimator.ofFloat(imageView, "rotation", new float[] { -95.0F, 160.0F });
                ani.setDuration(1000L);
                ani.start();

                addView(view,new FrameLayout.LayoutParams(-2, -2));
            }
        }

        //requestLayout();

    }
}
