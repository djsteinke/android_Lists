package com.rn5.lists;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static com.rn5.lists.MainActivity.filePathApp;
import static com.rn5.lists.MainActivity.loadListener;

public abstract class Constants {
    private Constants() {}

    public static final int dayInMs = 86400000;
    public static final SimpleDateFormat sdfError = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);

    public static <T> T fromJson(String json, Class<T> t) {
        Gson gson = new Gson();
        return gson.fromJson(json, t);
    }

    public static <T> String toJson(T t) {
        Gson gson = new Gson();
        return gson.toJson(t);
    }

    public static void saveToFile(String fileName, String content) throws IOException {
        File file = new File(filePathApp,fileName);
        if (file.exists() || (!file.exists() && file.createNewFile())) {
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
            fw.close();
        }
        Log.d("saveJSONToFile","File[" + fileName + "] saved.");
    }

    public static <T> T loadFromFile(String fileName, Class<T> t) throws Exception {
        File file = new File(filePathApp,fileName);
        if (file.exists()) {
            FileInputStream fis = new FileInputStream(file);
            BufferedReader bfr = new BufferedReader(new InputStreamReader(fis));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bfr.readLine()) != null) {
                sb.append(line).append("\n");
            }
            bfr.close();
            fis.close();
            Log.d("loadJSONFromFile","File[" + fileName + "] loaded.");
            return fromJson(sb.toString(), t);
        } else {
            Log.d("loadJSONFromFile","File[" + fileName + "] does not exist.");
            return null;
        }
    }
}
