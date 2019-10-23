package org.acra;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageManager.NameNotFoundException;
import android.preference.PreferenceManager;
import org.acra.annotation.ReportsCrashes;
import org.acra.log.ACRALog;
import org.acra.log.AndroidLogDelegate;

public class ACRA {
    public static final boolean DEV_LOGGING = false;
    public static final String LOG_TAG = ACRA.class.getSimpleName();
    public static final String PREF_ALWAYS_ACCEPT = "acra.alwaysaccept";
    public static final String PREF_DISABLE_ACRA = "acra.disable";
    public static final String PREF_ENABLE_ACRA = "acra.enable";
    public static final String PREF_ENABLE_DEVICE_ID = "acra.deviceid.enable";
    public static final String PREF_ENABLE_SYSTEM_LOGS = "acra.syslog.enable";
    public static final String PREF_LAST_VERSION_NR = "acra.lastVersionNr";
    public static final String PREF_USER_EMAIL_ADDRESS = "acra.user.email";
    private static ACRAConfiguration configProxy;
    private static ErrorReporter errorReporterSingleton;
    public static ACRALog log = new AndroidLogDelegate();
    private static Application mApplication;
    private static OnSharedPreferenceChangeListener mPrefListener;

    /* renamed from: org.acra.ACRA$2 */
    /* synthetic */ class C10502 {
        static final /* synthetic */ int[] $SwitchMap$org$acra$ReportingInteractionMode = new int[ReportingInteractionMode.values().length];

        static {
            try {
                $SwitchMap$org$acra$ReportingInteractionMode[ReportingInteractionMode.TOAST.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$acra$ReportingInteractionMode[ReportingInteractionMode.NOTIFICATION.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$acra$ReportingInteractionMode[ReportingInteractionMode.DIALOG.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    static void checkCrashResources(ReportsCrashes reportsCrashes) {
        switch (C10502.$SwitchMap$org$acra$ReportingInteractionMode[reportsCrashes.mode().ordinal()]) {
            case 1:
                if (reportsCrashes.resToastText() == 0) {
                    throw new ACRAConfigurationException("TOAST mode: you have to define the resToastText parameter in your application @ReportsCrashes() annotation.");
                }
                return;
            case 2:
                if (reportsCrashes.resNotifTickerText() == 0 || reportsCrashes.resNotifTitle() == 0 || reportsCrashes.resNotifText() == 0) {
                    throw new ACRAConfigurationException("NOTIFICATION mode: you have to define at least the resNotifTickerText, resNotifTitle, resNotifText parameters in your application @ReportsCrashes() annotation.");
                } else if (CrashReportDialog.class.equals(reportsCrashes.reportDialogClass()) && reportsCrashes.resDialogText() == 0) {
                    throw new ACRAConfigurationException("NOTIFICATION mode: using the (default) CrashReportDialog requires you have to define the resDialogText parameter in your application @ReportsCrashes() annotation.");
                } else {
                    return;
                }
            case ACRAConstants.DEFAULT_MAX_NUMBER_OF_REQUEST_RETRIES /*3*/:
                if (CrashReportDialog.class.equals(reportsCrashes.reportDialogClass()) && reportsCrashes.resDialogText() == 0) {
                    throw new ACRAConfigurationException("DIALOG mode: using the (default) CrashReportDialog requires you to define the resDialogText parameter in your application @ReportsCrashes() annotation.");
                }
                return;
            default:
                return;
        }
    }

    public static SharedPreferences getACRASharedPreferences() {
        ACRAConfiguration config = getConfig();
        return !"".equals(config.sharedPreferencesName()) ? mApplication.getSharedPreferences(config.sharedPreferencesName(), config.sharedPreferencesMode()) : PreferenceManager.getDefaultSharedPreferences(mApplication);
    }

    static Application getApplication() {
        return mApplication;
    }

    public static ACRAConfiguration getConfig() {
        if (configProxy == null) {
            if (mApplication == null) {
                log.mo2452w(LOG_TAG, "Calling ACRA.getConfig() before ACRA.init() gives you an empty configuration instance. You might prefer calling ACRA.getNewDefaultConfig(Application) to get an instance with default values taken from a @ReportsCrashes annotation.");
            }
            configProxy = getNewDefaultConfig(mApplication);
        }
        return configProxy;
    }

    public static ErrorReporter getErrorReporter() {
        if (errorReporterSingleton != null) {
            return errorReporterSingleton;
        }
        throw new IllegalStateException("Cannot access ErrorReporter before ACRA#init");
    }

    public static ACRAConfiguration getNewDefaultConfig(Application application) {
        return application != null ? new ACRAConfiguration((ReportsCrashes) application.getClass().getAnnotation(ReportsCrashes.class)) : new ACRAConfiguration(null);
    }

    public static void init(Application application) {
        ReportsCrashes reportsCrashes = (ReportsCrashes) application.getClass().getAnnotation(ReportsCrashes.class);
        if (reportsCrashes == null) {
            log.mo2445e(LOG_TAG, "ACRA#init called but no ReportsCrashes annotation on Application " + application.getPackageName());
        } else {
            init(application, new ACRAConfiguration(reportsCrashes));
        }
    }

    public static void init(Application application, ACRAConfiguration aCRAConfiguration) {
        init(application, aCRAConfiguration, true);
    }

    public static void init(Application application, ACRAConfiguration aCRAConfiguration, boolean z) {
        if (mApplication != null) {
            log.mo2452w(LOG_TAG, "ACRA#init called more than once. Won't do anything more.");
            return;
        }
        mApplication = application;
        if (aCRAConfiguration == null) {
            log.mo2445e(LOG_TAG, "ACRA#init called but no ACRAConfiguration provided");
            return;
        }
        setConfig(aCRAConfiguration);
        SharedPreferences aCRASharedPreferences = getACRASharedPreferences();
        try {
            checkCrashResources(aCRAConfiguration);
            log.mo2443d(LOG_TAG, "ACRA is enabled for " + mApplication.getPackageName() + ", intializing...");
            ErrorReporter errorReporter = new ErrorReporter(mApplication, aCRASharedPreferences, !shouldDisableACRA(aCRASharedPreferences));
            errorReporter.setDefaultReportSenders();
            errorReporterSingleton = errorReporter;
            if (z) {
                errorReporter.checkReportsOnApplicationStart();
            }
        } catch (ACRAConfigurationException e) {
            log.mo2453w(LOG_TAG, "Error : ", e);
        }
        mPrefListener = new OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
                if (ACRA.PREF_DISABLE_ACRA.equals(str) || ACRA.PREF_ENABLE_ACRA.equals(str)) {
                    ACRA.getErrorReporter().setEnabled(!ACRA.shouldDisableACRA(sharedPreferences));
                }
            }
        };
        aCRASharedPreferences.registerOnSharedPreferenceChangeListener(mPrefListener);
    }

    static boolean isDebuggable() {
        try {
            return (mApplication.getPackageManager().getApplicationInfo(mApplication.getPackageName(), 0).flags & 2) > 0;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public static void setConfig(ACRAConfiguration aCRAConfiguration) {
        configProxy = aCRAConfiguration;
    }

    public static void setLog(ACRALog aCRALog) {
        log = aCRALog;
    }

    /* access modifiers changed from: private */
    public static boolean shouldDisableACRA(SharedPreferences sharedPreferences) {
        boolean z = true;
        try {
            boolean z2 = sharedPreferences.getBoolean(PREF_ENABLE_ACRA, true);
            String str = PREF_DISABLE_ACRA;
            if (z2) {
                z = false;
            }
            return sharedPreferences.getBoolean(str, z);
        } catch (Exception e) {
            return false;
        }
    }
}
