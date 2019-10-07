package com.android.blog.bean

/**
 * created by jiangshide on 2019-09-15.
 * email:18311271399@163.com
 */
class BlogBean(
  var id: Long,
  var name: String,
  var url: String,
  var img: String,
  var selected: Boolean,
  var isShow: Boolean

) {
  companion object {
    private val imgs = arrayOf(
        "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-17_17-30-43.jpg",
        "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-10_10-09-58.jpg",
        "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-03_12-52-08.jpg",
        "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/04/2017-04-28_18-18-22.jpg",
        "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/04/2017-04-26_10-00-28.jpg",
        "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/04/2017-04-21_16-37-16.jpg",
        "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/04/2017-04-21_16-37-16.jpg"
    )
    private val videos = arrayOf(
        "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-17_17-33-30.mp4",
        "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-10_10-20-26.mp4",
        "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-03_13-02-41.mp4",
        "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/04/2017-04-28_18-20-56.mp4",
        "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/04/2017-04-26_10-06-25.mp4",
        "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/04/2017-04-21_16-41-07.mp4",
        "http://ovv.qianpailive.com/20171208/260cfb6b7552661c131b383a11338f94?auth_key=1568083922-66-0-e8944d7decedea875e3f2f7b42d1825d&a=yl3a0hoifg6s&vid=DVjdqPRJB58d&u=21353673&t=3071275013"
    )

    val datas = ArrayList<BlogBean>()

    fun data(): ArrayList<BlogBean> {
      imgs.forEachIndexed { index, i ->
        datas.add(
            BlogBean(
                index.toLong(), "janeky:" + index, videos[index],
                i, true, false
            )
        )
      }
      return datas
    }
  }

}


