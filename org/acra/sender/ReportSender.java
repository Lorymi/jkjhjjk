package org.acra.sender;

import android.content.Context;
import org.acra.collector.CrashReportData;

public interface ReportSender {
    void send(Context context, CrashReportData crashReportData);
}
