package com.rn5.lists.model;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Group {

    private String name;
    private ArrayList<Item> items = new ArrayList<>();

    public Group() {}
    public Group(String name) {
        this.name = name;
    }

    public void add(Item item) {
        int j = 0;
        for (Item i : items) {
            if (i.equals(item)) {
                items.set(j, item);
                return;
            }
            j++;
        }
        items.add(item);
    }

    public void remove(Item item) {
        items.remove(item);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + name.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Group) {
            String oName = ((Group) o).getName();
            return name.equals(oName);
        } else
            return false;
    }

}
