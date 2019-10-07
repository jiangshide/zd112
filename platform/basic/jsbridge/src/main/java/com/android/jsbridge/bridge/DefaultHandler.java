package com.android.jsbridge.bridge;

import com.android.jsbridge.listener.BridgeHandler;
import com.android.jsbridge.listener.CallBackFunction;

/**
 * created by jiangshide on 2019-08-13.
 * email:18311271399@163.com
 */
public class DefaultHandler implements BridgeHandler {

    @Override
    public void handler(String data, CallBackFunction function) {
        if(function != null){
            function.onCallBack("DefaultHandler response data");
        }
    }

}
