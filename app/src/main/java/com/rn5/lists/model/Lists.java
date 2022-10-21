package com.rn5.lists.model;

import android.util.Log;

import com.rn5.lists.enums.GroupColor;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.rn5.lists.Constants.loadFromFile;
import static com.rn5.lists.Constants.saveToFile;
import static com.rn5.lists.Constants.sdfError;
import static com.rn5.lists.Constants.toJson;
import static com.rn5.lists.MainActivity.settings;

@Getter
@Setter
@ToString
public class Lists extends AbstractList<Group> {

    private static final String TAG = Lists.class.getSimpleName();
    private static final String fileName = "lists.json";

    public Lists() {}

    public Group getGroup(Group group) {
        int i = values.indexOf(group);
        if (i > -1)
            return values.get(i);
        return null;
    }

    public void swapGroups(int from, int to) {
        Collections.swap(values, from, to);
        save();
    }

    public ArrayList<Group> getGroups() {
        return values;
    }

    public ArrayList<Item> getGroupItems(Group g) {
        for (Group group : values) {
            if (group.equals(g))
                return group.getItems();
        }
        return null;
    }

    public int getItemCnt(Group in) {
        int i = values.indexOf(in);
        Group g = values.get(i);
        return g.getItemCnt();
    }

    public void setColor(Group in, GroupColor color) {
        values.stream()
                .filter(in::equals)
                .findAny().ifPresent(group -> group.setColor(color));
        save();
    }

    public int add(Item item, Group group) {
        int j = 0;
        for (Group g : values) {
            if (g.equals(group)) {
                return g.add(item);
            }
        }
        return -1;
    }

    public int remove(Item item, Group group) {
        int j = -1;
        for (Group g : values) {
            if (g.equals(group)) {
                j = g.getItems().indexOf(item);
                if (j >= 0)
                    g.remove(item);
                break;
            }
        }
        return j;
    }

    public void expand(Group group) {
        int j = 0;
        for (Group g : values) {
            if (g.equals(group)) {
                values.get(j).expanded();
                save();
                return;
            }
            j++;
        }
    }

    public int check(String groupName, long id) {
        Log.d(TAG, "Group[" + groupName + "]" + " ID[" + id + "]");
        boolean checked = false;
        Group group = null;
        Item item = null;
        int pos = -1;
        for (Group g : values) {
            if (g.getName().equals(groupName)) {
                group = g;
                for (Item i : g.getItems()) {
                    if (i.getId() == id) {
                        item = i;
                        pos = g.getItems().indexOf(i);
                        checked = i.check();
                        break;
                    }
                }
            }
        }
        if (pos >= 0) {
            group.sort();
            if (!checked)
                pos = group.getItems().indexOf(item);
            save();
        }
        return pos;
    }

    public static Lists load() {
        Calendar today = Calendar.getInstance();
        LocalDate ld = LocalDate.of(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
        Lists lists = new Lists();
        try {
            lists = loadFromFile(fileName, Lists.class);
            if (lists == null)
                lists = new Lists();
            else {
                for (Group g : lists.getGroups()) {
                    g.sort();
                    ArrayList<Item> deleteItems = new ArrayList<>();
                    for (Item i : g.getItems()) {
                        if (i.getCompleteDt() > 0) {
                            Calendar iCal = Calendar.getInstance();
                            iCal.setTimeInMillis(i.getCompleteDt());
                            LocalDate iLD = LocalDate.of(iCal.get(Calendar.YEAR), iCal.get(Calendar.MONTH), iCal.get(Calendar.DAY_OF_MONTH));
                            int d = (int) ChronoUnit.DAYS.between(iLD,ld);
                            if (d >= settings.getDeleteAfter()) {
                                deleteItems.add(i);
                            }
                        }
                    }
                    if (deleteItems.size() > 0) {
                        for (Item i : deleteItems) {
                            g.remove(i);
                        }
                    }
                }
            }
            return lists;
        } catch (Exception e) {
            Log.e(TAG, "Lists.load() failed. Error: " + e.getMessage());
            if (lists != null)
                try {
                    String fileName = "lists.json.error." + sdfError.format(new Date());
                    saveToFile(fileName, toJson(lists));
                } catch (IOException ioE) {
                    Log.e(TAG, "Attempt to save loaded lists file failed. Error: " + ioE.getMessage());
                }
            return new Lists();
        }
    }

    public void save() {
        try {
            saveToFile(fileName, toJson(this));
        } catch (IOException e) {
            Log.e(TAG, "save() failed. Error: " + e.getMessage());
        }
    }
}
