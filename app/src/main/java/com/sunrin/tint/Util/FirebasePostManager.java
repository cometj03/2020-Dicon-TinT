package com.sunrin.tint.Util;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sunrin.tint.Feed.FeedAdapter;
import com.sunrin.tint.Feed.FeedItem;
import com.sunrin.tint.Feed.PostModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FirebasePostManager {
    // TODO: Complete Class

    public interface OnGetPostSuccessListener {
        void onSuccess();
    }

    // 데이터 불러온 후 adapter refresh
    public static void LoadFeedItems(FeedAdapter adapter) {
        ArrayList<FeedItem> list = new ArrayList<>();

        FirebaseFirestore
                .getInstance()
                .collection("posts")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            list.add(document.toObject(FeedItem.class));
                            list.sort(Comparator.reverseOrder());
                        }
                        adapter.setList(list);
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    public static void UploadPost(List<PostModel.Filter> filters, String userEmail, String userName, String date, String title, String subTitle, String content, List<String> imgUris) {
        // TODO: Use UerCache

    }
}
