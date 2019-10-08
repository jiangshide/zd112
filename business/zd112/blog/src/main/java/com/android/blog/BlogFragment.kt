package com.android.blog

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.base.BaseFragment
import com.android.blog.vm.BlogVM
import com.android.entity.entity.Blog
import com.android.img.Img
import com.android.utils.ScreenUtil
import com.android.widget.ZdDialog
import com.android.widget.adapter.ZdListAdapter
import kotlinx.android.synthetic.main.blog_fragment.blogListView
import kotlinx.android.synthetic.main.img1.blogAlbumImg

/**
 * created by jiangshide on 2019-09-15.
 * email:18311271399@163.com
 */
class BlogFragment : BaseFragment() {

  private lateinit var blog: BlogVM

  var maxImageHeight = 0
  var imageWidth = 0

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?

  ): View? {
    blog = ViewModelProviders.of(this)
        .get(BlogVM::class.java)
    return setView(R.layout.blog_fragment, true)
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {

    if (imageWidth == 0) {
      imageWidth = ScreenUtil.getScreenWidth(activity)
      maxImageHeight = ScreenUtil.getScreenHeight(activity)
    }

    blog.blog.observe(this, Observer {
      cancelRefresh()
      if (it.data != null) {
//        recyclerView.create(it.data as MutableList<Blog>, R.layout.blog_fragment_list, {
//          LogUtil.e("--------url:", it.url)
//          if (TextUtils.isEmpty(it.url)) return@create
//          if (!it.url.contains(",")) {
//            Img.with(activity)
//                .load(it.url)
//                .into(blogAlbumImg)
////                Img.loadImage(it.url,item?.findViewById(R.id.blogAlbumImg))
//            blogAlbumL1?.visibility = View.VISIBLE
//            blogAlbumL2?.visibility = View.GONE
//            blogAlbumL3?.visibility = View.GONE
//            blogAlbumL4?.visibility = View.GONE
//            return@create
//          }
//          val list = it.url.split(",")
//          if (list.size == 2) {
//                Img.with(activity).load(list[0]).into(blogAlbumImg1)
//                Img.with(activity).load(list[1]).into(blogAlbumImg2)
//
////            Img.loadImage(list[0], blogAlbumImg1)
////            Img.loadImage(list[1], blogAlbumImg2)
//
//            blogAlbumL1?.visibility = View.GONE
//            blogAlbumL2?.visibility = View.VISIBLE
//            blogAlbumL3?.visibility = View.GONE
//            blogAlbumL4?.visibility = View.GONE
//            return@create
//          }
//          if (list.size == 3) {
//                Img.with(activity).load(list[0]).into(blogAlbumImg31)
//                Img.with(activity).load(list[1]).into(blogAlbumImg32)
//                Img.with(activity).load(list[2]).into(blogAlbumImg33)
////            Img.loadImage(list[0], blogAlbumImg31)
////            Img.loadImage(list[1], blogAlbumImg32)
////            Img.loadImage(list[2], blogAlbumImg33)
//
//            blogAlbumL1?.visibility = View.GONE
//            blogAlbumL2?.visibility = View.GONE
//            blogAlbumL3?.visibility = View.VISIBLE
//            blogAlbumL4?.visibility = View.GONE
//            return@create
//          }
//          Img.with(activity)
//              .load(list[0])
//              .into(blogAlbumImg41)
//          Img.with(activity)
//              .load(list[1])
//              .into(blogAlbumImg42)
//          Img.with(activity)
//              .load(list[2])
//              .into(blogAlbumImg43)
//          Img.with(activity)
//              .load(list[3])
//              .into(blogAlbumImg44)
//          blogAlbumL1?.visibility = View.GONE
//          blogAlbumL2?.visibility = View.GONE
//          blogAlbumL3?.visibility = View.GONE
//          blogAlbumL4?.visibility = View.VISIBLE
//          val lastNum = list.size - 4
//          if (lastNum > 0) {
//            blogAlbumNums?.visibility = View.VISIBLE
//            blogAlbumNums?.text = "${lastNum}+"
//          } else {
//            blogAlbumNums?.visibility = View.GONE
//          }
//        })

        blogListView.adapter =
          object : ZdListAdapter<Blog>(activity, it.data, R.layout.blog_fragment_list) {
            override fun convertView(
              position: Int,
              item: View?,
              it: Blog
            ) {

              item?.findViewById<ImageView>(R.id.blogItemMore)?.setOnClickListener {
                ZdDialog.createList(getContext(), listOf("举报","不喜欢"))
                    .setOnItemListener { parent, view, position, id ->
                    }
                    .show()
              }

              val blogAlbumL1 = item?.findViewById<LinearLayout>(R.id.blogAlbumL1)
              val blogAlbumL2 = item?.findViewById<LinearLayout>(R.id.blogAlbumL2)
              val blogAlbumL3 = item?.findViewById<LinearLayout>(R.id.blogAlbumL3)
              val blogAlbumL4 = item?.findViewById<LinearLayout>(R.id.blogAlbumL4)
              if (TextUtils.isEmpty(it.url)) return
              if (!it.url.contains(",")) {
//                var height = (imageWidth.toFloat() / blog.imgs[0].width * blog.imgs[0].height).toInt()
//                if (height > maxImageHeight) {
//                  height = maxImageHeight
//                }
//                blogAlbumImg?.setLayoutAndLoadImage(
//                    imageWidth,
//                    maxImageHeight,
//                    Img.thumbAliOssUrl(it.url, imageWidth)
//                )
                val layoutParams = blogAlbumImg?.layoutParams
                layoutParams?.width = imageWidth
                layoutParams?.height = maxImageHeight
                blogAlbumImg?.layoutParams = layoutParams
//                Img.loadImage(
//                    Img.thumbAliOssUrl(it.url, imageWidth), item?.findViewById(R.id.blogAlbumImg)
//                )
                Img.with(activity)
                    .load(Img.thumbAliOssUrl(it.url, imageWidth))
                    .into(item?.findViewById(R.id.blogAlbumImg))
                blogAlbumL1?.visibility = View.VISIBLE
                blogAlbumL2?.visibility = View.GONE
                blogAlbumL3?.visibility = View.GONE
                blogAlbumL4?.visibility = View.GONE
                return
              }
              val list = it.url.split(",")
              if (list.size == 2) {
//                Img.with(activity).load(list[0]).into(item?.findViewById(R.id.blogAlbumImg1))
//                Img.with(activity).load(list[1]).into(item?.findViewById(R.id.blogAlbumImg2))

                val blogAlbumImg1 = item?.findViewById<ImageView>(R.id.blogAlbumImg1)
                val blogAlbumImg2 = item?.findViewById<ImageView>(R.id.blogAlbumImg2)
                val layoutParams1 =  blogAlbumImg1?.layoutParams
                layoutParams1?.width = imageWidth/2
                layoutParams1?.height = maxImageHeight/2
                blogAlbumImg1?.layoutParams = layoutParams1

                val layoutParams2 =  blogAlbumImg2?.layoutParams
                layoutParams2?.width = imageWidth/2
                layoutParams2?.height = maxImageHeight/2
                blogAlbumImg2?.layoutParams = layoutParams2

                Img.loadImage(list[0], blogAlbumImg1)
                Img.loadImage(list[1], blogAlbumImg2)

                blogAlbumL1?.visibility = View.GONE
                blogAlbumL2?.visibility = View.VISIBLE
                blogAlbumL3?.visibility = View.GONE
                blogAlbumL4?.visibility = View.GONE
                return
              }
              if (list.size == 3) {
//                Img.with(activity).load(list[0]).into(item?.findViewById(R.id.blogAlbumImg31))
//                Img.with(activity).load(list[1]).into(item?.findViewById(R.id.blogAlbumImg32))
//                Img.with(activity).load(list[2]).into(item?.findViewById(R.id.blogAlbumImg33))
                Img.loadImage(list[0], item?.findViewById(R.id.blogAlbumImg31))
                Img.loadImage(list[1], item?.findViewById(R.id.blogAlbumImg32))
                Img.loadImage(list[2], item?.findViewById(R.id.blogAlbumImg33))

                blogAlbumL1?.visibility = View.GONE
                blogAlbumL2?.visibility = View.GONE
                blogAlbumL3?.visibility = View.VISIBLE
                blogAlbumL4?.visibility = View.GONE
                return
              }
//              Img.with(activity).load(list[0]).into(item?.findViewById(R.id.blogAlbumImg41))
//              Img.with(activity).load(list[1]).into(item?.findViewById(R.id.blogAlbumImg42))
//              Img.with(activity).load(list[2]).into(item?.findViewById(R.id.blogAlbumImg43))
//              Img.with(activity).load(list[3]).into(item?.findViewById(R.id.blogAlbumImg44))

              Img.loadImage(list[0], item?.findViewById(R.id.blogAlbumImg41))
              Img.loadImage(list[1], item?.findViewById(R.id.blogAlbumImg42))
              Img.loadImage(list[2], item?.findViewById(R.id.blogAlbumImg43))
              Img.loadImage(list[3], item?.findViewById(R.id.blogAlbumImg44))

              val numsView = item?.findViewById<TextView>(R.id.blogAlbumNums)
              blogAlbumL1?.visibility = View.GONE
              blogAlbumL2?.visibility = View.GONE
              blogAlbumL3?.visibility = View.GONE
              blogAlbumL4?.visibility = View.VISIBLE
              val lastNum = list.size - 4
              if (lastNum > 0) {
                numsView?.visibility = View.VISIBLE
                numsView?.text = "${lastNum}+"
              } else {
                numsView?.visibility = View.GONE
              }

//              val blogItemImg = item?.findViewById<ImageView>(R.id.blogItemImg)
//              LogUtil.e(
//                  "--------position:", position, " | url:", it.url, " | blogItemImg:", blogItemImg
//              )
//              if (!TextUtils.isEmpty(it.url)) {
//                var url = it.url
//                if (it.url.contains(",")) {
//                  url = it.url.split(",")[0]
//                }
//                LogUtil.e("---------url~to:", url)
////                Img.loadImage(
////                    url,
////                    blogItemImg
////                )
////                Img.with(activity)
////                    .load(url)
////                    .into(blogItemImg)
//              }
            }
          }
      }
    })
    blog.blog()
  }

  override fun onRefresh() {
    super.onRefresh()
    blog.blog()
  }

  override fun onLoadMore() {
    super.onLoadMore()
    blog.blog()
  }
}