package com.sunrin.tint.Screen.Posting;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.github.okdroid.checkablechipview.CheckableChipView;
import com.sunrin.tint.Filter;
import com.sunrin.tint.Model.PostModel;
import com.sunrin.tint.R;
import com.sunrin.tint.Util.FirebaseUploadPost;
import com.sunrin.tint.Util.UserCache;
import com.sunrin.tint.View.LoadingDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreatePostActivity extends AppCompatActivity {

    Button postBtn;
    EditText titleText, subtitleText, contentText;
    List<CheckableChipView> chipViews = new ArrayList<>();

    private List<String> selectedImages;

    private ViewGroup selectedImageContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        init();

        String[] a = getIntent().getStringArrayExtra("images");
        selectedImages = new ArrayList<>(Arrays.asList(a));
        showUriList(selectedImages);

        postBtn.setOnClickListener(v -> UploadPost());
        titleText.addTextChangedListener(textWatcher);
        subtitleText.addTextChangedListener(textWatcher);
        contentText.addTextChangedListener(textWatcher);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkCanUpload();
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    private void UploadPost() {
        String title = titleText.getText().toString();
        String subTitle = subtitleText.getText().toString();
        String content = contentText.getText().toString();

        List<Filter> filters = getFilters();

        LoadingDialog dialog = new LoadingDialog(this);
        dialog.setMessage("포스트 업로드 중...").show();

        FirebaseUploadPost
                .Upload(this, new PostModel(filters, selectedImages, title, subTitle, content),
                        (documentID) -> {
                            dialog.setMessage("업로드 완료")
                                    .setFinishListener(() -> PostDone(documentID))
                                    .finish(true);
                        },
                        errorMsg -> dialog.setMessage(errorMsg).finish(false));
    }

    private void init() {
        postBtn = findViewById(R.id.postBtn);
        titleText = findViewById(R.id.titleText);
        subtitleText = findViewById(R.id.subtitleText);
        contentText = findViewById(R.id.contentText);
        selectedImageContainer = findViewById(R.id.selected_image_container);
        chipViews.add(findViewById(R.id.chip1));
        chipViews.add(findViewById(R.id.chip2));
        chipViews.add(findViewById(R.id.chip3));
        chipViews.add(findViewById(R.id.chip4));
        chipViews.add(findViewById(R.id.chip5));
        for (CheckableChipView chip : chipViews)
            chip.setOnCheckedChangeListener((chipView, aBoolean) -> {
                chip.setCheckedColor(ContextCompat.getColor(this,
                        aBoolean ? R.color.pink_700 : R.color.gray));
                return null;
            });
    }

    private void showUriList(List<String> imageList) {
        // Remove all views before
        // adding the new ones.
        selectedImageContainer.removeAllViews();

        selectedImageContainer.setVisibility(View.VISIBLE);

        int widthPixel = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 330, getResources().getDisplayMetrics());
        int heightPixel = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400, getResources().getDisplayMetrics());


        for (String s : imageList) {

            View imageHolder = LayoutInflater.from(this).inflate(R.layout.post_image_item, null);
            ImageView thumbnail = imageHolder.findViewById(R.id.media_image);

            Glide.with(thumbnail)
                    .load(s)
                    //.apply(new RequestOptions().fitCenter())
                    .into(thumbnail);

            selectedImageContainer.addView(imageHolder);

            thumbnail.setLayoutParams(new FrameLayout.LayoutParams(widthPixel, heightPixel));

        }
    }


    private void checkCanUpload() {
        boolean t = titleText.getText().toString().isEmpty();
        boolean s = subtitleText.getText().toString().isEmpty();
        postBtn.setEnabled(!t && !s);
    }

    private List<Filter> getFilters() {
        return new ArrayList<Filter>() {
            {
                for (int i = 0; i < chipViews.size(); i++)
                    if (chipViews.get(i).isChecked())
                        add(Filter.values()[i]);
            }
        };
    }

    private void PostDone(String docId) {
        // 포스팅 한 후 유저 정보 업데이트
        UserCache.updateUser(this, docId, null, UserCache.UPDATE_POST,
                aVoid -> {},
                errMsg -> Toast.makeText(this, errMsg, Toast.LENGTH_SHORT).show());
        finish();
    }
}
