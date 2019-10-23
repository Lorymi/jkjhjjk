package org.acra.collector;

import android.content.Context;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import org.acra.util.BoundedLinkedList;

class LogFileCollector {
    private LogFileCollector() {
    }

    /* JADX INFO: finally extract failed */
    public static String collectLogFile(Context context, String str, int i) {
        BoundedLinkedList boundedLinkedList = new BoundedLinkedList(i);
        BufferedReader bufferedReader = str.contains("/") ? new BufferedReader(new InputStreamReader(new FileInputStream(str)), 1024) : new BufferedReader(new InputStreamReader(context.openFileInput(str)), 1024);
        try {
            for (String readLine = bufferedReader.readLine(); readLine != null; readLine = bufferedReader.readLine()) {
                boundedLinkedList.add(readLine + "\n");
            }
            CollectorUtil.safeClose(bufferedReader);
            return boundedLinkedList.toString();
        } catch (Throwable th) {
            CollectorUtil.safeClose(bufferedReader);
            throw th;
        }
    }
}
