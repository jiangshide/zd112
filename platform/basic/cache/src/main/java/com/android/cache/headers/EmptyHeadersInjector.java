package com.android.cache.headers;

import java.util.HashMap;
import java.util.Map;

/**
 * created by jiangshide on 2019-06-14.
 * email:18311271399@163.com
 */
public class EmptyHeadersInjector implements HeaderInjector{
    @Override
    public Map<String, String> addHeaders(String url) {
        return new HashMap<>();
    }
}
