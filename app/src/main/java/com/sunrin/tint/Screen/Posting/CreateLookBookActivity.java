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

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.sunrin.tint.Model.LookBookModel;
import com.sunrin.tint.Model.PostModel;
import com.sunrin.tint.Model.UserModel;
import com.sunrin.tint.R;
import com.sunrin.tint.Screen.Profile.FirebaseUserCreation;
import com.sunrin.tint.Screen.Profile.ProfilePostAdapter;
import com.sunrin.tint.Util.FirebaseUploadLookBook;
import com.sunrin.tint.Util.UserCache;
import com.sunrin.tint.View.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

public class CreateLookBookActivity extends AppCompatActivity {

    ViewGroup emptyView;
    String mainImage;
    ShimmerRecyclerView linkPostRecycler;
    ProfilePostAdapter postAdapter;

    List<PostModel> postModelList = new ArrayList<>();
    List<Boolean> checkedPost = new ArrayList<>();

    private UserModel userModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lookbook);

        userModel = UserCache.getUser(this);

        mainImage = getIntent().getStringExtra("lookbook_mainImage");

        Button createBtn = findViewById(R.id.createBtn);
        createBtn.setOnClickListener(v -> createLookBook());

        emptyView = findViewById(R.id.empty_view);

        linkPostRecycler = findViewById(R.id.link_post_recycler);
        linkPostRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        postAdapter = new ProfilePostAdapter(emptyView);
        linkPostRecycler.setAdapter(postAdapter);
        getLinkPostData();

        Log.d("CreateLookBookActivity", "onCreate: size : *******" + postAdapter.getList().size());

        postAdapter.setOnItemClickListener((v, cover, position) -> {
            switch (v.getId()) {
                case R.id.post_thumb_nail:
                    if (checkedPost.get(position)) {
                        checkedPost.set(position, false);
                        cover.setVisibility(View.INVISIBLE);
                        Log.e("CreateLookBookActivity", "onCreate: " + position + " : " + checkedPost.get(position));
                    } else {
                        checkedPost.set(position, true);
                        cover.setVisibility(View.VISIBLE);
                        Log.e("CreateLookBookActivity", "onCreate: " + position + " : " + checkedPost.get(position));
                    }
                    break;
            }
        });
    }

    private void createLookBook() {
        LoadingDialog dialog = new LoadingDialog(this);
        dialog.setMessage("룩북 업로드 중...").show();

        List<String> linkIDs = getLinkIDs();
        Log.e("CreateLookBookActivity", "createLookBook: id size!!!!!" + linkIDs.size());

        FirebaseUploadLookBook
                .Upload(this, new LookBookModel(linkIDs, mainImage),
                        documentID -> {
                            UserCache.updateUser(this, documentID, null, UserCache.UPDATE_LOOKBOOK,
                                    success -> dialog.setMessage("업로드 성공!").setFinishListener(() -> {
                                        finish();
                                    }).finish(true),
                                    errMsg -> dialog.setMessage(errMsg).finish(false));
                        },
                        errorMsg -> dialog.setMessage(errorMsg).finish(false));
    }

    private void getLinkPostData() {
        if (userModel.getPostID().isEmpty()) {
            if (postAdapter != null) {
                postAdapter.setList(new ArrayList<>());
                postAdapter.notifyDataSetChanged();
            }
            return;
        }

        FirebaseUserCreation
                .LoadUserPosts(userModel.getPostID(),
                        postModels -> {
                            if (postAdapter != null) {
                                Log.d("CreateLookBookActivity", "getLinkPostData: *******size " + postModels.size());

                                for (int i = 0; i < postModels.size(); i++)
                                    checkedPost.add(false);

                                Log.e("CreateLookBookActivity", "getLinkPostData: *******size2 " + checkedPost.size());

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
