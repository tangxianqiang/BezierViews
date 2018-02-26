package com.tang.bezierviews.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;

import com.tang.bezierviews.R;

/**
 * Created by Administrator on 2018/2/1.
 */

public class DragFoamView extends FrameLayout {
    private static final String TAG = "DragFoamView";
    /*当前的气泡即将爆炸*/
    private static final int FOAM_UP_GONE = 1;
    /*起始圆的中心点*/
    private PointF srcCircleCenter;
    /*一直移动的圆的中心点*/
    private PointF dstCircleCenter;
    /*起始圆的起点*/
    private PointF[] srcStart;
    /*起始圆的中点*/
    private PointF[] dstEnd;
    /*起始圆的半径*/
    private float srcRadius;
    /*移动圆的半径*/
    private float  dstRadius;
    /*item的高度*/
    private float height;
    /*item的宽度*/
    private float width;
    /*item移动的时候之前的一个点的x坐标*/
    private float lastX;
    /*item移动的时候之前的一个点的y坐标*/
    private float lastY;
    /*标记什么时候开始绘制贝塞尔*/
    private boolean startMoving;
    /*两点间的距离*/
    private float distance;
    /*中心点*/
    private PointF centerPoint;
    /*画贝塞尔的画笔*/
    private Paint mPaint;
    /*移动距离过大，脱离黏着最大距离*/
    private int maxDistance;
    /*是否超出最大距离*/
    private boolean beyondMax;
    /*气泡爆炸的画笔*/
    private Paint mFoamPaint;
    /*爆炸动画的图片资源*/
    private int foamRes[] = {R.drawable.foam_1,R.drawable.foam_2,R.drawable.foam_3,R.drawable.foam_4,R.drawable.foam_5};
    /*当前图片的index*/
    private int currentFoam;
    /*当前气泡状态*/
    private int foamStatus = 0;


    public DragFoamView(@NonNull Context context) {
        this(context,null);
    }

    public DragFoamView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DragFoamView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //获取到item的宽高，用于确定中心点
        height = getChildAt(0).getHeight();
        width = getChildAt(0).getWidth();
        if (startMoving || beyondMax) {//item在移动中，或者item超出了黏着距离任然在移动
            int l = (int) (lastX - width/2);
            int t = (int) (lastY - height/2);
            getChildAt(0).layout(l,t,(int)(l + width),(int)(t + height));
        }else{//item没有移动
            int l = 0;
            int t = 0;
            getChildAt(0).layout(l,t,(int) (l + width),(int)(t + height));
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startMoving = true;//准备移动
                return true;//直接消费
            case MotionEvent.ACTION_MOVE:
                lastX = ev.getX();
                lastY = ev.getY();
                requestLayout();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (beyondMax) {//超出黏着距离
                    //气泡爆炸
                    setBoomAnim();
                    foamStatus = FOAM_UP_GONE;
                    startMoving = false;
                    getChildAt(0).setVisibility(INVISIBLE);//爆炸前先让item消失
                    invalidate();
                }else{
                    //回弹并且恢复原状
                    final ValueAnimator mAnim = ValueAnimator.ofObject(new PointFEvaluator()
                            ,new PointF(dstCircleCenter.x,dstCircleCenter.y)
                            ,new PointF(srcCircleCenter.x,srcCircleCenter.y));
                    mAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            PointF curPoint = (PointF) animation.getAnimatedValue();
                            lastX = curPoint.x;
                            lastY = curPoint.y;
                            requestLayout();
                            invalidate();
                        }
                    });
                    mAnim.setInterpolator(new OvershootInterpolator(5));
                    mAnim.setDuration(200);
                    mAnim.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            //在动画结束时在重新更新视图，一切回到原来的状态
                            startMoving = false;
                            //给出回调
                            if (foamListener != null) {
                                foamListener.foamBounce();
                            }
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    mAnim.start();
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * PointF动画估值器
     */
    public class PointFEvaluator implements TypeEvaluator<PointF> {
        @Override
        public PointF evaluate(float fraction, PointF startPointF, PointF endPointF) {
            float x = startPointF.x + fraction * (endPointF.x - startPointF.x);
            float y = startPointF.y + fraction * (endPointF.y - startPointF.y);
            return new PointF(x, y);
        }
    }

    /**
     * 初始化
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        //贝塞尔的画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        //气泡消失动画的画笔
        mFoamPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFoamPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        //设置贝塞尔的四个控制点
        srcStart = new PointF[2];
        srcStart[0] = new PointF();
        srcStart[1] = new PointF();
        dstEnd = new PointF[2];
        dstEnd[0] = new PointF();
        dstEnd[1] = new PointF();
        //设置起点、终点
        srcCircleCenter = new PointF();
        dstCircleCenter = new PointF();
        centerPoint = new PointF();
        //允许onDraw函数
        setWillNotDraw(false);
        //初始化
        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.DragFoamView);
        maxDistance = (int) ta.getDimension(R.styleable.DragFoamView_max_dis,40);
        ta.recycle();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (startMoving) {//绘制贝塞尔曲线
            //起始圆的起点是item的中点
            srcCircleCenter.x = width / 2;
            srcCircleCenter.y = height / 2;
            //初始化半径
            dstRadius = Math.min(width / 2,height / 2);
            //移动圆的中点是移动点
            dstCircleCenter.x = (int) lastX;
            dstCircleCenter.y = (int) lastY;
            distance = (int) Math.sqrt((dstCircleCenter.x - srcCircleCenter.x) * (dstCircleCenter.x - srcCircleCenter.x) +
                    (dstCircleCenter.y - srcCircleCenter.y) * (dstCircleCenter.y - srcCircleCenter.y));
            //起始圆随着距离越来越小
            srcRadius = ((1 - distance / maxDistance) > 0.3f ? (1 - distance / maxDistance) : 0.3f) * dstRadius;
            if (distance > maxDistance) {
                beyondMax = true;
                return;
            }
            beyondMax = false;
            //计算贝塞尔的控制点
            setPoint();
            canvas.drawCircle(srcCircleCenter.x,srcCircleCenter.y,srcRadius,mPaint);
            Path path = new Path();
            path.moveTo(srcStart[0].x,srcStart[0].y);
            path.quadTo(centerPoint.x,centerPoint.y,dstEnd[0].x,dstEnd[0].y);
            path.lineTo(dstEnd[1].x,dstEnd[1].y);
            path.quadTo(centerPoint.x,centerPoint.y,srcStart[1].x,srcStart[1].y);
            path.close();
            canvas.drawPath(path,mPaint);
        }
        //手指抬起，气泡炸开
        if (foamStatus == FOAM_UP_GONE) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), foamRes[currentFoam]);
            Rect r = new Rect();
            r.left = (int) (lastX - Math.min(width,height));
            r.top = (int) (lastY - Math.min(width,height));
            r.right = (int) (r.left + 2 * Math.min(width,height));
            r.bottom = (int) (r.top + 2 * Math.min(width,height));
            canvas.drawBitmap(bitmap, null, r, mFoamPaint);
        }
    }

    /**
     * 计算控制点的位置
     */
    private void setPoint(){
        centerPoint.x = (srcCircleCenter.x + dstCircleCenter.x) / 2;
        centerPoint.y = (srcCircleCenter.y + dstCircleCenter.y) / 2;

        float sin = (dstCircleCenter.y - srcCircleCenter.y) / distance;
        float cos = (dstCircleCenter.x - srcCircleCenter.x) / distance;

        srcStart[0].x = (int) (srcCircleCenter.x - sin * srcRadius);
        srcStart[0].y = (int) (srcCircleCenter.y + cos * srcRadius);
        srcStart[1].x = (int) (srcCircleCenter.x + sin * srcRadius);
        srcStart[1].y = (int) (srcCircleCenter.y - cos * srcRadius);

        dstEnd[0].x = (int) (dstCircleCenter.x - sin * dstRadius);
        dstEnd[0].y = (int) (dstCircleCenter.y + cos * dstRadius);
        dstEnd[1].x = (int) (dstCircleCenter.x + sin * dstRadius);
        dstEnd[1].y = (int) (dstCircleCenter.y - cos * dstRadius);
    }

    /**
     * 设置气泡消失的动画
     */
    private void setBoomAnim() {
        //做一个int型属性动画，从0开始，到气泡爆炸图片数组个数结束
        ValueAnimator anim = ValueAnimator.ofInt(0, foamRes.length);
        anim.setInterpolator(new LinearInterpolator());
        anim.setDuration(500);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //拿到当前的值并重绘
                currentFoam = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //重置所有标记
                foamStatus = 0;
                beyondMax = false;
                getChildAt(0).setVisibility(VISIBLE);
                setVisibility(GONE);
                //给出回调
                if (foamListener != null) {
                    foamListener.foamBoom();
                }
            }
        });
        anim.start();
    }
    public interface OnFoamActionListener{
        void foamBoom();
        void foamBounce();
    }
    private OnFoamActionListener foamListener;
    public void setFoamListener(OnFoamActionListener foamListener){
        this.foamListener = foamListener;
    }

}
