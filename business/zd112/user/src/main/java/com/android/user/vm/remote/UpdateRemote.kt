package com.android.user.vm.remote

import com.android.http.vm.data.RespData
import com.android.user.vm.data.UpdateResp
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * created by jiangshide on 2019-08-17.
 * email:18311271399@163.com
 */
interface UpdateRemote {

    @GET("/api/user/update")
    fun update(
            @Query("appVersion") appVersion: String,
            @Query("platform") platform: String? = "Android"
    ): Observable<Response<RespData<UpdateResp>>>


}