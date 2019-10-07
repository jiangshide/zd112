package com.android.http.transformer;

import androidx.annotation.Nullable;

import com.android.http.exception.ErrorResponseException;
import com.android.http.vm.data.RespData;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;
import retrofit2.Response;

/**
 * created by jiangshide on 2019-07-31.
 * email:18311271399@163.com
 */
public class ErrorCheckerTransformer<T extends Response<R>, R extends RespData>
        implements ObservableTransformer<T, R> {
    private String errorMessage;

    public ErrorCheckerTransformer(@Nullable String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public ObservableSource<R> apply(Observable<T> upstream) {
        return upstream.map(new Function<T, R>() {
            @Override
            public R apply(T t) {
                String msg = null;
                int code = 0;
                RespData body = t.body();

                if (!t.isSuccessful() || body == null) {
                    msg = errorMessage;
                    code = t.code();
                } else {
                    if (body.getCode() != 0) {
                        msg = body.getMsg();
                        code = body.getCode();
                        if (msg == null) {
                            msg = errorMessage;
                        }
                    }
                }

                if (msg != null) {
                    throw new ErrorResponseException(msg, code);
                }
                return t.body();
            }
        });
    }
}