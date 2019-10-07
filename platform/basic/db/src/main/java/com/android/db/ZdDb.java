package com.android.db;

/**
 * created by jiangshide on 2019-09-03.
 * email:18311271399@163.com
 */
public class ZdDb<T> {

  private static class ZdDbHolder {
    private static ZdDb instance = new ZdDb();
  }

  public static ZdDb getInstance() {
    return ZdDbHolder.instance;
  }

}
