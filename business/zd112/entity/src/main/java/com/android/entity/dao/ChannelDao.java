package com.android.entity.dao;

import androidx.room.Dao;
import androidx.room.Query;
import com.android.entity.entity.Channel;
import java.util.List;

/**
 * created by jiangshide on 2019-10-04.
 * email:18311271399@163.com
 */
@Dao
public interface ChannelDao extends BaseDao<Channel> {

  @Query("SELECT * FROM channel")
  List<Channel> getChannels();

  @Query("SELECT * FROM channel WHERE id=:id")
  Channel getChannel(int id);
}
