package com.android.blog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import com.android.widget.ZdBigView;
import java.io.IOException;
import java.io.InputStream;

/**
 * created by jiangshide on 2019-09-12.
 * email:18311271399@163.com
 */
public class ImgActivity extends Activity {

  private ZdBigView bigView;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  public void showBigImg(View view){
    InputStream inputStream = null;
    try {
      inputStream = getAssets().open("stars.png");
      bigView.setImage(inputStream);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
