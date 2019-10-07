package com.android.widget.recycleview;

import androidx.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * created by jiangshide on 2019-09-22.
 * email:18311271399@163.com
 */
@IntDef({ Mode.LINE, Mode.GRID, Mode.VIEWPAGER })
@Retention(RetentionPolicy.SOURCE)
public @interface Mode {
  int LINE = 0;
  int GRID = 1;
  int VIEWPAGER = 2;
}
