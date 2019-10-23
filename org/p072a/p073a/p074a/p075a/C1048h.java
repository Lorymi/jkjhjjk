package org.p072a.p073a.p074a.p075a;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Random;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;
import org.p072a.p073a.p074a.p075a.p076a.C1039b;

/* renamed from: org.a.a.a.a.h */
public class C1048h implements HttpEntity {

    /* renamed from: a */
    private static final char[] f2324a = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    /* renamed from: b */
    private final C1043c f2325b;

    /* renamed from: c */
    private final Header f2326c;

    /* renamed from: d */
    private long f2327d;

    /* renamed from: e */
    private volatile boolean f2328e;

    public C1048h() {
        this(C1045e.STRICT, null, null);
    }

    public C1048h(C1045e eVar, String str, Charset charset) {
        if (str == null) {
            str = mo2256a();
        }
        this.f2325b = new C1043c("form-data", charset, str);
        this.f2326c = new BasicHeader("Content-Type", mo2257a(str, charset));
        this.f2328e = true;
        if (eVar == null) {
            eVar = C1045e.STRICT;
        }
        this.f2325b.mo2250a(eVar);
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public String mo2256a() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int nextInt = random.nextInt(11) + 30;
        for (int i = 0; i < nextInt; i++) {
            sb.append(f2324a[random.nextInt(f2324a.length)]);
        }
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public String mo2257a(String str, Charset charset) {
        StringBuilder sb = new StringBuilder();
        sb.append("multipart/form-data; boundary=");
        sb.append(str);
        if (charset != null) {
            sb.append("; charset=");
            sb.append(charset.name());
        }
        return sb.toString();
    }

    /* renamed from: a */
    public void mo2258a(String str, C1039b bVar) {
        mo2259a(new C1037a(str, bVar));
    }

    /* renamed from: a */
    public void mo2259a(C1037a aVar) {
        this.f2325b.mo2249a(aVar);
        this.f2328e = true;
    }

    public void consumeContent() {
        if (isStreaming()) {
            throw new UnsupportedOperationException("Streaming entity does not implement #consumeContent()");
        }
    }

    public InputStream getContent() {
        throw new UnsupportedOperationException("Multipart form entity does not implement #getContent()");
    }

    public Header getContentEncoding() {
        return null;
    }

    public long getContentLength() {
        if (this.f2328e) {
            this.f2327d = this.f2325b.mo2252c();
            this.f2328e = false;
        }
        return this.f2327d;
    }

    public Header getContentType() {
        return this.f2326c;
    }

    public boolean isChunked() {
        return !isRepeatable();
    }

    public boolean isRepeatable() {
        for (C1037a b : this.f2325b.mo2247a()) {
            if (b.mo2233b().mo2242e() < 0) {
                return false;
            }
        }
        return true;
    }

    public boolean isStreaming() {
        return !isRepeatable();
    }

    public void writeTo(OutputStream outputStream) {
        this.f2325b.mo2248a(outputStream);
    }
}
