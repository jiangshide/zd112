package com.android.network.vm.data

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.Keep

/**
 * created by jiangshide on 2019-07-31.
 * email:18311271399@163.com
 */
@SuppressLint("ParcelCreator")
@Keep
data class RespData<T>(
        val code: Int = 0,
        val success: Boolean,
        val data: T? = null,
        val msg: String? = null
) : Parcelable {
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(code)
        parcel.writeByte(if (success) 1 else 0)
        parcel.writeString(msg)
    }

    override fun describeContents(): Int {
        return 0
    }
}

//@Keep
//class BaseResp<T> {
//    @SerializedName("code")
//    var code: Int = 0
//
//    @SerializedName("success")
//    var success: Boolean = true
//
//    @SerializedName("data")
//    var data: T? = null
//
//    @SerializedName("msg")
//    var msg: String? = null
//}