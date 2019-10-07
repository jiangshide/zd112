package com.android.cache.file;

import android.text.TextUtils;

import com.android.cache.ProxyCacheUtils;

/**
 * created by jiangshide on 2019-06-14.
 * email:18311271399@163.com
 */
public class Md5FileNameGenerator implements FileNameGenerator {

    private static final int MAX_EXTENSION_LENGTH = 4;

    @Override
    public String generate(String url) {
        int dotIndex = url.lastIndexOf('.');
        int slastIndex = url.lastIndexOf('/');
        String extendsion = dotIndex != -1 && dotIndex > slastIndex && dotIndex + 2 + MAX_EXTENSION_LENGTH > url.length() ? url.substring(dotIndex + 1, url.length()) : "";
        String name = ProxyCacheUtils.computeMD5(url);
        return TextUtils.isEmpty(extendsion) ? name : name + "." + extendsion;
    }

}
