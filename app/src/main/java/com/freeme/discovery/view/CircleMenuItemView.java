package com.freeme.discovery.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by server on 16-10-25.
 */

public class CircleMenuItemView extends TextView{


    private String categoryName;
    private int categoryId;
    private int categoryPid;
    private String mainType;

    public CircleMenuItemView(Context context){
        this(context, null, 0);
    }

    public CircleMenuItemView(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public CircleMenuItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setCategoryId(int id){
        categoryId = id;
    }

    public int getCategoryId(){
        return categoryId;
    }

    public void setCategoryPid(int pid){
        categoryPid = pid;
    }

    public int getCategoryPid(){
        return categoryPid;
    }

    public void setMainType(String mainType){
        this.mainType = mainType;
    }

    public String getMainType(){
        return mainType;
    }

    public interface onRequstItemInfoListen{
        void onRequstItemInfo();
    }
}
