package org.acra;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Looper;
import android.os.Process;
import android.text.format.Time;
import android.util.Log;
import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.acra.collector.Compatibility;
import org.acra.collector.ConfigurationCollector;
import org.acra.collector.CrashReportData;
import org.acra.collector.CrashReportDataFactory;
import org.acra.jraf.android.util.activitylifecyclecallbackscompat.ActivityLifecycleCallbacksCompat;
import org.acra.jraf.android.util.activitylifecyclecallbackscompat.ApplicationHelper;
import org.acra.sender.EmailIntentSender;
import org.acra.sender.HttpSender;
import org.acra.sender.ReportSender;
import org.acra.util.PackageManagerWrapper;
import org.acra.util.ToastSender;

public class ErrorReporter implements UncaughtExceptionHandler {
    private static final ExceptionHandlerInitializer NULL_EXCEPTION_HANDLER_INITIALIZER = new ExceptionHandlerInitializer() {
        public void initializeExceptionHandler(ErrorReporter errorReporter) {
        }
    };
    private static int mNotificationCounter = 0;
    /* access modifiers changed from: private */
    public static boolean toastWaitEnded = true;
    private final CrashReportDataFactory crashReportDataFactory;
    private boolean enabled = false;
    private volatile ExceptionHandlerInitializer exceptionHandlerInitializer = NULL_EXCEPTION_HANDLER_INITIALIZER;
    private final CrashReportFileNameParser fileNameParser = new CrashReportFileNameParser();
    /* access modifiers changed from: private */
    public WeakReference lastActivityCreated = new WeakReference(null);
    /* access modifiers changed from: private */
    public final Application mContext;
    private final UncaughtExceptionHandler mDfltExceptionHandler;
    private final List mReportSenders = new ArrayList();
    private final SharedPreferences prefs;

    public final class ReportBuilder {
        /* access modifiers changed from: private */
        public Map mCustomData;
        /* access modifiers changed from: private */
        public boolean mEndsApplication = false;
        /* access modifiers changed from: private */
        public Throwable mException;
        /* access modifiers changed from: private */
        public boolean mForceSilent = false;
        /* access modifiers changed from: private */
        public String mMessage;
        /* access modifiers changed from: private */
        public Thread mUncaughtExceptionThread;

        public ReportBuilder() {
        }

        private void initCustomData() {
            if (this.mCustomData == null) {
                this.mCustomData = new HashMap();
            }
        }

        /* access modifiers changed from: private */
        public ReportBuilder uncaughtExceptionThread(Thread thread) {
            this.mUncaughtExceptionThread = thread;
            return this;
        }

        public ReportBuilder customData(String str, String str2) {
            initCustomData();
            this.mCustomData.put(str, str2);
            return this;
        }

        public ReportBuilder customData(Map map) {
            initCustomData();
            this.mCustomData.putAll(map);
            return this;
        }

        public ReportBuilder endsApplication() {
            this.mEndsApplication = true;
            return this;
        }

        public ReportBuilder exception(Throwable th) {
            this.mException = th;
            return this;
        }

        public ReportBuilder forceSilent() {
            this.mForceSilent = true;
            return this;
        }

        public ReportBuilder message(String str) {
            this.mMessage = str;
            return this;
        }

        public void send() {
            if (this.mMessage == null && this.mException == null) {
                this.mMessage = "Report requested by developer";
            }
            ErrorReporter.this.report(this);
        }
    }

    class TimeHelper {
        /* access modifiers changed from: private */
        public Long initialTimeMillis;

        private TimeHelper() {
        }

        public long getElapsedTime() {
            if (this.initialTimeMillis == null) {
                return 0;
            }
            return System.currentTimeMillis() - this.initialTimeMillis.longValue();
        }

        public void setInitialTimeMillis(long j) {
            this.initialTimeMillis = Long.valueOf(j);
        }
    }

    ErrorReporter(Application application, SharedPreferences sharedPreferences, boolean z) {
        this.mContext = application;
        this.prefs = sharedPreferences;
        this.enabled = z;
        String collectConfiguration = ConfigurationCollector.collectConfiguration(this.mContext);
        Time time = new Time();
        time.setToNow();
        if (Compatibility.getAPILevel() >= 14) {
            ApplicationHelper.registerActivityLifecycleCallbacks(application, new ActivityLifecycleCallbacksCompat() {
                public void onActivityCreated(Activity activity, Bundle bundle) {
                    if (!(activity instanceof BaseCrashReportDialog)) {
                        ErrorReporter.this.lastActivityCreated = new WeakReference(activity);
                    }
                }

                public void onActivityDestroyed(Activity activity) {
                }

                public void onActivityPaused(Activity activity) {
                }

                public void onActivityResumed(Activity activity) {
                }

                public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
                }

                public void onActivityStarted(Activity activity) {
                }

                public void onActivityStopped(Activity activity) {
                }
            });
        }
        this.crashReportDataFactory = new CrashReportDataFactory(this.mContext, sharedPreferences, time, collectConfiguration);
        this.mDfltExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    private boolean containsOnlySilentOrApprovedReports(String[] strArr) {
        for (String isApproved : strArr) {
            if (!this.fileNameParser.isApproved(isApproved)) {
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: private */
    public Intent createCrashReportDialogIntent(String str, ReportBuilder reportBuilder) {
        Log.d(ACRA.LOG_TAG, "Creating DialogIntent for " + str + " exception=" + reportBuilder.mException);
        Intent intent = new Intent(this.mContext, ACRA.getConfig().reportDialogClass());
        intent.putExtra(ACRAConstants.EXTRA_REPORT_FILE_NAME, str);
        intent.putExtra(ACRAConstants.EXTRA_REPORT_EXCEPTION, reportBuilder.mException);
        return intent;
    }

    private void createNotification(String str, ReportBuilder reportBuilder) {
        NotificationManager notificationManager = (NotificationManager) this.mContext.getSystemService("notification");
        ACRAConfiguration config = ACRA.getConfig();
        Notification notification = new Notification(config.resNotifIcon(), this.mContext.getText(config.resNotifTickerText()), System.currentTimeMillis());
        CharSequence text = this.mContext.getText(config.resNotifTitle());
        CharSequence text2 = this.mContext.getText(config.resNotifText());
        Log.d(ACRA.LOG_TAG, "Creating Notification for " + str);
        Intent createCrashReportDialogIntent = createCrashReportDialogIntent(str, reportBuilder);
        Application application = this.mContext;
        int i = mNotificationCounter;
        mNotificationCounter = i + 1;
        notification.setLatestEventInfo(this.mContext, text, text2, PendingIntent.getActivity(application, i, createCrashReportDialogIntent, 134217728));
        notification.flags |= 16;
        Intent createCrashReportDialogIntent2 = createCrashReportDialogIntent(str, reportBuilder);
        createCrashReportDialogIntent2.putExtra("FORCE_CANCEL", true);
        notification.deleteIntent = PendingIntent.getActivity(this.mContext, -1, createCrashReportDialogIntent2, 0);
        notificationManager.notify(666, notification);
    }

    private void deletePendingReports(boolean z, boolean z2, int i) {
        String[] crashReportFiles = new CrashReportFinder(this.mContext).getCrashReportFiles();
        Arrays.sort(crashReportFiles);
        for (int i2 = 0; i2 < crashReportFiles.length - i; i2++) {
            String str = crashReportFiles[i2];
            boolean isApproved = this.fileNameParser.isApproved(str);
            if ((isApproved && z) || (!isApproved && z2)) {
                File file = new File(this.mContext.getFilesDir(), str);
                ACRA.log.mo2443d(ACRA.LOG_TAG, "Deleting file " + str);
                if (!file.delete()) {
                    Log.e(ACRA.LOG_TAG, "Could not delete report : " + file);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void endApplication(Thread thread, Throwable th) {
        boolean z = true;
        boolean z2 = ACRA.getConfig().mode() == ReportingInteractionMode.SILENT || (ACRA.getConfig().mode() == ReportingInteractionMode.TOAST && ACRA.getConfig().forceCloseDialogAfterToast());
        if (thread == null) {
            z = false;
        }
        if (!z || !z2 || this.mDfltExceptionHandler == null) {
            Log.e(ACRA.LOG_TAG, this.mContext.getPackageName() + " fatal error : " + th.getMessage(), th);
            Activity activity = (Activity) this.lastActivityCreated.get();
            if (activity != null) {
                Log.i(ACRA.LOG_TAG, "Finishing the last Activity prior to killing the Process");
                activity.finish();
                Log.i(ACRA.LOG_TAG, "Finished " + activity.getClass());
                this.lastActivityCreated.clear();
            }
            Process.killProcess(Process.myPid());
            System.exit(10);
            return;
        }
        Log.d(ACRA.LOG_TAG, "Handing Exception on to default ExceptionHandler");
        this.mDfltExceptionHandler.uncaughtException(thread, th);
    }

    @Deprecated
    public static ErrorReporter getInstance() {
        return ACRA.getErrorReporter();
    }

    private String getReportFileName(CrashReportData crashReportData) {
        Time time = new Time();
        time.setToNow();
        return "" + time.toMillis(false) + (crashReportData.getProperty(ReportField.IS_SILENT) != null ? ACRAConstants.SILENT_SUFFIX : "") + ACRAConstants.REPORTFILE_EXTENSION;
    }

    /* access modifiers changed from: private */
    public void report(ReportBuilder reportBuilder) {
        ReportingInteractionMode reportingInteractionMode;
        boolean z;
        final SendWorker sendWorker;
        if (this.enabled) {
            try {
                this.exceptionHandlerInitializer.initializeExceptionHandler(this);
            } catch (Exception e) {
                Log.d(ACRA.LOG_TAG, "Failed to initlize " + this.exceptionHandlerInitializer + " from #handleException");
            }
            if (!reportBuilder.mForceSilent) {
                reportingInteractionMode = ACRA.getConfig().mode();
                z = false;
            } else {
                ReportingInteractionMode reportingInteractionMode2 = ReportingInteractionMode.SILENT;
                if (ACRA.getConfig().mode() != ReportingInteractionMode.SILENT) {
                    reportingInteractionMode = reportingInteractionMode2;
                    z = true;
                } else {
                    reportingInteractionMode = reportingInteractionMode2;
                    z = false;
                }
            }
            boolean z2 = reportingInteractionMode == ReportingInteractionMode.TOAST || (ACRA.getConfig().resToastText() != 0 && (reportingInteractionMode == ReportingInteractionMode.NOTIFICATION || reportingInteractionMode == ReportingInteractionMode.DIALOG));
            final TimeHelper timeHelper = new TimeHelper();
            if (z2) {
                new Thread() {
                    public void run() {
                        Looper.prepare();
                        ToastSender.sendToast(ErrorReporter.this.mContext, ACRA.getConfig().resToastText(), 1);
                        timeHelper.setInitialTimeMillis(System.currentTimeMillis());
                        Looper.loop();
                    }
                }.start();
            }
            CrashReportData createCrashData = this.crashReportDataFactory.createCrashData(reportBuilder.mMessage, reportBuilder.mException, reportBuilder.mCustomData, reportBuilder.mForceSilent, reportBuilder.mUncaughtExceptionThread);
            final String reportFileName = getReportFileName(createCrashData);
            saveCrashReportFile(reportFileName, createCrashData);
            if (reportBuilder.mEndsApplication && !ACRA.getConfig().sendReportsAtShutdown()) {
                endApplication(reportBuilder.mUncaughtExceptionThread, reportBuilder.mException);
            }
            if (reportingInteractionMode == ReportingInteractionMode.SILENT || reportingInteractionMode == ReportingInteractionMode.TOAST || this.prefs.getBoolean(ACRA.PREF_ALWAYS_ACCEPT, false)) {
                Log.d(ACRA.LOG_TAG, "About to start ReportSenderWorker from #handleException");
                sendWorker = startSendingReports(z, true);
                if (reportingInteractionMode == ReportingInteractionMode.SILENT && !reportBuilder.mEndsApplication) {
                    return;
                }
            } else {
                if (reportingInteractionMode == ReportingInteractionMode.NOTIFICATION) {
                    Log.d(ACRA.LOG_TAG, "Creating Notification.");
                    createNotification(reportFileName, reportBuilder);
                }
                sendWorker = null;
            }
            if (z2) {
                toastWaitEnded = false;
                new Thread() {
                    public void run() {
                        Log.d(ACRA.LOG_TAG, "Waiting for 2000 millis from " + timeHelper.initialTimeMillis + " currentMillis=" + System.currentTimeMillis());
                        while (timeHelper.getElapsedTime() < 2000) {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                Log.d(ACRA.LOG_TAG, "Interrupted while waiting for Toast to end.", e);
                            }
                        }
                        ErrorReporter.toastWaitEnded = true;
                    }
                }.start();
            }
            final boolean z3 = reportingInteractionMode == ReportingInteractionMode.DIALOG && !this.prefs.getBoolean(ACRA.PREF_ALWAYS_ACCEPT, false);
            final ReportBuilder reportBuilder2 = reportBuilder;
            new Thread() {
                public void run() {
                    if (ErrorReporter.toastWaitEnded || sendWorker == null) {
                        Log.d(ACRA.LOG_TAG, "Toast (if any) and worker completed - not waiting");
                    } else {
                        Log.d(ACRA.LOG_TAG, "Waiting for " + (ErrorReporter.toastWaitEnded ? "Toast " : " -- ") + (sendWorker.isAlive() ? "and Worker" : ""));
                        while (true) {
                            if (ErrorReporter.toastWaitEnded && !sendWorker.isAlive()) {
                                break;
                            }
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                Log.e(ACRA.LOG_TAG, "Error : ", e);
                            }
                        }
                        Log.d(ACRA.LOG_TAG, "Finished waiting for Toast + Worker");
                    }
                    if (z3) {
                        Log.d(ACRA.LOG_TAG, "Creating CrashReportDialog for " + reportFileName);
                        Intent access$1200 = ErrorReporter.this.createCrashReportDialogIntent(reportFileName, reportBuilder2);
                        access$1200.setFlags(268435456);
                        ErrorReporter.this.mContext.startActivity(access$1200);
                    }
                    Log.d(ACRA.LOG_TAG, "Wait for Toast + worker ended. Kill Application ? " + reportBuilder2.mEndsApplication);
                    if (reportBuilder2.mEndsApplication) {
                        ErrorReporter.this.endApplication(reportBuilder2.mUncaughtExceptionThread, reportBuilder2.mException);
                    }
                }
            }.start();
        }
    }

    private void saveCrashReportFile(String str, CrashReportData crashReportData) {
        try {
            Log.d(ACRA.LOG_TAG, "Writing crash report file " + str + ".");
            new CrashReportPersister(this.mContext).store(crashReportData, str);
        } catch (Exception e) {
            Log.e(ACRA.LOG_TAG, "An error occurred while writing the report file...", e);
        }
    }

    @Deprecated
    public void addCustomData(String str, String str2) {
        this.crashReportDataFactory.putCustomData(str, str2);
    }

    public void addReportSender(ReportSender reportSender) {
        this.mReportSenders.add(reportSender);
    }

    public void checkReportsOnApplicationStart() {
        if (ACRA.getConfig().deleteOldUnsentReportsOnApplicationStart()) {
            long j = (long) this.prefs.getInt(ACRA.PREF_LAST_VERSION_NR, 0);
            PackageInfo packageInfo = new PackageManagerWrapper(this.mContext).getPackageInfo();
            if (packageInfo != null) {
                if (((long) packageInfo.versionCode) > j) {
                    deletePendingReports();
                }
                Editor edit = this.prefs.edit();
                edit.putInt(ACRA.PREF_LAST_VERSION_NR, packageInfo.versionCode);
                edit.commit();
            }
        }
        ReportingInteractionMode mode = ACRA.getConfig().mode();
        if ((mode == ReportingInteractionMode.NOTIFICATION || mode == ReportingInteractionMode.DIALOG) && ACRA.getConfig().deleteUnapprovedReportsOnApplicationStart()) {
            deletePendingNonApprovedReports(true);
        }
        String[] crashReportFiles = new CrashReportFinder(this.mContext).getCrashReportFiles();
        if (crashReportFiles != null && crashReportFiles.length > 0) {
            boolean containsOnlySilentOrApprovedReports = containsOnlySilentOrApprovedReports(crashReportFiles);
            if (!(mode == ReportingInteractionMode.SILENT || mode == ReportingInteractionMode.TOAST)) {
                if (!containsOnlySilentOrApprovedReports) {
                    return;
                }
                if (!(mode == ReportingInteractionMode.NOTIFICATION || mode == ReportingInteractionMode.DIALOG)) {
                    return;
                }
            }
            if (mode == ReportingInteractionMode.TOAST && !containsOnlySilentOrApprovedReports) {
                ToastSender.sendToast(this.mContext, ACRA.getConfig().resToastText(), 1);
            }
            Log.v(ACRA.LOG_TAG, "About to start ReportSenderWorker from #checkReportOnApplicationStart");
            startSendingReports(false, false);
        }
    }

    public void clearCustomData() {
        this.crashReportDataFactory.clearCustomData();
    }

    /* access modifiers changed from: 0000 */
    public void deletePendingNonApprovedReports(boolean z) {
        deletePendingReports(false, true, z ? 1 : 0);
    }

    /* access modifiers changed from: 0000 */
    public void deletePendingReports() {
        deletePendingReports(true, true, 0);
    }

    public String getCustomData(String str) {
        return this.crashReportDataFactory.getCustomData(str);
    }

    public void handleException(Throwable th) {
        reportBuilder().exception(th).send();
    }

    public void handleException(Throwable th, boolean z) {
        ReportBuilder exception = reportBuilder().exception(th);
        if (z) {
            exception.endsApplication();
        }
        exception.send();
    }

    public void handleSilentException(Throwable th) {
        if (this.enabled) {
            reportBuilder().exception(th).forceSilent().send();
            Log.d(ACRA.LOG_TAG, "ACRA sent Silent report.");
            return;
        }
        Log.d(ACRA.LOG_TAG, "ACRA is disabled. Silent report not sent.");
    }

    public String putCustomData(String str, String str2) {
        return this.crashReportDataFactory.putCustomData(str, str2);
    }

    public void removeAllReportSenders() {
        this.mReportSenders.clear();
    }

    public String removeCustomData(String str) {
        return this.crashReportDataFactory.removeCustomData(str);
    }

    public void removeReportSender(ReportSender reportSender) {
        this.mReportSenders.remove(reportSender);
    }

    public void removeReportSenders(Class cls) {
        if (ReportSender.class.isAssignableFrom(cls)) {
            for (ReportSender reportSender : this.mReportSenders) {
                if (cls.isInstance(reportSender)) {
                    this.mReportSenders.remove(reportSender);
                }
            }
        }
    }

    public ReportBuilder reportBuilder() {
        return new ReportBuilder();
    }

    public void setDefaultReportSenders() {
        ACRAConfiguration config = ACRA.getConfig();
        Application application = ACRA.getApplication();
        removeAllReportSenders();
        if (!"".equals(config.mailTo())) {
            Log.w(ACRA.LOG_TAG, application.getPackageName() + " reports will be sent by email (if accepted by user).");
            setReportSender(new EmailIntentSender(application));
        } else if (!new PackageManagerWrapper(application).hasPermission("android.permission.INTERNET")) {
            Log.e(ACRA.LOG_TAG, application.getPackageName() + " should be granted permission " + "android.permission.INTERNET" + " if you want your crash reports to be sent. If you don't want to add this permission to your application you can also enable sending reports by email. If this is your will then provide your email address in @ReportsCrashes(mailTo=\"your.account@domain.com\"");
        } else if (config.formUri() != null && !"".equals(config.formUri())) {
            setReportSender(new HttpSender(ACRA.getConfig().httpMethod(), ACRA.getConfig().reportType(), null));
        }
    }

    public void setEnabled(boolean z) {
        Log.i(ACRA.LOG_TAG, "ACRA is " + (z ? "enabled" : "disabled") + " for " + this.mContext.getPackageName());
        this.enabled = z;
    }

    public void setExceptionHandlerInitializer(ExceptionHandlerInitializer exceptionHandlerInitializer2) {
        if (exceptionHandlerInitializer2 == null) {
            exceptionHandlerInitializer2 = NULL_EXCEPTION_HANDLER_INITIALIZER;
        }
        this.exceptionHandlerInitializer = exceptionHandlerInitializer2;
    }

    public void setReportSender(ReportSender reportSender) {
        removeAllReportSenders();
        addReportSender(reportSender);
    }

    /* access modifiers changed from: 0000 */
    public SendWorker startSendingReports(boolean z, boolean z2) {
        SendWorker sendWorker = new SendWorker(this.mContext, this.mReportSenders, z, z2);
        sendWorker.start();
        return sendWorker;
    }

    public void uncaughtException(Thread thread, Throwable th) {
        try {
            if (this.enabled) {
                Log.e(ACRA.LOG_TAG, "ACRA caught a " + th.getClass().getSimpleName() + " for " + this.mContext.getPackageName(), th);
                Log.d(ACRA.LOG_TAG, "Building report");
                reportBuilder().uncaughtExceptionThread(thread).exception(th).endsApplication().send();
            } else if (this.mDfltExceptionHandler != null) {
                Log.e(ACRA.LOG_TAG, "ACRA is disabled for " + this.mContext.getPackageName() + " - forwarding uncaught Exception on to default ExceptionHandler");
                this.mDfltExceptionHandler.uncaughtException(thread, th);
            } else {
                Log.e(ACRA.LOG_TAG, "ACRA is disabled for " + this.mContext.getPackageName() + " - no default ExceptionHandler");
                Log.e(ACRA.LOG_TAG, "ACRA caught a " + th.getClass().getSimpleName() + " for " + this.mContext.getPackageName(), th);
            }
        } catch (Throwable th2) {
            if (this.mDfltExceptionHandler != null) {
                this.mDfltExceptionHandler.uncaughtException(thread, th);
            }
        }
    }
}
