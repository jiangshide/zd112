package com.android.player.audio;

import android.os.Handler;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * created by jiangshide on 2019-09-22.
 * email:18311271399@163.com
 */
public class AudioPlayer implements IMediaPlayer.OnCompletionListener,
    IMediaPlayer.OnPreparedListener {

  private static class AudioPlayerHolder {
    private static AudioPlayer instance = new AudioPlayer();
  }

  public static AudioPlayer getInstance() {
    return AudioPlayerHolder.instance;
  }

  public IjkMediaPlayer mPlayer;
  private OnAudioListener mOnAudioListener;

  private ExecutorService executorService;

  // Default size 2: for service and UI
  //  用于通知Service和播放界面更改UI
  //  使用HashSet防止多次注册回调
  private boolean isPaused;   // 播放状态
  private boolean isPrepared;   // 准备状态
  private boolean isLoadingNext;  //是否正在加载下一首
  private Handler mHandler;

  private AudioPlayer() {
    mPlayer = new IjkMediaPlayer();
    mPlayer.setOnCompletionListener(this);
    mPlayer.setOnPreparedListener(this);
    mPlayer.setOnBufferingUpdateListener(new IMediaPlayer.OnBufferingUpdateListener() {
      @Override
      public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {
      }
    });
    executorService = Executors.newFixedThreadPool(1);
  }

  public void setListener(OnAudioListener listener) {
    this.mOnAudioListener = listener;
  }

  public void setHandler(Handler handler) {
    mHandler = handler;
  }

  //  MediaPlayer的坑，在setDataSource之前调用类似getDuration、seekto的方法，onCompletion就会被调用
  @Override
  public void onCompletion(IMediaPlayer mp) {
  }

  public void playUrl(String url) {
    if (mOnAudioListener != null) {
      mOnAudioListener.onLoading(true);
    }
    try {
      if (mPlayer != null) {
        mPlayer.reset();
        mPlayer.setDataSource(url);
        mPlayer.setDisplay(null); // 强制ijk只进行音频解码播放
        mPlayer.prepareAsync();
        mPlayer.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
          @Override
          public boolean onError(IMediaPlayer mp, int what, int extra) {
            return false;
          }
        });
      }
      isPrepared = true;
    } catch (IOException e) {
    }
  }

  @Override
  public void onPrepared(IMediaPlayer mp) {
    isPrepared = false;
    //  start方法必须放到onPrepared回调中执行
    if (mOnAudioListener != null) {
      mOnAudioListener.onLoading(false);
    }
  }

  public interface OnAudioListener {
    public void onLoading(boolean isLoading);
  }
}
