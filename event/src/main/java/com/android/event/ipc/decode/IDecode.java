package com.android.event.ipc.decode;

import android.content.Intent;

/**
 * created by jiangshide on 2019-06-18.
 * email:18311271399@163.com
 */
public interface IDecode {
    Object decode(Intent intent) throws DecodeException;
}
