package com.android.publish.vm

import androidx.lifecycle.MutableLiveData
import com.android.base.BaseApplication
import com.android.base.vm.BaseVM
import com.android.http.Http
import com.android.http.observer.BaseObserver
import com.android.http.transformer.CommonTransformer
import com.android.http.vm.LiveResult
import com.android.http.vm.data.RespData
import com.android.publish.vm.remote.PublishRemote
import com.android.utils.LogUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

/**
 * created by jiangshide on 2019-10-07.
 * email:18311271399@163.com
 */
class PublishVM : BaseVM() {

  private val iPublish: PublishRemote = Http.createService(PublishRemote::class.java)
  var publish: MutableLiveData<LiveResult<Any>> = MutableLiveData()

  fun publish(
    name: String,
    des: String,
    channelId: Long,
    position: String,
    format: Int,
    url: String
  ) {
    iPublish.publish(BaseApplication.instance.uid, name, des, channelId, position, format, url)
        .compose(CommonTransformer<Response<RespData<Any>>, Any>())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(object : BaseObserver<Any>() {
          override fun onNext(t: Any) {
            super.onNext(t)
            LogUtil.e("----------t:", t.toString())
            publish.postValue(LiveResult.success(t))
          }

          override fun onError(e: Throwable) {
            super.onError(e)
            LogUtil.e("--------e:", e)
            publish.postValue(LiveResult.error(e))
          }
        })
  }
}