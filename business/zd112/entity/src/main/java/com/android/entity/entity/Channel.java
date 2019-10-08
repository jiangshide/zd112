package com.android.entity.entity;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * created by jiangshide on 2019-10-04.
 * email:18311271399@163.com
 */
@Entity(tableName = "channel", indices = {
    @Index(value = "id", unique = true), @Index(value = { "name", "format" })
})
public class Channel implements Parcelable {
  @PrimaryKey(autoGenerate = true)
  public int id;
  public String name;
  public String des;
  public int format;
  public int publish;
  public boolean isSelected;
  public boolean isEdit;

  public Channel() {
  }

  protected Channel(Parcel in) {
    id = in.readInt();
    name = in.readString();
    des = in.readString();
    format = in.readInt();
    publish = in.readInt();
    isSelected = in.readByte() != 0;
    isEdit = in.readByte() != 0;
  }

  public static final Creator<Channel> CREATOR = new Creator<Channel>() {
    @Override
    public Channel createFromParcel(Parcel in) {
      return new Channel(in);
    }

    @Override
    public Channel[] newArray(int size) {
      return new Channel[size];
    }
  };

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(id);
    dest.writeString(name);
    dest.writeString(des);
    dest.writeInt(format);
    dest.writeInt(publish);
    dest.writeByte((byte) (isSelected ? 1 : 0));
    dest.writeByte((byte) (isEdit ? 1 : 0));
  }

  @Override public String toString() {
    return "Channel{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", des='" + des + '\'' +
        ", types=" + format +
        ", publish=" + publish +
        ", isSelected=" + isSelected +
        ", isEdit=" + isEdit +
        '}';
  }
}
