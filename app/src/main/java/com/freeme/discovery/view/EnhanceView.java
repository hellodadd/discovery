package com.freeme.discovery.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

import com.freeme.discovery.utils.CommonUtils;

/**
 * Created by server on 16-11-3.
 */

public class EnhanceView extends RelativeLayout{


    private Context mContext;

    private Rect mClipBounds;


    public EnhanceView(Context context) {
        this(context, null);
    }


    public EnhanceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EnhanceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
    }

    @Override
    public void draw(Canvas canvas) {
        // Clip bounds implementation for JB_MR1 and older
        // Does not save the canvas, because the View implementation also doesn't...
        if (mClipBounds != null) {
            canvas.clipRect(mClipBounds);
        }
        super.draw(canvas);
    }


    @Override
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void setClipBounds(Rect clipBounds) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            super.setClipBounds(clipBounds);
            return;
        }

        if (clipBounds != null) {
            if (clipBounds.equals(mClipBounds)) {
                return;
            }
            if (mClipBounds == null) {
                invalidate();
                mClipBounds = new Rect(clipBounds);
            } else {
                invalidate(Math.min(mClipBounds.left, clipBounds.left),
                        Math.min(mClipBounds.top, clipBounds.top),
                        Math.max(mClipBounds.right, clipBounds.right),
                        Math.max(mClipBounds.bottom, clipBounds.bottom));
                mClipBounds.set(clipBounds);
                requestLayout();
            }
        } else {
            if (mClipBounds != null) {
                invalidate();
                mClipBounds = null;
            }
        }
    }

    @Override
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public Rect getClipBounds() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return super.getClipBounds();
        } else {
            return (mClipBounds != null) ? new Rect(mClipBounds) : null;
        }
    }
}
