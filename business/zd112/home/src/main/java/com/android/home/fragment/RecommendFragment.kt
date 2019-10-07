package com.android.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.base.BaseFragment
import com.android.home.R

/**
 * created by jiangshide on 2019-08-18.
 * email:18311271399@163.com
 */
class RecommendFragment : BaseFragment() {

  val url =
    "http://1256204895.vod2.myqcloud.com/c42a0e71vodtransgzp1256204895/a4b719025285890793088638893/v.f40.mp4";

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return setView(R.layout.home_recommend)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
  }
}