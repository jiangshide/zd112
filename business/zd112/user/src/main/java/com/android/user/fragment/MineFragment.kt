package com.android.user.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.base.BaseFragment
import com.android.blog.BlogFragment
import com.android.img.Img
import com.android.user.R
import com.android.user.fragment.mine.AlbumFragment
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.user_fragment_mine.appBarLayout
import kotlinx.android.synthetic.main.user_fragment_mine.tabTitle
import kotlinx.android.synthetic.main.user_fragment_mine.tabView
import kotlinx.android.synthetic.main.user_fragment_mine.tv_user_name
import kotlinx.android.synthetic.main.user_fragment_mine_head.userAvatar

/**
 * created by jiangshide on 2019-08-17.
 * email:18311271399@163.com
 */
class MineFragment : BaseFragment(), AppBarLayout.OnOffsetChangedListener {

  companion object {
    fun newInstance(): MineFragment {
      return MineFragment()
    }
  }

  private var mMaxOffset: Int = 0

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return setView(R.layout.user_fragment_mine)
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

//    Img.loadImage("https://zd112.oss-cn-beijing.aliyuncs.com/imgs/IMG_20191003_091439.jpg?Expires=1570287645&OSSAccessKeyId=TMP.hVSUmuvnzAAtVv8anPLmck5XHumFvwA5YRGDKd5CFzBWbs5g3WRtpaccBiMb2XfEv2RiZurLDJaUxbJ5eJ7FX2Wwi5TxS6riD8ur29BVByxSJqwKkfNScCh91E9Vh7.tmp&Signature=agLylO82MPclxdGTynPf2f47ZfE%3D",userAvatar)
    Img.loadImage("https://zd112.oss-cn-beijing.aliyuncs.com/imgs/IMG_20191003_091439.jpg",userAvatar)

    tabView.setAdapter(
        tabView.create(childFragmentManager)
            .setTitles(
                getString(R.string.all),
                getString(R.string.app_album)
            ).setFragment(
                BlogFragment(),
                AlbumFragment()
            ).initTabs(activity, tabTitle, tabView)
            .setLinePagerIndicator(getColor(R.color.colorAccent))
    )
    val headerHeight = resources.getDimensionPixelSize(R.dimen.user_header_height)
    val toolHeight =
      headerHeight - resources.getDimensionPixelSize(R.dimen.user_header_tool_bar_height)
    mMaxOffset = headerHeight - toolHeight * 2//包括tabLayout的高度
    appBarLayout.addOnOffsetChangedListener(this)
  }

  override fun onOffsetChanged(
    appBarLayout: AppBarLayout?,
    verticalOffset: Int
  ) {
    if (verticalOffset == 0) {
      tv_user_name.alpha = 0f
    } else {
      val aspect = Math.abs(verticalOffset).toFloat() / mMaxOffset.toFloat()
      tv_user_name.alpha = aspect
    }
  }
}
