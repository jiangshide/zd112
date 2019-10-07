package com.android.channel.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.base.BaseFragment
import com.android.channel.EVENT_CHANNEL
import com.android.channel.R
import com.android.event.ZdEvent
import com.android.widget.adapter.create
import kotlinx.android.synthetic.main.channel_fragment_channel_list.view.channelListItemTxt

/**
 * created by jiangshide on 2019-09-26.
 * email:18311271399@163.com
 */
class ChannelListFragment : BaseFragment() {

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return setTitleView(R.layout.default_recycleview, true)
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    val titles = ArrayList<String>()
    val arr = resources.getStringArray(R.array.channel)
    arr.forEach {
      titles.add(it)
    }
    recyclerView.create(titles, R.layout.channel_fragment_channel_list, {
      channelListItemTxt.text = it
    }, {
      ZdEvent.get().with(EVENT_CHANNEL).post(this)
      pop()
    })
  }
}