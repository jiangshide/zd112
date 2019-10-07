package com.android.zd112;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.android.base.BaseActivity;
import com.android.channel.vm.ChannelVM;
import com.android.entity.Entity;
import com.android.entity.entity.Channel;
import com.android.http.vm.LiveResult;
import com.android.utils.annotation.ContentView;
import com.android.zdannotations.BindPath;
import com.android.zdrouter.ZdRouter;
import java.util.List;

@BindPath("splash/splash")
@ContentView(R.layout.activity_splash)
public class SplashActivity extends BaseActivity {

  ChannelVM channelVM;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    channelVM = ViewModelProviders.of(this).get(ChannelVM.class);
    channelVM.getChannel().observe(this, new Observer<LiveResult<List<Channel>>>() {
      @Override public void onChanged(LiveResult<List<Channel>> listLiveResult) {
        if (listLiveResult.getError() == null) {
          try {
            Entity.insertChannel(listLiveResult.getData());
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
        ZdRouter.getInstance().go("main/main").build(SplashActivity.this);
      }
    });
    channelVM.channel();
  }
}
