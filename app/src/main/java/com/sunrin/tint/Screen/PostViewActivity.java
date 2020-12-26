package com.sunrin.tint.Screen;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sunrin.tint.Model.PostModel;
import com.sunrin.tint.R;
import com.sunrin.tint.Screen.Feed.FeedFragment;
import com.sunrin.tint.Util.FirebaseDeletePost;
import com.sunrin.tint.Util.FirebaseUpdateUser;
import com.sunrin.tint.Util.UserCache;
import com.sunrin.tint.View.LoadingDialog;

import java.util.List;

public class PostViewActivity extends AppCompatActivity {

    TextView titleText, subtitleText, contentText;
    ViewGroup imagesContainer;

    private boolean userIsAuthor;
    private PostModel data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_view);

        titleText = findViewById(R.id.title);
        subtitleText = findViewById(R.id.subtitle);
        contentText = findViewById(R.id.content);
        imagesContainer = findViewById(R.id.image_container);

        data = (PostModel) getIntent().getSerializableExtra("item");

        titleText.setText(data.getTitle());
        subtitleText.setText(data.getSubTitle());
        contentText.setText(data.getContent());
        showImageList(data.getImages());

        userIsAuthor = false;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && user.getEmail() != null) {
            String userEmail = user.getEmail();
            String authorEmail = data.getUserEmail();
            // 작성자와 현재 로그인 된 사용자가 같을 때
            // 삭제, 수정 할 수 있게 해줌.
            userIsAuthor = userEmail.equals(authorEmail);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);    // disable title
        toolbar.setTitle("");
        toolbar.setSubtitle("");
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

    private void onClickDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("경고").setMessage("정말 '" + data.getTitle() + "' 을(를) 삭제 하시겠습니까?");

        builder.setPositiveButton("삭제", (dialog, which) -> {

            LoadingDialog loadingDialog = new LoadingDialog(this);
            loadingDialog.setMessage("포스트 삭제 중...").show();

            // 파이어베이스에 있는 포스트 삭제
            FirebaseDeletePost
                    .DeletePost(data.getId(), PostViewActivity.this,
                            a -> {
                                // 유저 정보 업데이트
                                UserCache.updateUser(this, data.getId(), null, UserCache.DELETE_POST,
                                        a1 -> {
                                            // Finish Dialog and Activity
                                            loadingDialog.setMessage("성공적으로 삭제되었습니다.").setFinishListener(this::finish).finish(true);
                                        },
                                        errMsg -> loadingDialog.setMessage(errMsg).finish(false));

                            },
                            errMsg -> loadingDialog.setMessage(errMsg).finish(false));
        });

        builder.setNegativeButton("취소", (dialog, which) -> {});

        builder.setCancelable(true).show();
    }

    private void onClickModify() {
        // TODO: 수정 기능 추가
        Toast.makeText(this, "수정 기능은 개발중에 있습니다 조금만 기다려주세요!", Toast.LENGTH_SHORT).show();
    }

    // Toolbar에 메뉴를 인플레이트 함
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.post_delete_and_modify_menu, menu);
        return userIsAuthor;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.post_item_delete:
                onClickDelete();
                return userIsAuthor;
            case R.id.post_item_modify:
                onClickModify();
                return userIsAuthor;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
