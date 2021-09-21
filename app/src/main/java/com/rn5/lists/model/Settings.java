package com.rn5.lists.model;

import android.util.Log;

import java.io.IOException;

import lombok.Getter;
import lombok.Setter;

import static com.rn5.lists.Constants.loadFromFile;
import static com.rn5.lists.Constants.saveToFile;
import static com.rn5.lists.Constants.toJson;

@Getter
@Setter
public class Settings {

    private static final String TAG = Settings.class.getSimpleName();
    private static final String fileName = "settings.json";
    private int deleteAfter = 30;
    private int hideAfter = 1;

    public Settings() {}

    public static Settings load() {
        try {
            Settings settings = loadFromFile(fileName, Settings.class);
            if (settings == null) {
                settings = new Settings();
                settings.save();
            }
            return settings;
        } catch (Exception e) {
            Log.e(TAG, "load() failed. Error: " + e.getMessage());
            Settings settings = new Settings();
            settings.save();
            return settings;
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
