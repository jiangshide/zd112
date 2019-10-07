package com.android.push.bean;

import java.io.Serializable;

/**
 * created by jiangshide on 2019-09-04.
 * email:18311271399@163.com
 */
public class PushBean implements Serializable {
  public String taskId;
  public String msgId;
  public String payLoadId;

  public String title;
  public int action = -1;
  public String content;//文章详情字段:json
  public int type;//tab页面

  public String push_title;
  public String push_content;

  public boolean is_clear = true;//是否可以清除 true可以，false不可以:由服务端控制:如果是透传的话该项失效(只针对通知实现)
  public boolean is_ring;   //是否响铃         true 响铃  false不响
  public boolean is_wifi;    //WiFi展示         true只在wifi下展示  false不判断WiFi
  public boolean show_type; //  展会条件  true亮屏幕  false不亮
  public boolean is_float;    //       是否弹窗展示  true 弹窗  false 不弹
  public boolean is_vibrate;   //是否中东           true震动  false不震动
  public int float_type = 1;//1:中间弹窗;2:顶部弹窗
  public int float_duration = 5;//顶部弹窗停留时间,默认5秒

  public boolean isInner;//来自应用内还是应用外:true:应用内;false:应用外

  public Object object;//用来提示对象的

  public String url;//
  public int is_wap;//

  public PushBean setObject(Object object) {
    this.object = object;
    return this;
  }

  public PushBean setTaskId(String taskId) {
    this.taskId = taskId;
    return this;
  }

  public PushBean setMsgId(String msgId) {
    this.msgId = msgId;
    return this;
  }

  public PushBean setPayLoadId(String payLoadId) {
    this.payLoadId = payLoadId;
    return this;
  }

  public PushBean setTitle(String title) {
    this.title = title;
    return this;
  }

  public PushBean setContent(String content) {
    this.content = content;
    return this;
  }

  @Override public String toString() {
    return "PushBean{" +
        "taskId='" + taskId + '\'' +
        ", msgId='" + msgId + '\'' +
        ", payLoadId='" + payLoadId + '\'' +
        ", title='" + title + '\'' +
        ", action=" + action +
        ", content='" + content + '\'' +
        ", type='" + type + '\'' +
        ", push_title='" + push_title + '\'' +
        ", push_content='" + push_content + '\'' +
        ", is_clear=" + is_clear +
        ", is_ring=" + is_ring +
        ", is_wifi=" + is_wifi +
        ", show_type=" + show_type +
        ", is_float=" + is_float +
        ", is_vibrate=" + is_vibrate +
        ", float_type=" + float_type +
        ", isInner=" + isInner +
        ", object=" + object +
        ", url='" + url + '\'' +
        ", is_wap=" + is_wap +
        '}';
  }
}
