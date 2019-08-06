package com.android.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.android.base.BaseActivity;
import com.android.home.HomeFragment;
import com.android.shop.ShopFragment;
import com.android.star.StarFragment;
import com.android.user.UserFragment;
import com.android.utils.LogUtil;
import com.android.widget.ZdTab;
import com.android.zdannotations.BindPath;
import com.android.zdrouter.ZdRouter;

@BindPath("main/main")
public class MainActivity extends BaseActivity implements ZdTab.OnTabClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mZdTab.init(getSupportFragmentManager()).setFragments(
                new HomeFragment(), new StarFragment(), new ShopFragment(), new UserFragment()
                ).setIcons(R.drawable.ic_app_home, R.drawable.ic_app_search,R.drawable.ic_app_message,R.drawable.ic_app_me
                ).setTags(
                "home", "start", "shop", "activity_user"
                ).setTabOnClickListener(this).setTabLongClickListener(this).create()
        );
    }

    private void jumpUser(View view) {
        ZdRouter.getInstance().go("login/login").build();
    }

    @Override
    public void onTab(int position) {
        LogUtil.e("------position:", position);
    }

    @Override
    public void DoubleOnClick(int tabId, String tag) {
        LogUtil.e("-----------tabId:",tabId," | tag:",tag);
    }
}
