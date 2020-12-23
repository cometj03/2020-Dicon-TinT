package com.sunrin.tint.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.sunrin.tint.Model.UserModel;

public class UserCache {

    public static final int UPDATE_POST = 0;
    public static final int UPDATE_STORAGE = 1;

    private static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setUser(Context context, UserModel userModel) {
        Gson gson = new Gson();
        String json = gson.toJson(userModel);
        SharedPreferenceUtil.setString(context, "user_json", json);
    }

    public static UserModel getUser(Context context) {
        Gson gson = new Gson();
        return gson.fromJson(SharedPreferenceUtil.getString(context, "user_json"), UserModel.class);
    }

    public static void updateUser(Context context, String value, int tmp) {
        UserModel userModel = getUser(context);
        switch (tmp) {
            case UPDATE_POST:
                userModel.addPostID(value);
                break;
            case UPDATE_STORAGE:
                userModel.addStorageID(value);
                break;
            default:
                return;
        }
        FirebaseUpdateUser.updateIDs(userModel, context);
        setUser(context, userModel);
    }

    public static void logout(Context context) {
        SharedPreferenceUtil.setString(context, "user_json", null);
        FirebaseAuth.getInstance().signOut();
    }
}
