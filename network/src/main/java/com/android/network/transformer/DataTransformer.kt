package com.android.network.transformer

import com.android.network.vm.data.RespData
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer

/**
 * created by jiangshide on 2019-07-31.
 * email:18311271399@163.com
 */
class DataTransformer<T : RespData<R>, R> : ObservableTransformer<T, R> {
    override fun apply(upstream: Observable<T>): ObservableSource<R> {
        return upstream.map { t ->
            var data = t.data
            if (data == null) {
                data = Any() as R
            }
            data
        }
    }
}