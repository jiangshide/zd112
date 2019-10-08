package com.android.publish.vm.remote

import com.android.http.vm.data.RespData
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * created by jiangshide on 2019-10-07.
 * email:18311271399@163.com
 */
interface PublishRemote {

  @FormUrlEncoded
  @POST("api/blog")
  fun publish(
    @Field("uid") uid: Long,
    @Field("name") name: String,
    @Field("des") des: String,
    @Field("channelId") channelId: Long,
    @Field("position") position: String,
    @Field("format") format: Int,
    @Field("url") url: String
  ): Observable<Response<RespData<Any>>>
}