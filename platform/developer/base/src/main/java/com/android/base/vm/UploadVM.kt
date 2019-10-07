package com.android.base.vm

import com.android.http.vm.data.RespData
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * created by jiangshide on 2019-10-04.
 * email:18311271399@163.com
 */
class UploadVM : BaseVM() {

  interface UploadRemote {

    @FormUrlEncoded
    @POST("api/channel")
    fun createChannel(
      @Field("file") file: String
    ): Observable<Response<RespData<Any>>>
  }
}
