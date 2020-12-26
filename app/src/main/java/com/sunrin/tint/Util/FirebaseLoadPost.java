package com.sunrin.tint.Util;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sunrin.tint.Model.PostModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FirebaseLoadPost {

    private static OnLoadFailureListener onLoadFailureListener;

    public interface OnLoadSuccessListener {
        void onLoadSuccess(List<PostModel> postModels);
    }

    public interface OnLoadFailureListener {
        void onLoadFailed(String errorMsg);
    }

    public static void LoadPosts(OnLoadSuccessListener s, OnLoadFailureListener f) {
        onLoadFailureListener = f;

        List<PostModel> list = new ArrayList<>();

        LoadPostContents(s1 -> {
            for (QueryDocumentSnapshot document : s1) {
                list.add(document.toObject(PostModel.class));
                // list.sort(Comparator.reverseOrder());
                list.sort(Comparator.naturalOrder());   // recycler view의 아이템을 반대로 추가되게 해놨기 때문
            }
            s.onLoadSuccess(list);
        });
    }

    private static void LoadPostContents(OnSuccessListener<QuerySnapshot> s) {
        FirebaseFirestore
                .getInstance()
                .collection("posts")
                .get()
                .addOnSuccessListener(s)
                .addOnFailureListener(e -> onLoadFailureListener.onLoadFailed(
                        FirebaseErrorUtil.getErrorMessage(e, "데이터를 불러오지 못했습니다.")));
    }

    /*public static void LoadImage(String imgID, OnSuccessListener<Uri> s) {
        if (imgID == null)
            return;
        FirebaseStorage
                .getInstance()
                .getReference()
                .child("images/" + imgID)
                .getDownloadUrl()
                .addOnSuccessListener(s)
                .addOnFailureListener(e -> onLoadFailureListener.onLoadFailed(
                        FirebaseErrorUtil.getErrorMessage(e, "이미지를 불러오지 못했습니다.")));
    }*/
}
