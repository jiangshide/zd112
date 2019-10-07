package com.android.player;

/**
 * created by jiangshide on 2019-09-22.
 * email:18311271399@163.com
 */
public class Player {
  private static class PlayerHolder {
    private static Player instance = new Player();
  }

  public static Player getInstance() {
    return PlayerHolder.instance;
  }
}
