package com.android.entity.entity;

/**
 * created by jiangshide on 2019-10-07.
 * email:18311271399@163.com
 */
//@Entity(tableName = "blog", indices = { @Index(value = "id", unique = true) })
public class Blog {
  //@PrimaryKey(autoGenerate = true)
  public int id;
  public String name;
  public String des;
  public long channelId;
  public String channel;
  public String position;
  public int format;//0:text,1:img,2:gif,3:音频,4:视频,5:webview链接
  public String url;

  public long uid;
  public String userName;
  public String userIcon;
  public String userSex;
}
