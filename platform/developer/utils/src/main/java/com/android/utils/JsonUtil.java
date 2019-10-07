package com.android.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * created by jiangshide on 2019-08-17.
 * email:18311271399@163.com
 */
public final class JsonUtil<T> {

    private static final String ISO_DATETIME_FORMAT_SORT = "yyyy-MM-dd HH:mm:ss";

    /**
     * Null serialize is used because else Gson will ignore all null fields.
     */
    private static Gson gson = new GsonBuilder().serializeNulls().excludeFieldsWithoutExposeAnnotation()
            .setDateFormat(ISO_DATETIME_FORMAT_SORT).create();

    private static Gson noStrictGson = new GsonBuilder().serializeNulls()
            .setDateFormat(ISO_DATETIME_FORMAT_SORT).create();

    /**
     * To Json Converter using Goolge's Gson Package, No Need @Expose Annotation
     * <p>
     * this method converts a simple object to a json string
     *
     * @param obj
     * @return a json string
     */
    public static <T> String toJsonNoStrict(final T obj) {
        return noStrictGson.toJson(obj);
    }

    /**
     * Converts a map of objects using Google's Gson Package
     *
     * @param map
     * @return a json string
     */
    public static String toJson(final Map<String, ?> map) {
        return gson.toJson(map);
    }

    /**
     * Converts a collection of objects using Google's Gson Package
     *
     * @param list
     * @return a json string array
     */
    public static <T> String toJson(final List<T> list) {
        return gson.toJson(list);
    }

    /**
     * Returns the specific object given the Json String
     *
     * @param <T>
     * @param jsonString
     * @return a specific object as defined by the user calling the method
     */
    public static <T> T fromJsonNoStrict(final String jsonString, final Class<T> classOfT) {
        try {
            return (T) noStrictGson.fromJson(jsonString, classOfT);
        } catch (Exception e) {
            LogUtil.e("json can not convert to " + classOfT.getName(), e);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T fromJson(final String jsonString, final Type typeOf) {
        try {
            return (T) gson.fromJson(jsonString, typeOf);
        } catch (Exception e) {
            LogUtil.e("json can not convert to " + typeOf.toString(), e);
            return null;
        }
    }

    /**
     * Returns a list of specified object from the given json array
     *
     * @param <T>
     * @param jsonString
     * @param type       the type defined by the user
     * @return a list of specified objects as given in the json array
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> fromJsonToList(final String jsonString, final Type type) {
        try {
            return (List<T>) gson.fromJson(jsonString, type);
        } catch (Exception e) {
            LogUtil.e("json can not convert to " + type.getClass().getName(), e);
            return null;
        }
    }

    public static String getJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public ArrayList<T> getFileToCitys(Context context, String fileName, T clazz) {
        return getCitys(getJson(context, fileName), clazz);
    }

    public ArrayList<T> getCitys(String result, T clazz) {
        ArrayList<T> dataArrayList = new ArrayList<>();
        if (TextUtils.isEmpty(result)) return dataArrayList;
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                T entity = (T) gson.fromJson(data.optJSONObject(i).toString(), clazz.getClass());
                dataArrayList.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataArrayList;
    }

    /**
     * Returns the specific object given the Json String
     *
     * @return a specific object as defined by the user calling the method
     */
    public static <T> T fromJson(final String jsonString, final Class<T> classOfT) {
        try {
            return (T) gson.fromJson(jsonString, classOfT);
        } catch (Exception e) {
            LogUtil.e(e);
            return null;
        }
    }

    /**
     * To Json Converter using Goolge's Gson Package, Need @Expose Annotation
     * <p>
     * this method converts a simple object to a json string
     *
     * @return a json string
     */
    public static <T> String toJson(final T obj) {
        try {
            return gson.toJson(obj);
        } catch (Exception e) {
            LogUtil.e(e);
            return null;
        }
    }

    /**
     *
     * @param json
     * @return
     */
    public static Map<?, ?> toMap(String json) {
        try {
            return gson.fromJson(json, Map.class);
        } catch (Exception e) {
            LogUtil.e(e);
            return null;
        }
    }

    /**
     *
     * @param clazz
     * @return
     */
    public static Map<?, ?> toMap(Object clazz) {
        try {
            return toMap(gson.toJson(clazz));
        } catch (Exception e) {
            LogUtil.e(e);
            return null;
        }
    }

    /**
     *
     * @param map
     * @return
     */
    public static String encodeMap(Map<?, ?> map) {
        try {
            return gson.toJson(map);
        } catch (Exception e) {
            LogUtil.e(e);
            return null;
        }
    }
}
