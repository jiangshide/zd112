package com.android.user.fragment.mine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.android.base.BaseBean
import com.android.base.BaseFragment
import com.android.img.Img
import com.android.user.R
import com.android.widget.adapter.create
import kotlinx.android.synthetic.main.user_fragment_mine_album.view.albumItemImg

/**
 * created by jiangshide on 2019-08-17.
 * email:18311271399@163.com
 */
class AlbumFragment : BaseFragment() {

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return setView(R.layout.default_recycleview, true)
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {

    val datas = ArrayList<BaseBean>()
    for (index in 1..100) {
      val baseBean = BaseBean()
      baseBean.url = "http://pic16.nipic.com/20111006/6239936_092702973000_2.jpg"
      datas.add(baseBean)
    }

    recyclerView.create(datas, R.layout.user_fragment_mine_album, {
      Img.with(activity)
          .load(it.url)
          .into(albumItemImg)
    },{

    }, GridLayoutManager(activity, 3))
  }
}