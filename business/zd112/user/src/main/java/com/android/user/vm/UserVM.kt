package com.android.user.vm

import androidx.lifecycle.MutableLiveData
import com.android.base.vm.BaseVM
import com.android.http.Http
import com.android.http.observer.BaseObserver
import com.android.http.transformer.CommonTransformer
import com.android.http.vm.LiveResult
import com.android.http.vm.data.RespData
import com.android.user.vm.data.UpdateResp
import com.android.user.vm.data.UserData
import com.android.user.vm.remote.UpdateRemote
import com.android.user.vm.remote.UserRemote
import com.android.utils.AppUtil
import com.android.utils.LogUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

/**
 * created by jiangshide on 2019-08-17.
 * email:18311271399@163.com
 */
class UserVM : BaseVM() {

  private val iUser: UserRemote = Http.createService(UserRemote::class.java)
  var reg: MutableLiveData<LiveResult<Long>> = MutableLiveData()
  var login: MutableLiveData<LiveResult<UserData>> = MutableLiveData()
  var profile: MutableLiveData<LiveResult<UserData>> = MutableLiveData()

  fun reg(
    userName: String,
    password: String
  ) {
    iUser.reg(userName, password)
        .compose(CommonTransformer<Response<RespData<Long>>, Long>())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(object : BaseObserver<Long>() {
          override fun onNext(t: Long) {
            super.onNext(t)
            reg.postValue(LiveResult.success(t))
          }

          override fun onError(e: Throwable) {
            super.onError(e)
            reg.postValue(LiveResult.error(e))
          }
        })
  }

  fun login(
    userName: String,
    password: String
  ) {
    iUser.login(userName, password)
        .compose(CommonTransformer<Response<RespData<UserData>>, UserData>())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(object : BaseObserver<UserData>() {
          override fun onNext(t: UserData) {
            super.onNext(t)
            login.postValue(LiveResult.success(t))
          }

          override fun onError(e: Throwable) {
            super.onError(e)
            login.postValue(LiveResult.error(e))
          }
        })
  }

  fun profile(uid: Long) {
    iUser.profile(uid)
        .compose(CommonTransformer<Response<RespData<UserData>>, UserData>())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(object : BaseObserver<UserData>() {
          override fun onNext(t: UserData) {
            super.onNext(t)
            profile.postValue(LiveResult.success(t))
          }

          override fun onError(e: Throwable) {
            super.onError(e)
            profile.postValue(LiveResult.error(e))
          }
        })
  }

  private val iUpdate: UpdateRemote = Http.createService(UpdateRemote::class.java)
  var update: MutableLiveData<LiveResult<UpdateResp>> = MutableLiveData()

  fun update() {
    iUpdate.update(AppUtil.getAppVersionName())
        .compose(CommonTransformer<Response<RespData<UpdateResp>>, UpdateResp>())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(object : BaseObserver<Any>(null) {
          override fun onNext(t: Any) {
            super.onNext(t)
            LogUtil.e("update:~t", t)
            try {
              val updateResp = t as UpdateResp
              update.postValue(LiveResult.success(updateResp))
            } catch (e: Exception) {
              update.postValue(LiveResult.error(e))
            }
          }

          override fun onError(e: Throwable) {
            super.onError(e)
            LogUtil.e("update~e:", e)
            update.postValue(LiveResult.error(e))
          }
        })
  }

}