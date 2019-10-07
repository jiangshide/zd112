package com.android.event.ipc.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.android.event.ZdEvent;
import com.android.event.ipc.IpcConst;
import com.android.event.ipc.decode.DecodeException;
import com.android.event.ipc.decode.IDecode;
import com.android.event.ipc.decode.ValueDecode;

/**
 * created by jiangshide on 2019-06-18.
 * email:18311271399@163.com
 */
public class LebIpcReceiver extends BroadcastReceiver {

    private IDecode decoder = new ValueDecode();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (IpcConst.ACTION.equals(intent.getAction())) {
            String key = intent.getStringExtra(IpcConst.KEY);
            try {
                Object value = decoder.decode(intent);
                if (key != null) {
                    ZdEvent.Companion
                            .get()
                            .with(key)
                            .post(value);
                }
            } catch (DecodeException e) {
                e.printStackTrace();
            }
        }
    }
}
