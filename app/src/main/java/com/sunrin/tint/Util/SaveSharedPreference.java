package com.sunrin.tint.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference {
    static final String PREF_EX = "ex";

    static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    //*** set ***//
    public static void setEx(Context context, String ex) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_EX, ex);
        editor.apply();
    }

    //*** get ***//
    public static String getPrefEx(Context context) {
        return getSharedPreferences(context).getString(PREF_EX, "");
    }
}
