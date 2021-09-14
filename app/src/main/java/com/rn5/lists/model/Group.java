package com.rn5.lists.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Group {

    private String name;
    private boolean expanded = true;
    private ArrayList<Item> items = new ArrayList<>();

    public Group() {}
    public Group(String name) {
        this.name = name;
    }

    public int add(Item item) {
        int j = 0;
        for (Item i : items) {
            if (i.equals(item)) {
                items.set(j, item);
                return j;
            }
            j++;
        }
        items.add(item);
        return j;
    }

    public void remove(Item item) {
        items.remove(item);
    }

    public void expanded() {
        expanded = !expanded;
    }

    public int getInProgCnt() {
        int cnt = 0;
        for (Item i : items)
            if (i.getCompleteDt() == 0)
                cnt ++;
        return cnt;
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
