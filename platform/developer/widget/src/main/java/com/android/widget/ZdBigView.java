package com.android.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;
import androidx.annotation.Nullable;
import com.android.utils.LogUtil;
import java.io.IOException;
import java.io.InputStream;

/**
 * created by jiangshide on 2019-09-11.
 * email:18311271399@163.com
 */
public class ZdBigView extends View implements GestureDetector.OnGestureListener,View.OnTouchListener{

  private Rect mRect;
  private BitmapFactory.Options mOptions;
  private GestureDetector mGestureDetector;
  private Scroller mScroll;
  private int mImageWidth;
  private int mImageHeight;
  private BitmapRegionDecoder mDecoder;
  private int mViewWidth;
  private int mViewHeight;
  private float mScale;
  private Bitmap mBitmap;

  public ZdBigView(Context context) {
    super(context, null);
  }

  public ZdBigView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs, 0);
  }

  public ZdBigView(Context context, @Nullable AttributeSet attrs,
      int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    mRect = new Rect();
    LogUtil.e("-----------ZdBigView~context:",context);
    mOptions = new BitmapFactory.Options();
    mGestureDetector = new GestureDetector(context,this);
    mScroll = new Scroller(context);
    setOnTouchListener(this);
  }

  public void setImage(InputStream inputStream){
    mOptions.inJustDecodeBounds = true;
    BitmapFactory.decodeStream(inputStream,null,mOptions);
    mImageWidth = mOptions.outWidth;
    mImageHeight = mOptions.outHeight;

    mOptions.inMutable = true;//开启复用
    mOptions.inPreferredConfig = Bitmap.Config.RGB_565;

    mOptions.inJustDecodeBounds = false;

    //创建一个区域解码器
    try {
      mDecoder = BitmapRegionDecoder.newInstance(inputStream, false);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 开始测量
   * @param widthMeasureSpec
   * @param heightMeasureSpec
   */
  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    LogUtil.e("-----------onMeasure~widthMeasureSpec:",widthMeasureSpec," | heightMeasureSpec:",heightMeasureSpec);
    mViewWidth = getMeasuredWidth();
    mViewHeight = getMeasuredHeight();

    //确定加载图片的区域
    mRect.left = 0;
    mRect.top = 0;
    mRect.right = mImageWidth;
    //得到图片宽度,又知道view的宽度,计算缩放因子
    mScale = mViewWidth/(float)mImageWidth;
    mRect.bottom = (int)(mViewHeight/mScale);
  }

  /**
   * 开始画布
   * @param canvas
   */
  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    //解码器没拿到,表示没有设置过要显示的图片
    if(mDecoder == null){
      return;
    }
    //复用内存:复用的bitmap必须跟即将解码的bitmap尺寸一致
    mOptions.inBitmap = mBitmap;
    //指定解码区域
    mBitmap = mDecoder.decodeRegion(mRect,mOptions);
    //得到一个矩阵进行缩放，相当于得到view的大小
    Matrix matrix = new Matrix();
    matrix.setScale(mScale,mScale);
    canvas.drawBitmap(mBitmap,matrix,null);
  }

  /**
   * 处理点击事件
   * @param v
   * @param event
   * @return
   */
  @Override public boolean onTouch(View v, MotionEvent event) {
    //直接将事件交给手势事件处理
    return mGestureDetector.onTouchEvent(event);
  }

  /**
   *手按下去
   * @param e
   * @return
   */
  @Override public boolean onDown(MotionEvent e) {
    //如果移动没有停止，就强行停止
    if(!mScroll.isFinished()){
      mScroll.forceFinished(true);
    }
    //继续接收后续事件
    return true;
  }

  @Override public void onShowPress(MotionEvent e) {

  }

  @Override public boolean onSingleTapUp(MotionEvent e) {
    return false;
  }

  /**
   * 处理滑动事件
   * e1:开始事件，手指按下去开始获取坐标
   * e2:获取当前事件坐标
   * x,y 轴移动的距离
   * @param e1
   * @param e2
   * @param distanceX
   * @param distanceY
   * @return
   */
  @Override
  public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
    //上下移动的时候，mRect需要改变显示区域
    mRect.offset(0,(int)distanceY);
    //移动时，处理到达顶部和底部的情况
    if(mRect.bottom > mImageHeight){
      mRect.bottom=mImageHeight;
      mRect.top=mImageHeight-(int)(mViewHeight/mScale);
    }
    if(mRect.top<0){
      mRect.top=0;
      mRect.bottom = (int)(mViewHeight/mScale);
    }
    invalidate();
    return false;
  }

  /**
   * 处理惯性问题
   * @param e1
   * @param e2
   * @param velocityX
   * @param velocityY
   * @return
   */
  @Override
  public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
    mScroll.fling(0,mRect.top,0,(int)-velocityY,0,0,0,mImageHeight-(int)(mViewHeight/mScale));
    return false;
  }

  /**
   * 处理计算结果
   */
  @Override public void computeScroll() {
    super.computeScroll();
    if(mScroll.isFinished()){
      return;
    }
    //返回为true的时候,滑动还没有结束
    if(mScroll.computeScrollOffset()){
      mRect.top = mScroll.getCurrY();
      mRect.bottom = mRect.top+(int)(mViewHeight/mScale);
      invalidate();
    }
  }

  @Override public void onLongPress(MotionEvent e) {

  }
}
