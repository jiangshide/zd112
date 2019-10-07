//package com.android.adapter.kadapter
//
//import com.android.adapter.ZdAdapter
//
///**
// * created by jiangshide on 2019-09-21.
// * email:18311271399@163.com
// */
//object KAdapterFactory {
//
//  fun <T> KAdapter(body: ZdAdapter<T>.() -> Unit): ZdAdapter<T> {
//    val adapter = object : ZdAdapter<T>() {}
//    var resultAdapter = adapter as ZdAdapter<T>
//    resultAdapter.body()
//    return resultAdapter
//  }
//
//  inline fun <reified T> KAdapter(
//    layoutId: Int,
//    body: ZdAdapter<T>.() -> Unit
//  ): ZdAdapter<T> {
//    val adapter = object : ZdAdapter<T>() {}
//    var resultAdapter = adapter as ZdAdapter<T>
//    resultAdapter.layout { layoutId }
//    resultAdapter.body()
//    return resultAdapter
//  }
//
//}