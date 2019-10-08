package com.android.blog.vm

import androidx.lifecycle.MutableLiveData
import com.android.base.BaseApplication
import com.android.base.vm.BaseVM
import com.android.blog.vm.remote.BlogRemote
import com.android.entity.entity.Blog
import com.android.http.observer.BaseObserver
import com.android.http.transformer.CommonTransformer
import com.android.http.vm.LiveResult
import com.android.http.vm.data.RespData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

/**
 * created by jiangshide on 2019-10-07.
 * email:18311271399@163.com
 */
class BlogVM : BaseVM() {
  private val iBlog: BlogRemote = http.createService(BlogRemote::class.java)
  var blog: MutableLiveData<LiveResult<List<Blog>>> = MutableLiveData()

  fun blog() {
    this.blog(BaseApplication.instance.uid, -1)
  }

  fun blog(
    uid: Long,
    format: Int
  ) {
    iBlog.blog(uid, format)
        .compose(CommonTransformer<Response<RespData<List<Blog>>>, List<Blog>>())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(object : BaseObserver<List<Blog>>() {
          override fun onNext(t: List<Blog>) {
            super.onNext(t)
            blog.postValue(LiveResult.success(t))
          }

          override fun onError(e: Throwable) {
            super.onError(e)
            blog.postValue(LiveResult.error(e))
          }
        })
  }
}