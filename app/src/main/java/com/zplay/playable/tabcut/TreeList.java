package com.zplay.playable.tabcut;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * 非线程安全
 *
 * @param <E>
 */
public class TreeList<E extends Comparable<E>> implements Iterable<E> {
    private Map<E, Integer> mCounter = new HashMap<>();
    private Set<E> mElements = new TreeSet<>(Collections.reverseOrder());

    public void add(E e) {
        int cnt = getCountOf(e);
        mCounter.put(e, cnt + 1);
        mElements.add(e);
    }

    public E remove(E e) {
        int cnt = getCountOf(e);
        if (cnt == 0) {
            return null;
        }
        mCounter.put(e, cnt - 1);
        if (getCountOf(e) == 0) {
            mElements.remove(e);
        }
        return e;
    }

    public boolean isEmpty() {
        return mElements.isEmpty();
    }

    private int getCountOf(E e) {
        Integer i = mCounter.get(e);
        return i == null ? 0 : i;
    }

    public int getSize() {
        int size = 0;
        for (E e : mElements) {
            size += getCountOf(e);
        }
        return size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (E e : mElements) {
            sb.append(e).append(getCountOf(e)).append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public Iterator<E> iterator() {
        return mElements.iterator();
    }
}
