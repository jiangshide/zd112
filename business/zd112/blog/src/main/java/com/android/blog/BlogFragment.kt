package com.android.blog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.base.BaseFragment

/**
 * created by jiangshide on 2019-09-15.
 * email:18311271399@163.com
 */
class BlogFragment : BaseFragment() {

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?

  ): View? {
    return setView(R.layout.default_recycleview)
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
//    recyclerView.create()
  }
}