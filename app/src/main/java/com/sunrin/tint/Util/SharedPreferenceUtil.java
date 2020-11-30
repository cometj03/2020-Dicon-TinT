package com.sunrin.tint.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class SharedPreferenceUtil {
    static final String PREF_USERNAME = "user_name";
    static final String PREF_FILTER = "filter";

    static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    //*** set ***//
    public static void setPrefUsername(Context context, String userName) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_USERNAME, userName);
        editor.apply();
    }

    public static void setPrefFilterBool(Context context, ArrayList<Boolean> booleans) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < booleans.size(); i++)
            jsonArray.put(booleans.get(i));

        if (!booleans.isEmpty())
            editor.putString(PREF_FILTER, jsonArray.toString());
        else
            editor.putString(PREF_FILTER, "");
        editor.apply();
    }

    //*** get ***//
    public static String getPrefUsername(Context context) {
        return getSharedPreferences(context).getString(PREF_USERNAME, "");
    }

    public static ArrayList<Boolean> getPrefFilterBool(Context context) {
        String s = getSharedPreferences(context).getString(PREF_FILTER, "");
        ArrayList<Boolean> arrayList = new ArrayList<>();
        if (s != null) {
            try {
                JSONArray jsonArray = new JSONArray(s);
                for (int i = 0; i < jsonArray.length(); i++) {
                    boolean b = jsonArray.optBoolean(i);
                    arrayList.add(b);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return arrayList;
    }
}
