package org.acra.sender;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import org.acra.ACRA;
import org.acra.ACRAConstants;
import org.acra.ReportField;
import org.acra.collector.CrashReportData;

public class EmailIntentSender implements ReportSender {
    private final Context mContext;

    public EmailIntentSender(Context context) {
        this.mContext = context;
    }

    private String buildBody(CrashReportData crashReportData) {
        ReportField[] customReportContent = ACRA.getConfig().customReportContent();
        ReportField[] reportFieldArr = customReportContent.length == 0 ? ACRAConstants.DEFAULT_MAIL_REPORT_FIELDS : customReportContent;
        StringBuilder sb = new StringBuilder();
        for (ReportField reportField : reportFieldArr) {
            sb.append(reportField.toString()).append("=");
            sb.append((String) crashReportData.get(reportField));
            sb.append(10);
        }
        return sb.toString();
    }

    public void send(Context context, CrashReportData crashReportData) {
        String str = this.mContext.getPackageName() + " Crash Report";
        String buildBody = buildBody(crashReportData);
        Intent intent = new Intent("android.intent.action.SENDTO");
        intent.setData(Uri.fromParts("mailto", ACRA.getConfig().mailTo(), null));
        intent.addFlags(268435456);
        intent.putExtra("android.intent.extra.SUBJECT", str);
        intent.putExtra("android.intent.extra.TEXT", buildBody);
        this.mContext.startActivity(intent);
    }
}
