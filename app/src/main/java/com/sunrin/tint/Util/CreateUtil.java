package com.sunrin.tint.Util;

import android.content.Context;
import android.content.Intent;
import android.media.TimedMetaData;
import android.net.Uri;

import androidx.fragment.app.FragmentActivity;

import com.sunrin.tint.Model.PostModel;
import com.sunrin.tint.Screen.MainActivity;
import com.sunrin.tint.Screen.PostViewActivity;
import com.sunrin.tint.Screen.Posting.PostingActivity;

import java.util.ArrayList;
import java.util.List;

public class CreateUtil {

    public static void CreatePost(Context context, FragmentActivity fa) {
        ImagePickerUtil.PickImages(context, fa, images -> {
            if (!images.isEmpty()) {
                List<String> imageList = uriToString(images);
                Intent postIntent = new Intent(context, PostingActivity.class);
                postIntent.putExtra("post", imageList.toArray());
//                PostModel post = new PostModel(uriToString(images));
//                postIntent.putExtra("post", post); 아니 왜 안 되는거지
                fa.startActivity(postIntent);
            }
        });
    }

    public static void CreateLookBook(Context context, FragmentActivity fa) {
        ImagePickerUtil.PickImage(context, fa, "LookBook", image -> {

        });
    }

    private static List<String> uriToString(List<Uri> uris) {
        return new ArrayList<String>() {
            {
                for (Uri uri : uris)
                    add(uri.toString());
            }
        };
    }
}
