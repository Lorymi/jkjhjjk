package org.p072a.p073a.p074a.p075a.p076a;

/* renamed from: org.a.a.a.a.a.a */
public abstract class C1038a implements C1039b {

    /* renamed from: a */
    private final String f2302a;

    /* renamed from: b */
    private final String f2303b;

    /* renamed from: c */
    private final String f2304c;

    public C1038a(String str) {
        if (str == null) {
            throw new IllegalArgumentException("MIME type may not be null");
        }
        this.f2302a = str;
        int indexOf = str.indexOf(47);
        if (indexOf != -1) {
            this.f2303b = str.substring(0, indexOf);
            this.f2304c = str.substring(indexOf + 1);
            return;
        }
        this.f2303b = str;
        this.f2304c = null;
    }

    /* renamed from: a */
    public String mo2237a() {
        return this.f2302a;
    }
}
