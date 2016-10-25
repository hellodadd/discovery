package com.freeme.discovery.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;

import com.freeme.discovery.utils.CommonUtils;


public class CircleMenu extends RelativeLayout {

    private float angle = 270;

    private int mChildCount;

    private CircleMenuItemView[] mChildView;
    private double[] mPosition;

    private  int mWidth;
    private  int mHeight;

    private int mMenuHeight;

    private Context mContext;

    private int mItemMargin;
    private int mBottomMargin;
    private MenuItemClickListener mItemClickListener;
    private int mOperationHeight;
    private int mScanContenttype;

    private Rect touchRect = new Rect();

    private double mRadian;

    private CircleMenuItemView mFocusView;
    private int  mFocusIndex;

    private float downX;
    private float downY;
    private float lastX;
    private float lastY;

    private boolean mIsRotating;

    private  float mSlop;

    private Point mPoint1 = new Point();
    private Point mPoint2 = new Point();
    private Point mPoint3 = new Point();

    private boolean mIsSwitchView;


    /**
     * @param context
     */
    public CircleMenu(Context context) {
        this(context, null);
    }

    /**
     * @param context
     * @param attrs
     */
    public CircleMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public CircleMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mContext = context;

        mSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();
    }

    private int getDefaultWidth() {
        WindowManager wm = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels);
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent){
        return true;
    }


    public boolean onTouchEvent(MotionEvent motionEvent){
        double distance1,distance2,distance3;
        downX = motionEvent.getX();
        downY = motionEvent.getY();

        mPoint3.x = (int)downX;
        mPoint3.y = (int)downY;

        int event = motionEvent.getAction();
        switch (event){
            case MotionEvent.ACTION_DOWN:
                lastX = motionEvent.getX();
                lastY = motionEvent.getY();

                if(!touchRect.contains((int)lastX, (int)lastY)){
                    return false;
                }

                mPoint2.x = (int)lastX;
                mPoint2.y = (int)lastY;
                break;
            case MotionEvent.ACTION_MOVE:
                distance1 = Math.hypot(lastX - mWidth/2, lastY);
                distance2 = Math.hypot(downX - mWidth/2, downY);
                distance3 = Math.hypot(downX - lastX, downY - lastY);
                int direction = rotatedirection(mPoint2.x - mPoint1.x, mPoint2.y - mPoint1.y,
                        mPoint3.x - mPoint2.x, mPoint3.y - mPoint2.y);
                if(direction == 0){
                    mRadian -= CommonUtils.acos(distance1,distance2,distance3);
                }else {
                    mRadian += CommonUtils.acos(distance1, distance2, distance3);
                }
                mIsRotating = true;
               // if(Math.abs(downX - lastX) > mSlop && Math.abs(downY - lastY) < mSlop)

                switchView((int) (downX - lastX));

                setViewDegrees(mRadian);

                lastX = downX;
                lastY = downY;

                mPoint2.x = (int)downX;
                mPoint2.y = (int)downY;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                rotateCircleMenu();
                break;
        }


        return true;//super.onInterceptTouchEvent(motionEvent);
    }

    private static int rotatedirection(int a, int b, int c, int d){
        if(a * d - b * c > 0){
            return 0;
        }else{
            return 1;
        }
    }

    private void rotateCircleMenu(){
        if (Math.toDegrees(mRadian) < Math.abs(Math.toDegrees(mPosition[1]) / 2.0D)) {
            if (mFocusView == mChildView[0]){
                rotateCircleMenuAni(mRadian, 0.0D);
            }else{
                rotateCircleMenuAni(mRadian, Math.abs(mPosition[1]));
            }
        }else{
            if (mFocusView == mChildView[0]){
               rotateCircleMenuAni(mRadian, 0.0D);
            }else{
                rotateCircleMenuAni(mRadian, Math.abs(mPosition[1]));
            }
        }
    }

    private void rotateCircleMenuAni(final double a, double b){
        ValueAnimator animator = ValueAnimator.ofFloat(new float[] { 0.0F, 1.0F });
        double degrees = b - a;
        animator.setDuration((long) (3000.0F * (float)(Math.abs(degrees) / 3.141592653589793D)));
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            public final void onAnimationCancel(Animator paramAnimator) {
                mIsRotating = false;
            }

            public final void onAnimationEnd(Animator paramAnimator) {

                setViewDegrees(mFocusIndex);
                //mItemClickListener.menuItemClick(mFocusIndex);
            }

            public final void onAnimationStart(Animator paramAnimator) {
                mIsRotating = true;
            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = ((Float)animation.getAnimatedValue()).floatValue();
                //setViewDegrees(1 + (double) value * mWidth);
            }
        });
        animator.start();

    }

    private void switchView(int dis){
        int distance[] = new int[mChildCount];

        for(int i = 0; i < mChildCount; i++){
            distance[i] = Math.abs((int) getChildAt(i).getX() - mWidth/2);
        }

        int min = distance[0];
        mFocusIndex = 0;
        for(int i = 0; i < distance.length; i++){
            if(distance[i] < min){
                min = distance[i];
                mFocusIndex = i;
            }
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if(widthMode != MeasureSpec.UNSPECIFIED || heightMode != MeasureSpec.UNSPECIFIED){
            mWidth = Math.min(widthSize, heightSize);
            mHeight = Math.max(widthSize, heightSize);
        }else{
            mWidth = getSuggestedMinimumWidth();
            if(mWidth == 0){
                mWidth = getDefaultWidth();
            }
            mHeight = getSuggestedMinimumHeight();
            if(mHeight == 0){
                mHeight = getDefaultWidth();
            }
        }

        setMeasuredDimension(mWidth, mHeight);

        mMenuHeight = mHeight - mBottomMargin;

        mPoint1.x = mWidth / 2;
        mPoint1.y = 0;


        touchRect.set(0, mHeight - mOperationHeight, mWidth, mHeight);


    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        mChildCount = getChildCount();
        mChildView = new CircleMenuItemView[mChildCount];
        mPosition = new double[mChildCount];

        int childviewLeft = 0;
        int childviewLeft2 = 0;
        int childviewTop = 0;

        for(int i = 0; i < mChildCount; i++){
            View childview = getChildAt(i);
            mChildView[i] = (CircleMenuItemView) childview;
            int width = childview.getWidth();
            int height = childview.getHeight();
            if(childviewLeft == 0){
                childviewLeft = mWidth / 2 - width / 2;
                childviewLeft2 = childviewLeft;
            }
            if(childviewTop == 0){
                childviewTop = mMenuHeight - height / 2;
            }
            //if(i != 0){
                float menuHeight = childviewLeft2 + width / 2 - mWidth / 2;
                mPosition[i] = (-Math.atan(menuHeight / mMenuHeight));
            //}
            childview.layout(childviewLeft, childviewTop, childviewLeft + width, childviewTop + height);
            childviewLeft2 += width + mItemMargin;
        }

        if(mChildCount > 0) {
            setViewDegrees(mChildCount / 2);
        }

    }

    public final void setViewDegrees(int degrees){
        mFocusView = mChildView[degrees];
        mRadian = Math.abs(mPosition[degrees]);
        setViewDegrees(mRadian);
        mItemClickListener.menuItemClick(mFocusView.getCategoryPid());
    }

    public final void setViewDegrees(double degrees){
        for (int i = 0; i < mChildCount; i++) {
            double d1 = degrees + mPosition[i];
            double d2 = Math.toDegrees(d1) % 360.0D;
            float f1 = (float)(mMenuHeight * Math.sin(d1));
            float f2 = (float)(mMenuHeight * Math.cos(-d1)) - mMenuHeight;
            View view = getChildAt(i);
            view.setTranslationX(f1);
            view.setTranslationY(f2);
            view.setRotation(-(float)d2);
            view.setAlpha(1.0F - (float)(Math.abs(d2) / 20.0D));
        }
    }


    public void setItemMargin(int margin) {
        mItemMargin = margin;
    }

    public void setMenuBottomMargin(int margin) {
        mBottomMargin = margin;
    }

    public void setMenuItemClickListener(MenuItemClickListener listener) {
        mItemClickListener = listener;
    }

    public void setOperationHeight(int height) {
        mOperationHeight = height;
    }

    public void setScanContentType(int type) {
        mScanContenttype = type;
    }

    public static abstract interface MenuItemClickListener {
        public abstract void menuItemClick(int categoryId);
    }
}
