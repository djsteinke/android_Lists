package com.rn5.lists.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Item implements Comparable<Item> {

    private static final long compOffset = 100000000000L;
    private final int id;
    private String title;
    private String description;
    private long completeDt = 0;
    private boolean isChecked = false;

    public Item() {
        this.id = (int)(System.currentTimeMillis()/1000);
    }
    public Item(int id) {
        this.id = id;
    }

    public Item withTitle(String title) {
        this.title = title;
        return this;
    }
    public Item withDescription(String description) {
        this.description = description;
        return this;
    }

    public void isChecked(boolean checked) {
        completeDt = (checked ? System.currentTimeMillis() : 0);
        isChecked = checked;
    }

    public boolean check() {
        isChecked = !isChecked;
        completeDt = (isChecked ? System.currentTimeMillis() : 0);
        return isChecked;
    }


    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + id;
        return hash;
    }

    /*
    @Override
    public int compareTo(Item o1) {
        long oId = (completeDt > 0 ? completeDt-compOffset : id);
        long o1Id = (o1.getCompleteDt() > 0 ? o1.getCompleteDt()-compOffset : o1.getId());
        return Long.compare(oId, o1Id);
    } */

    @Override
    public int compareTo(Item o1) {
        boolean complete = false;
        long oId = id;
        long o1Id = o1.getId();

        if (completeDt > 0) {
            oId = completeDt+compOffset;
            complete = true;
        }

        if (o1.getCompleteDt() > 0)
            o1Id = o1.getCompleteDt()+compOffset;
        else
            complete = false;

        if (complete)
            return Long.compare(o1Id,oId);
        else
            return Long.compare(oId, o1Id);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Item) {
            return id == ((Item) o).getId();
        } else
            return false;
    }
}
