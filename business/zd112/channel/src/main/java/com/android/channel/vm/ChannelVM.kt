package com.android.channel.vm

import androidx.lifecycle.MutableLiveData
import com.android.base.BaseApplication
import com.android.base.vm.BaseVM
import com.android.channel.vm.remote.ChannelRemote
import com.android.entity.entity.Channel
import com.android.entity.entity.ContentType
import com.android.http.Http
import com.android.http.observer.BaseObserver
import com.android.http.transformer.CommonTransformer
import com.android.http.vm.LiveResult
import com.android.http.vm.data.RespData
import com.android.utils.LogUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

/**
 * created by jiangshide on 2019-10-01.
 * email:18311271399@163.com
 */
class ChannelVM : BaseVM() {

  private val iChannel: ChannelRemote = Http.createService(ChannelRemote::class.java)
  var contentType: MutableLiveData<LiveResult<List<ContentType>>> = MutableLiveData()
  var channel: MutableLiveData<LiveResult<List<Channel>>> = MutableLiveData()
  var channelCreate: MutableLiveData<LiveResult<Any>> = MutableLiveData()

  fun contentType() {
    iChannel.contentType()
        .compose(CommonTransformer<Response<RespData<List<ContentType>>>, List<ContentType>>())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(object : BaseObserver<List<ContentType>>() {
          override fun onNext(t: List<ContentType>) {
            super.onNext(t)
            contentType.postValue(LiveResult.success(t))
          }

          override fun onError(e: Throwable) {
            super.onError(e)
            contentType.postValue(LiveResult.error(e))
          }
        })
  }

  fun channel() {
    iChannel.channel()
        .compose(CommonTransformer<Response<RespData<List<Channel>>>, List<Channel>>())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(object : BaseObserver<List<Channel>>() {
          override fun onNext(t: List<Channel>) {
            super.onNext(t)
            channel.postValue(LiveResult.success(t))
          }

          override fun onError(e: Throwable) {
            super.onError(e)
            channel.postValue(LiveResult.error(e))
          }
        })
  }

  fun channel(
    name: String,
    des: String,
    type: Int,
    publish: Int
  ) {
    iChannel.createChannel(BaseApplication.instance.uid, name, des, type, publish)
        .compose(CommonTransformer<Response<RespData<Any>>, Any>())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(object : BaseObserver<Any>() {
          override fun onNext(t: Any) {
            super.onNext(t)
            LogUtil.e("----------t:", t.toString())
            channelCreate.postValue(LiveResult.success(t))
          }

          override fun onError(e: Throwable) {
            super.onError(e)
            LogUtil.e("--------e:", e)
            channelCreate.postValue(LiveResult.error(e))
          }
        })
  }
}