package com.android.main;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.android.base.BaseActivity;
import com.android.channel.ChannelFragment;
import com.android.event.ZdEvent;
import com.android.home.HomeFragment;
import com.android.permission.annotation.Permission;
import com.android.publish.PublishFragment;
import com.android.shop.ShopFragment;
import com.android.user.UserFragment;
import com.android.widget.ZdTab;
import com.android.zdannotations.BindPath;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import java.util.ArrayList;
import java.util.Set;

@BindPath("main/main")
public class MainActivity extends BaseActivity implements ZdTab.OnTabClickListener {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(mZdTab.init(getSupportFragmentManager())
        .setFragments(
            new HomeFragment(), new ChannelFragment(), new EmptyFragment(), new ShopFragment(),
            new UserFragment()
        )
        .setIcons(R.drawable.ic_app_home, R.drawable.ic_app_search, R.drawable.ic_app_publish,
            R.drawable.ic_app_message,
            R.drawable.ic_app_me
        )
        .setTitles(getResources().getStringArray(R.array.main))
        .setTags(
            "home", "channel", "publish", "shop", "mine"
        ).setState(ZdTab.TAB, ZdTab.TAB, ZdTab.NO_TAB, ZdTab.TAB, ZdTab.TAB)
        .setTabOnClickListener(this)
        .setTabLongClickListener(this)
        .create()
        .setTab(getIntent().getIntExtra("main", 1)).setDot()
    );
  }

  @Override
  public void onTab(int position) {
  }

  @Permission(value = { Manifest.permission.READ_EXTERNAL_STORAGE }, requestCode = 0)
  @Override public void onNoTab(int position) {
    selecteImgs(0, MimeType.ofAll());
  }

  @Override public void selecteImgs(int selected, Set set) {
    super.selecteImgs(selected, set);
    selected = 9 - selected;
    if (selected < 1) return;
    Matisse.from(this)
        .choose(set)
        .theme(R.style.Matisse_Dracula)
        .countable(true)
        .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
        .maxSelectable(selected)
        .originalEnable(true)
        .maxOriginalSize(10)
        .imageEngine(new GlideEngine())
        .forResult(REQUEST_CODE);
  }

  @Override
  public void DoubleOnClick(int tabId, String tag) {
    mZdTab.setTabIcon(R.mipmap.refresh);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (REQUEST_CODE == requestCode && resultCode == RESULT_OK && data != null) {
      Bundle bundle = new Bundle();
      bundle.putStringArrayList("list", (ArrayList<String>) Matisse.obtainPathResult(data));
      for (Fragment fragment : getSupportFragmentManager().getFragments()) {
        if (fragment instanceof PublishFragment) {
          ZdEvent.Companion.get().with("list").post(Matisse.obtainPathResult(data));
          return;
        }
      }
      push(new PublishFragment(), bundle).setRight("发布");
    }
  }
}
