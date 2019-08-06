package com.android.network.observer;

import android.content.Context;

import com.android.network.NetWorkApi;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * created by jiangshide on 2019-07-31.
 * email:18311271399@163.com
 */
public class BaseObserver<T> implements Observer<T> {

    public Context getContext() {
        return mContext;
    }

    protected Context mContext;

    public BaseObserver() {
        this(null);
    }

    public BaseObserver(Context context) {
        super();
        if (context == null) {
            mContext = NetWorkApi.Companion.getInstance().getApplication();
        } else {
            this.mContext = context.getApplicationContext();
        }
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}