package com.android.blog.bean;

import java.util.List;

/**
 * created by jiangshide on 2019-09-30.
 * email:18311271399@163.com
 */
public class BlogEntry {
  public String uid;
  public String title;
  public String des;
  public String createTime;
  public boolean like;
  public int likeCount;
  public int viewCount;

  public List<Imgs> imgs;
  public Gifs gifs;
  public Audio audio;
  public Video video;

  public class Imgs {
    public String url;
    public int width;
    public int height;
  }

  public class Gifs {
    public String url;
    public int width;
    public int height;
  }

  public class Audio {

  }

  public class Video {
    public long id;
    public String title;
    public String des;
    public int rotate;
    public String coverUrl;
    public int height;
    public int widtht;
    public String url;
  }
}
