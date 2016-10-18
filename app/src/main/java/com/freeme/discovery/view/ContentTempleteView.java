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

    private int sex;

    private float radian;

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

   /* public void requestLayout(){

    }*/
}
