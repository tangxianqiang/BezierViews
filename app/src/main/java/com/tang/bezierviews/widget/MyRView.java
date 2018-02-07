package com.tang.bezierviews.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2018/2/7.
 */

public class MyRView extends RecyclerView {
    private static final String TAG = "DragFoamView";
    public MyRView(Context context) {
        this(context,null);
    }

    public MyRView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyRView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "onTouchEvent: rv down");

                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "onTouchEvent: rv move");

                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "onTouchEvent: rv up");
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "dispatchTouchEvent: rv down");

                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "dispatchTouchEvent: rv move");

                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "dispatchTouchEvent: rv up");

                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "onInterceptTouchEvent: rv down");

                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "onInterceptTouchEvent: rv move");

                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "onInterceptTouchEvent: rv up");

                break;
        }
        return false;
    }
}
