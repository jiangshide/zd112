package com.android.permission.model;

import android.content.Context;

import java.util.List;

/**
 * Created by mq on 2018/3/28 下午5:19
 * mqcoder90@gmail.com
 */

public class GrantModel {

    public Context context;
    public int requestCode;
    public List<String> grantResults;
    public boolean isCancel;
}
