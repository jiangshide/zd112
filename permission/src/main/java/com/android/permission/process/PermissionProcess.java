package com.android.permission.process;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;

import com.android.permission.PermissionActivity;
import com.android.permission.R;
import com.android.permission.annotation.Permission;
import com.android.permission.annotation.PermissionResult;
import com.android.permission.listener.IPermission;
import com.android.permission.model.GrantModel;
import com.android.utils.AppUtil;
import com.android.utils.PermissionUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * created by jiangshide on 2019-08-09.
 * email:18311271399@163.com
 */
@Aspect
public final class PermissionProcess {

    private Context mContext;

    private static final String PERMISSION_REQUEST_POINTCUT =
            "execution(@com.android.permission.annotation.Permission * *(..))";

    @Pointcut(PERMISSION_REQUEST_POINTCUT + " && @annotation(permission)")
    public void requestPermissionMethod(Permission permission) {
    }

    @Around("requestPermissionMethod(permission)")
    public void AroundJoinPoint(final ProceedingJoinPoint joinPoint, final Permission permission) {
        final Object object = joinPoint.getThis();
        if (object == null) return;

        if (object instanceof Context) {
            mContext = (Context) object;
        } else if (object instanceof Fragment) {
            mContext = ((Fragment) object).getActivity();
        } else if (object instanceof android.support.v4.app.Fragment) {
            mContext = ((android.support.v4.app.Fragment) object).getActivity();
        } else {
            Object[] objects = joinPoint.getArgs();
            if (objects.length > 0) {
                if (objects[0] instanceof Context) {
                    mContext = (Context) objects[0];
                } else {
                    mContext = AppUtil.getApplicationContext();
                }
            } else {
                mContext = AppUtil.getApplicationContext();
            }
        }
        if (mContext == null || permission == null) return;
        PermissionActivity.PermissionRequest(mContext, permission.value(),
                permission.requestCode(), new IPermission() {
                    @Override
                    public void PermissionGranted() {
                        try {
                            joinPoint.proceed();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }

                    @Override
                    public void PermissionDenied(int requestCode, List<String> grantResults) {
                        if (permission.isSetting()) {
                            StringBuffer stringBuffer = new StringBuffer();
                            for (String str : permission.value()) {
                                str = str.substring(str.lastIndexOf(".") + 1, str.length());
                                stringBuffer.append(str).append("|");
                            }
                            String str = stringBuffer.toString();
                            str = str.substring(0, str.length() - 1);
                            new AlertDialog.Builder(mContext)
                                    .setTitle(mContext.getString(R.string.tips))
                                    .setMessage(str + mContext.getString(R.string.permission_tips))
                                    .setPositiveButton(mContext.getString(R.string.go_setting), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            PermissionUtils.setting(mContext);
                                        }
                                    })
                                    .setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            try {
                                                joinPoint.proceed();
                                            } catch (Throwable throwable) {
                                                throwable.printStackTrace();
                                            }
                                        }
                                    })
                                    .create().show();
                            return;
                        }
                        result(object, requestCode, grantResults, false, false);
                    }

                    @Override
                    public void PermissionCanceled(int requestCode) {
                        result(object, requestCode, null, true, false);
                    }
                });
    }

    private boolean result(Object object, int requestCode, List<String> grantResults, boolean isCancel, boolean isSupport) {
        Class<?> clazz = isSupport ? object.getClass().getSuperclass() : object.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        if (methods.length == 0) return false;
        for (Method method : methods) {
            if (isHasAnnotation(method, PermissionResult.class)) {
                PermissionResult permissionDenied = method.getAnnotation(PermissionResult.class);
                if (null == permissionDenied) return false;
                GrantModel grantModel = new GrantModel();
                grantModel.context = mContext;
                grantModel.requestCode = requestCode;
                grantModel.isCancel = isCancel;
                if (null != grantResults && grantResults.size() > 0) {
                    grantModel.grantResults = grantResults;
                }
                try {
                    method.invoke(object, grantModel);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return !isSupport ? result(object, requestCode, grantResults, isCancel, true) : false;
    }

    private boolean isHasAnnotation(Method method, Class _clazz) {
        boolean isHasAnnotation = method.isAnnotationPresent(_clazz);
        if (isHasAnnotation) {
            method.setAccessible(true);
            Class<?>[] types = method.getParameterTypes();
            return types.length == 1;
        }
        return false;
    }
}
