package org.acra.util;

import android.content.Context;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;
import org.acra.ACRA;

public class Installation {
    private static final String INSTALLATION = "ACRA-INSTALLATION";
    private static String sID;

    /* renamed from: id */
    public static synchronized String m3337id(Context context) {
        String str;
        synchronized (Installation.class) {
            if (sID == null) {
                File file = new File(context.getFilesDir(), INSTALLATION);
                try {
                    if (!file.exists()) {
                        writeInstallationFile(file);
                    }
                    sID = readInstallationFile(file);
                } catch (IOException e) {
                    Log.w(ACRA.LOG_TAG, "Couldn't retrieve InstallationId for " + context.getPackageName(), e);
                    str = "Couldn't retrieve InstallationId";
                } catch (RuntimeException e2) {
                    Log.w(ACRA.LOG_TAG, "Couldn't retrieve InstallationId for " + context.getPackageName(), e2);
                    str = "Couldn't retrieve InstallationId";
                }
            }
            str = sID;
        }
        return str;
    }

    /* JADX INFO: finally extract failed */
    private static String readInstallationFile(File file) {
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
        byte[] bArr = new byte[((int) randomAccessFile.length())];
        try {
            randomAccessFile.readFully(bArr);
            randomAccessFile.close();
            return new String(bArr);
        } catch (Throwable th) {
            randomAccessFile.close();
            throw th;
        }
    }

    private static void writeInstallationFile(File file) {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        try {
            fileOutputStream.write(UUID.randomUUID().toString().getBytes());
        } finally {
            fileOutputStream.close();
        }
    }
}
