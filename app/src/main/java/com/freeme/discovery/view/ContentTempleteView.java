package com.freeme.discovery.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.freeme.discovery.R;

/**
 * Created by server on 16-10-17.
 */

public class ContentTempleteView extends FrameLayout {
    private int x;
    private int y;

    private int radius;

    private Context mContext;

    private Paint mPaint;

    private ImageView circleAniImage;

    private IndicatorTextView indicatorTextView;

    private int sex;

    private float radian;

    private boolean ishowattention;

    private int distance;

    private long used;

    public ContentTempleteView(Context context) {
        this(context, null, 0);
    }

    public ContentTempleteView(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public ContentTempleteView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);

        mContext = context;

        setWillNotDraw(false);

        mPaint = new Paint();
    }

    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
    }

    public void setIconViewXY(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getIconViewX(){
        return x;
    }

    public int getIconViewY(){
        return y;
    }

    public void setIconViewSex(int s){
        sex = s;
    }

    public int getIconViewSex(){
        return sex;
    }

    public void setRadius(int r){
        radius = r;
    }

    public int getRadius(){
        return radius;
    }

    public float getRadian(){
        return radian;
    }

    public void setRadian(float rad){
        radian = rad;
    }

    public void setCircleAniImage(ImageView view){
        circleAniImage = view;
    }

    public ImageView getCircleAniImage(){
        return circleAniImage;
    }

    public void updateCircleAni(float rotation){
        circleAniImage.setRotation(rotation);
    }

    public void setIndicatorTextView(IndicatorTextView view){
        indicatorTextView = view;
    }

    public IndicatorTextView getIndicatorTextView(){
        return indicatorTextView;
    }

    public void setIshowattention(boolean show){
        ishowattention = show;
    }

    public boolean getIshowattention(){
        return  ishowattention;
    }

    public void setDistance(int dis){
        distance = dis;
    }

    public int getDistance(){
        return  distance;
    }

    public void setUsed(long used){
        this.used = used;
    }

    public long getUsed(){
        return used;
    }



   public void requestLayout(){
    }
}
