package org.acra.jraf.android.util.activitylifecyclecallbackscompat;

import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;

class ActivityLifecycleCallbacksWrapper implements ActivityLifecycleCallbacks {
    private ActivityLifecycleCallbacksCompat mCallback;

    public ActivityLifecycleCallbacksWrapper(ActivityLifecycleCallbacksCompat activityLifecycleCallbacksCompat) {
        this.mCallback = activityLifecycleCallbacksCompat;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ActivityLifecycleCallbacksWrapper)) {
            return false;
        }
        ActivityLifecycleCallbacksWrapper activityLifecycleCallbacksWrapper = (ActivityLifecycleCallbacksWrapper) obj;
        return this.mCallback == null ? activityLifecycleCallbacksWrapper.mCallback == null : this.mCallback.equals(activityLifecycleCallbacksWrapper.mCallback);
    }

    public int hashCode() {
        if (this.mCallback != null) {
            return this.mCallback.hashCode();
        }
        return 0;
    }

    public void onActivityCreated(Activity activity, Bundle bundle) {
        this.mCallback.onActivityCreated(activity, bundle);
    }

    public void onActivityDestroyed(Activity activity) {
        this.mCallback.onActivityDestroyed(activity);
    }

    public void onActivityPaused(Activity activity) {
        this.mCallback.onActivityPaused(activity);
    }

    public void onActivityResumed(Activity activity) {
        this.mCallback.onActivityResumed(activity);
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        this.mCallback.onActivitySaveInstanceState(activity, bundle);
    }

    public void onActivityStarted(Activity activity) {
        this.mCallback.onActivityStarted(activity);
    }

    public void onActivityStopped(Activity activity) {
        this.mCallback.onActivityStopped(activity);
    }
}
