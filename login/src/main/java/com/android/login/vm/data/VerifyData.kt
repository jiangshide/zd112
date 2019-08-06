package com.android.login.vm.data

import android.support.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * created by jiangshide on 2019-08-04.
 * email:18311271399@163.com
 */
@Keep
data class VerifyData(
        @SerializedName("name")
        var name: String? = null
)
