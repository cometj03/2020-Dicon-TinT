package com.sunrin.tint;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sunrin.tint.Feed.FeedItem;

public class PostViewActivity extends AppCompatActivity {

    TextView titleText, subtitleText, contentText;
    ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_view);

        titleText = findViewById(R.id.title);
        subtitleText = findViewById(R.id.subtitle);
        contentText = findViewById(R.id.content);
        imageView = findViewById(R.id.img);

        FeedItem data = (FeedItem) getIntent().getSerializableExtra("FeedItem");

        titleText.setText(data.getTitle());
        subtitleText.setText(data.getSubTitle());
        contentText.setText(data.getContent());

        if (data.getImageID().length() > 0) {
            // Storage 에 있는 이미지
            // Reference to an image file in Cloud Storage
            FirebaseStorage storage = FirebaseStorage.getInstance();
            //StorageReference storageReference = storage.getReferenceFromUrl("gs://tint-360b3.appspot.com/images/" + data.getImageID());
            StorageReference storageReference = storage.getReference().child("images/" + data.getImageID());

            storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Glide.with(getApplicationContext())
                                .load(task.getResult())
                                .into(imageView);
                    } else {
                        Toast.makeText(PostViewActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
}
