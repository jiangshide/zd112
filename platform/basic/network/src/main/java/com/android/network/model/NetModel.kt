package com.android.network.model

import com.android.network.annotation.INetType
import java.lang.reflect.Method

/**
 * created by jiangshide on 2019-07-31.
 * email:18311271399@163.com
 */
class NetModel(var type: Class<*>, @param:INetType @field:INetType @get:INetType var netType: Int, var method: Method)