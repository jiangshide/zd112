package com.android.channel

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.android.base.BaseFragment
import com.android.blog.BlogFragment
import com.android.channel.fragment.CreateChannelFragment
import com.android.channel.fragment.ManagerChannelFragment
import com.android.entity.Entity
import com.android.event.ZdEvent
import com.android.tablayout.ZdTabLayout
import com.android.widget.ZdButton
import com.android.widget.ZdViewPager

/**
 * created by jiangshide on 2019-09-13.
 * email:18311271399@163.com
 */
class ChannelFragment : BaseFragment() {

  var zdViewPager: ZdViewPager? = null

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    val titles = ArrayList<String>()
    Entity.getChannels()
        .forEach {
          titles.add(it.name)
        }
    val list = ArrayList<BlogFragment>()
    titles.forEach {
      list.add(BlogFragment())
    }
    zdViewPager = createTabLayout(
        view, true, titles, list as List<Fragment>?,
        savedInstanceState
    )
    ZdEvent.get()
        .with(CHANNEL)
        .observes(this, Observer {
          if (zdViewPager != null && titles != null && titles.size > 0 && it != null) {
            titles.forEachIndexed { index, s ->
              if (s.equals(it)) {
                zdViewPager?.setCurrentItem(index)
              }
            }
          }
        })
  }

  override fun onViewCreated(
    view: View?,
    tabTitle: ZdTabLayout?,
    dotImg: ImageView?,
    tabView: ZdViewPager?,
    tabBtn: ZdButton?,
    savedInstanceState: Bundle?
  ) {
    tabBtn?.setOnClickListener {
      push(ManagerChannelFragment()).setTitle(R.string.channleManager)
          .setRight(getString(R.string.createChannel))
          .setRightListener {
            push(CreateChannelFragment()).setTitle(R.string.createChannel)
          }
    }
  }
}

const val CHANNEL = "channel"
