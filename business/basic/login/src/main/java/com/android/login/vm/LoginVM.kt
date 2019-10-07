package com.android.login.vm

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.android.base.vm.BaseVM
import com.android.http.observer.ToastObserver
import com.android.http.transformer.CommonTransformer
import com.android.http.vm.LiveResult
import com.android.http.vm.data.RespData
import com.android.login.vm.data.UserData
import com.android.login.vm.data.VerifyData
import com.android.login.vm.remote.LoginRemote
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

/**
 * created by jiangshide on 2019-08-04.
 * email:18311271399@163.com
 */
class LoginVM : BaseVM() {

    private var loginRemote: LoginRemote? = null

    public var loginResult: MutableLiveData<LiveResult<UserData>> = MutableLiveData()
    public var phoneCodeResult: MutableLiveData<LiveResult<Object>> = MutableLiveData()
    public var verifyPhoneResult: MutableLiveData<LiveResult<VerifyData>> = MutableLiveData()
    public var setPasswordResult: MutableLiveData<LiveResult<Any>> = MutableLiveData()
    public var phoneBindingResult: MutableLiveData<LiveResult<Any>> = MutableLiveData()

    init {
        loginRemote = http.createService(LoginRemote::class.java)
    }

    fun wechatlogin(context: Context, code: String) {
        loginRemote!!.wechatLogin(com.android.base.BuildConfig.WECHAT_APPID, code)
                .compose(CommonTransformer<Response<RespData<UserData>>, UserData>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ToastObserver<UserData>(context) {
                    override fun onNext(t: UserData) {
                        loginResult.postValue(LiveResult.success(t))
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        loginResult.postValue(LiveResult.error(e))
                    }
                })
    }

    fun phoneLogin(context: Context?, phone: String, code: String?, pwd: String?) {
        loginRemote!!.phoneLogin(phone, code, pwd)
                .compose(CommonTransformer<Response<RespData<UserData>>, UserData>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ToastObserver<UserData>(context) {
                    override fun onNext(t: UserData) {
                        loginResult.postValue(LiveResult.success(t))
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        loginResult.postValue(LiveResult.error(e))
                    }
                })
    }

    fun getPhoneCode(context: Context?, phone: String, action: String) {
        loginRemote!!.phoneCode(phone, action)
                .compose(CommonTransformer<Response<RespData<Any>>, Object>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ToastObserver<Object>(context) {
                    override fun onNext(t: Object) {
                        phoneCodeResult.postValue(LiveResult.success(t))
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        phoneCodeResult.postValue(LiveResult.error(e))
                    }
                })
    }

    fun verifyPhone(context: Context?, phone: String) {
        loginRemote!!.verifyPhone(phone)
                .compose(CommonTransformer<Response<RespData<VerifyData>>, VerifyData>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ToastObserver<VerifyData>(context) {
                    override fun onNext(t: VerifyData) {
                        verifyPhoneResult.postValue(LiveResult.success(t))
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        verifyPhoneResult.postValue(LiveResult.error(e))
                    }
                })
    }

    fun setPassword(context: Context?, password: String) {
        loginRemote!!.setPassword(password)
                .compose(CommonTransformer<Response<RespData<Object>>, Object>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ToastObserver<Object>(context) {
                    override fun onNext(t: Object) {
                        setPasswordResult.postValue(LiveResult.success(t))
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        setPasswordResult.postValue(LiveResult.error(e))
                    }
                })
    }

    fun phoneBinding(context: Context?, phone: String, code: String) {
        loginRemote!!.phoneBinding(phone, code)
                .compose(CommonTransformer<Response<RespData<Any>>, Any>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ToastObserver<Any>(context) {
                    override fun onNext(t: Any) {
                        phoneBindingResult.postValue(LiveResult.success(t))
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        phoneBindingResult.postValue(LiveResult.error(e))
                    }
                })
    }
}
