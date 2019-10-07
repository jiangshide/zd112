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
import java.io.IOException;
import java.io.InputStream;

/**
 * 读取大长图:eg
 *
 * ZdBigImg zdBigImg = findViewById(R.id.zdBigImg);
 * InputStream inputStream = null;
 * try {
 * inputStream = getAssets().open("big.jpg");
 * zdBigImg.setImage(inputStream);
 * } catch (IOException e) {
 * e.printStackTrace();
 * }
 *
 * created by jiangshide on 2019-10-07.
 * email:18311271399@163.com
 */
public class ZdBigImg extends View
    implements GestureDetector.OnGestureListener, View.OnTouchListener {

  private final Rect mRect;
  private final BitmapFactory.Options mOptions;
  private final GestureDetector mGestureDetector;
  private final Scroller mScroller;
  private int mImageWidth;
  private int mImageHeight;
  private BitmapRegionDecoder mDecoder;
  private int mViewWidth;
  private int mViewHeight;
  private float mScale;
  private Bitmap mBitmap;

  public ZdBigImg(Context context) {
    this(context, null);
  }

  public ZdBigImg(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public ZdBigImg(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    mRect = new Rect();
    mOptions = new BitmapFactory.Options();
    mGestureDetector = new GestureDetector(context, this);
    mScroller = new Scroller(context);
    setOnTouchListener(this);
  }

  public void setImage(InputStream inputStream) {
    mOptions.inJustDecodeBounds = true;
    BitmapFactory.decodeStream(inputStream, null, mOptions);
    mImageWidth = mOptions.outWidth;
    mImageHeight = mOptions.outHeight;

    mOptions.inMutable = true;
    mOptions.inPreferredConfig = Bitmap.Config.RGB_565;

    mOptions.inJustDecodeBounds = false;

    try {
      mDecoder = BitmapRegionDecoder.newInstance(inputStream, false);
    } catch (IOException e) {
      e.printStackTrace();
    }
    requestLayout();
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    mViewWidth = getMeasuredWidth();
    mViewHeight = getMeasuredHeight();

    mRect.left = 0;
    mRect.top = 0;
    mRect.right = mImageWidth;

    mScale = mViewWidth / (float) mImageWidth;
    mRect.bottom = (int) (mViewHeight / mScale);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if (mDecoder == null) {
      return;
    }
    //        mOptions.inMutable = true;

    mOptions.inBitmap = mBitmap;
    mBitmap = mDecoder.decodeRegion(mRect, mOptions);
    Matrix matrix = new Matrix();
    matrix.setScale(mScale, mScale);
    canvas.drawBitmap(mBitmap, matrix, null);
  }

  @Override
  public boolean onTouch(View view, MotionEvent motionEvent) {
    return mGestureDetector.onTouchEvent(motionEvent);
  }

  @Override
  public boolean onDown(MotionEvent motionEvent) {
    if (!mScroller.isFinished()) {
      mScroller.forceFinished(true);
    }
    return true;
  }

  @Override
  public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float distanceX,
      float distanceY) {
    mRect.offset(0, (int) distanceY);
    if (mRect.bottom > mImageHeight) {
      mRect.bottom = mImageHeight;
      mRect.top = mImageHeight - (int) (mImageHeight / mScale);
    }
    if (mRect.top < 0) {
      mRect.top = 0;
      mRect.bottom = (int) (mViewHeight / mScale);
    }
    invalidate();
    return false;
  }

  @Override
  public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float velocityX,
      float velocityY) {
    mScroller.fling(0, mRect.top, 0, (int) -velocityY, 0, 0, 0,
        mImageHeight - (int) (mViewHeight / mScale));
    return false;
  }

  @Override
  public void computeScroll() {
    super.computeScroll();
    if (mScroller.isFinished()) {
      return;
    }
    if (mScroller.computeScrollOffset()) {
      mRect.top = mScroller.getCurrY();
      mRect.bottom = mRect.top + (int) (mViewHeight / mScale);
      invalidate();
    }
  }

  @Override
  public void onShowPress(MotionEvent motionEvent) {

  }

  @Override
  public boolean onSingleTapUp(MotionEvent motionEvent) {
    return false;
  }

  @Override
  public void onLongPress(MotionEvent motionEvent) {

  }
}
