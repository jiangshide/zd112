package com.android.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.math.roundToInt

/**
 * created by jiangshide on 2019-09-22.
 * email:18311271399@163.com
 */
class ZdWaveView(
  context: Context,
  attrs: AttributeSet?
) : View(context, attrs) {

  var waveArray: List<Int>? = null

  private var columnMaxHeight: Int = 0
  private var columnMinHeight: Int = 0
  private var columnWidth: Int = 0
  private var columnGap: Int = 0
  private var columnWidthWithGap: Int = 0

  private var colorUp: Int = 0
  private var colorUpMask: Int = 0
  private var colorDown: Int = 0
  private var colorDownMask: Int = 0

  private var rect: Rect = Rect()

  private lateinit var paint: Paint

  private var halfHeight: Int = 0
  var waveStart: Int = 0
  var waveOffset: Int = 0

  companion object {
    val GOLD_SPLIT = 0.618f
  }

  init {
    val array = context.obtainStyledAttributes(attrs, R.styleable.waveStyle, 0, 0)
    if (array != null) {
      columnMaxHeight = array.getDimensionPixelSize(R.styleable.waveStyle_columnHeight, 40)
      columnMinHeight = array.getDimensionPixelSize(R.styleable.waveStyle_columnMinHeight, 1)
      columnWidth = array.getDimensionPixelSize(R.styleable.waveStyle_columnWidth, 1.5.roundToInt())
      columnGap = array.getDimensionPixelSize(R.styleable.waveStyle_columnGap, 1)
      columnWidthWithGap = columnWidth + columnGap

      colorUp =
        array.getColor(
            R.styleable.waveStyle_upColor, ContextCompat.getColor(context, R.color.white)
        )
      colorUpMask = array.getColor(
          R.styleable.waveStyle_upColor, ContextCompat.getColor(context, R.color.yellow)
      )
      colorDown = array.getColor(
          R.styleable.waveStyle_upColor, ContextCompat.getColor(context, R.color.yellow)
      )
      colorDownMask = array.getColor(
          R.styleable.waveStyle_upColor, ContextCompat.getColor(context, R.color.yellow)
      )
      paint = Paint()
      array.recycle()
    }
  }

  override fun onMeasure(
    widthMeasureSpec: Int,
    heightMeasureSpec: Int
  ) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    halfHeight = measuredHeight / 2
  }

  fun updateWaveArray(waveArray: List<Int>) {
    this.waveArray = waveArray
  }

  fun moveTo(percent: Float) {
    if (waveArray == null) return
    var tempPercent: Float = percent
    if (percent > 1) {
      tempPercent = 1f
    }
    waveOffset = (waveArray?.size!! * tempPercent * columnWidthWithGap).toInt()
    invalidate()
  }

  override fun onDraw(canvas: Canvas?) {
    super.onDraw(canvas)

    if (waveArray == null) return

    for (i in 0 until waveArray!!.size) {

      var left = i * columnWidthWithGap + waveStart - waveOffset
      var right = left + columnWidth

      if (left > measuredWidth) {
        return
      }
      if (left < 0) {
        continue
      }

      var upColumnHeight = columnMinHeight
      if (waveArray!![i] > 0) {
        upColumnHeight = (waveArray!![i] / 256f * columnMaxHeight).toInt()
      }
      var downColumnHeight = (upColumnHeight * GOLD_SPLIT).toInt()
      if (left < waveStart) {
        paint.color = colorUp
        paint.alpha = 255
      } else {
        paint.color = colorUpMask
        paint.alpha = 128
      }
      rect.set(left, halfHeight - upColumnHeight, right, halfHeight)
      canvas?.drawRect(rect, paint)
      if (left < waveStart) {
        paint.color = colorDown
        paint.alpha = 255
      } else {
        paint.color = colorDownMask
        paint.alpha = 128
      }
      rect.set(left, halfHeight, right, halfHeight + downColumnHeight)
      canvas?.drawRect(rect, paint)

    }
  }
}
