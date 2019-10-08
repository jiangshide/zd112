package com.android.channel.vm.remote

import com.android.entity.entity.Channel
import com.android.entity.entity.ContentType
import com.android.http.vm.data.RespData
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * created by jiangshide on 2019-10-01.
 * email:18311271399@163.com
 */
interface ChannelRemote {

  @GET("api/content_type")
  fun contentType(): Observable<Response<RespData<List<ContentType>>>>

  @GET("api/channel")
  fun channel(): Observable<Response<RespData<List<Channel>>>>

  @FormUrlEncoded
  @POST("api/channel")
  fun createChannel(
    @Field("uid") uid: Long,
    @Field("name") name: String,
    @Field("des") des: String,
    @Field("format") format: Int,
    @Field("publish") publish: Int
  ): Observable<Response<RespData<Any>>>
}