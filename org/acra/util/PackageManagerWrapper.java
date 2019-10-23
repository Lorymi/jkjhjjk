package org.acra.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import org.acra.ACRA;

public final class PackageManagerWrapper {
    private final Context context;

    public PackageManagerWrapper(Context context2) {
        this.context = context2;
    }

    public PackageInfo getPackageInfo() {
        PackageInfo packageInfo = null;
        PackageManager packageManager = this.context.getPackageManager();
        if (packageManager == null) {
            return packageInfo;
        }
        try {
            return packageManager.getPackageInfo(this.context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            Log.v(ACRA.LOG_TAG, "Failed to find PackageInfo for current App : " + this.context.getPackageName());
            return packageInfo;
        } catch (RuntimeException e2) {
            return packageInfo;
        }
    }

    public boolean hasPermission(String str) {
        PackageManager packageManager = this.context.getPackageManager();
        if (packageManager == null) {
            return false;
        }
        try {
            return packageManager.checkPermission(str, this.context.getPackageName()) == 0;
        } catch (RuntimeException e) {
            return false;
        }
    }
}
