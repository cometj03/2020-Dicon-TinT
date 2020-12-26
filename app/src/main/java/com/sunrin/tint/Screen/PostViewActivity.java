package com.sunrin.tint.Screen;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sunrin.tint.Filter;
import com.sunrin.tint.Model.PostModel;
import com.sunrin.tint.R;

import java.util.List;

public class PostViewActivity extends AppCompatActivity {

    TextView titleText, subtitleText, contentText;
    ViewGroup imagesContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_view);

        titleText = findViewById(R.id.title);
        subtitleText = findViewById(R.id.subtitle);
        contentText = findViewById(R.id.content);
        imagesContainer = findViewById(R.id.image_container);

        PostModel data = (PostModel) getIntent().getSerializableExtra("item");

        titleText.setText(data.getTitle());
        subtitleText.setText(data.getSubTitle());
        contentText.setText(data.getContent());

        showImageList(data.getImages());

        if (data.getFilters().get(0) == Filter.eFashion) {
            Toast.makeText(this, data.getFilters().get(0).toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showImageList(List<String> list) {
        // Remove all views before
        // adding the new ones.
        imagesContainer.removeAllViews();

        imagesContainer.setVisibility(View.VISIBLE);

        int widthPixel = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, getResources().getDisplayMetrics());
        int heightPixel = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, getResources().getDisplayMetrics());


        for (String s : list) {

            View imageHolder = LayoutInflater.from(this).inflate(R.layout.post_image_item, null);
            ImageView thumbnail = imageHolder.findViewById(R.id.media_image);

            Glide.with(thumbnail)
                    .load(s)
                    .apply(new RequestOptions().fitCenter())
                    .into(thumbnail);

            imagesContainer.addView(imageHolder);

            thumbnail.setLayoutParams(new FrameLayout.LayoutParams(widthPixel, heightPixel));

        }
    }
}
