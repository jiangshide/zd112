package com.android.entity.dao;

import androidx.room.Dao;
import androidx.room.Query;
import com.android.entity.entity.ContentType;
import java.util.List;

/**
 * created by jiangshide on 2019-10-06.
 * email:18311271399@163.com
 */
@Dao
public interface ContentTypeDao extends BaseDao<ContentType> {

  @Query("SELECT * FROM contentType")
  List<ContentType> getContentTypes();
}
