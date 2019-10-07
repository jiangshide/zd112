package com.android.blog.fragment

import android.content.ContentValues
import android.media.MediaPlayer
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import com.android.base.BaseFragment
import com.android.blog.R
import com.android.blog.bean.BlogBean
import com.android.blog.view.FullScreenVideoView
import com.android.img.Img
import com.android.utils.LogUtil
import com.android.widget.adapter.create
import com.android.widget.recycleview.listener.OnViewPagerListener
import kotlinx.android.synthetic.main.blog_fragment_full_video.view.img_thumb
import kotlinx.android.synthetic.main.blog_fragment_full_video.view.video_view

/**
 * created by jiangshide on 2019-09-22.
 * email:18311271399@163.com
 */
class BlogFullVideoFragment : BaseFragment(), OnViewPagerListener {

  override fun onPageRelease(
    isNext: Boolean,
    position: Int
  ) {
    var index = 0
    if (isNext) {
      index = 0
    } else {
      index = 1
    }
    releaseVideo(index)
  }

  @RequiresApi(VERSION_CODES.JELLY_BEAN_MR1)
  override fun onPageSelected(
    position: Int,
    isBottom: Boolean
  ) {
    playVideo(position)
  }

  override fun onInitComplete() {
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return setView(R.layout.default_recycleview, true)
  }

  @RequiresApi(VERSION_CODES.JELLY_BEAN_MR1)
  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

    recyclerView.create(BlogBean.Companion.data(), R.layout.blog_fragment_full_video, {
      Img.with(activity)
          .load(it?.url)
          .into(img_thumb)
      video_view?.setVideoPath(it?.url)
    }, {
      push(BlogDetailFragment())
    })

  }

  private fun releaseVideo(index: Int) {
    if (recyclerView == null) return
    val itemView = recyclerView.getChildAt(index)
    val videoView = itemView.findViewById<FullScreenVideoView>(R.id.video_view)
    val imgThumb = itemView.findViewById<ImageView>(R.id.img_thumb)
    val imgPlay = itemView.findViewById<ImageView>(R.id.img_play)
    videoView.stopPlayback()
    imgThumb.animate()
        .alpha(1f)
        .start()
    imgPlay.animate()
        .alpha(0f)
        .start()
  }

  @RequiresApi(VERSION_CODES.JELLY_BEAN_MR1)
  private fun playVideo(position: Int) {
    val itemView = recyclerView.getChildAt(0)
    playVideo(itemView)
  }

  @RequiresApi(VERSION_CODES.JELLY_BEAN_MR1)
  private fun playVideo(view: View) {
    val videoView = view.findViewById<FullScreenVideoView>(R.id.video_view)
    val imgPlay = view.findViewById<ImageView>(R.id.img_play)
    val imgThumb = view.findViewById<ImageView>(R.id.img_thumb)
    val rootView = view.findViewById<RelativeLayout>(R.id.root_view)
    val mediaPlayer = arrayOfNulls<MediaPlayer>(1)
    videoView.start()
    videoView.setOnInfoListener(MediaPlayer.OnInfoListener { mp, what, extra ->
      mediaPlayer[0] = mp
      Log.e(ContentValues.TAG, "onInfo")
      mp.isLooping = true
      imgThumb.animate()
          .alpha(0f)
          .setDuration(200)
          .start()
      false
    })
    videoView.setOnPreparedListener(
        MediaPlayer.OnPreparedListener { Log.e(ContentValues.TAG, "onPrepared") })


    imgPlay.setOnClickListener(object : View.OnClickListener {
      internal var isPlaying = true
      override fun onClick(v: View) {
        if (videoView.isPlaying()) {
          LogUtil.e("isPlaying:" + videoView.isPlaying())
          imgPlay.animate()
              .alpha(1f)
              .start()
          videoView.pause()
          isPlaying = false
        } else {
          LogUtil.e("isPlaying:" + videoView.isPlaying())
          imgPlay.animate()
              .alpha(0f)
              .start()
          videoView.start()
          isPlaying = true
        }
      }
    })
  }

  override fun onRefresh() {
    super.onRefresh()
    cancelRefresh()
  }

}