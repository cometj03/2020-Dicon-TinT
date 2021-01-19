package com.sunrin.tint.Util;

import android.content.Context;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.sunrin.tint.Filter;
import com.sunrin.tint.Firebase.User.FirebaseUpdateUser;
import com.sunrin.tint.Models.UserModel;

import java.util.List;

public class UserCache {

    public interface onUpdateFailureListener {
        void onUpdateFailed(String errMsg);
    }

    public static final int UPDATE_POST = 0;
    public static final int UPDATE_STORAGE = 1;
    public static final int UPDATE_LOOKBOOK = 2;
    public static final int UPDATE_FILTERS = 3;
    public static final int DELETE_POST = 4;
    public static final int DELETE_POST_FROM_STORAGE = 5;

    public static void setUser(Context context, UserModel userModel) {
        Gson gson = new Gson();
        String json = gson.toJson(userModel);
        SharedPreferenceUtil.setString(context, "user_json", json);
    }

    public static UserModel getUser(Context context) {
        String json = SharedPreferenceUtil.getString(context, "user_json");
        if (json.length() <= 0)
            return null;
        Gson gson = new Gson();
        return gson.fromJson(json, UserModel.class);
    }

    public static void updateUser(Context context, String value, List<Filter> filterList, int tmp,
                                  OnSuccessListener<Void> s, onUpdateFailureListener f) {
        UserModel user = getUser(context);
        if (user == null) {
            f.onUpdateFailed("유저 정보를 찾을 수 없습니다.");
            return;
        }
        switch (tmp) {
            case UPDATE_POST:
                user.addPostID(value);
                break;
            case UPDATE_STORAGE:
                if (user.getStorageID().contains(value)) {
                    // 이미 보관함에 존재함
                    f.onUpdateFailed("이미 보관함에 존재합니다.");
                    return;
                }
                user.addStorageID(value);
                break;
            case UPDATE_LOOKBOOK:
                user.addLookBookID(value);
                break;
            case UPDATE_FILTERS:
                user.setUserFilters(filterList);
                break;
            case DELETE_POST:
                user.deletePostID(value);
                break;
            case DELETE_POST_FROM_STORAGE:
                if (!user.getStorageID().contains(value)) {
                    f.onUpdateFailed("해당 게시물을 찾을 수 없습니다.");
                    return;
                }
                user.deletePostIDFromStorage(value);
                break;
            default:
                return;
        }
        FirebaseUpdateUser.updateUser(user, s, f);
        setUser(context, user);
    }

    public static void logout(Context context) {
        SharedPreferenceUtil.setString(context, "user_json", "");
        SharedPreferenceUtil.setString(context, "is_first_app", "");
        FirebaseAuth.getInstance().signOut();
    }
}
