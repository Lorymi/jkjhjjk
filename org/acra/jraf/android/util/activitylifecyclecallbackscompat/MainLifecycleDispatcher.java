package org.acra.jraf.android.util.activitylifecyclecallbackscompat;

import android.app.Activity;
import android.os.Bundle;
import java.util.ArrayList;

public class MainLifecycleDispatcher implements ActivityLifecycleCallbacksCompat {
    private static final MainLifecycleDispatcher INSTANCE = new MainLifecycleDispatcher();
    private ArrayList mActivityLifecycleCallbacks = new ArrayList();

    private MainLifecycleDispatcher() {
    }

    private Object[] collectActivityLifecycleCallbacks() {
        Object[] objArr = null;
        synchronized (this.mActivityLifecycleCallbacks) {
            if (this.mActivityLifecycleCallbacks.size() > 0) {
                objArr = this.mActivityLifecycleCallbacks.toArray();
            }
        }
        return objArr;
    }

    public static MainLifecycleDispatcher get() {
        return INSTANCE;
    }

    public void onActivityCreated(Activity activity, Bundle bundle) {
        Object[] collectActivityLifecycleCallbacks = collectActivityLifecycleCallbacks();
        if (collectActivityLifecycleCallbacks != null) {
            for (Object obj : collectActivityLifecycleCallbacks) {
                ((ActivityLifecycleCallbacksCompat) obj).onActivityCreated(activity, bundle);
            }
        }
    }

    public void onActivityDestroyed(Activity activity) {
        Object[] collectActivityLifecycleCallbacks = collectActivityLifecycleCallbacks();
        if (collectActivityLifecycleCallbacks != null) {
            for (Object obj : collectActivityLifecycleCallbacks) {
                ((ActivityLifecycleCallbacksCompat) obj).onActivityDestroyed(activity);
            }
        }
    }

    public void onActivityPaused(Activity activity) {
        Object[] collectActivityLifecycleCallbacks = collectActivityLifecycleCallbacks();
        if (collectActivityLifecycleCallbacks != null) {
            for (Object obj : collectActivityLifecycleCallbacks) {
                ((ActivityLifecycleCallbacksCompat) obj).onActivityPaused(activity);
            }
        }
    }

    public void onActivityResumed(Activity activity) {
        Object[] collectActivityLifecycleCallbacks = collectActivityLifecycleCallbacks();
        if (collectActivityLifecycleCallbacks != null) {
            for (Object obj : collectActivityLifecycleCallbacks) {
                ((ActivityLifecycleCallbacksCompat) obj).onActivityResumed(activity);
            }
        }
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        Object[] collectActivityLifecycleCallbacks = collectActivityLifecycleCallbacks();
        if (collectActivityLifecycleCallbacks != null) {
            for (Object obj : collectActivityLifecycleCallbacks) {
                ((ActivityLifecycleCallbacksCompat) obj).onActivitySaveInstanceState(activity, bundle);
            }
        }
    }

    public void onActivityStarted(Activity activity) {
        Object[] collectActivityLifecycleCallbacks = collectActivityLifecycleCallbacks();
        if (collectActivityLifecycleCallbacks != null) {
            for (Object obj : collectActivityLifecycleCallbacks) {
                ((ActivityLifecycleCallbacksCompat) obj).onActivityStarted(activity);
            }
        }
    }

    public void onActivityStopped(Activity activity) {
        Object[] collectActivityLifecycleCallbacks = collectActivityLifecycleCallbacks();
        if (collectActivityLifecycleCallbacks != null) {
            for (Object obj : collectActivityLifecycleCallbacks) {
                ((ActivityLifecycleCallbacksCompat) obj).onActivityStopped(activity);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void registerActivityLifecycleCallbacks(ActivityLifecycleCallbacksCompat activityLifecycleCallbacksCompat) {
        synchronized (this.mActivityLifecycleCallbacks) {
            this.mActivityLifecycleCallbacks.add(activityLifecycleCallbacksCompat);
        }
    }

    /* access modifiers changed from: 0000 */
    public void unregisterActivityLifecycleCallbacks(ActivityLifecycleCallbacksCompat activityLifecycleCallbacksCompat) {
        synchronized (this.mActivityLifecycleCallbacks) {
            this.mActivityLifecycleCallbacks.remove(activityLifecycleCallbacksCompat);
        }
    }
}
