package com.android.cache;

/**
 * created by jiangshide on 2019-06-14.
 * email:18311271399@163.com
 */
public final class Preconditions {
    public static <T> T checkNotNull(T t){
        if(null == t){
            throw new NullPointerException();
        }
        return t;
    }

    public static void checkAllNotNull(Object... objects){
        for(Object object:objects){
            if(null == object){
                throw new NullPointerException();
            }
        }
    }

    public static <T> T checkNotNull(T t,String errorMsg){
        if(null == t){
            throw new NullPointerException(errorMsg);
        }
        return t;
    }

    static void checkArgument(boolean expression){
        if(!expression){
            throw new IllegalArgumentException();
        }
    }

    static void checkArgument(boolean expression,String errorMsg){
        if(!expression){
            throw new IllegalArgumentException(errorMsg);
        }
    }
}
