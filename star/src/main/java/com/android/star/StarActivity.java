package com.android.star;

import android.os.Bundle;

import com.android.base.BaseActivity;
import com.android.zdannotations.BindPath;

@BindPath("star/star")
public class StarActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity("star",StarFragment.class,true);
    }
}
