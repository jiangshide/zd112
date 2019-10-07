package com.android.base;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * created by jiangshide on 2019-09-22.
 * email:18311271399@163.com
 */
public class BaseBean implements Parcelable {
  public long id;
  public int resIcon;
  public String urlIcon;
  public String title;
  public String content;
  public String des;
  public String url;
  public long timeStamp;
  public boolean isSelected;

  public BaseBean(){}

  public BaseBean(long id, int resIcon, String urlIcon, String title, String content,
      String des, String url, long timeStamp, boolean isSelected) {
    this.id = id;
    this.resIcon = resIcon;
    this.urlIcon = urlIcon;
    this.title = title;
    this.content = content;
    this.des = des;
    this.url = url;
    this.timeStamp = timeStamp;
    this.isSelected = isSelected;
  }

  protected BaseBean(Parcel in) {
    id = in.readLong();
    resIcon = in.readInt();
    urlIcon = in.readString();
    title = in.readString();
    content = in.readString();
    des = in.readString();
    url = in.readString();
    timeStamp = in.readLong();
    isSelected = in.readByte() != 0;
  }

  public static final Creator<BaseBean> CREATOR = new Creator<BaseBean>() {
    @Override
    public BaseBean createFromParcel(Parcel in) {
      return new BaseBean(in);
    }

    @Override
    public BaseBean[] newArray(int size) {
      return new BaseBean[size];
    }
  };

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel parcel, int i) {
    parcel.writeLong(id);
    parcel.writeInt(resIcon);
    parcel.writeString(urlIcon);
    parcel.writeString(title);
    parcel.writeString(content);
    parcel.writeString(des);
    parcel.writeString(url);
    parcel.writeLong(timeStamp);
    parcel.writeByte((byte) (isSelected ? 1 : 0));
  }

  @Override public String toString() {
    return "BaseBean{" +
        "id=" + id +
        ", resIcon=" + resIcon +
        ", urlIcon='" + urlIcon + '\'' +
        ", title='" + title + '\'' +
        ", content='" + content + '\'' +
        ", des='" + des + '\'' +
        ", url='" + url + '\'' +
        ", timeStamp=" + timeStamp +
        ", isSelected=" + isSelected +
        '}';
  }
}
