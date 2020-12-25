package com.sunrin.tint.Util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class SharedPreferenceUtil {
    static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    //*** set ***//
    public static void setString(Context context, String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(key, value).apply();
    }

    //*** get ***//
    public static String getString(Context context, String key) {
        return getSharedPreferences(context).getString(key, "");
    }
}
