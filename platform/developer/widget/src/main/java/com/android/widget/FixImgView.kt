package com.android.widget

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import com.android.img.Img

/**
 * created by jiangshide on 2019-10-08.
 * email:18311271399@163.com
 */
class FixImgView(
  context: Context?,
  attrs: AttributeSet?
) : ImageView(context, attrs) {

  private var imageUrl: String? = null

  fun setLayoutAndLoadImage(
    width: Int,
    height: Int,
    url: String
  ) {
    if (layoutParams == null) {
      layoutParams = ViewGroup.LayoutParams(width, height)
    } else {
      if (layoutParams.width != width || layoutParams.height != height) {
        layoutParams.width = width
        layoutParams.height = height
      } else {
        // 布局没有改变，就直接加载图片
        Img.loadImage(imageUrl, this)
      }
    }
    this.imageUrl = url
  }

  override fun onLayout(
    changed: Boolean,
    left: Int,
    top: Int,
    right: Int,
    bottom: Int
  ) {
    super.onLayout(changed, left, top, right, bottom)
    // 重新layout后设置图片
    Img.loadImage(imageUrl, this)
  }
}