package com.android.network.observer;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.widget.Toast;

import com.android.network.exception.ErrorResponseException;

/**
 * created by jiangshide on 2019-07-31.
 * email:18311271399@163.com
 */
public class ToastObserver<T> extends BaseObserver<T> {

    public ToastObserver(Context context) {
        super(context);
    }

    public ToastObserver() {
        super(null);
    }

    @CallSuper
    @Override
    public void onError(Throwable e) {
        try {
            if (e instanceof ErrorResponseException) {
                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(mContext, "网络错误!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

//    private String errorTipsMsg;
//
//    public ToastObserver(Context context) {
//        super(context);
//    }
//
//    public ToastObserver(String errorTipsMsg) {
//        this.errorTipsMsg = errorTipsMsg;
//    }
//
//    @CallSuper
//    @Override
//    public void onError(Throwable e) {
//        try {
//            if (e instanceof ErrorResponseException) {
//                errorTipsMsg = e.getMessage();
//            }
//            Toast.makeText(mContext, errorTipsMsg, Toast.LENGTH_LONG).show();
//        } catch (Exception e1) {
//            e1.printStackTrace();
//        }
//    }
}