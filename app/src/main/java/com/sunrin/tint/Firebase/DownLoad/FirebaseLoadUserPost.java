package com.sunrin.tint.Firebase.DownLoad;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sunrin.tint.Models.PostModel;
import com.sunrin.tint.Util.FirebaseErrorUtil;
import com.sunrin.tint.Firebase.DownLoad.FirebaseLoadPost;

import java.util.ArrayList;
import java.util.List;

public class FirebaseLoadUserPost {

    // Variables
    private static int tmp, listSize;
    private static List<PostModel> postList;
    private static List<String> IDs;

    // Listeners
    private static FirebaseLoadPost.OnLoadFailureListener onLoadFailureListener;
    private static OnPostSuccessListener onPostSuccessListener;
    private static OnSuccessListener<DocumentSnapshot> onSuccessListener;

    public interface OnPostSuccessListener {
        void onSuccess(List<PostModel> postModels);
    }


    // MainFunction
    public static void LoadUserPosts(List<String> postIDs, OnPostSuccessListener s, FirebaseLoadPost.OnLoadFailureListener f) {
        // 데이터 초기화
        tmp = 0;
        listSize = postIDs.size();
        postList = new ArrayList<>();
        IDs = postIDs;
        onLoadFailureListener = f;
        onPostSuccessListener = s;

        // 유저가 올린 포스트가 없을 때
        if (postIDs.isEmpty()) {
            s.onSuccess(new ArrayList<>());
            return;
        }

        // 포스트 데이터 하나 로드될 때마다 실행되는 리스너
        onSuccessListener = document -> {
            // tmp++ 해주고 AddPost 함수 실행함
            tmp++;
            AddPost(document.toObject(PostModel.class));
        };
        loadUserPost(postIDs.get(tmp));
    }

    private static void loadUserPost(String id) {
        FirebaseFirestore
                .getInstance()
                .collection("posts")
                .document(id)
                .get()
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(e -> onLoadFailureListener.onLoadFailed(
                        FirebaseErrorUtil.getErrorMessage(e, "데이터를 불러오지 못했습니다.")));
    }

    private static void AddPost(PostModel postModel) {
        postList.add(postModel);
        if (postList.size() < listSize) {
            loadUserPost(IDs.get(tmp));
            return;
        }
        // 모든 데이터 불러왔을 때
        onPostSuccessListener.onSuccess(postList);
    }
}
