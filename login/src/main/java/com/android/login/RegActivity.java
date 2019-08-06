package com.android.login;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.android.base.BaseActivity;
import com.android.zdannotations.BindPath;

/**
 * created by jiangshide on 2019-08-04.
 * email:18311271399@163.com
 */
@BindPath("reg/reg")
public class RegActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
    }
}
