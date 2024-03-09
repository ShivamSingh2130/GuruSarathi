package com.example.gurusarthi;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SharedPref {
    private static final String PREF_NAME = "icon_prefs";
    private static final String KEY_ICON_LIST = "icon_list";

    // Save the list of icons to SharedPreferences
    public static void saveIconList(Context context, List<ChatAlertOpt> iconList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Convert the list to JSON string
        Gson gson = new Gson();
        String json = gson.toJson(iconList);

        editor.putString(KEY_ICON_LIST, json);
        editor.apply();
    }

    // Retrieve the list of icons from SharedPreferences
    public static List<ChatAlertOpt> getIconList(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = preferences.getString(KEY_ICON_LIST, "");

        // Check if the json string is not empty before conversion
        if (!json.isEmpty()) {
            // Convert JSON string to list of ChatAlertOpt objects
            Gson gson = new Gson();
            Type type = new TypeToken<List<ChatAlertOpt>>() {}.getType();
            return gson.fromJson(json, type);
        } else {
            return new ArrayList<>(); // Return an empty list if no data is found
        }
    }
}

