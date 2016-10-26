package com.freeme.discovery.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by zwb on 2016/10/17.
 */

public class RadarScanDotView extends ImageView {

    private int orginX;
    private int orginY;
    private int radius;
    private boolean layouted;
    private float radian;

    public RadarScanDotView(Context context){
        this(context, null);
    }

    public RadarScanDotView(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public RadarScanDotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
    }

    public int getOrginX(){
        return orginX;
    }

    public int getOrginY(){
        return orginY;
    }

    public void setOrginX(int x){
        orginX = x;
    }

    public void setOrginY(int y){
        orginY = y;
    }

    public void setRadius(int r){
        radius = r;
    }

    public int getRadius(){
        return radius;
    }

    public void setLayouted(boolean layouted){
        this.layouted = layouted;
    }

    public boolean getLayouted(){
        return layouted;
    }

    public void setRadian(float radian){
        this.radian = radian;
    }

    public float getRadian(){
        return radian;
    }

    public void requestLayout(){

    }
}
