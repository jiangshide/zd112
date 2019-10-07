package com.android.cache;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * created by jiangshide on 2019-06-15.
 * email:18311271399@163.com
 */
class GetRequest {
    private static final Pattern RANGE_HEADER_PATTERN = Pattern.compile("[R,r]ange:[ ]?bytes=(\\d*)-");
    private static final Pattern URL_PATTERN = Pattern.compile("GET /(.*) HTTP");

    public final String uri;
    public final long rangeOffset;
    public final boolean partial;

    public GetRequest(String request){
        Preconditions.checkNotNull(request);
        long offset = findRangeOffset(request);
        this.rangeOffset = Math.max(0,offset);
        this.partial = offset >= 0;
        this.uri = findUri(request);
    }

    public static GetRequest read(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF_8"));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while(!TextUtils.isEmpty(line = bufferedReader.readLine())){
            stringBuilder.append(line).append('\n');
        }
        return new GetRequest(stringBuilder.toString());
    }

    private String findUri(String request){
        Matcher matcher = URL_PATTERN.matcher(request);
        if(matcher.find()){
            return matcher.group(1);
        }
        throw new IllegalArgumentException("Invalid request `"+request+"`:url not found!");
    }

    private long findRangeOffset(String request){
        Matcher matcher = RANGE_HEADER_PATTERN.matcher(request);
        if(matcher.find()){
            String rangeValue = matcher.group(1);
            return Long.parseLong(rangeValue);
        }
        return -1;
    }

    @Override
    public String toString() {
        return "GetRequest{" +
                "uri='" + uri + '\'' +
                ", rangeOffset=" + rangeOffset +
                ", partial=" + partial +
                '}';
    }
}
