package com.android.widget.anim;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import com.android.utils.AppUtil;
import com.android.widget.R;

/**
 * created by jiangshide on 2019-09-07.
 * email:18311271399@163.com
 */
public class Anim {

  public static final Interpolator FAST_OUT_SLOW_IN_INTERPOLATOR = new FastOutSlowInInterpolator();
  public static final Interpolator FAST_OUT_LINEAR_IN_INTERPOLATOR =
      new FastOutLinearInInterpolator();
  public static final Interpolator LINEAR_OUT_SLOW_IN_INTERPOLATOR =
      new LinearOutSlowInInterpolator();
  public static final Interpolator DECELERATE_INTERPOLATOR = new DecelerateInterpolator();

  static final Interpolator LINEAR_INTERPOLATOR = new LinearInterpolator();

  public static void shakeView(View view) {
    Animation translateAnimation = new TranslateAnimation(-10, 10, 0, 0);
    translateAnimation.setDuration(50);//每次时间
    translateAnimation.setRepeatCount(10);//重复次数
    translateAnimation.setRepeatMode(Animation.REVERSE);
    view.startAnimation(translateAnimation);
  }

  public static float lerp(float startValue, float endValue, float fraction) {
    return startValue + (fraction * (endValue - startValue));
  }

  public static int lerp(int startValue, int endValue, float fraction) {
    return startValue + Math.round(fraction * (endValue - startValue));
  }

  static class AnimationListenerAdapter implements Animation.AnimationListener {
    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }
  }

  public static void anim(View view) {
    anim(view, R.anim.circulate);
  }

  public static void anim(View view, int anim) {
    Animation animation = AnimationUtils.loadAnimation(AppUtil.getApplicationContext(), anim);
    LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
    animation.setInterpolator(lin);
    view.startAnimation(animation);
  }
}
