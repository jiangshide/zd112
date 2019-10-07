package com.android.user

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.android.base.BaseFragment
import com.android.tablayout.ZdTabLayout
import com.android.user.fragment.MineFragment
import com.android.user.fragment.SetFragment
import com.android.widget.ZdButton
import com.android.widget.ZdViewPager

/**
 * created by jiangshide on 2019-07-24.
 * email:18311271399@163.com
 */
class UserFragment : BaseFragment() {

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    createTabLayout(view,false, listOf(getString(com.android.base.R.string.me),
        getString(com.android.base.R.string.app_settings)), listOf(MineFragment.newInstance(),
        SetFragment.newInstance()),savedInstanceState)
  }
}
