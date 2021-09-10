package com.rn5.lists.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Item {

    private final int id;
    private String title;
    private String description;
    private long completeDt = 0;
    private boolean isChecked = false;

    public Item() {
        this.id = (int)(System.currentTimeMillis()/1000);
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + id;
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Item) {
            return id == ((Item) o).getId();
        } else
            return false;
    }
}
