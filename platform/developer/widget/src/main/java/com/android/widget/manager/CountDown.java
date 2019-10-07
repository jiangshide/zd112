package com.android.widget.manager;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import com.android.widget.R;
import com.android.widget.ZdDialog;

/**
 * created by jiangshide on 2019-08-27.
 * email:18311271399@163.com
 * todo:temp
 */
public class CountDown<T> {

    private String TAG = CountDown.class.getCanonicalName();

    private static class CountDownHolder {
        private static CountDown instance = new CountDown();
    }

    public static CountDown getInstance() {
        return CountDownHolder.instance;
    }

    private CountDownTimer mCountDownTimer;
    private T mOnCountDownListener;
    private final long SECOND = 1000;
    private long mCountTime = 30;//second
    private long mInterval = SECOND;
    private boolean mIsLoop;

    private ZdDialog mDialogView;

    public CountDown setListener(T listener) {
        this.mOnCountDownListener = listener;
        return this;
    }

    public CountDown setTime(long secondTime) {
        this.mCountTime = secondTime;
        return this;
    }

    public CountDown setInterval(long interval) {
        this.mInterval = interval;
        return this;
    }

    public CountDown setLoop(boolean isLoop) {
        this.mIsLoop = isLoop;
        return this;
    }

    public CountDown create() {
        return create(this.mCountTime);
    }

    public CountDown create(long secondTime) {
        return create(secondTime * SECOND, this.mInterval);
    }

    public CountDown create(long millisInFuture, long countDownInterval) {
        this.mCountTime = millisInFuture;
        this.mInterval = countDownInterval;
        cancelTime();
        mCountDownTimer = new CountDownTimer(millisInFuture, countDownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (mOnCountDownListener != null) {
                    if (mOnCountDownListener instanceof OnCountDownTickListener) {
                        ((OnCountDownTickListener) mOnCountDownListener).onTick(millisUntilFinished);
                    }
                }
            }

            @Override
            public void onFinish() {
                if (mOnCountDownListener != null) {
                    ((OnCountDownListener) mOnCountDownListener).onFinish();
                }
                if (mIsLoop) {
                    create(mCountTime, mInterval);
                }
            }
        }.start();
        return this;
    }

    public void cancelTime() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        mCountDownTimer = null;
    }

    public void cancel() {
//        mIsLoop = false;
        cancelTime();
        dismiss();
    }

    /**
     * todo:temp
     *
     * @param context:为activity
     * @param listener
     * @return
     */
    public CountDown createView(Context context, int layout, ZdDialog.DialogViewListener listener) {
        return createView(context, LayoutInflater.from(context).inflate(layout, null), listener, null);
    }

    /**
     * todo:temp
     *
     * @param context:为activity
     * @param listener
     * @return
     */
    public CountDown createView(Context context, int layout, ZdDialog.DialogViewListener listener, ZdDialog.DialogOnClickListener dialogOnClickListener) {
        return createView(context, LayoutInflater.from(context).inflate(layout, null), listener, dialogOnClickListener);
    }


    /**
     * todo:temp
     *
     * @param context:activity
     * @param view
     * @param listener
     * @return
     */
    public CountDown createView(Context context, View view, ZdDialog.DialogViewListener listener, ZdDialog.DialogOnClickListener dialogOnClickListener) {
        dismiss();
        mDialogView = new ZdDialog(context, R.style.DialogTheme, view, listener).setFull(true).setOutsideClose(false).setListener(dialogOnClickListener);
        return this;
    }

    public void show() {
        if (mDialogView != null) {
            try {
                mDialogView.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void dismiss() {
        if (mDialogView != null) {
            try {
                mDialogView.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mDialogView = null;
    }

    public interface OnCountDownListener {
        void onFinish();
    }

    public interface OnCountDownTickListener extends OnCountDownListener {
        void onTick(long millisUntilFinished);
    }
}
