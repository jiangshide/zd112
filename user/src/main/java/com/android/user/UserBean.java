package com.android.user;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * created by jiangshide on 2019-07-23.
 * email:18311271399@163.com
 */
public class UserBean implements Parcelable {
    public String name;
    public int age;

    public UserBean(Parcel in) {
        name = in.readString();
        age = in.readInt();
    }

    public static final Creator<UserBean> CREATOR = new Creator<UserBean>() {
        @Override
        public UserBean createFromParcel(Parcel in) {
            return new UserBean(in);
        }

        @Override
        public UserBean[] newArray(int size) {
            return new UserBean[size];
        }
    };

    public UserBean() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(age);
    }
}
