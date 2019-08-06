package com.android.network.transformer;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;

/**
 * created by jiangshide on 2019-07-31.
 * email:18311271399@163.com
 */
public class CommonTransformer<T, R> implements ObservableTransformer<T, R> {
    private String errorMsg;

    public CommonTransformer(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public CommonTransformer() {

    }


    @Override
    public ObservableSource<R> apply(Observable<T> upstream) {
        return upstream.compose(new ErrorCheckerTransformer(errorMsg))
                .compose(new DataTransformer());
    }
}