package com.android.user.fragment.set

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.base.BaseFragment
import com.android.event.ZdEvent
import com.android.http.download.Download
import com.android.user.R
import com.android.user.vm.UserVM
import com.android.user.vm.data.UpdateResp
import com.android.user.vm.data.Version
import com.android.utils.AppUtil
import com.android.utils.Constant.STORAGE_UPDATE
import com.android.utils.Constant.UPDATE_APK
import com.android.utils.FileUtil
import com.android.utils.JsonUtil
import com.android.utils.LogUtil
import com.android.utils.SPUtil
import com.android.utils.SystemUtil
import com.android.widget.ZdDialog
import com.android.widget.ZdToast
import com.android.widget.adapter.ZdListAdapter
import kotlinx.android.synthetic.main.user_fragment_set_about.aboutListView
import kotlinx.android.synthetic.main.user_fragment_set_about.aboutVersion
import kotlinx.android.synthetic.main.user_fragment_set_about.dialogProgressBar
import kotlinx.android.synthetic.main.user_fragment_set_about.dialogProgressBarL
import kotlinx.android.synthetic.main.user_fragment_set_about.dialogProgressBarTxt
import java.io.File

/**
 * created by jiangshide on 2019-08-17.
 * email:18311271399@163.com
 */
class AboutFragment : BaseFragment() {

  private lateinit var userVM: UserVM
  private var adapter: ZdListAdapter<Version>? = null
  private var versionArr: ArrayList<Version>? = null
  private var versionNum: String? = ""

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    ZdEvent.get()
        .with("UPDATE_API")
        .observes(this, Observer {
          if (dialogProgressBar != null) {
            dialogProgressBarTxt.text = "${it}%"
            dialogProgressBar.setProgress(it as Int)
          }
        })
    userVM = ViewModelProviders.of(this)
        .get(UserVM::class.java)
    return setTitleView(R.layout.user_fragment_set_about)
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

    aboutVersion.setText("Version  " + AppUtil.getAppVersionName() + "~测试版本")

    val json = SPUtil.getString(STORAGE_UPDATE)
    var name = getString(R.string.checkVersion)
    var url = ""
    if (!TextUtils.isEmpty(json)) {
      val update = JsonUtil.fromJson(json, UpdateResp::class.java)
      versionNum = update.version
      val updateApk = SPUtil.getString(UPDATE_APK)
      name = getString(R.string.downVersion)
      url = update.url
      if (!TextUtils.isEmpty(updateApk)) {
        name = getString(R.string.updateVersion)
      }
      ZdEvent.get()
          .with(UPDATE_APK)
          .post(UPDATE_APK)
    }

    var version = Version(name, versionNum!!, url, false)
    versionArr = ArrayList()
    versionArr!!.add(version)
    adapter = object :
        ZdListAdapter<Version>(activity, versionArr, R.layout.default_list_item) {
      override fun convertView(
        position: Int,
        item: View,
        s: Version
      ) {
        get<TextView>(item, R.id.listItemTitle).text = s.name
        if (!TextUtils.isEmpty(s.version)) {
          get<TextView>(item, R.id.listItemDes).text = s.version
          get<ImageView>(item, R.id.listItemDot).visibility = View.VISIBLE
        } else {
          get<ImageView>(item, R.id.listItemDot).visibility = View.GONE
        }
      }
    }
    aboutListView.adapter = adapter
    aboutListView.setOnItemClickListener { parent, view, position, id ->
      when (adapter?.getItem(position)?.name) {
        getString(R.string.checkVersion) -> {
          val json = SPUtil.getString(STORAGE_UPDATE)
          if (!TextUtils.isEmpty(json)) {
            val update = JsonUtil.fromJson(json, UpdateResp::class.java)
            val updateApk = SPUtil.getString(UPDATE_APK)
            if (update != null && !TextUtils.isEmpty(update.url) && !TextUtils.isEmpty(updateApk)) {
              ZdDialog.create(activity)
                  .setTitles(update.title)
                  .setContent(update.desc)
                  .setListener { isCancel, editMessage ->
                    if (isCancel) return@setListener
                    SystemUtil.installApkFile(updateApk)
                  }
                  .show()
              return@setOnItemClickListener
            } else {
              SPUtil.clear(STORAGE_UPDATE)
              SPUtil.clear(UPDATE_APK)
            }
          }
          showLoading()
          userVM.update()
        }
        getString(R.string.downVersion) -> {
          var url = adapter?.getItem(position)
              ?.url
          LogUtil.e("download~apk:", url)
          download(url)
        }
        getString(R.string.updateVersion) -> {
          val updateApk = SPUtil.getString(UPDATE_APK)
          LogUtil.e("------------updateApk:", updateApk)
          if (!TextUtils.isEmpty(updateApk)) {
            SystemUtil.installApkFile(updateApk)
          }
        }
      }
    }
    userVM.update.observe(this, Observer {
      hiddle()
      if (it?.data != null) {
        versionNum = it!!.data?.version
        if (it!!.data != null && it.data != null && !TextUtils.isEmpty(
                it.data?.url
            ) && (it.data?.url?.contains(
                "http"
            )!! || it.data?.url?.contains("https")!!)
        ) {
          it.data?.createAt = System.currentTimeMillis()
          val json = JsonUtil.toJson(it.data)
          SPUtil.putString(STORAGE_UPDATE, json)
          ZdEvent.get()
              .with(UPDATE_APK)
              .post(UPDATE_APK)
          versionArr?.get(0)
              ?.name = getString(R.string.downVersion)
          versionArr?.get(0)
              ?.version = versionNum as String
          versionArr?.get(0)
              ?.url = it.data?.url!!
          adapter?.notifyDataSetChanged()
          ZdDialog.create(activity)
              .setTitles(it.data?.title)
              .setContent(it.data?.desc)
              .setRightTxt("下载更新")
              .setListener { isCancel, editMessage ->
                if (isCancel) return@setListener
                download(it.data?.url)
              }
              .show()
        } else {
          ZdToast.txt(getString(R.string.noUpdate))
        }
      } else {
        ZdToast.txt(getString(R.string.noUpdate))
      }
    })

    isDowning()
  }

  fun download(url: String?) {
    dialogProgressBarL.visibility = View.VISIBLE
    http?.download(url!!,
        FileUtil.getPath("apk"),
        AppUtil.getAppName() + ".apk", object : Download.OnDownloadListener {
      override fun onDownloadSuccess(file: File?) {
        var fileUrl: String = file!!.absolutePath
        LogUtil.e(
            "---------file:",
            fileUrl,
            " | adapter:",
            adapter,
            " | dialogProgressBarL:",
            dialogProgressBarL
        )
        SPUtil.putString(UPDATE_APK, fileUrl)
        if (adapter != null && dialogProgressBarL != null) {
          adapter?.getItem(0)
              ?.name = getString(R.string.updateVersion)
          adapter?.getItem(0)
              ?.version = versionNum as String
          adapter?.notifyDataSetChanged()
          dialogProgressBarL.visibility = View.GONE
        }
        ZdEvent.get()
            .with(UPDATE_APK)
            .post(UPDATE_APK)
//                                    SystemUtils.installApkFile(fileUrl)
      }

      override fun onDownloading(progress: Int) {
        if (dialogProgressBarTxt != null) {
          dialogProgressBarTxt.text = "${progress}%"
          dialogProgressBar.setProgress(progress)
          isDowning()
        }
        ZdEvent.get()
            .with("UPDATE_API")
            .post(progress)
      }

      override fun onDownloadFailed(e: Exception?) {
        LogUtil.e("update~e:", e)
      }

    })
  }

  fun isDowning() {
    if (http.dowload()?.isDownling!!) {
      dialogProgressBarL.visibility = View.VISIBLE
      adapter?.getItem(0)
          ?.name = getString(R.string.downing)
      adapter?.getItem(0)
          ?.version = versionNum as String
      adapter?.notifyDataSetChanged()
    } else {
      dialogProgressBarL.visibility = View.GONE
      val jsonApk = SPUtil.getString(STORAGE_UPDATE)
      var name = getString(R.string.checkVersion)
      if (!TextUtils.isEmpty(jsonApk)) {
        name = getString(R.string.downVersion)
      }
      val updateApk = SPUtil.getString(UPDATE_APK)
      if (!TextUtils.isEmpty(updateApk)) {
        name = getString(R.string.updateVersion)
      }
      adapter?.getItem(0)
          ?.name = name
      adapter?.notifyDataSetChanged()
    }
  }
}