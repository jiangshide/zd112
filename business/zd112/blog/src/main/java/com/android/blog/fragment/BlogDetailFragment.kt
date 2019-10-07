package com.android.blog.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.base.BaseFragment
import com.android.blog.R
import com.android.img.Img
import com.android.player.video.NiceVideoPlayer
import com.android.player.video.TxVideoPlayerController
import kotlinx.android.synthetic.main.blog_fragment_list_detail.*

/**
 * created by jiangshide on 2019-09-15.
 * email:18311271399@163.com
 */
class BlogDetailFragment : BaseFragment() {

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return setView(R.layout.blog_fragment_list_detail)
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    videoPlayer.setPlayerType(NiceVideoPlayer.TYPE_IJK)
    val videoUrl =
      "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-17_17-33-30.mp4"
    videoPlayer.setUp(videoUrl,null)
    val txVideoPlayerController = TxVideoPlayerController(activity)
    txVideoPlayerController.setTitle("办公室小野开番外了，居然在办公室开澡堂！老板还点赞？")
    txVideoPlayerController.setLenght(980000)
    Img.with(activity).load("http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-17_17-30-43.jpg").into(txVideoPlayerController.imageView())
    videoPlayer.setController(txVideoPlayerController)
  }
}