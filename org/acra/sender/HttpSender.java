package org.acra.sender;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.acra.ACRA;
import org.acra.ACRAConfiguration;
import org.acra.ACRAConstants;
import org.acra.ReportField;
import org.acra.collector.CrashReportData;
import org.acra.util.HttpRequest;
import org.acra.util.JSONReportBuilder.JSONReportException;

public class HttpSender implements ReportSender {
    private final Uri mFormUri;
    private final Map mMapping;
    private final Method mMethod;
    private String mPassword;
    private final Type mType;
    private String mUsername;

    public enum Method {
        POST,
        PUT
    }

    public enum Type {
        FORM {
            public String getContentType() {
                return "application/x-www-form-urlencoded";
            }
        },
        JSON {
            public String getContentType() {
                return "application/json";
            }
        };

        public abstract String getContentType();
    }

    public HttpSender(Method method, Type type, String str, Map map) {
        this.mMethod = method;
        this.mFormUri = Uri.parse(str);
        this.mMapping = map;
        this.mType = type;
        this.mUsername = null;
        this.mPassword = null;
    }

    public HttpSender(Method method, Type type, Map map) {
        this.mMethod = method;
        this.mFormUri = null;
        this.mMapping = map;
        this.mType = type;
        this.mUsername = null;
        this.mPassword = null;
    }

    private Map remap(Map map) {
        ReportField[] customReportContent = ACRA.getConfig().customReportContent();
        if (customReportContent.length == 0) {
            customReportContent = ACRAConstants.DEFAULT_REPORT_FIELDS;
        }
        HashMap hashMap = new HashMap(map.size());
        for (ReportField reportField : customReportContent) {
            if (this.mMapping == null || this.mMapping.get(reportField) == null) {
                hashMap.put(reportField.toString(), map.get(reportField));
            } else {
                hashMap.put(this.mMapping.get(reportField), map.get(reportField));
            }
        }
        return hashMap;
    }

    public void send(Context context, CrashReportData crashReportData) {
        String jSONObject;
        try {
            URL url = this.mFormUri == null ? new URL(ACRA.getConfig().formUri()) : new URL(this.mFormUri.toString());
            Log.d(ACRA.LOG_TAG, "Connect to " + url.toString());
            String formUriBasicAuthLogin = this.mUsername != null ? this.mUsername : ACRAConfiguration.isNull(ACRA.getConfig().formUriBasicAuthLogin()) ? null : ACRA.getConfig().formUriBasicAuthLogin();
            String formUriBasicAuthPassword = this.mPassword != null ? this.mPassword : ACRAConfiguration.isNull(ACRA.getConfig().formUriBasicAuthPassword()) ? null : ACRA.getConfig().formUriBasicAuthPassword();
            HttpRequest httpRequest = new HttpRequest();
            httpRequest.setConnectionTimeOut(ACRA.getConfig().connectionTimeout());
            httpRequest.setSocketTimeOut(ACRA.getConfig().socketTimeout());
            httpRequest.setMaxNrRetries(ACRA.getConfig().maxNumberOfRequestRetries());
            httpRequest.setLogin(formUriBasicAuthLogin);
            httpRequest.setPassword(formUriBasicAuthPassword);
            httpRequest.setHeaders(ACRA.getConfig().getHttpHeaders());
            switch (this.mType) {
                case JSON:
                    jSONObject = crashReportData.toJSON().toString();
                    break;
                default:
                    jSONObject = HttpRequest.getParamsAsFormString(remap(crashReportData));
                    break;
            }
            switch (this.mMethod) {
                case POST:
                    break;
                case PUT:
                    url = new URL(url.toString() + '/' + crashReportData.getProperty(ReportField.REPORT_ID));
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown method: " + this.mMethod.name());
            }
            httpRequest.send(context, url, this.mMethod, jSONObject, this.mType);
        } catch (IOException e) {
            throw new ReportSenderException("Error while sending " + ACRA.getConfig().reportType() + " report via Http " + this.mMethod.name(), e);
        } catch (JSONReportException e2) {
            throw new ReportSenderException("Error while sending " + ACRA.getConfig().reportType() + " report via Http " + this.mMethod.name(), e2);
        }
    }

    public void setBasicAuth(String str, String str2) {
        this.mUsername = str;
        this.mPassword = str2;
    }
}
