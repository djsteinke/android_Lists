package com.rn5.lists.model;

import com.rn5.lists.enums.GroupColor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.rn5.lists.MainActivity.settings;

@Getter
@Setter
@ToString
public class Group extends AbstractList<Item> implements Comparable<Group> {

    private String name;
    private boolean expanded = true;
    private GroupColor color = GroupColor.GRAY;

    public Group() {}
    public Group(String name) {
        this.name = name;
        values.add(new Item(-1).withTitle("Add new item"));
    }

    public ArrayList<Item> getItems() {
        return values;
    }

    public int getItemCnt() {
        int cnt = 0;
        Calendar today = Calendar.getInstance();
        LocalDate ld = LocalDate.of(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
        for (Item i : values) {
            if (i.getCompleteDt() > 0) {
                Calendar iCal = Calendar.getInstance();
                iCal.setTimeInMillis(i.getCompleteDt());
                LocalDate iLD = LocalDate.of(iCal.get(Calendar.YEAR), iCal.get(Calendar.MONTH), iCal.get(Calendar.DAY_OF_MONTH));
                int d = (int) ChronoUnit.DAYS.between(iLD,ld);
                if (d <= settings.getHideAfter())
                    cnt++;
            } else
                cnt++;
        }
        return cnt;
    }

    public void expanded() {
        expanded = !expanded;
    }

    public int getInProgCnt() {
        int cnt = -1;
        for (Item i : values)
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

    @Override
    public int compareTo(Group o) {
        return name.compareToIgnoreCase(o.getName());
    }
}
