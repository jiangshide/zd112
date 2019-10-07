package com.android.user.vm.data

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep

/**
 * created by jiangshide on 2019-08-17.
 * email:18311271399@163.com
 */
@Keep
data class UpdateResp(
        var title: String,
        var url: String,
        var promptDuration: Int,
        var version: String,
        var createAt: Long,
        var id: Long,
        var desc: String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
            parcel.readString()!!,
            parcel.readInt(),
            parcel.readString()!!,
            parcel.readLong(),
            parcel.readLong(),
            parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(url)
        parcel.writeInt(promptDuration)
        parcel.writeString(version)
        parcel.writeLong(createAt)
        parcel.writeLong(id)
        parcel.writeString(desc)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UpdateResp> {
        override fun createFromParcel(parcel: Parcel): UpdateResp {
            return UpdateResp(parcel)
        }

        override fun newArray(size: Int): Array<UpdateResp?> {
            return arrayOfNulls(size)
        }
    }
}