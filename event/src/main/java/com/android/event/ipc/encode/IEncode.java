package com.android.event.ipc.encode;

import android.content.Intent;

/**
 * created by jiangshide on 2019-06-18.
 * email:18311271399@163.com
 */
public interface IEncode {
    void encode(Intent intent, Object value)throws EncodeException;
}
