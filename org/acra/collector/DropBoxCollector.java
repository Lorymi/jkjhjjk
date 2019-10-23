package org.acra.collector;

import android.content.Context;
import android.text.format.Time;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import org.acra.ACRA;

final class DropBoxCollector {
    private static final String NO_RESULT = "N/A";
    private static final String[] SYSTEM_TAGS = {"system_app_anr", "system_app_wtf", "system_app_crash", "system_server_anr", "system_server_wtf", "system_server_crash", "BATTERY_DISCHARGE_INFO", "SYSTEM_RECOVERY_LOG", "SYSTEM_BOOT", "SYSTEM_LAST_KMSG", "APANIC_CONSOLE", "APANIC_THREADS", "SYSTEM_RESTART", "SYSTEM_TOMBSTONE", "data_app_strictmode"};

    DropBoxCollector() {
    }

    public static String read(Context context, String[] strArr) {
        try {
            String dropBoxServiceName = Compatibility.getDropBoxServiceName();
            if (dropBoxServiceName == null) {
                return NO_RESULT;
            }
            Object systemService = context.getSystemService(dropBoxServiceName);
            Method method = systemService.getClass().getMethod("getNextEntry", new Class[]{String.class, Long.TYPE});
            if (method == null) {
                return "";
            }
            Time time = new Time();
            time.setToNow();
            time.minute -= ACRA.getConfig().dropboxCollectionMinutes();
            time.normalize(false);
            long millis = time.toMillis(false);
            ArrayList<String> arrayList = new ArrayList<>();
            if (ACRA.getConfig().includeDropBoxSystemTags()) {
                arrayList.addAll(Arrays.asList(SYSTEM_TAGS));
            }
            if (strArr != null && strArr.length > 0) {
                arrayList.addAll(Arrays.asList(strArr));
            }
            if (arrayList.isEmpty()) {
                return "No tag configured for collection.";
            }
            StringBuilder sb = new StringBuilder();
            for (String str : arrayList) {
                sb.append("Tag: ").append(str).append(10);
                Object invoke = method.invoke(systemService, new Object[]{str, Long.valueOf(millis)});
                if (invoke == null) {
                    sb.append("Nothing.").append(10);
                } else {
                    Method method2 = invoke.getClass().getMethod("getText", new Class[]{Integer.TYPE});
                    Method method3 = invoke.getClass().getMethod("getTimeMillis", null);
                    Method method4 = invoke.getClass().getMethod("close", null);
                    while (invoke != null) {
                        long longValue = ((Long) method3.invoke(invoke, null)).longValue();
                        time.set(longValue);
                        sb.append("@").append(time.format2445()).append(10);
                        String str2 = (String) method2.invoke(invoke, new Object[]{Integer.valueOf(500)});
                        if (str2 != null) {
                            sb.append("Text: ").append(str2).append(10);
                        } else {
                            sb.append("Not Text!").append(10);
                        }
                        method4.invoke(invoke, null);
                        invoke = method.invoke(systemService, new Object[]{str, Long.valueOf(longValue)});
                    }
                }
            }
            return sb.toString();
        } catch (SecurityException e) {
            Log.i(ACRA.LOG_TAG, "DropBoxManager not available.");
        } catch (NoSuchMethodException e2) {
            Log.i(ACRA.LOG_TAG, "DropBoxManager not available.");
        } catch (IllegalArgumentException e3) {
            Log.i(ACRA.LOG_TAG, "DropBoxManager not available.");
        } catch (IllegalAccessException e4) {
            Log.i(ACRA.LOG_TAG, "DropBoxManager not available.");
        } catch (InvocationTargetException e5) {
            Log.i(ACRA.LOG_TAG, "DropBoxManager not available.");
        } catch (NoSuchFieldException e6) {
            Log.i(ACRA.LOG_TAG, "DropBoxManager not available.");
        }
        return NO_RESULT;
    }
}
