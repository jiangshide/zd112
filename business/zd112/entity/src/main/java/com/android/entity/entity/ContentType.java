package com.android.entity.entity;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * created by jiangshide on 2019-10-06.
 * email:18311271399@163.com
 */
@Entity(tableName = "contentType", indices = {
    @Index(value = "id", unique = true), @Index(value = { "name"})
})
public class ContentType implements Parcelable {
  @PrimaryKey(autoGenerate = true)
  public int id;//内容类型:0~文字,1~图片,2~Gif,3~音频,4～视频
  public String name;
  public String des;

  public ContentType(){}

  protected ContentType(Parcel in) {
    id = in.readInt();
    name = in.readString();
    des = in.readString();
  }

  public static final Creator<ContentType> CREATOR = new Creator<ContentType>() {
    @Override
    public ContentType createFromParcel(Parcel in) {
      return new ContentType(in);
    }

    @Override
    public ContentType[] newArray(int size) {
      return new ContentType[size];
    }
  };

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(id);
    dest.writeString(name);
    dest.writeString(des);
  }

  @Override public String toString() {
    return "ContentTypeDao{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", des='" + des + '\'' +
        '}';
  }
}
