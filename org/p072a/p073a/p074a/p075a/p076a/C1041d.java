package org.p072a.p073a.p074a.p075a.p076a;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/* renamed from: org.a.a.a.a.a.d */
public class C1041d extends C1038a {

    /* renamed from: a */
    private final byte[] f2305a;

    /* renamed from: b */
    private final Charset f2306b;

    public C1041d(String str) {
        this(str, "text/plain", null);
    }

    public C1041d(String str, String str2, Charset charset) {
        super(str2);
        if (str == null) {
            throw new IllegalArgumentException("Text may not be null");
        }
        if (charset == null) {
            charset = Charset.defaultCharset();
        }
        this.f2305a = str.getBytes(charset.name());
        this.f2306b = charset;
    }

    /* renamed from: a */
    public void mo2238a(OutputStream outputStream) {
        if (outputStream == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.f2305a);
        byte[] bArr = new byte[4096];
        while (true) {
            int read = byteArrayInputStream.read(bArr);
            if (read != -1) {
                outputStream.write(bArr, 0, read);
            } else {
                outputStream.flush();
                return;
            }
        }
    }

    /* renamed from: b */
    public String mo2239b() {
        return null;
    }

    /* renamed from: c */
    public String mo2240c() {
        return this.f2306b.name();
    }

    /* renamed from: d */
    public String mo2241d() {
        return "8bit";
    }

    /* renamed from: e */
    public long mo2242e() {
        return (long) this.f2305a.length;
    }
}
