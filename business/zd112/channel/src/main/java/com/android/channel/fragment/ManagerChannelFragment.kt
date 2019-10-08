package com.android.channel.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.android.base.BaseFragment
import com.android.channel.CHANNEL
import com.android.channel.R
import com.android.channel.vm.ChannelVM
import com.android.entity.Entity
import com.android.entity.entity.Channel
import com.android.event.ZdEvent
import com.android.utils.LogUtil
import com.android.widget.adapter.KAdapter
import com.android.widget.adapter.create
import kotlinx.android.synthetic.main.channel_fragment_manager.channelRecycleView
import kotlinx.android.synthetic.main.channel_fragment_manager.channeledRecycleView
import kotlinx.android.synthetic.main.channel_fragment_manager.channeledRecycleViewBtn
import kotlinx.android.synthetic.main.channel_fragment_manager.channeledRecycleViewDes
import kotlinx.android.synthetic.main.channel_fragment_manager_selected.view.channelItemBtn
import kotlinx.android.synthetic.main.channel_fragment_manager_selected.view.channelItemImg

/**
 * created by jiangshide on 2019-09-14.
 * email:18311271399@163.com
 */
class ManagerChannelFragment : BaseFragment() {

  lateinit var channeledAdapter: KAdapter<Channel>
  lateinit var channelAdapter: KAdapter<Channel>

  var channeledList = ArrayList<Channel>()
  val channelList = ArrayList<Channel>()

  private lateinit var channelVM: ChannelVM

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    channelVM = ViewModelProviders.of(this)
        .get(ChannelVM::class.java)
    return setTitleView(R.layout.channel_fragment_manager)
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {

    mActivity.mZdTab.setOnItemListener { adapterView, view, i, l ->
      channelVM.channel()
      channelVM.channel()
    }

    channeledList = Entity.getChannels() as ArrayList<Channel>
//    if (channeledList == null || channelList.size == 0) {
//      resources.getStringArray(R.array.channel)
//          .forEach {
//            val channelEntity = Channel()
//            channelEntity.name = it
//            channeledList.add(channelEntity)
//          }
//    }
    channeledRecycleViewBtn.setOnClickListener {
      if (channeledRecycleViewBtn.text.equals("编辑")) {
        channeledRecycleViewBtn.text = "完成"
        channeledRecycleViewDes.text = "拖拽可以排序"
        setStatus(true)
      } else {
        channeledRecycleViewBtn.text = "编辑"
        channeledRecycleViewDes.text = "点击进入频道"
        setStatus(false)
      }
    }

    channeledAdapter =
      channeledRecycleView.create(channeledList, R.layout.channel_fragment_manager_selected, {
        channelItemBtn.text = it.name
        if (it.isEdit) {
          channelItemImg.visibility = View.VISIBLE
        } else {
          channelItemImg.visibility = View.GONE
        }
        if (it.isSelected) {
          channelItemBtn.setTextColor(getColor(R.color.white))
        } else {
          channelItemBtn.setTextColor(getColor(R.color.black))
        }
      }, {
        LogUtil.e(" this:", this)
        if (channeledRecycleViewBtn.text.equals("完成")) {
          if (isEdit) {
            isSelected = false
            channeledAdapter.remove(this)
            channelAdapter.add(this)
          }
        } else {
          LogUtil.e("data:", this)
          setSelected(this)
          ZdEvent.get()
              .with(CHANNEL)
              .post(name)
          pop()
        }
      }, GridLayoutManager(activity, 4))
          .drag(channeledRecycleView, listOf(0, 1))

    channelAdapter =
      channelRecycleView.create(channelList, R.layout.channel_fragment_manager_selected, {
        channelItemBtn.text = "+" + it.name
      }, {
        channelAdapter.remove(this)
        this.isEdit = !channeledRecycleViewBtn.text.equals("编辑")
        channeledAdapter.add(this)
      }, GridLayoutManager(activity, 4))
  }

  fun setSelected(channel: Channel) {
    var i = 0
    channeledAdapter.datas()
        .forEach {
          channeledAdapter.datas()[i].isSelected = channel.name.equals(it.name)
          i++
          channeledAdapter.notifyDataSetChanged()
        }

  }

  fun setStatus(isEdit: Boolean) {
    val list = channeledAdapter.datas()
    for (index in list.indices) {
      channeledAdapter.datas()[index].isEdit = isEdit
      if (list[index].name.equals("推荐") || list[index].name.equals("关注")) {
        channeledAdapter.datas()[index].isEdit = false
      }
    }
    channeledAdapter.notifyDataSetChanged()
  }
}