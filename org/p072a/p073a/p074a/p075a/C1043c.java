package org.p072a.p073a.p074a.p075a;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.http.util.ByteArrayBuffer;

/* renamed from: org.a.a.a.a.c */
public class C1043c {

    /* renamed from: a */
    private static final ByteArrayBuffer f2309a = m3296a(C1046f.f2321a, ": ");

    /* renamed from: b */
    private static final ByteArrayBuffer f2310b = m3296a(C1046f.f2321a, "\r\n");

    /* renamed from: c */
    private static final ByteArrayBuffer f2311c = m3296a(C1046f.f2321a, "--");

    /* renamed from: d */
    private final String f2312d;

    /* renamed from: e */
    private final Charset f2313e;

    /* renamed from: f */
    private final String f2314f;

    /* renamed from: g */
    private final List f2315g;

    /* renamed from: h */
    private C1045e f2316h;

    public C1043c(String str, Charset charset, String str2) {
        if (str == null) {
            throw new IllegalArgumentException("Multipart subtype may not be null");
        } else if (str2 == null) {
            throw new IllegalArgumentException("Multipart boundary may not be null");
        } else {
            this.f2312d = str;
            if (charset == null) {
                charset = C1046f.f2321a;
            }
            this.f2313e = charset;
            this.f2314f = str2;
            this.f2315g = new ArrayList();
            this.f2316h = C1045e.STRICT;
        }
    }

    /* renamed from: a */
    private static ByteArrayBuffer m3296a(Charset charset, String str) {
        ByteBuffer encode = charset.encode(CharBuffer.wrap(str));
        ByteArrayBuffer byteArrayBuffer = new ByteArrayBuffer(encode.remaining());
        byteArrayBuffer.append(encode.array(), encode.position(), encode.remaining());
        return byteArrayBuffer;
    }

    /* renamed from: a */
    private static void m3297a(String str, OutputStream outputStream) {
        m3302a(m3296a(C1046f.f2321a, str), outputStream);
    }

    /* renamed from: a */
    private static void m3298a(String str, Charset charset, OutputStream outputStream) {
        m3302a(m3296a(charset, str), outputStream);
    }

    /* renamed from: a */
    private void m3299a(C1045e eVar, OutputStream outputStream, boolean z) {
        ByteArrayBuffer a = m3296a(this.f2313e, mo2251b());
        for (C1037a aVar : this.f2315g) {
            m3302a(f2311c, outputStream);
            m3302a(a, outputStream);
            m3302a(f2310b, outputStream);
            C1042b c = aVar.mo2235c();
            switch (eVar) {
                case STRICT:
                    Iterator it = c.iterator();
                    while (it.hasNext()) {
                        m3300a((C1047g) it.next(), outputStream);
                    }
                    break;
                case BROWSER_COMPATIBLE:
                    m3301a(aVar.mo2235c().mo2243a("Content-Disposition"), this.f2313e, outputStream);
                    if (aVar.mo2233b().mo2239b() != null) {
                        m3301a(aVar.mo2235c().mo2243a("Content-Type"), this.f2313e, outputStream);
                        break;
                    }
                    break;
            }
            m3302a(f2310b, outputStream);
            if (z) {
                aVar.mo2233b().mo2238a(outputStream);
            }
            m3302a(f2310b, outputStream);
        }
        m3302a(f2311c, outputStream);
        m3302a(a, outputStream);
        m3302a(f2311c, outputStream);
        m3302a(f2310b, outputStream);
    }

    /* renamed from: a */
    private static void m3300a(C1047g gVar, OutputStream outputStream) {
        m3297a(gVar.mo2253a(), outputStream);
        m3302a(f2309a, outputStream);
        m3297a(gVar.mo2254b(), outputStream);
        m3302a(f2310b, outputStream);
    }

    /* renamed from: a */
    private static void m3301a(C1047g gVar, Charset charset, OutputStream outputStream) {
        m3298a(gVar.mo2253a(), charset, outputStream);
        m3302a(f2309a, outputStream);
        m3298a(gVar.mo2254b(), charset, outputStream);
        m3302a(f2310b, outputStream);
    }

    /* renamed from: a */
    private static void m3302a(ByteArrayBuffer byteArrayBuffer, OutputStream outputStream) {
        outputStream.write(byteArrayBuffer.buffer(), 0, byteArrayBuffer.length());
    }

    /* renamed from: a */
    public List mo2247a() {
        return this.f2315g;
    }

    /* renamed from: a */
    public void mo2248a(OutputStream outputStream) {
        m3299a(this.f2316h, outputStream, true);
    }

    /* renamed from: a */
    public void mo2249a(C1037a aVar) {
        if (aVar != null) {
            this.f2315g.add(aVar);
        }
    }

    /* renamed from: a */
    public void mo2250a(C1045e eVar) {
        this.f2316h = eVar;
    }

    /* renamed from: b */
    public String mo2251b() {
        return this.f2314f;
    }

    /* renamed from: c */
    public long mo2252c() {
        long j = 0;
        for (C1037a b : this.f2315g) {
            long e = b.mo2233b().mo2242e();
            if (e < 0) {
                return -1;
            }
            j += e;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            m3299a(this.f2316h, (OutputStream) byteArrayOutputStream, false);
            return j + ((long) byteArrayOutputStream.toByteArray().length);
        } catch (IOException e2) {
            return -1;
        }
    }
}
