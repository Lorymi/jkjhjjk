package org.acra.util;

import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

class NaiveTrustManager implements X509TrustManager {
    NaiveTrustManager() {
    }

    public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) {
    }

    public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) {
    }

    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}
