package com.android.blog.vm.remote

import com.android.entity.entity.Blog
import com.android.http.vm.data.RespData
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * created by jiangshide on 2019-10-07.
 * email:18311271399@163.com
 */
interface BlogRemote {

  @GET("api/blog")
  fun blog(
    @Query("uid") uid: Long,
    @Query("format") format: Int
  ): Observable<Response<RespData<List<Blog>>>>
}