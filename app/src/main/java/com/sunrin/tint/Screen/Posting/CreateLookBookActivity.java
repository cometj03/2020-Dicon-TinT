package com.sunrin.tint.Screen.Posting;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sunrin.tint.Models.LookBookModel;
import com.sunrin.tint.Models.PostModel;
import com.sunrin.tint.Models.UserModel;
import com.sunrin.tint.R;
import com.sunrin.tint.Firebase.DownLoad.FirebaseLoadUserPost;
import com.sunrin.tint.Screen.Profile.PostGridAdapter;
import com.sunrin.tint.Firebase.UpLoad.FirebaseUploadLB;
import com.sunrin.tint.Util.UserCache;
import com.sunrin.tint.View.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

public class CreateLookBookActivity extends AppCompatActivity {

    ViewGroup emptyView;
    String mainImage;
    RecyclerView linkPostRecycler;
    PostGridAdapter postAdapter;

    List<PostModel> postModelList = new ArrayList<>();
    List<Boolean> checkedPost = new ArrayList<>();

    private UserModel user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lookbook);

        user = UserCache.getUser(this);

        mainImage = getIntent().getStringExtra("lookbook_mainImage");

        Button createBtn = findViewById(R.id.createBtn);
        createBtn.setOnClickListener(v -> createLookBook());

        emptyView = findViewById(R.id.empty_view);

        linkPostRecycler = findViewById(R.id.link_post_recycler);
        linkPostRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        postAdapter = new PostGridAdapter(emptyView);
        linkPostRecycler.setAdapter(postAdapter);
        getLinkPostData();

        postAdapter.setOnItemClickListener((v, cover, position) -> {
            if (v.getId() == R.id.post_thumb_nail) {
                checkedPost.set(position, !checkedPost.get(position));
                cover.setVisibility(checkedPost.get(position) ?
                        View.VISIBLE : View.INVISIBLE);
            }
        });
    }

    private void createLookBook() {
        LoadingDialog dialog = new LoadingDialog(this);
        dialog.setMessage("룩북 업로드 중...").show();

        List<String> linkID = getLinkIDs();

        FirebaseUploadLB
                .Upload(this, new LookBookModel(linkID, mainImage),
                        documentID -> {
                            UserCache.updateUser(this, documentID, null, UserCache.UPDATE_LOOKBOOK,
                                    success -> dialog.setMessage("업로드 성공!").setFinishListener(this::finish).finish(true),
                                    errMsg -> dialog.setMessage(errMsg).finish(false));
                        },
                        errorMsg -> dialog.setMessage(errorMsg).finish(false));
    }

    private void getLinkPostData() {
        FirebaseLoadUserPost
                .LoadUserPosts(user.getPostID(),
                        postModels -> {
                            if (postAdapter != null) {
                                for (int i = 0; i < postModels.size(); i++)
                                    checkedPost.add(false);
                                postModelList = postModels;
                                postAdapter.setList(postModels);
                                postAdapter.notifyDataSetChanged();
                            }
                        },
                        errorMsg -> Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show());
    }

    private List<String> getLinkIDs() {
        return new ArrayList<String>() {
            {
                for (int i = 0; i < checkedPost.size(); i++)
                    if (checkedPost.get(i))
                        add(postModelList.get(i).getId());
            }
        };
    }
}
