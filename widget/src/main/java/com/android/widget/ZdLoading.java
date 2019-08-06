package com.android.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import com.android.utils.LogUtil;

import java.util.Map;

/**
 * created by jiangshide on 2019-07-29.
 * email:18311271399@163.com
 */
public class ZdLoading extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private enum LoadingState {
        DOWN, UP, FREE
    }

    private LoadingState loadingState = LoadingState.DOWN;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private Paint paint;
    private Path path;

    private int ballColor;
    private int lineColor;
    private int lineWidth;
    private int strokeWidth;
    private float downDistance;
    private float upDistance;
    private float freeDownDistance;
    private ValueAnimator downControl;
    private ValueAnimator upControl;
    private ValueAnimator freeDownControl;
    private AnimatorSet animatorSet;
    private boolean isRunning;
    private boolean isAnimationShowing;

    public ZdLoading(Context context) {
        this(context, null);
    }

    public ZdLoading(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZdLoading(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //进行数据初始化
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        //自定义属性
        initAttrs(context, attributeSet);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(strokeWidth);
        path = new Path();
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        //初始化动画控制
        initControl();
    }

    private void initControl() {
        downControl = ValueAnimator.ofFloat(0, 1);
        downControl.setDuration(500);
        downControl.setInterpolator(new ShockInterpolator());
        downControl.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                downDistance = 50 * (float) animation.getAnimatedValue();
            }
        });
        downControl.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                loadingState = LoadingState.DOWN;
                isAnimationShowing = true;
            }
        });

        upControl = ValueAnimator.ofFloat(0, 1);
        upControl.setDuration(500);
        upControl.setInterpolator(new DecelerateInterpolator());
        upControl.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                upDistance = 50 * (float) animation.getAnimatedValue();
                if (upDistance >= 50 && !freeDownControl.isStarted() && !freeDownControl.isRunning()) {
                    freeDownControl.start();
                }
            }
        });
        upControl.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                loadingState = LoadingState.UP;
            }
        });

        freeDownControl = ValueAnimator.ofFloat(0, (float) (2 * Math.sqrt(10)));
        freeDownControl.setDuration(600);
        freeDownControl.setInterpolator(new DecelerateInterpolator());
        freeDownControl.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float t = (float) animation.getAnimatedValue();
                freeDownDistance = (float) (10 * Math.sqrt(10)*t - 5 * t * t);
            }
        });
        freeDownControl.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimationShowing = false;
                startAllAnimation();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                loadingState = LoadingState.FREE;
            }
        });

        animatorSet = new AnimatorSet();
        animatorSet.play(downControl).before(upControl);
    }

    private void initAttrs(Context context, AttributeSet attributeSet) {
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ZdLoading);
        ballColor = typedArray.getColor(R.styleable.ZdLoading_ball_color, Color.BLUE);
        lineColor = typedArray.getColor(R.styleable.ZdLoading_zdline_color, Color.BLUE);
        lineWidth = typedArray.getDimensionPixelOffset(R.styleable.ZdLoading_line_with, 200);
        strokeWidth = typedArray.getDimensionPixelOffset(R.styleable.ZdLoading_stroke_with, 2);
        typedArray.recycle();//Google建议用完就回收
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isRunning = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //销毁回收资源
        isRunning = false;
    }

    @Override
    public void run() {
        while (isRunning) {
            drawView();//待优化～需要时去绘制
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                LogUtil.e(e);
            }
        }
    }

    private void drawView() {
        try {
            if (surfaceHolder != null) {
                //获取canvas
                canvas = surfaceHolder.lockCanvas();//缓存机制?
                //清屏
                canvas.drawColor(Color.WHITE);
                //做绘制处理
                paint.setColor(lineColor);
                path.reset();
                path.moveTo(getWidth() / 2 - lineWidth / 2, getHeight() / 2);
                if (loadingState == LoadingState.DOWN) {
                    //下降,小球还在绳子上
                    path.rQuadTo(lineWidth / 2, 2 * downDistance, lineWidth, 0);
                    paint.setColor(lineColor);
                    paint.setStyle(Paint.Style.STROKE);
                    canvas.drawPath(path, paint);

                    //绘制小球
                    paint.setColor(ballColor);
                    paint.setStyle(Paint.Style.FILL);
                    canvas.drawCircle(getWidth()/2,getHeight()/2+downDistance-10-strokeWidth/2,10,paint);
                } else {
                    //上升 自由落体
                    path.rQuadTo(lineWidth/2,2*(50-upDistance),lineWidth,0);
                    paint.setColor(lineColor);
                    paint.setStyle(Paint.Style.STROKE);
                    canvas.drawPath(path,paint);

                    //小球绘制
                    paint.setColor(ballColor);
                    paint.setStyle(Paint.Style.FILL);
                    if(loadingState == LoadingState.FREE){
                        //自由落体
                        canvas.drawCircle(getWidth()/2,-freeDownDistance-10-strokeWidth/2,10,paint);
                    }else{
                        //在绳子上
                        canvas.drawCircle(getWidth()/2,getHeight()/2+(50-upDistance)-10-strokeWidth/2,10,paint);
                    }
                }
                paint.setColor(ballColor);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(getWidth() / 2 - lineWidth / 2, getHeight() / 2, 10, paint);
                canvas.drawCircle(getWidth() / 2 + lineWidth / 2, getHeight() / 2, 10, paint);
            }
        } catch (Exception e) {
            LogUtil.e(e);
        } finally {
            if (canvas != null) {
                surfaceHolder.unlockCanvasAndPost(canvas);//提交刷新
            }
        }
    }

    public void startAllAnimation() {
        if (isAnimationShowing) {
            return;
        }
        if (animatorSet.isRunning()) {
            animatorSet.end();
            animatorSet.cancel();
        }
        //开启动画组合
        animatorSet.start();
    }

    class ShockInterpolator implements Interpolator {

        @Override
        public float getInterpolation(float input) {
            return (float)(1-Math.exp(-3*input)* Math.cos(10*input));
        }
    }
}
