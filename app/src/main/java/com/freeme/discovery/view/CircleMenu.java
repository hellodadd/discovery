package com.freeme.discovery.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;


public class CircleMenu extends RelativeLayout {

    // Child sizes
    private int childWidth = 0;
    private int childHeight = 0;

    // Sizes of the ViewGroup
    private int circleWidth, circleHeight;
    private int radius = 0;

    private float angle = 270;

    private int mChildCount;

    private View[] mChildView;
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

    private double mDegrees;

    private View mView;


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
    }

    private int getDefaultWidth() {
        WindowManager wm = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels);
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


    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        mChildCount = getChildCount();
        mChildView = new View[mChildCount];
        mPosition = new double[mChildCount];

        int childviewLeft = 0;
        int childviewLeft2 = 0;
        int childviewTop = 0;

        for(int i = 0; i < mChildCount; i++){
            View childview = getChildAt(i);
            mChildView[i] = childview;
            int width = childview.getWidth();
            int height = childview.getHeight();
            if(childviewLeft == 0){
                childviewLeft = mWidth / 2 - width / 2;
                childviewLeft2 = childviewLeft;
            }
            if(childviewTop == 0){
                childviewTop = mMenuHeight - height / 2;
            }
            if(i != 0){
                float menuHeight = childviewLeft2 + width / 2 - mWidth / 2;
                mPosition[i] = (-Math.atan(menuHeight / mMenuHeight));
                //Math.cos(mPosition[i]);
            }
            childview.layout(childviewLeft, childviewTop, childviewLeft + width, childviewTop + height);
            childviewLeft2 += width + mItemMargin;
        }

        setViewDegrees(0);

        /*int layoutWidth = r - l;

        // Laying out the child views
        final int childCount = getChildCount();
        int left, top;

        comuteRadius(layoutWidth);
        childHeight = childWidth = (int) (radius / 5);

        float angleDelay = 30;//360f / childCount;

        angle = -270;

        for (int i = 0; i < childCount; i++) {
            final View child = (View) getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }

            if (angle > 360) {
                angle -= 360;
            } else if (angle < 0) {
                angle += 360;
            }

           // child.setAngle(angle);
           // child.setPosition(i);

            left = (int) (((getWidth() / 2) - childWidth / 2) + radius
                    * Math.cos(Math.toRadians(angle)));
            top = (int) (radius + radius* Math.sin(Math.toRadians(angle)) + childHeight / 2);

            child.layout(left, top, left + childWidth, top + childHeight);

            angle += angleDelay;
        }
        */
    }

    public final void setViewDegrees(int degrees){
        if (degrees == 0) {
            setViewDegrees(0.0D);
            mDegrees = 0.0D;
            mView = mChildView[0];
        }else{
            mView = mChildView[1];
            setViewDegrees(mPosition[1]);
            mDegrees = Math.abs(mPosition[1]);

        }
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

    private void comuteRadius(int width) {
        int outRadius = (int) (((float) (Math.sqrt(3) / 3)) * width);
        this.radius = outRadius - outRadius/10;
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
        public abstract void menuItemClick(int postion);
    }
}
