package com.android.permission.listener;

import java.util.List;

/**
 * created by jiangshide on 2019-08-09.
 * email:18311271399@163.com
 */
public interface IPermission {
    //同意权限
    void PermissionGranted();

    //拒绝权限并选中时不再提示
    void PermissionDenied(int requestCode, List<String> grantResults);

    //取消权限
    void PermissionCanceled(int requestCode);
}
