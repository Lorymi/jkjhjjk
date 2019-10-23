package org.acra.util;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.format.Time;
import android.util.Log;
import android.util.SparseArray;
import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.Locale;
import org.acra.ACRA;
import org.acra.ACRAConstants;

public final class ReportUtils {
    public static String getApplicationFilePath(Context context) {
        File filesDir = context.getFilesDir();
        if (filesDir != null) {
            return filesDir.getAbsolutePath();
        }
        Log.w(ACRA.LOG_TAG, "Couldn't retrieve ApplicationFilePath for : " + context.getPackageName());
        return "Couldn't retrieve ApplicationFilePath";
    }

    public static long getAvailableInternalMemorySize() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return ((long) statFs.getAvailableBlocks()) * ((long) statFs.getBlockSize());
    }

    public static String getDeviceId(Context context) {
        try {
            return ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
        } catch (RuntimeException e) {
            Log.w(ACRA.LOG_TAG, "Couldn't retrieve DeviceId for : " + context.getPackageName(), e);
            return null;
        }
    }

    public static String getLocalIpAddress() {
        boolean z;
        StringBuilder sb = new StringBuilder();
        boolean z2 = true;
        try {
            Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                Enumeration inetAddresses = ((NetworkInterface) networkInterfaces.nextElement()).getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) inetAddresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        if (!z2) {
                            sb.append(10);
                        }
                        sb.append(inetAddress.getHostAddress().toString());
                        z = false;
                    } else {
                        z = z2;
                    }
                    z2 = z;
                }
            }
        } catch (SocketException e) {
            ACRA.log.mo2452w(ACRA.LOG_TAG, e.toString());
        }
        return sb.toString();
    }

    public static String getTimeString(Time time) {
        return new SimpleDateFormat(ACRAConstants.DATE_TIME_FORMAT_STRING, Locale.ENGLISH).format(Long.valueOf(time.toMillis(true)));
    }

    public static long getTotalInternalMemorySize() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return ((long) statFs.getBlockCount()) * ((long) statFs.getBlockSize());
    }

    public static String sparseArrayToString(SparseArray sparseArray) {
        StringBuilder sb = new StringBuilder();
        if (sparseArray == null) {
            return "null";
        }
        sb.append('{');
        for (int i = 0; i < sparseArray.size(); i++) {
            sb.append(sparseArray.keyAt(i));
            sb.append(" => ");
            if (sparseArray.valueAt(i) == null) {
                sb.append("null");
            } else {
                sb.append(sparseArray.valueAt(i).toString());
            }
            if (i < sparseArray.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append('}');
        return sb.toString();
    }
}
