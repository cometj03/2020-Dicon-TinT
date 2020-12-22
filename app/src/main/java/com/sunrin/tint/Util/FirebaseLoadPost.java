package com.sunrin.tint.Util;

import android.content.Context;
import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sunrin.tint.Feed.FeedAdapter;
import com.sunrin.tint.Feed.FeedItem;
import com.sunrin.tint.Model.PostModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FirebaseLoadPost {

    private static OnLoadFailureListener onLoadFailureListener;

    public interface  OnLoadFailureListener {
        void onLoadFailed(String errorMsg);
    }

    // 데이터 불러온 후 adapter refresh
    public static void LoadPosts(Context context, FeedAdapter adapter, OnLoadFailureListener f) {
        onLoadFailureListener = f;

        ArrayList<PostModel> list = new ArrayList<>();
        ArrayList<FeedItem> feedItemList = new ArrayList<>();

        FirebaseFirestore
                .getInstance()
                .collection("posts")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            list.add(document.toObject(PostModel.class));
                            list.sort(Comparator.reverseOrder());
                        }
                        for (PostModel p : list)
                            feedItemList.add(p.convertIntoFeedItem(context));
                        adapter.setList(feedItemList);
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> f.onLoadFailed(FirebaseErrorUtil.getErrorMessage(e, "데이터를 불러오지 못했습니다.")));
    }

    public static void LoadImages() {
        /*
        * if (item.getImageID().length() > 0) {
            // Storage 에 있는 이미지
            // Reference to an image file in Cloud Storage
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference().child("images/" + item.getImageID());

            storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Glide.with(holder.feed_img)
                                .load(task.getResult())
                                .into(holder.feed_img);
                    } else {
                        Toast.makeText(mContext, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        * */
    }
}
