package org.acra.collector;

import android.os.Process;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import org.acra.ACRA;
import org.acra.ACRAConstants;
import org.acra.util.BoundedLinkedList;

class LogCatCollector {
    private static final int DEFAULT_TAIL_COUNT = 100;

    LogCatCollector() {
    }

    public static String collectLogCat(String str) {
        int i;
        BufferedReader bufferedReader;
        BufferedReader bufferedReader2 = null;
        int myPid = Process.myPid();
        CharSequence charSequence = (!ACRA.getConfig().logcatFilterByPid() || myPid <= 0) ? null : Integer.toString(myPid) + "):";
        ArrayList arrayList = new ArrayList();
        arrayList.add("logcat");
        if (str != null) {
            arrayList.add("-b");
            arrayList.add(str);
        }
        ArrayList arrayList2 = new ArrayList(Arrays.asList(ACRA.getConfig().logcatArguments()));
        int indexOf = arrayList2.indexOf("-t");
        if (indexOf <= -1 || indexOf >= arrayList2.size()) {
            i = -1;
        } else {
            i = Integer.parseInt((String) arrayList2.get(indexOf + 1));
            if (Compatibility.getAPILevel() < 8) {
                arrayList2.remove(indexOf + 1);
                arrayList2.remove(indexOf);
                arrayList2.add("-d");
            }
        }
        if (i <= 0) {
            i = 100;
        }
        BoundedLinkedList boundedLinkedList = new BoundedLinkedList(i);
        arrayList.addAll(arrayList2);
        try {
            final Process exec = Runtime.getRuntime().exec((String[]) arrayList.toArray(new String[arrayList.size()]));
            bufferedReader = new BufferedReader(new InputStreamReader(exec.getInputStream()), ACRAConstants.DEFAULT_BUFFER_SIZE_IN_BYTES);
            try {
                Log.d(ACRA.LOG_TAG, "Retrieving logcat output...");
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            do {
                            } while (exec.getErrorStream().read(new byte[ACRAConstants.DEFAULT_BUFFER_SIZE_IN_BYTES]) >= 0);
                        } catch (IOException e) {
                        }
                    }
                }).start();
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    if (charSequence != null) {
                        if (!readLine.contains(charSequence)) {
                        }
                    }
                    boundedLinkedList.add(readLine + "\n");
                }
                CollectorUtil.safeClose(bufferedReader);
            } catch (IOException e) {
                e = e;
                bufferedReader2 = bufferedReader;
                try {
                    Log.e(ACRA.LOG_TAG, "LogCatCollector.collectLogCat could not retrieve data.", e);
                    CollectorUtil.safeClose(bufferedReader2);
                    return boundedLinkedList.toString();
                } catch (Throwable th) {
                    th = th;
                    bufferedReader = bufferedReader2;
                    CollectorUtil.safeClose(bufferedReader);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                CollectorUtil.safeClose(bufferedReader);
                throw th;
            }
        } catch (IOException e2) {
            e = e2;
            Log.e(ACRA.LOG_TAG, "LogCatCollector.collectLogCat could not retrieve data.", e);
            CollectorUtil.safeClose(bufferedReader2);
            return boundedLinkedList.toString();
        } catch (Throwable th3) {
            th = th3;
            bufferedReader = null;
            CollectorUtil.safeClose(bufferedReader);
            throw th;
        }
        return boundedLinkedList.toString();
    }
}
