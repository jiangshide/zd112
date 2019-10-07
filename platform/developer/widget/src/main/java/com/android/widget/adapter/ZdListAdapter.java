package com.android.widget.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import com.android.widget.R;
import java.util.List;

/**
 * created by jiangshide on 2019-07-24.
 * email:18311271399@163.com
 */
public abstract class ZdListAdapter<T> extends BaseAdapter implements View.OnClickListener {
  private Context mContext;
  private List<T> mData;
  private int mLayoutId, index, count;
  private Callback mCallback;
  public static final int MIN_CLICK_DELAY_TIME = 500;
  private long lastClickTime = 0;

  public ZdListAdapter(Context context, List<T> data) {
    this.mContext = context;
    this.mData = data;
    this.mLayoutId = R.layout.default_list_item;
  }

  public ZdListAdapter(Context context, List<T> data, int layoutId) {
    this.mContext = context;
    this.mData = data;
    this.mLayoutId = layoutId;
  }

  public ZdListAdapter(Context context, List<T> data, int layoutId, Callback callback) {
    this.mContext = context;
    this.mData = data;
    this.mLayoutId = layoutId;
    this.mCallback = callback;
  }

  public List<T> getList() {
    return mData;
  }

  public ZdListAdapter<T> setViewTypeCount(int count) {
    this.count = count;
    return this;
  }

  public ZdListAdapter<T> setItemViewType(int index) {
    this.index = index;
    return this;
  }

  public int getSize() {
    return mData != null ? mData.size() : 0;
  }

  public void addList(List<T> data) {
    this.addList(data, false);
  }

  public void addList(List<T> data, Boolean isRefresh) {
    if (isRefresh) {
      mData.clear();
    }
    mData.addAll(data);
    this.notifyDataSetChanged();
  }

  public List<T> getData() {
    return mData;
  }

  public void data(List<T> data) {
    if (mData != null && data != null) {
      mData.clear();
      mData.addAll(data);
      this.notifyDataSetChanged();
    }
  }

  public void remove(T t) {
    if (mData != null && mData.contains(t)) {
      mData.remove(t);
    }
    notifyDataSetChanged();
  }

  public void remove(int position) {
    if (mData != null && mData.size() - 1 >= position) {
      mData.remove(position);
    }
  }

  public void clear() {
    if (mData != null) {
      mData.clear();
    }
    mData = null;
  }

  @Override
  public int getViewTypeCount() {
    return count > 0 ? count : super.getViewTypeCount();
  }

  @Override
  public int getItemViewType(int position) {
    return index > 0 ? position < index ? position : index : super.getItemViewType(position);
  }

  @Override
  public int getCount() {
    return mData == null ? 0 : mData.size();
  }

  @Override
  public T getItem(int position) {
    return mData.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    if (mContext == null) {
      return convertView;
    }
    if (convertView == null) {
      convertView = LayoutInflater.from(mContext).inflate(mLayoutId, null);
    }
    T t = getItem(position);
    if (convertView != null) {
      convertView(position, convertView, t);
    }
    return convertView;
  }

  public void notifyDataSetChanged(ListView listView, int position) {
    int firstVisiblePosition = listView.getFirstVisiblePosition();
    int lastVisiblePosition = listView.getLastVisiblePosition();
    if (position >= firstVisiblePosition && position <= lastVisiblePosition) {
      View view = listView.getChildAt(position - firstVisiblePosition);
      getView(position, view, listView);
    }
  }

  public <T extends View> T get(View view, int id) {
    SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
    if (viewHolder == null) {
      viewHolder = new SparseArray<View>();
      view.setTag(viewHolder);
    }
    View childView = viewHolder.get(id);
    if (childView == null) {
      childView = view.findViewById(id);
      viewHolder.put(id, childView);
    }
    return (T) childView;
  }

  protected abstract void convertView(int position, View item, T t);

  public interface Callback {
    public void click(View v);
  }

  @Override
  public void onClick(View v) {
    long currentTime = System.currentTimeMillis();
    if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
      lastClickTime = currentTime;
      mCallback.click(v);
    }
  }
}
