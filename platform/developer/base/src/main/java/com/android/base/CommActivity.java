package com.android.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.android.widget.ZdTopView;

/**
 * created by jiangshide on 2019-08-05.
 * email:18311271399@163.com
 */
public class CommActivity<T> extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.comm_activity);

        ZdTopView zdTabTop = findViewById(R.id.commTabTop);
        if (intent.hasExtra("title")) {
            String title = intent.getStringExtra("title");
            zdTabTop.setTitle(title);
            zdTabTop.setVisibility(View.VISIBLE);
        } else {
            zdTabTop.setVisibility(View.GONE);
        }
        BaseFragment _clazz = null;
        try {
            _clazz = ((Class<BaseFragment>) intent.getSerializableExtra("_class")).newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.commonActivity, _clazz).commit();
    }
}
