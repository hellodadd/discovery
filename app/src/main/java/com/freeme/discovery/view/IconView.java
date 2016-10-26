package com.freeme.discovery.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.freeme.discovery.R;

/**
 * Created by server on 16-9-28.
 */

public class IconView extends FrameLayout{

    private int x;
    private int y;

    private int radius;

    private Context mContext;

    private Paint mPaint;

    private ImageView circleAniImage;

    public IconView(Context context) {
        this(context, null, 0);
    }

    public IconView(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public IconView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);

        mContext = context;

        setWillNotDraw(false);

        mPaint = new Paint();
    }

    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

        /*
        Log.i("IconView", " ------- onDraw------");

        Drawable bg = mContext.getResources().
               getDrawable(R.drawable.discovery_radar_icon_bg);
        bg.setBounds(10,0, 180, 180);

        Drawable lcok = mContext.getResources().
                getDrawable(R.drawable.discovery_radar_icon_lock_bg);
        lcok.setBounds(10,0, 180, 180);

        //canvas.rotate(180);

        //bg.draw(canvas);
        //lcok.draw(canvas);





        //canvas.restoreToCount(1);

       // Bitmap bitmap = BitmapFactory.decodeResource(
                //mContext.getResources(), R.drawable.discovery_radar_icon_bg);
        //canvas.drawBitmap(bitmap, 0, 0, mPaint);
        */
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

    public void setRadius(int r){
        radius = r;
    }

    public int getRadius(){
        return radius;
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

    //public void requestLayout(){
    //}


}
