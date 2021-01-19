package com.sunrin.tint.Screen;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sunrin.tint.Firebase.DownLoad.FirebaseLoadPosts;
import com.sunrin.tint.Models.UserModel;
import com.sunrin.tint.R;
import com.sunrin.tint.Screen.Profile.PostGridAdapter;
import com.sunrin.tint.Screen.Show.ShowPostActivity;
import com.sunrin.tint.Util.UserCache;

public class StorageActivity extends AppCompatActivity {

    private UserModel user;

    RecyclerView recyclerView;
    PostGridAdapter adapter;
    ViewGroup emptyView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        Toolbar toolbar = findViewById(R.id.storage_toolbar);
        toolbar.setTitle("보관함");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        user = UserCache.getUser(this);

        emptyView = findViewById(R.id.empty_view);

        recyclerView = findViewById(R.id.storage_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new PostGridAdapter(emptyView);
        recyclerView.setAdapter(adapter);
        getStoragePost();

        adapter.setOnItemClickListener((v, cover, position) -> {
            if (v.getId() == R.id.post_thumb_nail) {
                Intent intent = new Intent(this, ShowPostActivity.class);
                intent.putExtra("item", adapter.getList().get(position));
                startActivity(intent);
            }
        });
    }

    private void getStoragePost() {
        FirebaseLoadPosts
                .LoadPosts(user.getStorageID(),
                        postModels -> {
                            if (adapter != null) {
                                adapter.setList(postModels);
                                adapter.notifyDataSetChanged();
                            }
                        },
                        errorMsg -> Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
