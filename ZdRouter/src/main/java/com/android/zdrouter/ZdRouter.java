package com.android.zdrouter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import com.android.utils.ClassUtil;
import com.android.utils.LogUtil;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * created by jiangshide on 2019-07-23.
 * email:18311271399@163.com
 */
public class ZdRouter {

    private Context mContext;

    public final static String OBJECT = "object";

    private Map<String, Class<? extends Activity>> mActivitys;

    private ZdRouter() {
        mActivitys = new HashMap<>();
    }

    private static class ZdRouterHolder {
        private static final ZdRouter instance = new ZdRouter();
    }

    public static ZdRouter getInstance() {
        return ZdRouterHolder.instance;
    }

    public void init(Application application) {
        this.mContext = application;
        List<String> classNames = ClassUtil.getClassName(mContext, "com.android.router");
        for (String className : classNames) {
            try {
                Class<?> clazz = Class.forName(className);
                if (IZdRouter.class.isAssignableFrom(clazz)) {
                    IZdRouter iZdRouter = (IZdRouter) clazz.newInstance();
                    iZdRouter.putActivity();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void putActivity(String path, Class<? extends Activity> clazz) {
        if (!TextUtils.isEmpty(path) && clazz != null) {
            mActivitys.put(path, clazz);
        }
    }


    public Build go(String path) {
        return this.go(path, -1);
    }

    public Build go(String path, int requestCode) {
        Class<? extends Activity> clazz = mActivitys.get(path);
        if (clazz == null) return null;
        return new Build(mContext, clazz, requestCode);
    }

    public class Build<T> {
        Intent intent;
        final Class _class;
        final int requestCode;

        public Build(Context context, Class _clazz, int requestCode) {
            this._class = _clazz;
            this.intent = new Intent(context, _clazz);
            this.requestCode = requestCode;
        }

        public Build put(Intent intent) {
            if (this.intent != null) {
                this.intent = null;
            }
            this.intent = intent;
            this.intent.setClass(mContext, this._class);
            return this;
        }

        public Build put(String key, boolean value) {
            this.intent.putExtra(key, value);
            return this;
        }

        public Build put(String key, char value) {
            this.intent.putExtra(key, value);
            return this;
        }

        public Build put(String key, int value) {
            this.intent.putExtra(key, value);
            return this;
        }

        public Build put(String key, long value) {
            this.intent.putExtra(key, value);
            return this;
        }

        public Build put(String key, float value) {
            this.intent.putExtra(key, value);
            return this;
        }

        public Build put(String key, double value) {
            this.intent.putExtra(key, value);
            return this;
        }

        public Build put(String key, String value) {
            this.intent.putExtra(key, value);
            return this;
        }

        public Build put(T _class, boolean isComm) {
            if (isComm) return this.put(_class);
            String json = new Gson().toJson(_class);
            this.put(OBJECT, json);
            return this;
        }

        public Build put(T _class) {
            if (_class instanceof Parcelable) {
                this.intent.putExtra(OBJECT, (Parcelable) _class);
            } else if (_class instanceof Serializable) {
                this.intent.putExtra(OBJECT, (Serializable) _class);
            } else {
                LogUtil.e("the objects need to be serialized!");
            }
            return this;
        }

        public Build put(Bundle bundle) {
            this.intent.putExtras(bundle);
            return this;
        }

        public void build() {
            this.build(null);
        }

        public void build(Activity activity) {
            ZdRouter.getInstance().go(requestCode, intent, activity);
        }
    }

    public void go(int requestCode, Intent intent) {
        this.go(requestCode, intent, null);
    }

    @TargetApi(android.os.Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void go(int requestCode, Intent intent, Activity activity) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (requestCode >= 0) {
//            ((Activity) mContext).startActivityForResult((Activity) mContext, intent, requestCode);
            ActivityCompat.startActivityForResult((Activity) mContext, intent, requestCode, null);
        } else {
            mContext.startActivity(intent);
        }
        if (null != activity && !activity.isDestroyed()) {
            activity.finish();
        }
    }
}
