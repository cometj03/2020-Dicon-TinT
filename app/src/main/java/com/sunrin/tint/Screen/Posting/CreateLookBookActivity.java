package com.sunrin.tint.Screen.Posting;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.sunrin.tint.Model.PostModel;
import com.sunrin.tint.Model.UserModel;
import com.sunrin.tint.R;
import com.sunrin.tint.Screen.Profile.FirebaseUserCreation;
import com.sunrin.tint.Screen.Profile.ProfilePostAdapter;
import com.sunrin.tint.Util.UserCache;

import java.util.ArrayList;
import java.util.List;

public class CreateLookBookActivity extends AppCompatActivity {

    String mainImage;
    ShimmerRecyclerView linkPostRecycler;
    ProfilePostAdapter postAdapter;

    List<PostModel> postModelList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lookbook);

        mainImage = getIntent().getStringExtra("lookbook_mainImage");

        Button createBtn = findViewById(R.id.createBtn);
        createBtn.setOnClickListener(v -> createLookBook());

        linkPostRecycler = findViewById(R.id.link_post_recycler);
        linkPostRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        postAdapter = new ProfilePostAdapter();
        linkPostRecycler.setAdapter(postAdapter);
        getLinkPostData();
    }

    private void createLookBook() {

    }

    private void getLinkPostData() {
        UserModel userModel = UserCache.getUser(this);

        FirebaseUserCreation
                .LoadUserPosts(userModel.getPostID(),
                        postModels -> {
                            if (postAdapter != null) {
                                postModelList = postModels;
                                postAdapter.setList(postModels);
                                postAdapter.notifyDataSetChanged();
                            }
                        },
                        errorMsg -> Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show());
    }
}
