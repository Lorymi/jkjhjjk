package org.p072a.p073a.p074a.p075a;

import org.p072a.p073a.p074a.p075a.p076a.C1039b;

/* renamed from: org.a.a.a.a.a */
public class C1037a {

    /* renamed from: a */
    private final String f2299a;

    /* renamed from: b */
    private final C1042b f2300b;

    /* renamed from: c */
    private C1039b f2301c;

    public C1037a(String str, C1039b bVar) {
        if (str == null) {
            throw new IllegalArgumentException("Name may not be null");
        } else if (bVar == null) {
            throw new IllegalArgumentException("Body may not be null");
        } else {
            this.f2299a = str;
            this.f2301c = bVar;
            this.f2300b = new C1042b();
            mo2232a(bVar);
            mo2234b(bVar);
            mo2236c(bVar);
        }
    }

    /* renamed from: a */
    public String mo2230a() {
        return this.f2299a;
    }

    /* renamed from: a */
    public void mo2231a(String str, String str2) {
        if (str == null) {
            throw new IllegalArgumentException("Field name may not be null");
        }
        this.f2300b.mo2244a(new C1047g(str, str2));
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo2232a(C1039b bVar) {
        StringBuilder sb = new StringBuilder();
        sb.append("form-data; name=\"");
        sb.append(mo2230a());
        sb.append("\"");
        if (bVar.mo2239b() != null) {
            sb.append("; filename=\"");
            sb.append(bVar.mo2239b());
            sb.append("\"");
        }
        mo2231a("Content-Disposition", sb.toString());
    }

    /* renamed from: b */
    public C1039b mo2233b() {
        return this.f2301c;
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public void mo2234b(C1039b bVar) {
        if (bVar.mo2237a() != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(bVar.mo2237a());
            if (bVar.mo2240c() != null) {
                sb.append("; charset=");
                sb.append(bVar.mo2240c());
            }
            mo2231a("Content-Type", sb.toString());
        }
    }

    /* renamed from: c */
    public C1042b mo2235c() {
        return this.f2300b;
    }

    /* access modifiers changed from: protected */
    /* renamed from: c */
    public void mo2236c(C1039b bVar) {
        if (bVar.mo2241d() != null) {
            mo2231a("Content-Transfer-Encoding", bVar.mo2241d());
        }
    }
}
