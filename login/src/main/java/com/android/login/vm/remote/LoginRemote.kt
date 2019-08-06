package com.android.login.vm.remote

import com.android.login.vm.data.UserData
import com.android.login.vm.data.VerifyData
import com.android.network.vm.data.RespData
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*

/**
 * created by jiangshide on 2019-03-04.
 * email:18311271399@163.com
 */
interface LoginRemote {
    /**
     * 验证手机号
     */
    @GET("/api/user/phone/verify")
    fun verifyPhone(@Query("phone") phone: String): Observable<Response<RespData<VerifyData>>>

    /**
     * 获取手机验证码
     */
    @GET("/api/user/phone/code")
    fun phoneCode(
            @Query("phone") phone: String,
            @Query("action") action: String
    ): Observable<Response<RespData<Any>>>


    /**
     * 微信登陆
     */
    @GET("/api/user/login/wechat")
    fun wechatLogin(
            @Query("appid") appid: String,
            @Query("code") code: String
    ): Observable<Response<RespData<UserData>>>

    /**
     * QQ登陆
     */
    @GET("/api/user/login/qq")
    fun qqLogin(
            @Query("appid") appid: String,
            @Query("access_token") accessToken: String,
            @Query("openid") openid: String
    ): Observable<Response<RespData<UserData>>>

    /**
     * 手机号登陆
     */
    @FormUrlEncoded
    @POST("/api/user/login/phone")
    fun phoneLogin(
            @Field("phone") phone: String,
            @Field("code") code: String?,
            @Field("pwd") pwd: String?
    ): Observable<Response<RespData<UserData>>>


    /**
     * 设置密码
     */
    @FormUrlEncoded
    @POST("/api/user/user/pwd")
    fun setPassword(
            @Field("pwd") pwd: String
    ): Observable<Response<RespData<Object>>>

    /**
     * 绑定手机号
     */
    @FormUrlEncoded
    @POST("/api/user/phone/binding")
    fun phoneBinding(
            @Field("phone") phone: String,
            @Field("code") vode: String
    ): Observable<Response<RespData<Any>>>
}
