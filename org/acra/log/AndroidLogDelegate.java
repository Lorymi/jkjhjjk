package org.acra.log;

import android.util.Log;

public final class AndroidLogDelegate implements ACRALog {
    /* renamed from: d */
    public int mo2443d(String str, String str2) {
        return Log.d(str, str2);
    }

    /* renamed from: d */
    public int mo2444d(String str, String str2, Throwable th) {
        return Log.d(str, str2, th);
    }

    /* renamed from: e */
    public int mo2445e(String str, String str2) {
        return Log.e(str, str2);
    }

    /* renamed from: e */
    public int mo2446e(String str, String str2, Throwable th) {
        return Log.e(str, str2, th);
    }

    public String getStackTraceString(Throwable th) {
        return Log.getStackTraceString(th);
    }

    /* renamed from: i */
    public int mo2448i(String str, String str2) {
        return Log.i(str, str2);
    }

    /* renamed from: i */
    public int mo2449i(String str, String str2, Throwable th) {
        return Log.i(str, str2, th);
    }

    /* renamed from: v */
    public int mo2450v(String str, String str2) {
        return Log.v(str, str2);
    }

    /* renamed from: v */
    public int mo2451v(String str, String str2, Throwable th) {
        return Log.v(str, str2, th);
    }

    /* renamed from: w */
    public int mo2452w(String str, String str2) {
        return Log.w(str, str2);
    }

    /* renamed from: w */
    public int mo2453w(String str, String str2, Throwable th) {
        return Log.w(str, str2, th);
    }

    /* renamed from: w */
    public int mo2454w(String str, Throwable th) {
        return Log.w(str, th);
    }
}
