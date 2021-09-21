package com.rn5.lists.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public abstract class AbstractList<T extends Comparable<? super T>> {

    ArrayList<T> values = new ArrayList<>();

    public AbstractList() {}

    public int add(T t) {
        int j = values.indexOf(t);
        if (j >= 0)
            values.set(j, t);
        else {
            values.add(t);
            sort();
            j = values.indexOf(t);
        }
        return j;
    }

    public int remove(T t) {
        int k = values.indexOf(t);
        values.remove(t);
        return k;
    }

    public void sort() {
        values.sort(Comparator.naturalOrder());
    }
}
