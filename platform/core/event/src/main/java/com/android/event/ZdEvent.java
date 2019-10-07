//package com.android.event;
//
//import android.arch.lifecycle.ExternalLiveData;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Build;
//import android.os.Handler;
//import android.os.Looper;
//
//import androidx.annotation.MainThread;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.lifecycle.Lifecycle;
//import androidx.lifecycle.LifecycleOwner;
//import androidx.lifecycle.Observer;
//
//import com.android.event.ipc.IpcConst;
//import com.android.event.ipc.encode.IEncode;
//import com.android.event.ipc.encode.ValueEncode;
//import com.android.event.ipc.receiver.LebIpcReceiver;
//import com.android.utils.ThreadUtil;
//import com.google.gson.Gson;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * created by jiangshide on 2019-06-18.
// * email:18311271399@163.com
// */
//public final class ZdEvent {
//    private final Map<String, LiveEvent<?>> bus;
//
//    private ZdEvent() {
//        bus = new HashMap<>();
//    }
//
//    private static class SingletonHolder {
//        private static final ZdEvent DEFAULT_BUS = new ZdEvent();
//    }
//
//    public static ZdEvent get() {
//        return SingletonHolder.DEFAULT_BUS;
//    }
//
//    private Context appContext;
//    private boolean lifecycleObserverAlwaysActive = true;
//    private boolean autoClear = false;
//    private IEncode encoder = new ValueEncode();
//    private Config config = new Config();
//    private LebIpcReceiver receiver = new LebIpcReceiver();
//
//    public synchronized <T> Observable<T> with(String key, Class<T> type) {
//        return this.with(key, type, false);
//    }
//
//    public synchronized <T> Observable<T> with(String key, Class<T> type, boolean isOut) {
//        if (!bus.containsKey(key)) {
//            bus.put(key, new LiveEvent<>(key, isOut ? type : null));
//        }
//        return (Observable<T>) bus.get(key);
//    }
//
//    public Observable<Object> with(String key) {
//        return with(key, Object.class);
//    }
//
//
//    /**
//     * use the inner class Config to set params
//     * first of all, call config to get the Config instance
//     * then, call the method of Config to config ZdEvent
//     * call this method in Application.onCreate
//     */
//
//    public Config config() {
//        return config;
//    }
//
//    public class Config {
//
//        /**
//         * lifecycleObserverAlwaysActive
//         * set if then observer can always receive message
//         * true: observer can always receive message
//         * false: observer can only receive message when resumed
//         *
//         * @param active
//         * @return
//         */
//        public Config lifecycleObserverAlwaysActive(boolean active) {
//            lifecycleObserverAlwaysActive = active;
//            return this;
//        }
//
//        /**
//         * @param clear
//         * @return true: clear livedata when no observer observe it
//         * false: not clear livedata unless app was killed
//         */
//        public Config autoClear(boolean clear) {
//            autoClear = clear;
//            return this;
//        }
//
//        /**
//         * config broadcast
//         * only if you called this method, you can use broadcastValue() to send broadcast message
//         *
//         * @param context
//         * @return
//         */
//        public Config supportBroadcast(Context context) {
//            if (context != null) {
//                appContext = context.getApplicationContext();
//            }
//            if (appContext != null) {
//                IntentFilter intentFilter = new IntentFilter();
//                intentFilter.addAction(IpcConst.ACTION);
//                appContext.registerReceiver(receiver, intentFilter);
//            }
//            return this;
//        }
//    }
//
//    public interface Observable<T> {
//
//        /**
//         * 发送一个消息，支持前台线程、后台线程发送
//         *
//         * @param value
//         */
//        void post(T value);
//
//        /**
//         * 发送一个消息，支持前台线程、后台线程发送
//         * 需要跨进程、跨APP发送消息的时候调用该方法
//         *
//         * @param value
//         */
//        void broadcast(T value);
//
//        /**
//         * 延迟发送一个消息，支持前台线程、后台线程发送
//         *
//         * @param value
//         * @param delay 延迟毫秒数
//         */
//        void postDelay(T value, long delay);
//
//        /**
//         * 发送一个消息，支持前台线程、后台线程发送
//         * 需要跨进程、跨APP发送消息的时候调用该方法
//         *
//         * @param value
//         */
//        void broadcast(T value, boolean foreground);
//
//        /**
//         * 注册一个Observer，生命周期感知，自动取消订阅
//         *
//         * @param owner
//         * @param observer
//         */
//        void observe(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer);
//
//        /**
//         * 注册一个Observer，生命周期感知，自动取消订阅
//         * 如果之前有消息发送，可以在注册时收到消息（消息同步）
//         *
//         * @param owner
//         * @param observer
//         */
//        void observeSticky(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer);
//
//        /**
//         * 注册一个Observer
//         *
//         * @param observer
//         */
//        void observeForever(@NonNull Observer<T> observer);
//
//        /**
//         * 注册一个Observer
//         * 如果之前有消息发送，可以在注册时收到消息（消息同步）
//         *
//         * @param observer
//         */
//        void observeStickyForever(@NonNull Observer<T> observer);
//
//        /**
//         * 通过observeForever或observeStickyForever注册的，需要调用该方法取消订阅
//         *
//         * @param observer
//         */
//        void removeObserver(@NonNull Observer<T> observer);
//    }
//
//    private class LiveEvent<T> implements Observable<T> {
//
//        @NonNull
//        private final String key;
//        private Class<T> type;
//        private final LifecycleLiveData<T> liveData;
//        private final Map<Observer, ObserverWrapper<T>> observerMap = new HashMap<>();
//        private final Handler mainHandler = new Handler(Looper.getMainLooper());
//
//
//        LiveEvent(@NonNull String key, Class<T> type) {
//            this.key = key;
//            this.type = type;
//            this.liveData = new LifecycleLiveData<>();
//        }
//
//        @Override
//        public void post(T value) {
//            if (ThreadUtil.isMainThread()) {
//                postInternal(value);
//            } else {
//                mainHandler.post(new PostValueTask(value));
//            }
//        }
//
//        @Override
//        public void broadcast(T value) {
//            broadcast(value, false);
//        }
//
//        @Override
//        public void postDelay(T value, long delay) {
//            mainHandler.postDelayed(new PostValueTask(value), delay);
//        }
//
//        @Override
//        public void broadcast(final T value, final boolean foreground) {
//            if (appContext != null) {
//                if (ThreadUtil.isMainThread()) {
//                    broadcastInternal(value, foreground);
//                } else {
//                    mainHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            broadcastInternal(value, foreground);
//                        }
//                    });
//                }
//            } else {
//                post(value);
//            }
//        }
//
//        @Override
//        public void observe(@NonNull final LifecycleOwner owner, @NonNull final Observer<T> observer) {
//            if (ThreadUtil.isMainThread()) {
//                observeInternal(owner, observer);
//            } else {
//                mainHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        observeInternal(owner, observer);
//                    }
//                });
//            }
//        }
//
//        @Override
//        public void observeSticky(@NonNull final LifecycleOwner owner, @NonNull final Observer<T> observer) {
//            if (ThreadUtil.isMainThread()) {
//                observeStickyInternal(owner, observer);
//            } else {
//                mainHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        observeStickyInternal(owner, observer);
//                    }
//                });
//            }
//        }
//
//        @Override
//        public void observeForever(@NonNull final Observer<T> observer) {
//            if (ThreadUtil.isMainThread()) {
//                observeForeverInternal(observer);
//            } else {
//                mainHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        observeForeverInternal(observer);
//                    }
//                });
//            }
//        }
//
//        @Override
//        public void observeStickyForever(@NonNull final Observer<T> observer) {
//            if (ThreadUtil.isMainThread()) {
//                observeStickyForeverInternal(observer);
//            } else {
//                mainHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        observeStickyForeverInternal(observer);
//                    }
//                });
//            }
//        }
//
//        @Override
//        public void removeObserver(@NonNull final Observer<T> observer) {
//            if (ThreadUtil.isMainThread()) {
//                removeObserverInternal(observer);
//            } else {
//                mainHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        removeObserverInternal(observer);
//                    }
//                });
//            }
//        }
//
//        @MainThread
//        private void postInternal(T value) {
//            liveData.setValue(value);
//        }
//
//        @MainThread
//        private void broadcastInternal(T value, boolean foreground) {
//            Intent intent = new Intent(IpcConst.ACTION);
//            if (foreground && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
//            }
//            intent.putExtra(IpcConst.KEY, key);
//            try {
//                encoder.encode(intent, value);
//                appContext.sendBroadcast(intent);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        @MainThread
//        private void observeInternal(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer) {
//            ObserverWrapper<T> observerWrapper = new ObserverWrapper<>(observer, type);
//            observerWrapper.preventNextEvent = liveData.getVersion() > ExternalLiveData.START_VERSION;
//            liveData.observe(owner, observerWrapper);
//        }
//
//        @MainThread
//        private void observeStickyInternal(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer) {
//            ObserverWrapper<T> observerWrapper = new ObserverWrapper<>(observer, type);
//            liveData.observe(owner, observerWrapper);
//        }
//
//        @MainThread
//        private void observeForeverInternal(@NonNull Observer<T> observer) {
//            ObserverWrapper<T> observerWrapper = new ObserverWrapper<>(observer, type);
//            observerWrapper.preventNextEvent = liveData.getVersion() > ExternalLiveData.START_VERSION;
//            observerMap.put(observer, observerWrapper);
//            liveData.observeForever(observerWrapper);
//        }
//
//        @MainThread
//        private void observeStickyForeverInternal(@NonNull Observer<T> observer) {
//            ObserverWrapper<T> observerWrapper = new ObserverWrapper<>(observer, type);
//            observerMap.put(observer, observerWrapper);
//            liveData.observeForever(observerWrapper);
//        }
//
//        @MainThread
//        private void removeObserverInternal(@NonNull Observer<T> observer) {
//            Observer<T> realObserver;
//            if (observerMap.containsKey(observer)) {
//                realObserver = observerMap.remove(observer);
//            } else {
//                realObserver = observer;
//            }
//            liveData.removeObserver(realObserver);
//        }
//
//        private class LifecycleLiveData<T> extends ExternalLiveData<T> {
//            @Override
//            protected Lifecycle.State observerActiveLevel() {
//                return lifecycleObserverAlwaysActive ? Lifecycle.State.CREATED : Lifecycle.State.STARTED;
//            }
//
//            @Override
//            public void removeObserver(@NonNull Observer<? super T> observer) {
//                super.removeObserver(observer);
//                if (autoClear && !liveData.hasObservers()) {
//                    ZdEvent.get().bus.remove(key);
//                }
//            }
//        }
//
//        private class PostValueTask implements Runnable {
//            private Object newValue;
//
//            public PostValueTask(@NonNull Object newValue) {
//                this.newValue = newValue;
//            }
//
//            @Override
//            public void run() {
//                postInternal((T) newValue);
//            }
//        }
//    }
//
//    private static class ObserverWrapper<T> implements Observer<T> {
//
//        @NonNull
//        private final Observer<T> observer;
//        private Class<T> type;
//        private boolean preventNextEvent = false;
//
//        ObserverWrapper(@NonNull Observer<T> observer, Class<T> type) {
//            this.observer = observer;
//            this.type = type;
//        }
//
//        @Override
//        public void onChanged(@Nullable T t) {
//            if (preventNextEvent) {
//                preventNextEvent = false;
//                return;
//            }
//            try {
//                if (null != type) {
//                    observer.onChanged(new Gson().fromJson(new Gson().toJson(t), type));
//                } else {
//                    observer.onChanged(t);
//                }
//            } catch (ClassCastException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
