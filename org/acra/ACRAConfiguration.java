package org.acra;

import java.security.KeyStore;
import java.util.Map;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender.Method;
import org.acra.sender.HttpSender.Type;
import org.acra.util.DefaultHttpsSocketFactoryFactory;
import org.acra.util.HttpsSocketFactoryFactory;
import org.acra.util.ReflectionException;
import org.acra.util.ReflectionHelper;

public class ACRAConfiguration implements ReportsCrashes {
    private String[] mAdditionalDropboxTags;
    private String[] mAdditionalSharedPreferences;
    private String mApplicationLogFile;
    private Integer mApplicationLogFileLines;
    private Class mBuildConfigClass;
    private Integer mConnectionTimeout;
    private ReportField[] mCustomReportContent;
    private Boolean mDeleteOldUnsentReportsOnApplicationStart;
    private Boolean mDeleteUnapprovedReportsOnApplicationStart;
    private Boolean mDisableSSLCertValidation;
    private Integer mDropboxCollectionMinutes;
    private String[] mExcludeMatchingSettingsKeys;
    private String[] mExcludeMatchingSharedPreferencesKeys;
    private Boolean mForceCloseDialogAfterToast;
    private String mFormUri;
    private String mFormUriBasicAuthLogin;
    private String mFormUriBasicAuthPassword;
    private Map mHttpHeaders;
    private Method mHttpMethod;
    private HttpsSocketFactoryFactory mHttpsSocketFactoryFactory;
    private String mHttpsSocketFactoryFactoryClass;
    private Boolean mIncludeDropboxSystemTags;
    private KeyStore mKeyStore;
    private String[] mLogcatArguments;
    private Boolean mLogcatFilterByPid;
    private String mMailTo;
    private Integer mMaxNumberOfRequestRetries;
    private ReportingInteractionMode mMode;
    private Class mReportDialogClass;
    private Type mReportType;
    private ReportsCrashes mReportsCrashes;
    private Integer mResDialogCommentPrompt;
    private Integer mResDialogEmailPrompt;
    private Integer mResDialogIcon;
    private Integer mResDialogNegativeButtonText;
    private Integer mResDialogOkToast;
    private Integer mResDialogPositiveButtonText;
    private Integer mResDialogText;
    private Integer mResDialogTitle;
    private Integer mResNotifIcon;
    private Integer mResNotifText;
    private Integer mResNotifTickerText;
    private Integer mResNotifTitle;
    private Integer mResToastText;
    private Boolean mSendReportsAtShutdown;
    private Boolean mSendReportsInDevMode;
    private Integer mSharedPreferenceMode;
    private String mSharedPreferenceName;
    private Integer mSocketTimeout;
    private final ReflectionHelper reflectionHelper;

    public ACRAConfiguration() {
        this(null);
    }

    public ACRAConfiguration(ReportsCrashes reportsCrashes) {
        this.reflectionHelper = new ReflectionHelper();
        this.mAdditionalDropboxTags = null;
        this.mAdditionalSharedPreferences = null;
        this.mConnectionTimeout = null;
        this.mCustomReportContent = null;
        this.mDeleteUnapprovedReportsOnApplicationStart = null;
        this.mDeleteOldUnsentReportsOnApplicationStart = null;
        this.mDropboxCollectionMinutes = null;
        this.mForceCloseDialogAfterToast = null;
        this.mFormUri = null;
        this.mFormUriBasicAuthLogin = null;
        this.mFormUriBasicAuthPassword = null;
        this.mIncludeDropboxSystemTags = null;
        this.mLogcatArguments = null;
        this.mMailTo = null;
        this.mMaxNumberOfRequestRetries = null;
        this.mMode = null;
        this.mReportsCrashes = null;
        this.mReportDialogClass = null;
        this.mResDialogPositiveButtonText = null;
        this.mResDialogNegativeButtonText = null;
        this.mResDialogCommentPrompt = null;
        this.mResDialogEmailPrompt = null;
        this.mResDialogIcon = null;
        this.mResDialogOkToast = null;
        this.mResDialogText = null;
        this.mResDialogTitle = null;
        this.mResNotifIcon = null;
        this.mResNotifText = null;
        this.mResNotifTickerText = null;
        this.mResNotifTitle = null;
        this.mResToastText = null;
        this.mSharedPreferenceMode = null;
        this.mSharedPreferenceName = null;
        this.mSocketTimeout = null;
        this.mLogcatFilterByPid = null;
        this.mSendReportsInDevMode = null;
        this.mSendReportsAtShutdown = null;
        this.mExcludeMatchingSharedPreferencesKeys = null;
        this.mExcludeMatchingSettingsKeys = null;
        this.mApplicationLogFile = null;
        this.mApplicationLogFileLines = null;
        this.mDisableSSLCertValidation = null;
        this.mHttpsSocketFactoryFactoryClass = null;
        this.mHttpMethod = null;
        this.mReportType = null;
        this.mReportsCrashes = reportsCrashes;
    }

    public static boolean isNull(String str) {
        return str == null || ACRAConstants.NULL_VALUE.equals(str);
    }

    public String[] additionalDropBoxTags() {
        return this.mAdditionalDropboxTags != null ? this.mAdditionalDropboxTags : this.mReportsCrashes != null ? this.mReportsCrashes.additionalDropBoxTags() : new String[0];
    }

    public String[] additionalSharedPreferences() {
        return this.mAdditionalSharedPreferences != null ? this.mAdditionalSharedPreferences : this.mReportsCrashes != null ? this.mReportsCrashes.additionalSharedPreferences() : new String[0];
    }

    public Class annotationType() {
        return this.mReportsCrashes.annotationType();
    }

    public String applicationLogFile() {
        return this.mApplicationLogFile != null ? this.mApplicationLogFile : this.mReportsCrashes != null ? this.mReportsCrashes.applicationLogFile() : "";
    }

    public int applicationLogFileLines() {
        if (this.mApplicationLogFileLines != null) {
            return this.mApplicationLogFileLines.intValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.applicationLogFileLines();
        }
        return 100;
    }

    public Class buildConfigClass() {
        if (this.mBuildConfigClass != null) {
            return this.mBuildConfigClass;
        }
        if (this.mReportsCrashes == null || this.mReportsCrashes.buildConfigClass() == null) {
            return null;
        }
        return this.mReportsCrashes.buildConfigClass();
    }

    public int connectionTimeout() {
        return this.mConnectionTimeout != null ? this.mConnectionTimeout.intValue() : this.mReportsCrashes != null ? this.mReportsCrashes.connectionTimeout() : ACRAConstants.DEFAULT_CONNECTION_TIMEOUT;
    }

    public ReportField[] customReportContent() {
        return this.mCustomReportContent != null ? this.mCustomReportContent : this.mReportsCrashes != null ? this.mReportsCrashes.customReportContent() : new ReportField[0];
    }

    public boolean deleteOldUnsentReportsOnApplicationStart() {
        if (this.mDeleteOldUnsentReportsOnApplicationStart != null) {
            return this.mDeleteOldUnsentReportsOnApplicationStart.booleanValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.deleteOldUnsentReportsOnApplicationStart();
        }
        return true;
    }

    public boolean deleteUnapprovedReportsOnApplicationStart() {
        if (this.mDeleteUnapprovedReportsOnApplicationStart != null) {
            return this.mDeleteUnapprovedReportsOnApplicationStart.booleanValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.deleteUnapprovedReportsOnApplicationStart();
        }
        return true;
    }

    public boolean disableSSLCertValidation() {
        if (this.mDisableSSLCertValidation != null) {
            return this.mDisableSSLCertValidation.booleanValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.disableSSLCertValidation();
        }
        return false;
    }

    public int dropboxCollectionMinutes() {
        if (this.mDropboxCollectionMinutes != null) {
            return this.mDropboxCollectionMinutes.intValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.dropboxCollectionMinutes();
        }
        return 5;
    }

    public String[] excludeMatchingSettingsKeys() {
        return this.mExcludeMatchingSettingsKeys != null ? this.mExcludeMatchingSettingsKeys : this.mReportsCrashes != null ? this.mReportsCrashes.excludeMatchingSettingsKeys() : new String[0];
    }

    public String[] excludeMatchingSharedPreferencesKeys() {
        return this.mExcludeMatchingSharedPreferencesKeys != null ? this.mExcludeMatchingSharedPreferencesKeys : this.mReportsCrashes != null ? this.mReportsCrashes.excludeMatchingSharedPreferencesKeys() : new String[0];
    }

    public boolean forceCloseDialogAfterToast() {
        if (this.mForceCloseDialogAfterToast != null) {
            return this.mForceCloseDialogAfterToast.booleanValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.forceCloseDialogAfterToast();
        }
        return false;
    }

    public String formUri() {
        return this.mFormUri != null ? this.mFormUri : this.mReportsCrashes != null ? this.mReportsCrashes.formUri() : "";
    }

    public String formUriBasicAuthLogin() {
        return this.mFormUriBasicAuthLogin != null ? this.mFormUriBasicAuthLogin : this.mReportsCrashes != null ? this.mReportsCrashes.formUriBasicAuthLogin() : ACRAConstants.NULL_VALUE;
    }

    public String formUriBasicAuthPassword() {
        return this.mFormUriBasicAuthPassword != null ? this.mFormUriBasicAuthPassword : this.mReportsCrashes != null ? this.mReportsCrashes.formUriBasicAuthPassword() : ACRAConstants.NULL_VALUE;
    }

    public Map getHttpHeaders() {
        return this.mHttpHeaders;
    }

    public HttpsSocketFactoryFactory getHttpSocketFactoryFactory() {
        if (this.mHttpsSocketFactoryFactory != null) {
            return this.mHttpsSocketFactoryFactory;
        }
        String httpsSocketFactoryFactoryClass = httpsSocketFactoryFactoryClass();
        if (httpsSocketFactoryFactoryClass != null) {
            try {
                Object create = this.reflectionHelper.create(this.mReportsCrashes.httpsSocketFactoryFactoryClass());
                if (create instanceof HttpsSocketFactoryFactory) {
                    this.mHttpsSocketFactoryFactory = (HttpsSocketFactoryFactory) create;
                } else {
                    ACRA.log.mo2452w(ACRA.LOG_TAG, "Using default httpsSocketFactoryFactory - not a HttpSocketFactoryFactory : " + httpsSocketFactoryFactoryClass);
                }
            } catch (ReflectionException e) {
                ACRA.log.mo2452w(ACRA.LOG_TAG, "Using default httpsSocketFactoryFactory - Could not construct : " + httpsSocketFactoryFactoryClass);
            }
        }
        if (this.mHttpsSocketFactoryFactoryClass == null) {
            this.mHttpsSocketFactoryFactory = DefaultHttpsSocketFactoryFactory.INSTANCE;
        }
        return this.mHttpsSocketFactoryFactory;
    }

    public Method httpMethod() {
        return this.mHttpMethod != null ? this.mHttpMethod : this.mReportsCrashes != null ? this.mReportsCrashes.httpMethod() : Method.POST;
    }

    public String httpsSocketFactoryFactoryClass() {
        if (this.mHttpsSocketFactoryFactoryClass != null) {
            return this.mHttpsSocketFactoryFactoryClass;
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.httpsSocketFactoryFactoryClass();
        }
        return null;
    }

    public boolean includeDropBoxSystemTags() {
        if (this.mIncludeDropboxSystemTags != null) {
            return this.mIncludeDropboxSystemTags.booleanValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.includeDropBoxSystemTags();
        }
        return false;
    }

    public KeyStore keyStore() {
        if (this.mKeyStore != null) {
            return this.mKeyStore;
        }
        return null;
    }

    public String[] logcatArguments() {
        if (this.mLogcatArguments != null) {
            return this.mLogcatArguments;
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.logcatArguments();
        }
        return new String[]{"-t", Integer.toString(100), "-v", "time"};
    }

    public boolean logcatFilterByPid() {
        if (this.mLogcatFilterByPid != null) {
            return this.mLogcatFilterByPid.booleanValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.logcatFilterByPid();
        }
        return false;
    }

    public String mailTo() {
        return this.mMailTo != null ? this.mMailTo : this.mReportsCrashes != null ? this.mReportsCrashes.mailTo() : "";
    }

    public int maxNumberOfRequestRetries() {
        if (this.mMaxNumberOfRequestRetries != null) {
            return this.mMaxNumberOfRequestRetries.intValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.maxNumberOfRequestRetries();
        }
        return 3;
    }

    public ReportingInteractionMode mode() {
        return this.mMode != null ? this.mMode : this.mReportsCrashes != null ? this.mReportsCrashes.mode() : ReportingInteractionMode.SILENT;
    }

    public Class reportDialogClass() {
        return this.mReportDialogClass != null ? this.mReportDialogClass : this.mReportsCrashes != null ? this.mReportsCrashes.reportDialogClass() : CrashReportDialog.class;
    }

    public Type reportType() {
        return this.mReportType != null ? this.mReportType : this.mReportsCrashes != null ? this.mReportsCrashes.reportType() : Type.FORM;
    }

    public int resDialogCommentPrompt() {
        if (this.mResDialogCommentPrompt != null) {
            return this.mResDialogCommentPrompt.intValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.resDialogCommentPrompt();
        }
        return 0;
    }

    public int resDialogEmailPrompt() {
        if (this.mResDialogEmailPrompt != null) {
            return this.mResDialogEmailPrompt.intValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.resDialogEmailPrompt();
        }
        return 0;
    }

    public int resDialogIcon() {
        return this.mResDialogIcon != null ? this.mResDialogIcon.intValue() : this.mReportsCrashes != null ? this.mReportsCrashes.resDialogIcon() : ACRAConstants.DEFAULT_DIALOG_ICON;
    }

    public int resDialogNegativeButtonText() {
        if (this.mResDialogNegativeButtonText != null) {
            return this.mResDialogNegativeButtonText.intValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.resDialogNegativeButtonText();
        }
        return 0;
    }

    public int resDialogOkToast() {
        if (this.mResDialogOkToast != null) {
            return this.mResDialogOkToast.intValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.resDialogOkToast();
        }
        return 0;
    }

    public int resDialogPositiveButtonText() {
        if (this.mResDialogPositiveButtonText != null) {
            return this.mResDialogPositiveButtonText.intValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.resDialogPositiveButtonText();
        }
        return 0;
    }

    public int resDialogText() {
        if (this.mResDialogText != null) {
            return this.mResDialogText.intValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.resDialogText();
        }
        return 0;
    }

    public int resDialogTitle() {
        if (this.mResDialogTitle != null) {
            return this.mResDialogTitle.intValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.resDialogTitle();
        }
        return 0;
    }

    public int resNotifIcon() {
        return this.mResNotifIcon != null ? this.mResNotifIcon.intValue() : this.mReportsCrashes != null ? this.mReportsCrashes.resNotifIcon() : ACRAConstants.DEFAULT_NOTIFICATION_ICON;
    }

    public int resNotifText() {
        if (this.mResNotifText != null) {
            return this.mResNotifText.intValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.resNotifText();
        }
        return 0;
    }

    public int resNotifTickerText() {
        if (this.mResNotifTickerText != null) {
            return this.mResNotifTickerText.intValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.resNotifTickerText();
        }
        return 0;
    }

    public int resNotifTitle() {
        if (this.mResNotifTitle != null) {
            return this.mResNotifTitle.intValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.resNotifTitle();
        }
        return 0;
    }

    public int resToastText() {
        if (this.mResToastText != null) {
            return this.mResToastText.intValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.resToastText();
        }
        return 0;
    }

    public boolean sendReportsAtShutdown() {
        if (this.mSendReportsAtShutdown != null) {
            return this.mSendReportsAtShutdown.booleanValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.sendReportsAtShutdown();
        }
        return true;
    }

    public boolean sendReportsInDevMode() {
        if (this.mSendReportsInDevMode != null) {
            return this.mSendReportsInDevMode.booleanValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.sendReportsInDevMode();
        }
        return true;
    }

    public ACRAConfiguration setAdditionalDropboxTags(String[] strArr) {
        this.mAdditionalDropboxTags = strArr;
        return this;
    }

    public ACRAConfiguration setAdditionalSharedPreferences(String[] strArr) {
        this.mAdditionalSharedPreferences = strArr;
        return this;
    }

    public ACRAConfiguration setApplicationLogFile(String str) {
        this.mApplicationLogFile = str;
        return this;
    }

    public ACRAConfiguration setApplicationLogFileLines(int i) {
        this.mApplicationLogFileLines = Integer.valueOf(i);
        return this;
    }

    public ACRAConfiguration setBuildConfigClass(Class cls) {
        this.mBuildConfigClass = cls;
        return this;
    }

    public ACRAConfiguration setConnectionTimeout(Integer num) {
        this.mConnectionTimeout = num;
        return this;
    }

    public ACRAConfiguration setCustomReportContent(ReportField[] reportFieldArr) {
        this.mCustomReportContent = reportFieldArr;
        return this;
    }

    public ACRAConfiguration setDeleteOldUnsentReportsOnApplicationStart(Boolean bool) {
        this.mDeleteOldUnsentReportsOnApplicationStart = bool;
        return this;
    }

    public ACRAConfiguration setDeleteUnapprovedReportsOnApplicationStart(Boolean bool) {
        this.mDeleteUnapprovedReportsOnApplicationStart = bool;
        return this;
    }

    public ACRAConfiguration setDisableSSLCertValidation(boolean z) {
        this.mDisableSSLCertValidation = Boolean.valueOf(z);
        return this;
    }

    public ACRAConfiguration setDropboxCollectionMinutes(Integer num) {
        this.mDropboxCollectionMinutes = num;
        return this;
    }

    public ACRAConfiguration setExcludeMatchingSettingsKeys(String[] strArr) {
        this.mExcludeMatchingSettingsKeys = strArr;
        return this;
    }

    public ACRAConfiguration setExcludeMatchingSharedPreferencesKeys(String[] strArr) {
        this.mExcludeMatchingSharedPreferencesKeys = strArr;
        return this;
    }

    public ACRAConfiguration setForceCloseDialogAfterToast(Boolean bool) {
        this.mForceCloseDialogAfterToast = bool;
        return this;
    }

    public ACRAConfiguration setFormUri(String str) {
        this.mFormUri = str;
        return this;
    }

    public ACRAConfiguration setFormUriBasicAuthLogin(String str) {
        this.mFormUriBasicAuthLogin = str;
        return this;
    }

    public ACRAConfiguration setFormUriBasicAuthPassword(String str) {
        this.mFormUriBasicAuthPassword = str;
        return this;
    }

    public ACRAConfiguration setHttpHeaders(Map map) {
        this.mHttpHeaders = map;
        return this;
    }

    public ACRAConfiguration setHttpMethod(Method method) {
        this.mHttpMethod = method;
        return this;
    }

    public void setHttpsSocketFactoryFactory(HttpsSocketFactoryFactory httpsSocketFactoryFactory) {
        this.mHttpsSocketFactoryFactory = httpsSocketFactoryFactory;
    }

    public ACRAConfiguration setIncludeDropboxSystemTags(Boolean bool) {
        this.mIncludeDropboxSystemTags = bool;
        return this;
    }

    public void setKeyStore(KeyStore keyStore) {
        this.mKeyStore = keyStore;
    }

    public ACRAConfiguration setLogcatArguments(String[] strArr) {
        this.mLogcatArguments = strArr;
        return this;
    }

    public ACRAConfiguration setLogcatFilterByPid(Boolean bool) {
        this.mLogcatFilterByPid = bool;
        return this;
    }

    public ACRAConfiguration setMailTo(String str) {
        this.mMailTo = str;
        return this;
    }

    public ACRAConfiguration setMaxNumberOfRequestRetries(Integer num) {
        this.mMaxNumberOfRequestRetries = num;
        return this;
    }

    public ACRAConfiguration setMode(ReportingInteractionMode reportingInteractionMode) {
        this.mMode = reportingInteractionMode;
        ACRA.checkCrashResources(this);
        return this;
    }

    public ACRAConfiguration setReportDialogClass(Class cls) {
        this.mReportDialogClass = cls;
        return this;
    }

    public ACRAConfiguration setReportType(Type type) {
        this.mReportType = type;
        return this;
    }

    public ACRAConfiguration setResDialogCommentPrompt(int i) {
        this.mResDialogCommentPrompt = Integer.valueOf(i);
        return this;
    }

    public ACRAConfiguration setResDialogEmailPrompt(int i) {
        this.mResDialogEmailPrompt = Integer.valueOf(i);
        return this;
    }

    public ACRAConfiguration setResDialogIcon(int i) {
        this.mResDialogIcon = Integer.valueOf(i);
        return this;
    }

    public ACRAConfiguration setResDialogNegativeButtonText(int i) {
        this.mResDialogNegativeButtonText = Integer.valueOf(i);
        return this;
    }

    public ACRAConfiguration setResDialogOkToast(int i) {
        this.mResDialogOkToast = Integer.valueOf(i);
        return this;
    }

    public ACRAConfiguration setResDialogPositiveButtonText(int i) {
        this.mResDialogPositiveButtonText = Integer.valueOf(i);
        return this;
    }

    public ACRAConfiguration setResDialogText(int i) {
        this.mResDialogText = Integer.valueOf(i);
        return this;
    }

    public ACRAConfiguration setResDialogTitle(int i) {
        this.mResDialogTitle = Integer.valueOf(i);
        return this;
    }

    public ACRAConfiguration setResNotifIcon(int i) {
        this.mResNotifIcon = Integer.valueOf(i);
        return this;
    }

    public ACRAConfiguration setResNotifText(int i) {
        this.mResNotifText = Integer.valueOf(i);
        return this;
    }

    public ACRAConfiguration setResNotifTickerText(int i) {
        this.mResNotifTickerText = Integer.valueOf(i);
        return this;
    }

    public ACRAConfiguration setResNotifTitle(int i) {
        this.mResNotifTitle = Integer.valueOf(i);
        return this;
    }

    public ACRAConfiguration setResToastText(int i) {
        this.mResToastText = Integer.valueOf(i);
        return this;
    }

    public ACRAConfiguration setSendReportsAtShutdown(Boolean bool) {
        this.mSendReportsAtShutdown = bool;
        return this;
    }

    public ACRAConfiguration setSendReportsInDevMode(Boolean bool) {
        this.mSendReportsInDevMode = bool;
        return this;
    }

    public ACRAConfiguration setSharedPreferenceMode(Integer num) {
        this.mSharedPreferenceMode = num;
        return this;
    }

    public ACRAConfiguration setSharedPreferenceName(String str) {
        this.mSharedPreferenceName = str;
        return this;
    }

    public ACRAConfiguration setSocketTimeout(Integer num) {
        this.mSocketTimeout = num;
        return this;
    }

    public int sharedPreferencesMode() {
        if (this.mSharedPreferenceMode != null) {
            return this.mSharedPreferenceMode.intValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.sharedPreferencesMode();
        }
        return 0;
    }

    public String sharedPreferencesName() {
        return this.mSharedPreferenceName != null ? this.mSharedPreferenceName : this.mReportsCrashes != null ? this.mReportsCrashes.sharedPreferencesName() : "";
    }

    public int socketTimeout() {
        return this.mSocketTimeout != null ? this.mSocketTimeout.intValue() : this.mReportsCrashes != null ? this.mReportsCrashes.socketTimeout() : ACRAConstants.DEFAULT_SOCKET_TIMEOUT;
    }
}
