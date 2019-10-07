package com.android.entity;

import com.android.entity.db.Db;
import com.android.entity.entity.Channel;
import com.android.entity.entity.ContentType;
import com.android.utils.LogUtil;
import java.util.List;

/**
 * created by jiangshide on 2019-10-04.
 * email:18311271399@163.com
 */
public class Entity {

  public static void insertChannel(List<Channel> list) {
    try {
      List<Channel> datas = getChannels();
      if (datas != null && datas.size() > 0) {
        for (Channel channel : list) {
          Db.getInstance().getChannelDao().update(channel);
        }
      } else {
        Db.getInstance().getChannelDao().inserts(list);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void delChannel(Channel channel) {
    Db.getInstance().getChannelDao().delete(channel);
  }

  public static void updateChannel(Channel channel) {
    Db.getInstance().getChannelDao().update(channel);
  }

  public static List<Channel> getChannels() {
    return Db.getInstance().getChannelDao().getChannels();
  }

  public static Channel getChannel(int id) {
    return Db.getInstance().getChannelDao().getChannel(id);
  }

  public static void insertContentType(List<ContentType> list) {
    try {
      List<ContentType> datas = getContentType();
      if (datas != null && datas.size() > 0) {
        for (ContentType contentType : list) {
          Db.getInstance().getContentTypeDao().update(contentType);
        }
      } else {
        Db.getInstance().getContentTypeDao().inserts(list);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static List<ContentType> getContentType() {
    return Db.getInstance().getContentTypeDao().getContentTypes();
  }
}
