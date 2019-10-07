package com.android.home;

import android.os.Bundle;

import com.android.base.BaseActivity;
import com.android.zdannotations.BindPath;

@BindPath("home/home")
public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        goFragment("home", HomeFragment.class, true);
    }
}
