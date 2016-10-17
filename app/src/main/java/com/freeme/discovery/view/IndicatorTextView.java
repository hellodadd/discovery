package com.freeme.discovery.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by zwb on 2016/10/17.
 */

public class IndicatorTextView extends TextView{

    private final Paint mPaint = new Paint();
    private int mUnderlineHeight = 0;

    public IndicatorTextView(Context context){
        this(context, null, 0);
    }
    public IndicatorTextView(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }
    public IndicatorTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mUnderlineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,1,
                context.getResources().getDisplayMetrics());
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setColor(getTextColors().getDefaultColor());
        canvas.drawRect(0, getHeight() - mUnderlineHeight, getWidth(), getHeight(), mPaint);
    }


    }
