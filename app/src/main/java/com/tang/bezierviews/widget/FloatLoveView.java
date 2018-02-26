package com.tang.bezierviews.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tang.bezierviews.R;

/**
 * 贝塞尔曲线的漂浮桃心视图，常用于直播点赞
 */

public class FloatLoveView extends RelativeLayout {
    /*上下文环境*/
    private Context context;
    /*桃心背景图*/
    private Drawable[] drawables;
    /*动画差值器，桃心可能匀速做贝塞尔曲线运动，也可能加速*/
    private Interpolator[] interpolators;
    /*桃心的宽度*/
    private int itemWidth;
    /*桃心的高度*/
    private int itemHeight;
    //桃心漂浮的活动范围宽度
    private int width;
    //桃心漂浮的活动范围高度
    private int height;
    public FloatLoveView(Context context) {
        this(context,null);
    }

    public FloatLoveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FloatLoveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr);
    }

    /**
     * 初始化
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;
        // 初始化显示的图片
        drawables = new Drawable[3];
        drawables[0] = ContextCompat.getDrawable(context,R.drawable.love_blue);
        drawables[1] = ContextCompat.getDrawable(context,R.drawable.love_pink);
        drawables[2] = ContextCompat.getDrawable(context,R.drawable.love_red);
        // 初始化插补器
        interpolators = new Interpolator[4];
        interpolators[0] = new LinearInterpolator();// 线性
        interpolators[1] = new AccelerateInterpolator();// 加速
        interpolators[2] = new DecelerateInterpolator();// 减速
        interpolators[3] = new AccelerateDecelerateInterpolator();// 先加速后减速
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //如果整个用于桃心漂浮的视图没有宽高，那么就给一个默认的宽高
        if (height == 0) {
            height = MeasureSpec.getSize(heightMeasureSpec);
        }
        if (width == 0) {
            width = MeasureSpec.getSize(widthMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        itemWidth = findViewById(R.id.id_love_item).getWidth();
        itemHeight = findViewById(R.id.id_love_item).getHeight();
        height = (int) findViewById(R.id.id_love_item).getY();
    }

    /**
     * 贝塞尔动画估值器，通过该估值器可以设置动画的轨迹
     */
    public class BezierEvaluator implements TypeEvaluator<PointF> {
        //控制点1
        private PointF mControlP1;
        //控制点2
        private PointF mControlP2;

        public BezierEvaluator(PointF controlP1, PointF controlP2) {
            this.mControlP1 = controlP1;
            this.mControlP2 = controlP2;
        }

        @Override
        public PointF evaluate(float time, PointF start, PointF end) {

            float timeLeft = 1.0f - time;
            PointF point = new PointF();
            //两个控制点的三阶贝塞尔数学表达式
            point.x = timeLeft * timeLeft * timeLeft * (start.x) + 3 * timeLeft * timeLeft * time *
                    (mControlP1.x) + 3 * timeLeft * time *
                    time * (mControlP2.x) + time * time * time * (end.x);
            //两个控制点的三阶贝塞尔数学表达式
            point.y = timeLeft * timeLeft * timeLeft * (start.y) + 3 * timeLeft * timeLeft * time *
                    (mControlP1.y) + 3 * timeLeft * time *
                    time * (mControlP2.y) + time * time * time * (end.y);
            return point;
        }
    }

    /**
     * 设置桃心开始的动画
     * @param target
     * @return
     */
    private AnimatorSet getStartAnimator(final View target) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, View.ALPHA, 0.2f, 1f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, View.SCALE_X, 0.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, View.SCALE_Y, 0.2f, 1f);
        AnimatorSet enter = new AnimatorSet();
        enter.setTarget(target);
        enter.setInterpolator(new LinearInterpolator());
        enter.setDuration(500).playTogether(alpha, scaleX, scaleY);
        return enter;
    }

    /**
     * 设置贝塞尔曲线动画
     * @param target
     * @return
     */
    private ValueAnimator getBezierValueAnimator(final View target) {
        // 初始化贝塞尔估值器
        BezierEvaluator evaluator = new BezierEvaluator(randomPointF(2), randomPointF(1));
        //设置贝塞尔动画，被设置动画的起点和终点
        ValueAnimator animator = ValueAnimator.ofObject(evaluator, new PointF(width / 2 - itemWidth /2, height - itemHeight), new PointF((float) (Math.random() * height), 0));
        animator.setTarget(target);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                // 这里获取到贝塞尔曲线计算出来的的x y值 赋值给view 这样就能让爱心随着曲线走啦
                PointF pointF = (PointF) valueAnimator.getAnimatedValue();
                target.setX(pointF.x);
                target.setY(pointF.y);
                // alpha动画
                target.setAlpha(1 - valueAnimator.getAnimatedFraction());
            }
        });

        animator.setDuration(5000);
        return animator;
    }

    /**
     * 随机贝塞尔曲线中间的控制点
     * @param scale
     * @return
     */
    private PointF randomPointF(float scale){
        PointF pointF = new PointF();
        pointF.x = (float) (Math.random() * width);
        pointF.y = (float) (Math.random() * height) / scale;//scale如果大于1可以保证达到扩散效果
        return pointF;
    }

    /**
     * 手动添加一个桃心
     */
    public void addHeart() {
        final ImageView imageView = new ImageView(getContext());
        imageView.setImageDrawable(drawables[(int)(Math.random() * 3)]);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(itemWidth, itemHeight);
        lp.addRule(CENTER_HORIZONTAL, TRUE);
        lp.addRule(ALIGN_PARENT_BOTTOM, TRUE);
        addView(imageView,lp);
        AnimatorSet finalSet = new AnimatorSet();
        //开始动画
        AnimatorSet enterAnimatorSet = getStartAnimator(imageView);
        //贝塞尔曲线路径动画
        ValueAnimator bezierValueAnimator = getBezierValueAnimator(imageView);
        finalSet.playSequentially(enterAnimatorSet, bezierValueAnimator);
        finalSet.setInterpolator(interpolators[(int) (Math.random() * 4)]);
        finalSet.setTarget(imageView);
        finalSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                removeView((imageView));//删除爱心
            }
        });
        finalSet.start();
    }

    /**
     * 获取屏幕宽度
     * @return
     */
    private int getScreenWidth(){
        DisplayMetrics display = context.getResources().getDisplayMetrics();
        return display.widthPixels;
    }
    /**
     * 获取屏幕高度
     * @return
     */
    private int getScreenHeight(){
        DisplayMetrics display = context.getResources().getDisplayMetrics();
        return display.heightPixels;
    }

}
