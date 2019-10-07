package com.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.android.widget.adapter.KAdapter;
import com.android.widget.recycleview.Mode;
import com.android.widget.recycleview.ViewPagerLayoutManager;
import com.android.widget.recycleview.listener.OnViewPagerListener;
import java.util.Collections;

/**
 * created by jiangshide on 2019-09-22.
 * email:18311271399@163.com
 */
public class ZdRecycleView<T> extends RecyclerView {

  private int mGridSize = 3;

  public ZdRecycleView(@NonNull Context context) {
    super(context);
  }

  public ZdRecycleView(@NonNull Context context,
      @Nullable AttributeSet attrs) {
    super(context, attrs);
    setMode(Mode.LINE);
  }

  public ZdRecycleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    setMode(Mode.LINE);
  }

  public ZdRecycleView setGridSize(int gridSize) {
    this.mGridSize = gridSize;
    return this;
  }

  public ZdRecycleView setMode(@Mode int mode) {
    return setMode(mode, false, null);
  }

  public ZdRecycleView setMode(@Mode int mode, boolean isHorizontal, OnViewPagerListener listener) {
    setLayoutManager(mode == Mode.VIEWPAGER ? new ViewPagerLayoutManager(getContext(),
        isHorizontal ? OrientationHelper.HORIZONTAL
            : OrientationHelper.VERTICAL).setOnViewPagerListener(listener)
        : mode == Mode.GRID
            ? new GridLayoutManager(getContext(), mGridSize)
            : new LinearLayoutManager(getContext()));
    setItemAnimator(new DefaultItemAnimator());
    return this;
  }

  public ZdRecycleView setDrag(KAdapter<T> adapter) {
    return setDrag(adapter, true);
  }

  public ZdRecycleView setDrag(KAdapter<T> adapter, boolean isDrag) {
    new ItemTouchHelper(new ItemTouchHelper.Callback() {
      @Override public int getMovementFlags(@NonNull RecyclerView recyclerView,
          @NonNull ViewHolder viewHolder) {
        int dragFlags;
        int swipeFlags;
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
          dragFlags = ItemTouchHelper.UP
              | ItemTouchHelper.DOWN
              | ItemTouchHelper.LEFT
              | ItemTouchHelper.RIGHT;
          swipeFlags = 0;
        } else {
          dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
          swipeFlags = 0;
        }
        return makeMovementFlags(dragFlags, swipeFlags);
      }

      @Override
      public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull ViewHolder viewHolder,
          @NonNull ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        if (fromPosition < toPosition) {
          for (int i = fromPosition; i < fromPosition; i++) {
            Collections.swap(adapter.datas(), i, i + 1);
          }
        } else {
          for (int i = fromPosition; i > toPosition; i--) {
            Collections.swap(adapter.datas(), i, i - 1);
          }
        }
        adapter.notifyItemMoved(fromPosition, toPosition);
        return true;
      }

      @Override public void onSwiped(@NonNull ViewHolder viewHolder, int direction) {

      }
    }).attachToRecyclerView(isDrag ? this : null);
    return this;
  }
}
