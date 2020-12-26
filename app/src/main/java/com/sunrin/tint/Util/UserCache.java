package com.sunrin.tint.Util;

import android.content.Context;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.sunrin.tint.Filter;
import com.sunrin.tint.Model.UserModel;

import java.util.List;

public class UserCache {

    public interface onUpdateFailureListener {
        void onUpdateFailed(String errMsg);
    }

    public static final int UPDATE_POST = 0;
    public static final int UPDATE_STORAGE = 1;
    public static final int UPDATE_FILTERS = 2;
    public static final int DELETE_POST = 3;

    public static void setUser(Context context, UserModel userModel) {
        Gson gson = new Gson();
        String json = gson.toJson(userModel);
        SharedPreferenceUtil.setString(context, "user_json", json);
    }

    public static UserModel getUser(Context context) {
        Gson gson = new Gson();
        return gson.fromJson(SharedPreferenceUtil.getString(context, "user_json"), UserModel.class);
    }

    public static void updateUser(Context context, String value, List<Filter> filterList, int tmp,
                                  OnSuccessListener<Void> s, onUpdateFailureListener f) {
        UserModel userModel = getUser(context);
        switch (tmp) {
            case UPDATE_POST:
                userModel.addPostID(value);
                break;
            case UPDATE_STORAGE:
                if (userModel.getStorageID().contains(value)) {
                    // 이미 존재함
                    f.onUpdateFailed("이미 보관함에 존재합니다.");
                    return;
                }
                userModel.addStorageID(value);
                break;
            case UPDATE_FILTERS:
                userModel.setUserFilters(filterList);
                break;
            case DELETE_POST:
                userModel.deletePostID(value);
                break;
            default:
                return;
        }
        FirebaseUpdateUser.updateUser(userModel, s, f);
        setUser(context, userModel);
    }

    public static void logout(Context context) {
        SharedPreferenceUtil.setString(context, "user_json", null);
        FirebaseAuth.getInstance().signOut();
    }
}
