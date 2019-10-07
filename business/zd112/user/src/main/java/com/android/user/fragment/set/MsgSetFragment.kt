package com.android.user.fragment.set

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.base.BaseBean
import com.android.base.BaseFragment
import com.android.user.R
import com.android.widget.adapter.create
import kotlinx.android.synthetic.main.user_fragment_set_msg.view.msgItemBSwitch
import kotlinx.android.synthetic.main.user_fragment_set_msg.view.msgItemTxt

/**
 * created by jiangshide on 2019-08-17.
 * email:18311271399@163.com
 */
class MsgSetFragment : BaseFragment() {

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return setTitleView(R.layout.default_recycleview)
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    mActivity.mZdTab.showFail(activity)
    val datas = ArrayList<BaseBean>()
    val sound = BaseBean()
    sound.title = "声音"
    sound.isSelected = true
    datas.add(sound)
    val vibra = BaseBean();
    vibra.title = "震动"
    vibra.isSelected = false
    datas.add(vibra)

    recyclerView.create(datas, R.layout.user_fragment_set_msg, {
      msgItemTxt.text = it.title
      msgItemBSwitch.isChecked = it.isSelected
    }, {

    })
  }
}