package com.freeme.discovery.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by server on 16-10-18.
 */

public class GuideView extends FrameLayout {


    public GuideView(Context context){
        this(context, null);
    }

    public GuideView(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public GuideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
