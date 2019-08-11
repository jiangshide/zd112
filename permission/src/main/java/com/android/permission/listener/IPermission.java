package com.android.permission;

import java.util.List;

/**
 * created by jiangshide on 2019-08-09.
 * email:18311271399@163.com
 */
public interface IPermission {
    //同意权限
    void PermissionGranted();

    //拒绝权限并且选中不再提示
    void PermissionDenied(int requestCode, List<String> denyList);

    //取消权限
    void PermissionCanceled(int requestCode);
}
