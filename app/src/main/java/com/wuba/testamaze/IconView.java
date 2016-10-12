package com.wuba.testamaze;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by server on 16-9-28.
 */

public class IconView extends FrameLayout{

    private int x;
    private int y;

    public IconView(Context context) {
        this(context, null, 0);
    }

    public IconView(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public IconView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
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
}
