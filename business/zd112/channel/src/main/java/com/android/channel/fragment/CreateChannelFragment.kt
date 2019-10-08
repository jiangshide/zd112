package com.android.channel.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.base.BaseFragment
import com.android.channel.R
import com.android.channel.vm.ChannelVM
import com.android.entity.Entity
import com.android.entity.entity.ContentType
import com.android.utils.LogUtil
import com.android.widget.ZdDialog
import com.android.widget.anim.Anim
import kotlinx.android.synthetic.main.channel_fragment_create.createChannelDes
import kotlinx.android.synthetic.main.channel_fragment_create.createChannelName
import kotlinx.android.synthetic.main.channel_fragment_create.createChannelRadio
import kotlinx.android.synthetic.main.channel_fragment_create.createChannelRadioComm
import kotlinx.android.synthetic.main.channel_fragment_create.createChannelRadioPrivate
import kotlinx.android.synthetic.main.channel_fragment_create.createChannelRadioTips
import kotlinx.android.synthetic.main.channel_fragment_create.createChannelSubmit
import kotlinx.android.synthetic.main.channel_fragment_create.createChannelType
import kotlinx.android.synthetic.main.channel_fragment_create.createChannelTypeMore

/**
 * created by jiangshide on 2019-09-26.
 * email:18311271399@163.com
 */
class CreateChannelFragment : BaseFragment() {

  private lateinit var channelVM: ChannelVM
  private val list = ArrayList<String>()

  private var typeList: List<ContentType>? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    channelVM = ViewModelProviders.of(this)
        .get(ChannelVM::class.java)

    return setTitleView(R.layout.channel_fragment_create, "创建频道", true)
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    channelVM.contentType.observe(this, Observer {
      if (it.data != null && it.data.isNotEmpty()) {
        Entity.insertContentType(it.data)
        initList()
      }
    })
    channelVM.contentType()
    initList()

    createChannelTypeMore.setOnClickListener {
      if (list != null && list.size > 0) {
        ZdDialog.createList(getContext(), list)
            .setOnItemListener { parent, view, position, id ->
              createChannelType.text = list.get(position)
            }
            .show()
      }
    }

    var publish = 2
    createChannelRadio.setOnCheckedChangeListener { group, checkedId ->
      if (checkedId == createChannelRadioComm.id) {
        createChannelRadioTips.text = "每两天只能创建一个频道"
        publish = 1
      } else if (checkedId == createChannelRadioPrivate.id) {
        createChannelRadioTips.text = "每天只能创建一个频道"
        publish = 2
      }
    }
    createChannelSubmit.setOnClickListener {
      validate(publish)
    }
  }

  fun initList() {
    typeList = Entity.getContentType()
    if (typeList == null || typeList?.size == 0) return
    typeList?.forEach {
      list.add(it.name)
    }
    createChannelType.text = list[0]
  }

  fun validate(publish: Int) {
    val name = createChannelName.text?.trim()
        .toString()
    if (TextUtils.isEmpty(name)) {
      Anim.shakeView(createChannelName)
      return
    }
    val des = createChannelDes.text?.trim()
        .toString()
    if (TextUtils.isEmpty(des)) {
      Anim.shakeView(createChannelDes)
      return
    }
    createChannelType.text
    var id = 0
    typeList?.forEach {
      if (it.name.equals(createChannelType.text)) {
        id = it.id
      }
    }
    channelVM.channelCreate.observe(this, Observer {
      LogUtil.e("-----it:", it.data)
      if (it.error == null) {
        pop()
      } else {
        LogUtil.e("error:", it.error)
      }
    })
    channelVM.channel(name, des, id, publish)
  }

}