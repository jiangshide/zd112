package com.android.star.vm.data

import android.support.annotation.Keep

/**
 * created by jiangshide on 2019-08-05.
 * email:18311271399@163.com

 */
@Keep
data class StarData(
        val name:String,
        val match:Int//0:陌生人,1:好友
)