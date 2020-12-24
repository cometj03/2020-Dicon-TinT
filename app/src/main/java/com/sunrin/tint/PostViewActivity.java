package com.sunrin.tint;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.sunrin.tint.Model.PostModel;

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

        PostModel data = (PostModel) getIntent().getSerializableExtra("item");

        titleText.setText(data.getTitle());
        subtitleText.setText(data.getSubTitle());
        contentText.setText(data.getContent());

        if (data.getImages() != null) {
            Glide.with(this)
                    .load(data.getImages().get(0))
                    .into(imageView);
        }

        if (data.getFilters().get(0) == Filter.eFashion) {
            Toast.makeText(this, data.getFilters().get(0).toString(), Toast.LENGTH_SHORT).show();
        }

        // TODO: Get Uris
    }
}
