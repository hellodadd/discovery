package com.freeme.discovery.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import com.freeme.discovery.R;

/**
 * Created by server on 16-9-28.
 */

public class IconView extends FrameLayout{

    private int x;
    private int y;

    private int radius;

    private Context mContext;

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
    }

    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

        Log.i("IconView", " ------- onDraw------");

        Drawable drawable = mContext.getResources().
                getDrawable(R.drawable.discovery_radar_icon_bg);
        //drawable.setBounds(400,400, 600, 600);

        drawable.draw(canvas);

        canvas.restoreToCount(1);
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

    public void requestLayout(){

    }
}
