package com.ewheelers.ewheelers.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionPreference {
    public static String tokenvalue = "token";
    public static String username = "username";

    public static void saveString(Context context, String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences("Pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getStrings(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences("Pref", Context.MODE_PRIVATE);
        return sharedPref.getString(key, null);
    }

    public static void clearString(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences("Pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, null);
        editor.apply();
    }
}
