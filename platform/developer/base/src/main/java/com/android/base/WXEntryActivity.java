package com.android.base;

import android.content.Intent;
import android.os.Bundle;

import com.android.widget.ZdToast;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * created by jiangshide on 2017-08-04.
 * email:18311271399@163.com
 */
public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseApplication) getApplicationContext()).mWxApi.handleIntent(getIntent(), this);
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        ((BaseApplication) getApplicationContext()).mWxApi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
    }

    @Override
    public void onResp(BaseResp baseResp) {
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                String result = getString(R.string.share_success);
                setTitle(result);
                ZdToast.fixTxt(this, getString(R.string.cancel));
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                ZdToast.fixTxt(this, getString(R.string.cancel));
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                ZdToast.fixTxt(this, getString(R.string.weixin_false));
                break;
            default:
                ZdToast.fixTxt(this, getString(R.string.back));
                break;
        }
    }

}
