package org.p072a.p073a.p074a.p075a;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/* renamed from: org.a.a.a.a.b */
public class C1042b implements Iterable {

    /* renamed from: a */
    private final List f2307a = new LinkedList();

    /* renamed from: b */
    private final Map f2308b = new HashMap();

    /* renamed from: a */
    public C1047g mo2243a(String str) {
        if (str == null) {
            return null;
        }
        List list = (List) this.f2308b.get(str.toLowerCase(Locale.US));
        if (list == null || list.isEmpty()) {
            return null;
        }
        return (C1047g) list.get(0);
    }

    /* renamed from: a */
    public void mo2244a(C1047g gVar) {
        if (gVar != null) {
            String lowerCase = gVar.mo2253a().toLowerCase(Locale.US);
            List list = (List) this.f2308b.get(lowerCase);
            if (list == null) {
                list = new LinkedList();
                this.f2308b.put(lowerCase, list);
            }
            list.add(gVar);
            this.f2307a.add(gVar);
        }
    }

    public Iterator iterator() {
        return Collections.unmodifiableList(this.f2307a).iterator();
    }

    public String toString() {
        return this.f2307a.toString();
    }
}
