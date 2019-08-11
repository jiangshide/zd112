package com.android.permission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.android.permission.listener.IPermission;
import com.android.utils.PermissionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * created by jiangshide on 2019-08-09.
 * email:18311271399@163.com
 */
public class PermissionActivity extends Activity {

    private static IPermission permissionListener;
    private String[] permissions;
    private int requestCode;

    /**
     * 跳转到Activity申请权限
     *
     * @param context     Context
     * @param permissions PermissionProcess List
     * @param iPermission Interface
     */
    public static void PermissionRequest(Context context, String[] permissions, int requestCode, IPermission iPermission) {
        permissionListener = iPermission;
        Intent intent = new Intent(context, PermissionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle bundle = new Bundle();
        bundle.putStringArray("permission", permissions);
        bundle.putInt("requestCode", requestCode);
        intent.putExtras(bundle);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(0, 0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            permissions = bundle.getStringArray("permission");
            requestCode = bundle.getInt("requestCode", 0);
        }
        if (permissions == null || permissions.length <= 0) {
            finish();
            return;
        }
        requestPermission(permissions);
    }


    /**
     * 申请权限
     *
     * @param permissions permission list
     */
    private void requestPermission(String[] permissions) {

        if (PermissionUtils.hasSelfPermissions(this, permissions)) {
            if (permissionListener != null) {
                permissionListener.PermissionGranted();
                permissionListener = null;
            }
            finish();
            overridePendingTransition(0, 0);
        } else {
            ActivityCompat.requestPermissions(this, permissions, requestCode);
        }
    }

    /**
     * 请求权限结果
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (PermissionUtils.verifyPermissions(grantResults)) {
            if (permissionListener != null) {
                permissionListener.PermissionGranted();
            }
        } else {
            if (!PermissionUtils.shouldShowRequestPermissionRationale(this, permissions)) {
                if (permissions.length != grantResults.length) return;
                List<String> results = new ArrayList<>();
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == -1) {
                        results.add(permissions[i]);
                    }
                }
                if (permissionListener != null) {
                    permissionListener.PermissionDenied(requestCode, results);
                }
            } else {
                if (permissionListener != null) {
                    permissionListener.PermissionCanceled(requestCode);
                }
            }

        }
        permissionListener = null;
        finish();
        overridePendingTransition(0, 0);
    }
}
