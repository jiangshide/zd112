package com.android.user.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import com.android.base.BaseFragment
import com.android.event.ZdEvent
import com.android.user.R
import com.android.user.fragment.set.AboutFragment
import com.android.user.fragment.set.BlackListFragment
import com.android.user.fragment.set.CacheFragment
import com.android.user.fragment.set.CreateChannelFragment
import com.android.user.fragment.set.EditProfileFragment
import com.android.user.fragment.set.MsgSetFragment
import com.android.user.fragment.set.RecyclerFragment
import com.android.utils.Constant.STORAGE_UPDATE
import com.android.utils.Constant.UPDATE_APK
import com.android.utils.SPUtil
import com.android.widget.ZdDialog
import com.android.widget.adapter.ZdListAdapter
import com.android.zdrouter.ZdRouter
import kotlinx.android.synthetic.main.user_fragment_set.defaultListView

/**
 * created by jiangshide on 2019-08-17.
 * email:18311271399@163.com
 */
class SetFragment : BaseFragment() {

  private var adapter: ZdListAdapter<String>? = null

  companion object {
    fun newInstance(): SetFragment {
      return SetFragment()
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return setView(R.layout.user_fragment_set)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)

    val listData = listOf(
        getString(R.string.modify_persion_info),
        getString(R.string.settings_history_box),
        getString(R.string.me_created_channel),
        getString(R.string.black_list),
        getString(R.string.clear_cache),
        getString(R.string.msg_notification),
        getString(R.string.about),
        getString(R.string.action_logout)
    )
    ZdEvent.get()
        .with(UPDATE_APK)
        .observes(this, Observer {
          adapter?.notifyDataSetChanged()
        })
    adapter = object :
        ZdListAdapter<String>(activity, listData, R.layout.default_list_item) {
      override fun convertView(
        position: Int,
        item: View,
        s: String
      ) {
        get<TextView>(item, R.id.listItemTitle).text = s
        var jsonAPk = SPUtil.getString(STORAGE_UPDATE)
        if (s.equals(getString(R.string.about)) && !TextUtils.isEmpty(jsonAPk)) {
          get<ImageView>(item, R.id.listItemDot).visibility = View.VISIBLE
        } else {
          get<ImageView>(item, R.id.listItemDot).visibility = View.GONE
        }
      }
    }
    defaultListView.adapter = adapter
    defaultListView.setOnItemClickListener { parent, view, position, id ->
      when (listData[position]) {
        getString(R.string.modify_persion_info) -> {
          push(EditProfileFragment()).setTitle(R.string.modify_persion_info)
        }
        getString(R.string.me_created_channel) -> {
          push(CreateChannelFragment()).setTitle(R.string.me_created_channel)
        }
        getString(R.string.settings_history_box) -> {
          push(RecyclerFragment()).setTitle(R.string.settings_history_box)
        }
        getString(R.string.black_list) -> {
          push(BlackListFragment()).setTitle(R.string.black_list)
              .setSmallTitle("黑名单")
        }
        getString(R.string.clear_cache) -> {
          push(CacheFragment()).setTitle(R.string.clear_cache)
        }
        getString(R.string.msg_notification) -> {
          push(MsgSetFragment()).setTitle(R.string.msg_notification)
        }
        getString(R.string.about) -> {
          push(AboutFragment()).setTitle(R.string.about)
        }
        getString(R.string.action_logout) -> {
          ZdDialog.create(activity!!)
              .setContent(R.string.settings_logout_tip)
              .setListener { isCancel, editMessage ->
                if (isCancel) return@setListener
                ZdRouter.getInstance()
                    .go("login/login")
                    .build(activity)
              }
              .show()
        }
      }
    }
  }
}
