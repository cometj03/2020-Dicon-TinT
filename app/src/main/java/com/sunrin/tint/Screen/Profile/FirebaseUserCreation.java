package com.sunrin.tint.Screen.Profile;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sunrin.tint.Model.LookBookModel;
import com.sunrin.tint.Model.PostModel;
import com.sunrin.tint.Util.FirebaseErrorUtil;
import com.sunrin.tint.Util.FirebaseLoadPost;

import java.util.ArrayList;
import java.util.List;

public class FirebaseUserCreation {

    private static int tmp1, tmp2, listSize;
    private static List<PostModel> postList;
    private static List<LookBookModel> lookBookList;
    private static List<String> IDs;

    private static FirebaseLoadPost.OnLoadFailureListener onLoadFailureListener;
    private static OnPostSuccessListener onPostSuccessListener;
    private static OnLookBookSuccessListener onLookBookSuccessListener;
    private static OnSuccessListener<DocumentSnapshot> onSuccessListener1, onSuccessListener2;

    public interface OnPostSuccessListener {
        void onSuccess(List<PostModel> postModels);
    }

    public interface OnLookBookSuccessListener {
        void onSuccess(List<LookBookModel> lookBookModels);
    }


    //******** Post **********//
    public static void LoadUserPosts(List<String> postIDs, OnPostSuccessListener s, FirebaseLoadPost.OnLoadFailureListener f) {
        tmp1 = 0;
        listSize = postIDs.size();
        postList = new ArrayList<>();
        IDs = postIDs;
        onLoadFailureListener = f;
        onPostSuccessListener = s;

        onSuccessListener1 = document -> {
            tmp1++;
            AddPost(document.toObject(PostModel.class));
        };
        loadUserPost(postIDs.get(tmp1));
    }

    private static void loadUserPost(String id) {
        FirebaseFirestore
                .getInstance()
                .collection("posts")
                .document(id)
                .get()
                .addOnSuccessListener(onSuccessListener1)
                .addOnFailureListener(e -> onLoadFailureListener.onLoadFailed(
                        FirebaseErrorUtil.getErrorMessage(e, "데이터를 불러오지 못했습니다.")));
    }

    private static void AddPost(PostModel postModel) {
        postList.add(postModel);
        if (postList.size() < listSize) {
            loadUserPost(IDs.get(tmp1));
            return;
        }
        onPostSuccessListener.onSuccess(postList);
    }



    //******** LookBook **********//
    public static void LoadUserLookBooks(List<String> lookBookIDs, OnLookBookSuccessListener s, FirebaseLoadPost.OnLoadFailureListener f) {
        tmp2 = 0;
        listSize = lookBookIDs.size();
        lookBookList = new ArrayList<>();
        IDs = lookBookIDs;
        onLoadFailureListener = f;
        onLookBookSuccessListener = s;

        onSuccessListener2 = document -> {
            tmp2++;
            AddLookBook(document.toObject(LookBookModel.class));
        };
        loadUserLookBook(lookBookIDs.get(tmp2));
    }

    private static void loadUserLookBook(String id) {
        FirebaseFirestore
                .getInstance()
                .collection("lookbooks")
                .document(id)
                .get()
                .addOnSuccessListener(onSuccessListener2)
                .addOnFailureListener(e ->  onLoadFailureListener.onLoadFailed(
                        FirebaseErrorUtil.getErrorMessage(e, "데이터를 불러오지 못했습니다.")));
    }

    private static void AddLookBook(LookBookModel lookBookModel) {
        lookBookList.add(lookBookModel);
        if (lookBookList.size() < listSize) {
            loadUserLookBook(IDs.get(tmp2));
            return;
        }
        onLookBookSuccessListener.onSuccess(lookBookList);
    }
}
