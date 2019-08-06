package com.android.shop;

import android.os.Bundle;

import com.android.base.BaseActivity;
import com.android.zdannotations.BindPath;

@BindPath("shop/shop")
public class ShopActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity("shop", ShopFragment.class, true);
    }
}
