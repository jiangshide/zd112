package com.android.http.vm.data

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep

/**
 * created by jiangshide on 2019-07-31.
 * email:18311271399@163.com
 */
@SuppressLint("ParcelCreator")
@Keep
data class RespData<T>(
        val code: Int = 0,
        val date: Long,
        val res: T? = null,
        val msg: String? = null
) : Parcelable {
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(code)
        parcel.writeLong(date)
        parcel.writeString(msg)
    }

    override fun describeContents(): Int {
        return 0
    }

  override fun toString(): String {
    return "RespData(code=$code, date=$date, res=$res, msg=$msg)"
  }

}