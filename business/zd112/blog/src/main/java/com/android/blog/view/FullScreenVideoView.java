package com.android.blog.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * created by jiangshide on 2019-09-15.
 * email:18311271399@163.com
 */
public class FullScreenVideoView extends VideoView {
  public FullScreenVideoView(Context context) {
    super(context);
  }

  public FullScreenVideoView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public FullScreenVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int width = getDefaultSize(0, widthMeasureSpec);
    int height = getDefaultSize(0, heightMeasureSpec);
    setMeasuredDimension(width, height);
  }
}
