package org.acra.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public class BoundedLinkedList extends LinkedList {
    private final int maxSize;

    public BoundedLinkedList(int i) {
        this.maxSize = i;
    }

    public void add(int i, Object obj) {
        if (size() == this.maxSize) {
            removeFirst();
        }
        super.add(i, obj);
    }

    public boolean add(Object obj) {
        if (size() == this.maxSize) {
            removeFirst();
        }
        return super.add(obj);
    }

    public boolean addAll(int i, Collection collection) {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(Collection collection) {
        int size = (size() + collection.size()) - this.maxSize;
        if (size > 0) {
            removeRange(0, size);
        }
        return super.addAll(collection);
    }

    public void addFirst(Object obj) {
        throw new UnsupportedOperationException();
    }

    public void addLast(Object obj) {
        add(obj);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator it = iterator();
        while (it.hasNext()) {
            sb.append(it.next().toString());
        }
        return sb.toString();
    }
}
