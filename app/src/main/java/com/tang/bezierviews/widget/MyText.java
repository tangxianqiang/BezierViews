package com.tang.bezierviews.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Administrator on 2018/2/1.
 */

public class MyText extends TextView {
    private static final String TAG = "DragFoamView";
    public MyText(Context context) {
        this(context,null);
    }

    public MyText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "onTouchEvent: MyText down");

                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "onTouchEvent: MyText move");

                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "onTouchEvent: MyText up");
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "dispatchTouchEvent: MyText down");

                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "dispatchTouchEvent: MyText move");

                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "dispatchTouchEvent: MyText up");

                break;
        }
        return super.dispatchTouchEvent(event);
    }
}
