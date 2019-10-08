package com.android.publish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.android.base.BaseFragment
import com.android.event.ZdEvent
import com.android.file.ZdFile
import com.android.file.ZdFile.OnUploadListener
import com.android.img.Img
import com.android.publish.vm.PublishVM
import com.android.utils.LogUtil
import com.android.widget.adapter.KAdapter
import com.android.widget.adapter.create
import com.zhihu.matisse.MimeType
import kotlinx.android.synthetic.main.publish_fragment.publishContents
import kotlinx.android.synthetic.main.publish_fragment.publishEditText
import kotlinx.android.synthetic.main.publish_fragment_item.view.publishItemBtn
import kotlinx.android.synthetic.main.publish_fragment_item.view.publishItemImg

/**
 * created by jiangshide on 2019-10-02.
 * email:18311271399@163.com
 */
class PublishFragment : BaseFragment(), OnUploadListener {

  private lateinit var publishVm: PublishVM

  override fun onSuccess(urls: String) {
    val name = publishEditText.text?.trim()
        .toString()
    publishVm.publish.observe(this, Observer {
      LogUtil.e("-----it:", it)
      if (it != null) {
        pop()
      }
    })
    publishVm.publish(name, name, 1, "北京市长阳", 1, urls)
  }

  override fun onFaile(msg: String?) {
  }

  override fun onProgress(progress: Int) {
    ZdFile.getInstance()
        .showProgress(activity, progress)
  }

  var list = ArrayList<String>()
  lateinit var listAdapter: KAdapter<String>

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    publishVm = ViewModelProviders.of(this)
        .get(PublishVM::class.java)
    return setTitleView(R.layout.publish_fragment)
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    mActivity.mZdTab.setRightListener {
      val uploadList = ArrayList<String>()
      listAdapter.datas()
          .forEach {
            if (!it.equals("+")) {
              uploadList.add(it)
            }
          }
      ZdFile.getInstance()
          .upload(ZdFile.IMG, uploadList, this)
          .go()
    }
    ZdEvent.get()
        .with("list")
        .observes(this, Observer {
          if (it != null && listAdapter != null) {
            var data = it as ArrayList<String>
            listAdapter.add(data)
            var size = listAdapter.itemCount
            if (size > 9) {
              listAdapter.remove(size - 1)
            }
          }
        })
    list = arguments?.getStringArrayList("list") as ArrayList<String>
    if (list.size < 9) {
      list.add("+")
    }
    listAdapter = publishContents.create(list, R.layout.publish_fragment_item, {
      if (!it.equals("+")) {
        Img.loadImageFromDisk(it, publishItemImg)
        publishItemBtn.visibility = View.GONE
      } else {
        publishItemBtn.visibility = View.VISIBLE
        publishItemBtn.setOnClickListener {
          var mimeType: Set<MimeType>? = null
          listAdapter.datas()
              .forEach {
                if (MimeType.isImage(it)) {
                  mimeType = MimeType.ofImage()
                } else if (MimeType.isGif(it)) {
                  mimeType = MimeType.ofGif()
                } else if (MimeType.isVideo(it)) {
                  mimeType = MimeType.ofVideo()
                } else {
                  mimeType = MimeType.ofAll()
                }
                selecteImgs(listAdapter.itemCount - 1, mimeType)
                return@forEach
              }
        }
      }
    }, {}, GridLayoutManager(activity, 3))
        .drag(publishContents, listOf())
  }
}