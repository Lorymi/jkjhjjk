package org.acra;

import android.content.Context;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.acra.collector.CrashReportData;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;

final class SendWorker extends Thread {
    private final boolean approvePendingReports;
    private final Context context;
    private final CrashReportFileNameParser fileNameParser = new CrashReportFileNameParser();
    private final List reportSenders;
    private final boolean sendOnlySilentReports;

    public SendWorker(Context context2, List list, boolean z, boolean z2) {
        this.context = context2;
        this.reportSenders = list;
        this.sendOnlySilentReports = z;
        this.approvePendingReports = z2;
    }

    private void approvePendingReports() {
        String[] crashReportFiles;
        Log.d(ACRA.LOG_TAG, "Mark all pending reports as approved.");
        for (String str : new CrashReportFinder(this.context).getCrashReportFiles()) {
            if (!this.fileNameParser.isApproved(str)) {
                File file = new File(this.context.getFilesDir(), str);
                File file2 = new File(this.context.getFilesDir(), str.replace(ACRAConstants.REPORTFILE_EXTENSION, "-approved.stacktrace"));
                if (!file.renameTo(file2)) {
                    Log.e(ACRA.LOG_TAG, "Could not rename approved report from " + file + " to " + file2);
                }
            }
        }
    }

    private void checkAndSendReports(Context context2, boolean z) {
        int i = 0;
        Log.d(ACRA.LOG_TAG, "#checkAndSendReports - start");
        String[] crashReportFiles = new CrashReportFinder(context2).getCrashReportFiles();
        Arrays.sort(crashReportFiles);
        for (String str : crashReportFiles) {
            if (!z || this.fileNameParser.isSilent(str)) {
                if (i >= 5) {
                    break;
                }
                Log.i(ACRA.LOG_TAG, "Sending file " + str);
                try {
                    sendCrashReport(new CrashReportPersister(context2).load(str));
                    deleteFile(context2, str);
                } catch (RuntimeException e) {
                    Log.e(ACRA.LOG_TAG, "Failed to send crash reports for " + str, e);
                    deleteFile(context2, str);
                } catch (IOException e2) {
                    Log.e(ACRA.LOG_TAG, "Failed to load crash report for " + str, e2);
                    deleteFile(context2, str);
                } catch (ReportSenderException e3) {
                    Log.e(ACRA.LOG_TAG, "Failed to send crash report for " + str, e3);
                }
                i++;
            }
        }
        Log.d(ACRA.LOG_TAG, "#checkAndSendReports - finish");
    }

    private void deleteFile(Context context2, String str) {
        if (!context2.deleteFile(str)) {
            Log.w(ACRA.LOG_TAG, "Could not delete error report : " + str);
        }
    }

    private void sendCrashReport(CrashReportData crashReportData) {
        if (!ACRA.isDebuggable() || ACRA.getConfig().sendReportsInDevMode()) {
            boolean z = false;
            Iterator it = this.reportSenders.iterator();
            while (true) {
                boolean z2 = z;
                if (it.hasNext()) {
                    ReportSender reportSender = (ReportSender) it.next();
                    try {
                        reportSender.send(this.context, crashReportData);
                        z = true;
                    } catch (ReportSenderException e) {
                        if (!z2) {
                            throw e;
                        }
                        Log.w(ACRA.LOG_TAG, "ReportSender of class " + reportSender.getClass().getName() + " failed but other senders completed their task. ACRA will not send this report again.");
                        z = z2;
                    }
                } else {
                    return;
                }
            }
        }
    }

    public void run() {
        if (this.approvePendingReports) {
            approvePendingReports();
        }
        checkAndSendReports(this.context, this.sendOnlySilentReports);
    }
}
