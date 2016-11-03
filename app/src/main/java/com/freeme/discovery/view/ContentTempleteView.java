package com.freeme.discovery.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.freeme.discovery.R;
import com.freeme.discovery.utils.CommonUtils;

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

    private int attentionType;

    private String url;

    private String mainType;

    private ImageView video_h_t;
    private ImageView video_h_b;
    private ImageView video_v_l;
    private ImageView video_v_r;

    private ImageView indline;

    View animlayout;

    private Rect mClipBounds;


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

        if(animlayout != null && animlayout instanceof EnhanceView){
            if(CommonUtils.VIDEO_TYPE.equals(mainType)) {
                ((EnhanceView)animlayout).setClipBounds(new Rect(0, CommonUtils.dip2px(mContext, 10),
                        CommonUtils.dip2px(mContext, 120), CommonUtils.dip2px(mContext, 90)));
            }else if(CommonUtils.SHOP_TYPE.equals(mainType)){
                ((EnhanceView)animlayout).setClipBounds(new Rect(0, CommonUtils.dip2px(mContext, 10),
                        CommonUtils.dip2px(mContext, 180), CommonUtils.dip2px(mContext, 90)));
            }
        }

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

    public void setAttentionType(int type){
        attentionType = type;
    }

    public int getAttentionType(){
        return attentionType;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public String getUrl(){
        return url;
    }

    public void setMainType(String mainType){
        this.mainType = mainType;
    }

    public String getMainType(){
        return mainType;
    }

    public void setVideo_h_t(ImageView imageView){
        video_h_t = imageView;
    }

    public ImageView getVideo_h_t(){
        return video_h_t;
    }

    public void setVideo_h_b(ImageView imageView){
        video_h_b = imageView;
    }

    public ImageView getVideo_h_b(){
        return video_h_b;
    }

    public void setVideo_v_l(ImageView imageView){
        video_v_l = imageView;
    }

    public ImageView getVideo_v_l(){
        return video_v_l;
    }

    public void setVideo_v_r(ImageView imageView){
        video_v_r = imageView;
    }

    public ImageView getVideo_v_r(){
        return video_v_r;
    }

    public void setIndline(ImageView imageView){
        this.indline = imageView;
    }

    public ImageView getIndline(){
        return indline;
    }

    public void setAnimlayout(View view){
        this.animlayout = view;
    }





    //public void requestLayout(){
    //}
}
