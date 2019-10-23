package org.acra;

import android.content.Context;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.Map.Entry;
import org.acra.collector.CrashReportData;

final class CrashReportPersister {
    private static final int CONTINUE = 3;
    private static final int IGNORE = 5;
    private static final int KEY_DONE = 4;
    private static final String LINE_SEPARATOR = "\n";
    private static final int NONE = 0;
    private static final int SLASH = 1;
    private static final int UNICODE = 2;
    private final Context context;

    CrashReportPersister(Context context2) {
        this.context = context2;
    }

    private void dumpString(StringBuilder sb, String str, boolean z) {
        int i;
        if (z || 0 >= str.length() || str.charAt(0) != ' ') {
            i = 0;
        } else {
            sb.append("\\ ");
            i = SLASH;
        }
        while (i < str.length()) {
            char charAt = str.charAt(i);
            switch (charAt) {
                case 9:
                    sb.append("\\t");
                    break;
                case 10:
                    sb.append("\\n");
                    break;
                case 12:
                    sb.append("\\f");
                    break;
                case 13:
                    sb.append("\\r");
                    break;
                default:
                    if ("\\#!=:".indexOf(charAt) >= 0 || (z && charAt == ' ')) {
                        sb.append('\\');
                    }
                    if (charAt >= ' ' && charAt <= '~') {
                        sb.append(charAt);
                        break;
                    } else {
                        String hexString = Integer.toHexString(charAt);
                        sb.append("\\u");
                        for (int i2 = 0; i2 < 4 - hexString.length(); i2 += SLASH) {
                            sb.append("0");
                        }
                        sb.append(hexString);
                        break;
                    }
                    break;
            }
            i += SLASH;
        }
    }

    private boolean isEbcdic(BufferedInputStream bufferedInputStream) {
        byte read;
        do {
            read = (byte) bufferedInputStream.read();
            if (read == -1 || read == 35 || read == 10 || read == 61) {
                return false;
            }
        } while (read != 21);
        return true;
    }

    /* JADX WARNING: type inference failed for: r8v0 */
    /* JADX WARNING: type inference failed for: r5v1 */
    /* JADX WARNING: type inference failed for: r8v1 */
    /* JADX WARNING: type inference failed for: r5v2 */
    /* JADX WARNING: type inference failed for: r8v2 */
    /* JADX WARNING: type inference failed for: r5v3 */
    /* JADX WARNING: type inference failed for: r3v5 */
    /* JADX WARNING: type inference failed for: r3v6 */
    /* JADX WARNING: type inference failed for: r3v7 */
    /* JADX WARNING: type inference failed for: r5v6 */
    /* JADX WARNING: type inference failed for: r5v7 */
    /* JADX WARNING: type inference failed for: r5v8 */
    /* JADX WARNING: type inference failed for: r8v3 */
    /* JADX WARNING: type inference failed for: r5v9 */
    /* JADX WARNING: type inference failed for: r5v10 */
    /* JADX WARNING: type inference failed for: r5v11 */
    /* JADX WARNING: type inference failed for: r5v12 */
    /* JADX WARNING: type inference failed for: r3v19 */
    /* JADX WARNING: type inference failed for: r0v21 */
    /* JADX WARNING: type inference failed for: r0v22 */
    /* JADX WARNING: type inference failed for: r3v23 */
    /* JADX WARNING: type inference failed for: r5v13 */
    /* JADX WARNING: type inference failed for: r5v14 */
    /* JADX WARNING: type inference failed for: r3v27 */
    /* JADX WARNING: type inference failed for: r0v23 */
    /* JADX WARNING: type inference failed for: r0v24 */
    /* JADX WARNING: type inference failed for: r8v9 */
    /* JADX WARNING: type inference failed for: r5v15 */
    /* JADX WARNING: type inference failed for: r0v25 */
    /* JADX WARNING: type inference failed for: r5v16 */
    /* JADX WARNING: type inference failed for: r5v17 */
    /* JADX WARNING: type inference failed for: r5v18 */
    /* JADX WARNING: type inference failed for: r0v32 */
    /* JADX WARNING: type inference failed for: r3v39 */
    /* JADX WARNING: type inference failed for: r5v20 */
    /* JADX WARNING: type inference failed for: r8v10 */
    /* JADX WARNING: type inference failed for: r8v11 */
    /* JADX WARNING: type inference failed for: r8v12 */
    /* JADX WARNING: type inference failed for: r8v13 */
    /* JADX WARNING: type inference failed for: r8v14 */
    /* JADX WARNING: type inference failed for: r8v15 */
    /* JADX WARNING: type inference failed for: r8v16 */
    /* JADX WARNING: type inference failed for: r8v17 */
    /* JADX WARNING: type inference failed for: r8v18 */
    /* JADX WARNING: type inference failed for: r8v19 */
    /* JADX WARNING: type inference failed for: r8v20 */
    /* JADX WARNING: type inference failed for: r8v21 */
    /* JADX WARNING: type inference failed for: r8v22 */
    /* JADX WARNING: type inference failed for: r5v24 */
    /* JADX WARNING: type inference failed for: r8v23 */
    /* JADX WARNING: type inference failed for: r5v25 */
    /* JADX WARNING: type inference failed for: r3v43 */
    /* JADX WARNING: type inference failed for: r3v44 */
    /* JADX WARNING: type inference failed for: r3v45 */
    /* JADX WARNING: type inference failed for: r0v34 */
    /* JADX WARNING: Code restructure failed: missing block: B:140:0x0199, code lost:
        r3 = r3;
     */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r8v2
      assigns: []
      uses: []
      mth insns count: 230
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 9 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized org.acra.collector.CrashReportData load(java.io.Reader r15) {
        /*
            r14 = this;
            monitor-enter(r14)
            r3 = 0
            r5 = 0
            r4 = 0
            r0 = 40
            char[] r7 = new char[r0]     // Catch:{ all -> 0x0031 }
            r1 = 0
            r2 = -1
            r0 = 1
            org.acra.collector.CrashReportData r9 = new org.acra.collector.CrashReportData     // Catch:{ all -> 0x0031 }
            r9.<init>()     // Catch:{ all -> 0x0031 }
            java.io.BufferedReader r10 = new java.io.BufferedReader     // Catch:{ all -> 0x0031 }
            r6 = 8192(0x2000, float:1.14794E-41)
            r10.<init>(r15, r6)     // Catch:{ all -> 0x0031 }
            r8 = r0
            r13 = r4
            r4 = r5
            r5 = r3
            r3 = r13
        L_0x001c:
            int r0 = r10.read()     // Catch:{ all -> 0x0031 }
            r6 = -1
            if (r0 != r6) goto L_0x0034
            r0 = 2
            if (r5 != r0) goto L_0x0157
            r0 = 4
            if (r3 > r0) goto L_0x0157
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException     // Catch:{ all -> 0x0031 }
            java.lang.String r1 = "luni.08"
            r0.<init>(r1)     // Catch:{ all -> 0x0031 }
            throw r0     // Catch:{ all -> 0x0031 }
        L_0x0031:
            r0 = move-exception
            monitor-exit(r14)
            throw r0
        L_0x0034:
            char r0 = (char) r0
            int r6 = r7.length     // Catch:{ all -> 0x0031 }
            if (r1 != r6) goto L_0x0043
            int r6 = r7.length     // Catch:{ all -> 0x0031 }
            int r6 = r6 * 2
            char[] r6 = new char[r6]     // Catch:{ all -> 0x0031 }
            r11 = 0
            r12 = 0
            java.lang.System.arraycopy(r7, r11, r6, r12, r1)     // Catch:{ all -> 0x0031 }
            r7 = r6
        L_0x0043:
            r6 = 2
            if (r5 != r6) goto L_0x01a4
            r6 = 16
            int r6 = java.lang.Character.digit(r0, r6)     // Catch:{ all -> 0x0031 }
            if (r6 < 0) goto L_0x0059
            int r4 = r4 << 4
            int r6 = r6 + r4
            int r4 = r3 + 1
            r3 = 4
            if (r4 >= r3) goto L_0x01a1
            r3 = r4
            r4 = r6
            goto L_0x001c
        L_0x0059:
            r5 = 4
            if (r3 > r5) goto L_0x0064
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException     // Catch:{ all -> 0x0031 }
            java.lang.String r1 = "luni.09"
            r0.<init>(r1)     // Catch:{ all -> 0x0031 }
            throw r0     // Catch:{ all -> 0x0031 }
        L_0x0064:
            r5 = r4
            r4 = r3
        L_0x0066:
            r6 = 0
            int r3 = r1 + 1
            char r11 = (char) r5     // Catch:{ all -> 0x0031 }
            r7[r1] = r11     // Catch:{ all -> 0x0031 }
            r1 = 10
            if (r0 == r1) goto L_0x0079
            r1 = 133(0x85, float:1.86E-43)
            if (r0 == r1) goto L_0x0079
            r1 = r3
            r3 = r4
            r4 = r5
            r5 = r6
            goto L_0x001c
        L_0x0079:
            r1 = r3
            r3 = r6
        L_0x007b:
            r6 = 1
            if (r3 != r6) goto L_0x00b7
            r3 = 0
            switch(r0) {
                case 10: goto L_0x009b;
                case 13: goto L_0x0095;
                case 98: goto L_0x00a2;
                case 102: goto L_0x00a5;
                case 110: goto L_0x00a8;
                case 114: goto L_0x00ab;
                case 116: goto L_0x00ae;
                case 117: goto L_0x00b1;
                case 133: goto L_0x009b;
                default: goto L_0x0082;
            }     // Catch:{ all -> 0x0031 }
        L_0x0082:
            r13 = r0
            r0 = r3
            r3 = r13
        L_0x0085:
            r6 = 0
            r8 = 4
            if (r0 != r8) goto L_0x008b
            r0 = 0
            r2 = r1
        L_0x008b:
            int r8 = r1 + 1
            r7[r1] = r3     // Catch:{ all -> 0x0031 }
            r1 = r8
            r3 = r4
            r8 = r6
            r4 = r5
            r5 = r0
            goto L_0x001c
        L_0x0095:
            r3 = 3
            r13 = r4
            r4 = r5
            r5 = r3
            r3 = r13
            goto L_0x001c
        L_0x009b:
            r3 = 5
            r13 = r4
            r4 = r5
            r5 = r3
            r3 = r13
            goto L_0x001c
        L_0x00a2:
            r0 = 8
            goto L_0x0082
        L_0x00a5:
            r0 = 12
            goto L_0x0082
        L_0x00a8:
            r0 = 10
            goto L_0x0082
        L_0x00ab:
            r0 = 13
            goto L_0x0082
        L_0x00ae:
            r0 = 9
            goto L_0x0082
        L_0x00b1:
            r3 = 2
            r4 = 0
            r5 = r3
            r3 = r4
            goto L_0x001c
        L_0x00b7:
            switch(r0) {
                case 10: goto L_0x00f3;
                case 13: goto L_0x00fd;
                case 33: goto L_0x00d1;
                case 35: goto L_0x00d1;
                case 58: goto L_0x0136;
                case 61: goto L_0x0136;
                case 92: goto L_0x012a;
                case 133: goto L_0x00fd;
                default: goto L_0x00ba;
            }     // Catch:{ all -> 0x0031 }
        L_0x00ba:
            boolean r6 = java.lang.Character.isWhitespace(r0)     // Catch:{ all -> 0x0031 }
            if (r6 == 0) goto L_0x014b
            r6 = 3
            if (r3 != r6) goto L_0x00c4
            r3 = 5
        L_0x00c4:
            if (r1 == 0) goto L_0x0199
            if (r1 == r2) goto L_0x0199
            r6 = 5
            if (r3 != r6) goto L_0x0141
            r13 = r4
            r4 = r5
            r5 = r3
            r3 = r13
            goto L_0x001c
        L_0x00d1:
            if (r8 == 0) goto L_0x00ba
        L_0x00d3:
            int r0 = r10.read()     // Catch:{ all -> 0x0031 }
            r6 = -1
            if (r0 != r6) goto L_0x00e0
            r13 = r4
            r4 = r5
            r5 = r3
            r3 = r13
            goto L_0x001c
        L_0x00e0:
            char r0 = (char) r0     // Catch:{ all -> 0x0031 }
            r6 = 13
            if (r0 == r6) goto L_0x0199
            r6 = 10
            if (r0 == r6) goto L_0x0199
            r6 = 133(0x85, float:1.86E-43)
            if (r0 != r6) goto L_0x00d3
            r13 = r4
            r4 = r5
            r5 = r3
            r3 = r13
            goto L_0x001c
        L_0x00f3:
            r0 = 3
            if (r3 != r0) goto L_0x00fd
            r3 = 5
            r13 = r4
            r4 = r5
            r5 = r3
            r3 = r13
            goto L_0x001c
        L_0x00fd:
            r3 = 0
            r0 = 1
            if (r1 > 0) goto L_0x0105
            if (r1 != 0) goto L_0x0121
            if (r2 != 0) goto L_0x0121
        L_0x0105:
            r6 = -1
            if (r2 != r6) goto L_0x0109
            r2 = r1
        L_0x0109:
            java.lang.String r6 = new java.lang.String     // Catch:{ all -> 0x0031 }
            r8 = 0
            r6.<init>(r7, r8, r1)     // Catch:{ all -> 0x0031 }
            java.lang.Class<org.acra.ReportField> r1 = org.acra.ReportField.class
            r8 = 0
            java.lang.String r8 = r6.substring(r8, r2)     // Catch:{ all -> 0x0031 }
            java.lang.Enum r1 = java.lang.Enum.valueOf(r1, r8)     // Catch:{ all -> 0x0031 }
            java.lang.String r2 = r6.substring(r2)     // Catch:{ all -> 0x0031 }
            r9.put(r1, r2)     // Catch:{ all -> 0x0031 }
        L_0x0121:
            r2 = -1
            r1 = 0
            r8 = r0
            r13 = r4
            r4 = r5
            r5 = r3
            r3 = r13
            goto L_0x001c
        L_0x012a:
            r0 = 4
            if (r3 != r0) goto L_0x019f
            r0 = r1
        L_0x012e:
            r3 = 1
            r2 = r0
            r13 = r4
            r4 = r5
            r5 = r3
            r3 = r13
            goto L_0x001c
        L_0x0136:
            r6 = -1
            if (r2 != r6) goto L_0x00ba
            r3 = 0
            r2 = r1
            r13 = r4
            r4 = r5
            r5 = r3
            r3 = r13
            goto L_0x001c
        L_0x0141:
            r6 = -1
            if (r2 != r6) goto L_0x014b
            r3 = 4
            r13 = r4
            r4 = r5
            r5 = r3
            r3 = r13
            goto L_0x001c
        L_0x014b:
            r6 = 5
            if (r3 == r6) goto L_0x0151
            r6 = 3
            if (r3 != r6) goto L_0x0194
        L_0x0151:
            r3 = 0
            r13 = r0
            r0 = r3
            r3 = r13
            goto L_0x0085
        L_0x0157:
            r0 = -1
            if (r2 != r0) goto L_0x015d
            if (r1 <= 0) goto L_0x015d
            r2 = r1
        L_0x015d:
            if (r2 < 0) goto L_0x018f
            java.lang.String r3 = new java.lang.String     // Catch:{ all -> 0x0031 }
            r0 = 0
            r3.<init>(r7, r0, r1)     // Catch:{ all -> 0x0031 }
            java.lang.Class<org.acra.ReportField> r0 = org.acra.ReportField.class
            r1 = 0
            java.lang.String r1 = r3.substring(r1, r2)     // Catch:{ all -> 0x0031 }
            java.lang.Enum r0 = java.lang.Enum.valueOf(r0, r1)     // Catch:{ all -> 0x0031 }
            org.acra.ReportField r0 = (org.acra.ReportField) r0     // Catch:{ all -> 0x0031 }
            java.lang.String r1 = r3.substring(r2)     // Catch:{ all -> 0x0031 }
            r2 = 1
            if (r5 != r2) goto L_0x018c
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0031 }
            r2.<init>()     // Catch:{ all -> 0x0031 }
            java.lang.StringBuilder r1 = r2.append(r1)     // Catch:{ all -> 0x0031 }
            java.lang.String r2 = "\u0000"
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch:{ all -> 0x0031 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x0031 }
        L_0x018c:
            r9.put(r0, r1)     // Catch:{ all -> 0x0031 }
        L_0x018f:
            org.acra.collector.CollectorUtil.safeClose(r15)     // Catch:{ all -> 0x0031 }
            monitor-exit(r14)
            return r9
        L_0x0194:
            r13 = r0
            r0 = r3
            r3 = r13
            goto L_0x0085
        L_0x0199:
            r13 = r4
            r4 = r5
            r5 = r3
            r3 = r13
            goto L_0x001c
        L_0x019f:
            r0 = r2
            goto L_0x012e
        L_0x01a1:
            r5 = r6
            goto L_0x0066
        L_0x01a4:
            r13 = r3
            r3 = r5
            r5 = r4
            r4 = r13
            goto L_0x007b
        */
        throw new UnsupportedOperationException("Method not decompiled: org.acra.CrashReportPersister.load(java.io.Reader):org.acra.collector.CrashReportData");
    }

    public CrashReportData load(String str) {
        CrashReportData load;
        FileInputStream openFileInput = this.context.openFileInput(str);
        if (openFileInput == null) {
            throw new IllegalArgumentException("Invalid crash report fileName : " + str);
        }
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(openFileInput, ACRAConstants.DEFAULT_BUFFER_SIZE_IN_BYTES);
            bufferedInputStream.mark(Integer.MAX_VALUE);
            boolean isEbcdic = isEbcdic(bufferedInputStream);
            bufferedInputStream.reset();
            if (!isEbcdic) {
                load = load((Reader) new InputStreamReader(bufferedInputStream, "ISO8859-1"));
            } else {
                load = load((Reader) new InputStreamReader(bufferedInputStream));
                openFileInput.close();
            }
            return load;
        } finally {
            openFileInput.close();
        }
    }

    public void store(CrashReportData crashReportData, String str) {
        FileOutputStream openFileOutput = this.context.openFileOutput(str, 0);
        try {
            StringBuilder sb = new StringBuilder(200);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput, "ISO8859_1");
            for (Entry entry : crashReportData.entrySet()) {
                dumpString(sb, ((ReportField) entry.getKey()).toString(), true);
                sb.append('=');
                dumpString(sb, (String) entry.getValue(), false);
                sb.append(LINE_SEPARATOR);
                outputStreamWriter.write(sb.toString());
                sb.setLength(0);
            }
            outputStreamWriter.flush();
        } finally {
            openFileOutput.close();
        }
    }
}
