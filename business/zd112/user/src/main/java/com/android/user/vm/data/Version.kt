package com.android.user.vm.data

import androidx.annotation.Keep

/**
 * created by jiangshide on 2019-08-17.
 * email:18311271399@163.com
 */
@Keep
data class Version(
        var name:String,
        var version:String,
        var url:String,
        var isUpdate:Boolean
)
