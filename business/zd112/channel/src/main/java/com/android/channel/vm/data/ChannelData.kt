package com.android.channel.vm.data

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import androidx.annotation.Keep
import com.android.entity.entity.Channel

/**
 * created by jiangshide on 2019-09-14.
 * email:18311271399@163.com
 */
@Keep
data class ChannelData(
  val list: List<Channel>
) : Parcelable {
  constructor(parcel: Parcel) : this(parcel.createTypedArrayList(Channel.CREATOR)!!) {
  }

  override fun writeToParcel(
    parcel: Parcel,
    flags: Int
  ) {
    parcel.writeTypedList(list)
  }

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Creator<ChannelData> {
    override fun createFromParcel(parcel: Parcel): ChannelData {
      return ChannelData(parcel)
    }

    override fun newArray(size: Int): Array<ChannelData?> {
      return arrayOfNulls(size)
    }
  }
}




