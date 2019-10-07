package com.android.base.vm;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.android.http.Http;

/**
 * created by jiangshide on 2019-08-14.
 * email:18311271399@163.com
 */
public class BaseVM extends ViewModel {
    protected int page = 0;
    protected int size = 20;
    public boolean isRefresh = false;
    public String action;
    public int type;

    public Http http;

    public BaseVM() {
        if (null == http) http = Http.INSTANCE;
    }

    public static <T extends ViewModel> T of(FragmentActivity activity, Class<T> modelClass) {
        return ViewModelProviders.of(activity).get(modelClass);
    }
}
