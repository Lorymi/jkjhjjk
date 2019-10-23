package org.acra.util;

import android.content.Context;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.Map;
import org.acra.ACRA;
import org.acra.ACRAConstants;
import org.acra.sender.HttpSender.Method;
import org.acra.sender.HttpSender.Type;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public final class HttpRequest {
    private int connectionTimeOut = ACRAConstants.DEFAULT_CONNECTION_TIMEOUT;
    private Map headers;
    private String login;
    private int maxNrRetries = 3;
    private String password;
    private int socketTimeOut = ACRAConstants.DEFAULT_CONNECTION_TIMEOUT;

    class SocketTimeOutRetryHandler implements HttpRequestRetryHandler {
        private final HttpParams httpParams;
        private final int maxNrRetries;

        private SocketTimeOutRetryHandler(HttpParams httpParams2, int i) {
            this.httpParams = httpParams2;
            this.maxNrRetries = i;
        }

        public boolean retryRequest(IOException iOException, int i, HttpContext httpContext) {
            if (iOException instanceof SocketTimeoutException) {
                if (i <= this.maxNrRetries) {
                    if (this.httpParams != null) {
                        int soTimeout = HttpConnectionParams.getSoTimeout(this.httpParams) * 2;
                        HttpConnectionParams.setSoTimeout(this.httpParams, soTimeout);
                        ACRA.log.mo2443d(ACRA.LOG_TAG, "SocketTimeOut - increasing time out to " + soTimeout + " millis and trying again");
                    } else {
                        ACRA.log.mo2443d(ACRA.LOG_TAG, "SocketTimeOut - no HttpParams, cannot increase time out. Trying again with current settings");
                    }
                    return true;
                }
                ACRA.log.mo2443d(ACRA.LOG_TAG, "SocketTimeOut but exceeded max number of retries : " + this.maxNrRetries);
            }
            return false;
        }
    }

    private UsernamePasswordCredentials getCredentials() {
        if (this.login == null && this.password == null) {
            return null;
        }
        return new UsernamePasswordCredentials(this.login, this.password);
    }

    private HttpClient getHttpClient(Context context) {
        BasicHttpParams basicHttpParams = new BasicHttpParams();
        basicHttpParams.setParameter("http.protocol.cookie-policy", "rfc2109");
        HttpConnectionParams.setConnectionTimeout(basicHttpParams, this.connectionTimeOut);
        HttpConnectionParams.setSoTimeout(basicHttpParams, this.socketTimeOut);
        HttpConnectionParams.setSocketBufferSize(basicHttpParams, ACRAConstants.DEFAULT_BUFFER_SIZE_IN_BYTES);
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", new PlainSocketFactory(), 80));
        if (ACRA.getConfig().disableSSLCertValidation()) {
            schemeRegistry.register(new Scheme("https", new FakeSocketFactory(), 443));
        } else if (ACRA.getConfig().keyStore() != null) {
            try {
                SSLSocketFactory sSLSocketFactory = new SSLSocketFactory(ACRA.getConfig().keyStore());
                sSLSocketFactory.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
                schemeRegistry.register(new Scheme("https", sSLSocketFactory, 443));
            } catch (KeyManagementException e) {
                schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
            } catch (UnrecoverableKeyException e2) {
                schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
            } catch (NoSuchAlgorithmException e3) {
                schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
            } catch (KeyStoreException e4) {
                schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
            }
        } else {
            schemeRegistry.register(new Scheme("https", ACRA.getConfig().getHttpSocketFactoryFactory().create(context), 443));
        }
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient(new SingleClientConnManager(basicHttpParams, schemeRegistry), basicHttpParams);
        defaultHttpClient.setHttpRequestRetryHandler(new SocketTimeOutRetryHandler(basicHttpParams, this.maxNrRetries));
        return defaultHttpClient;
    }

    private HttpEntityEnclosingRequestBase getHttpRequest(URL url, Method method, String str, Type type) {
        HttpPut httpPut;
        switch (method) {
            case POST:
                httpPut = new HttpPost(url.toString());
                break;
            case PUT:
                httpPut = new HttpPut(url.toString());
                break;
            default:
                throw new UnsupportedOperationException("Unknown method: " + method.name());
        }
        UsernamePasswordCredentials credentials = getCredentials();
        if (credentials != null) {
            httpPut.addHeader(BasicScheme.authenticate(credentials, "UTF-8", false));
        }
        httpPut.setHeader("User-Agent", "Android");
        httpPut.setHeader("Accept", "text/html,application/xml,application/json,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
        httpPut.setHeader("Content-Type", type.getContentType());
        if (this.headers != null) {
            for (String str2 : this.headers.keySet()) {
                httpPut.setHeader(str2, (String) this.headers.get(str2));
            }
        }
        httpPut.setEntity(new StringEntity(str, "UTF-8"));
        return httpPut;
    }

    public static String getParamsAsFormString(Map map) {
        StringBuilder sb = new StringBuilder();
        for (Object next : map.keySet()) {
            if (sb.length() != 0) {
                sb.append('&');
            }
            Object obj = map.get(next);
            if (obj == null) {
                obj = "";
            }
            sb.append(URLEncoder.encode(next.toString(), "UTF-8"));
            sb.append('=');
            sb.append(URLEncoder.encode(obj.toString(), "UTF-8"));
        }
        return sb.toString();
    }

    /* JADX INFO: finally extract failed */
    public void send(Context context, URL url, Method method, String str, Type type) {
        HttpClient httpClient = getHttpClient(context);
        HttpEntityEnclosingRequestBase httpRequest = getHttpRequest(url, method, str, type);
        ACRA.log.mo2443d(ACRA.LOG_TAG, "Sending request to " + url);
        HttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpRequest, new BasicHttpContext());
            if (httpResponse != null) {
                if (httpResponse.getStatusLine() != null) {
                    String num = Integer.toString(httpResponse.getStatusLine().getStatusCode());
                    if (!num.equals("409") && !num.equals("403") && (num.startsWith("4") || num.startsWith("5"))) {
                        throw new IOException("Host returned error code " + num);
                    }
                }
                EntityUtils.toString(httpResponse.getEntity());
            }
            if (httpResponse != null) {
                httpResponse.getEntity().consumeContent();
            }
        } catch (Throwable th) {
            if (httpResponse != null) {
                httpResponse.getEntity().consumeContent();
            }
            throw th;
        }
    }

    public void setConnectionTimeOut(int i) {
        this.connectionTimeOut = i;
    }

    public void setHeaders(Map map) {
        this.headers = map;
    }

    public void setLogin(String str) {
        this.login = str;
    }

    public void setMaxNrRetries(int i) {
        this.maxNrRetries = i;
    }

    public void setPassword(String str) {
        this.password = str;
    }

    public void setSocketTimeOut(int i) {
        this.socketTimeOut = i;
    }
}
