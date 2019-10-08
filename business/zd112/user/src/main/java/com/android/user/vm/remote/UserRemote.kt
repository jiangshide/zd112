package com.android.user.vm.remote

import com.android.http.vm.data.RespData
import com.android.user.vm.data.UserData
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * created by jiangshide on 2019-10-08.
 * email:18311271399@163.com
 */
interface UserRemote {

  @POST("api/user/reg")
  fun reg(
    @Query("username") username: String,
    @Query("password") password: String
  ): Observable<Response<RespData<Long>>>

  @POST("api/user/login")
  fun login(
    @Query("username") username: String,
    @Query("password") password: String
  ): Observable<Response<RespData<UserData>>>

  @GET("api/user/profile")
  fun profile(@Query("uid") uid: Long): Observable<Response<RespData<UserData>>>
}