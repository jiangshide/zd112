package com.android.entity.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;
import java.util.List;

/**
 * created by jiangshide on 2019-09-03.
 * email:18311271399@163.com
 */
@Dao
public interface BaseDao<T> {

  @Insert
  public void insert(T t);

  @Insert
  public void inserts(List<T> t);

  @Delete
  public void delete(T t);

  @Update
  public void update(T t);
}
