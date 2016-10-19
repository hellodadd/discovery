package com.freeme.discovery.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.freeme.discovery.R;

/**
 * Created by server on 16-10-18.
 */

public class GuideView extends FrameLayout {


    private ImageView mGuideMale;
    private ImageView mGuideFemale;
    private boolean mGuideMalePress = false;
    private boolean mGuideFemalePress = false;
    private TextView mGuideStart;



    public GuideView(Context context){
        this(context, null);
    }

    public GuideView(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public GuideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void onFinishInflate(){
        super.onFinishInflate();
        mGuideFemale = (ImageView)findViewById(R.id.guide_female);
        mGuideMale = (ImageView)findViewById(R.id.guide_male);

        mGuideFemale.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mGuideFemalePress) {
                    mGuideFemale.setAlpha(0.35f);
                    mGuideFemalePress = true;
                    if(mGuideMalePress){
                        mGuideMale.setAlpha(1.0f);
                        mGuideMalePress = false;
                    }
                }else{
                    mGuideFemale.setAlpha(1.0f);
                    mGuideFemalePress = false;
                }
            }
        });

        mGuideMale.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mGuideMalePress) {
                    mGuideMale.setAlpha(0.35f);
                    mGuideMalePress = true;
                    if(mGuideFemalePress){
                        mGuideFemale.setAlpha(1.0f);
                        mGuideFemalePress = false;
                    }
                }else{
                    mGuideMale.setAlpha(1.0f);
                    mGuideMalePress = false;
                }
            }
        });

        mGuideStart = (TextView)findViewById(R.id.discovery_rule_start);
        mGuideStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                  
            }
        });
    }

}
