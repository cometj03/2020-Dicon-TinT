package com.sunrin.tint.Firebase.DownLoad;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sunrin.tint.Models.LookBookModel;
import com.sunrin.tint.Util.FirebaseErrorUtil;

import java.util.ArrayList;
import java.util.List;

public class FirebaseLoadLBs {

    // LB : LookBook

    // Variables
    private static int tmp, listSize;
    private static List<LookBookModel> lookBookList;
    private static List<String> IDs;

    // Listeners
    private static FirebaseLoadAllPost.OnLoadFailureListener onLoadFailureListener;
    private static OnLookBookSuccessListener onLookBookSuccessListener;
    private static OnSuccessListener<DocumentSnapshot> onSuccessListener;

    public interface OnLookBookSuccessListener {
        void onSuccess(List<LookBookModel> lookBookModels);
    }


    // MainFunction
    public static void LoadLookBooks(List<String> lookBookIDs, OnLookBookSuccessListener s, FirebaseLoadAllPost.OnLoadFailureListener f) {
        // 데이터 초기화
        tmp = 0;
        listSize = lookBookIDs.size();
        lookBookList = new ArrayList<>();
        IDs = lookBookIDs;
        onLoadFailureListener = f;
        onLookBookSuccessListener = s;

        // 유저가 올린 룩북이 없을 때
        if (lookBookIDs.isEmpty()) {
            s.onSuccess(new ArrayList<>());
            return;
        }

        // 룩북 데이터 하나 로드될 때마다 실행되는 리스너
        onSuccessListener = document -> {
            // tmp++ 해주고 AddPost 함수 실행함
            tmp++;
            AddLookBook(document.toObject(LookBookModel.class));
        };
        loadUserLookBook(lookBookIDs.get(tmp));
    }

    private static void loadUserLookBook(String id) {
        FirebaseFirestore
                .getInstance()
                .collection("lookbooks")
                .document(id)
                .get()
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(e -> onLoadFailureListener.onLoadFailed(
                        FirebaseErrorUtil.getErrorMessage(e, "데이터를 불러오지 못했습니다.")));
    }

    private static void AddLookBook(LookBookModel lookBookModel) {
        lookBookList.add(lookBookModel);
        if (lookBookList.size() < listSize) {
            loadUserLookBook(IDs.get(tmp));
            return;
        }
        // 모든 데이터 불러왔을 때
        onLookBookSuccessListener.onSuccess(lookBookList);
    }
}
