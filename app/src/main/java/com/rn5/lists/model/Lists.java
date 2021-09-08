package com.rn5.lists.model;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.rn5.lists.Constants.loadFromFile;
import static com.rn5.lists.Constants.saveToFile;
import static com.rn5.lists.Constants.toJson;

@Getter
@Setter
@ToString
public class Lists {

    private static final String TAG = Lists.class.getSimpleName();
    private static final String fileName = "lists.json";
    private ArrayList<Group> groups = new ArrayList<>();

    public Lists() { }

    public void add(Group group) {
        int j = 0;
        for (Group g : groups) {
            if (g.equals(group)) {
                groups.set(j, group);
                return;
            }
            j++;
        }
        groups.add(group);
    }

    public void remove(Group group) {
        groups.remove(group);
    }

    public void add(Item item) {
        String groupName = getItemGroup(item);
        for (Group g : groups) {
            if (g.getName().equals(groupName)) {
                g.add(item);
                return;
            }
        }
    }

    public String getItemGroup(Item item) {
        for (Group g : groups) {
            for (Item i : g.getItems()) {
                if (i.equals(item)) {
                    return g.getName();
                }
            }
        }
        return null;
    }

    public static Lists load() {
        try {
            Lists lists = loadFromFile(fileName, Lists.class);
            if (lists == null)
                lists = new Lists();
            return lists;
        } catch (Exception e) {
            Log.e(TAG, "Lists.load() failed. Error: " + e.getMessage());
            return new Lists();
        }
    }

    public void save() throws IOException {
        saveToFile(fileName, toJson(this));
    }
}
