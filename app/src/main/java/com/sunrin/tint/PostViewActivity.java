package com.sunrin.tint;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sunrin.tint.Feed.FeedItem;

public class PostViewActivity extends AppCompatActivity {

    TextView idText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_view);

        idText = findViewById(R.id.idTextView);

        FeedItem data = (FeedItem) getIntent().getSerializableExtra("FeedItem");
      
        idText.setText(data.getTitle());
    }
}
