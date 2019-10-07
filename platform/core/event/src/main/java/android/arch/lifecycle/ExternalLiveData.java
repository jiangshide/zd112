//package android.arch.lifecycle;
//
//import androidx.annotation.NonNull;
//
//import androidx.lifecycle.Lifecycle;
//import androidx.lifecycle.LifecycleOwner;
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//import androidx.lifecycle.Observer;
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//
//import static androidx.lifecycle.Lifecycle.State.CREATED;
//import static androidx.lifecycle.Lifecycle.State.DESTROYED;
//
///**
// * created by jiangshide on 2019-06-18.
// * email:18311271399@163.com
// */
//public class ExternalLiveData<T> extends MutableLiveData<T> {
//
//    public static final int START_VERSION = LiveData.START_VERSION;
//
//    @Override
//    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
//        if (owner.getLifecycle().getCurrentState() == DESTROYED) {
//            // ignore
//            return;
//        }
//        try {
//            //use ExternalLifecycleBoundObserver instead of LifecycleBoundObserver
//            LiveData.LifecycleBoundObserver wrapper = new ExternalLifecycleBoundObserver(owner, observer);
//            LiveData.LifecycleBoundObserver existing = (LiveData.LifecycleBoundObserver) callMethodPutIfAbsent(observer, wrapper);
//            if (existing != null && !existing.isAttachedTo(owner)) {
//                throw new IllegalArgumentException("Cannot add the same observer"
//                        + " with different lifecycles");
//            }
//            if (existing != null) {
//                return;
//            }
//            owner.getLifecycle().addObserver(wrapper);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public int getVersion() {
//        return super.getVersion();
//    }
//
//    /**
//     * determine when the observer is active, means the observer can receive message
//     * the default value is CREATED, means if the observer's state is above create,
//     * for example, the onCreate() of activity is called
//     * you can change this value to CREATED/STARTED/RESUMED
//     * determine on witch state, you can receive message
//     *
//     * @return Lifecycle.State
//     */
//    protected Lifecycle.State observerActiveLevel() {
//        return CREATED;
//    }
//
//    class ExternalLifecycleBoundObserver extends LiveData.LifecycleBoundObserver {
//
//        ExternalLifecycleBoundObserver(@NonNull LifecycleOwner owner, Observer<? super T> observer) {
//            super(owner, observer);
//        }
//
//        @Override
//        boolean shouldBeActive() {
//            return mOwner.getLifecycle().getCurrentState().isAtLeast(observerActiveLevel());
//        }
//    }
//
//    private Object getFieldObservers() throws Exception {
//        Field fieldObservers = LiveData.class.getDeclaredField("mObservers");
//        fieldObservers.setAccessible(true);
//        return fieldObservers.get(this);
//    }
//
//    private Object callMethodPutIfAbsent(Object observer, Object wrapper) throws Exception {
//        Object mObservers = getFieldObservers();
//        Class<?> classOfSafeIterableMap = mObservers.getClass();
//        Method putIfAbsent = classOfSafeIterableMap.getDeclaredMethod("putIfAbsent",
//                Object.class, Object.class);
//        putIfAbsent.setAccessible(true);
//        return putIfAbsent.invoke(mObservers, observer, wrapper);
//    }
//}
