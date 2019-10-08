package com.android.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.android.base.BaseFragment
import com.android.blog.BlogFragment

/**
 * created by jiangshide on 2019-07-24.
 * email:18311271399@163.com
 */
class HomeFragment : BaseFragment() {

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    createTabLayout(
        view, false, listOf("推荐", "世界"), listOf<Fragment>(BlogFragment(), BlogFragment()),
        savedInstanceState
    )
  }
}