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
import com.sunrin.tint.Models.PostModel;
import com.sunrin.tint.Models.UserModel;
import com.sunrin.tint.R;
import com.sunrin.tint.Firebase.FirebaseDeletePost;
import com.sunrin.tint.Util.UserCache;
import com.sunrin.tint.View.LoadingDialog;

import java.util.List;

public class ShowPostActivity extends AppCompatActivity {

    TextView titleText, subtitleText, contentText;
    ViewGroup imagesContainer;

    private boolean authorIsUser, isContainsStorage;
    private PostModel data;
    private final int Delete_From_Storage = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_post);

        titleText = findViewById(R.id.title);
        subtitleText = findViewById(R.id.subtitle);
        contentText = findViewById(R.id.content);
        imagesContainer = findViewById(R.id.image_container);

        data = (PostModel) getIntent().getSerializableExtra("item");

        titleText.setText(data.getTitle());
        subtitleText.setText(data.getSubTitle());
        contentText.setText(data.getContent());
        showImageList(data.getImages());

        isContainsStorage = authorIsUser = false;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && user.getEmail() != null) {
            String userEmail = user.getEmail();
            String authorEmail = data.getUserEmail();
            // 작성자와 현재 로그인 된 사용자가 같을 때
            // 삭제, 수정 할 수 있게 해줌.
            authorIsUser = userEmail.equals(authorEmail);
        }

        UserModel userCache = UserCache.getUser(this);
        if (userCache != null && userCache.getStorageID().contains(data.getId())) {
            // 이 게시물이 사용자의 보관함 안에 들어있을 때
            isContainsStorage = true;
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);    // disable title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
    }

    private void showImageList(List<String> list) {
        // Remove all views before
        // adding the new ones.
        imagesContainer.removeAllViews();

        imagesContainer.setVisibility(View.VISIBLE);

        int widthPixel = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());
        int heightPixel = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 350, getResources().getDisplayMetrics());


        for (String s : list) {

            View imageHolder = LayoutInflater.from(this).inflate(R.layout.post_image_item, null);
            ImageView thumbnail = imageHolder.findViewById(R.id.media_image);

            Glide.with(thumbnail)
                    .load(s)
                    .apply(new RequestOptions().fitCenter())
                    .placeholder(R.drawable.post_image_empty)   // 사진이 로딩 되기 전 미리보기 이미지
                    .error(R.drawable.post_image_empty)         // 사진 불러오지 못했을 때
                    .into(thumbnail);

            imagesContainer.addView(imageHolder);

            thumbnail.setLayoutParams(new FrameLayout.LayoutParams(widthPixel, heightPixel));
        }
    }

    private void onClickDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("경고 : " + data.getTitle()).setMessage("정말 이 게시물을 삭제 하시겠습니까?\n복구할 수 없습니다.");

        builder.setPositiveButton("삭제", (dialog, which) -> {

            LoadingDialog loadingDialog = new LoadingDialog(this);
            loadingDialog.setMessage("게시물 삭제 중...").show();

            // 파이어베이스에 있는 포스트 삭제
            FirebaseDeletePost
                    .DeletePost(data.getId(), ShowPostActivity.this,
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

    private void onClickModifyPost() {
        // TODO: 수정 기능 추가
        Toast.makeText(this, "수정 기능은 개발중에 있습니다 조금만 기다려주세요!", Toast.LENGTH_SHORT).show();
    }

    private void onClickDeleteFromStorage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(data.getTitle()).setMessage("정말 이 게시물을 보관함에서 삭제 하시겠습니까?");

        builder.setPositiveButton("삭제", (dialog, which) -> {

            LoadingDialog loadingDialog = new LoadingDialog(this);
            loadingDialog.setMessage("보관함에서 삭제 중...").show();

            UserCache.updateUser(this, data.getId(), null, UserCache.DELETE_POST_FROM_STORAGE,
                    a -> loadingDialog.setMessage("성공적으로 처리되었습니다.").setFinishListener(this::finish).finish(true),
                    errMsg -> loadingDialog.setMessage(errMsg).finish(false));
        });

        builder.setNegativeButton("취소", (dialog, which) -> {});

        builder.setCancelable(true).show();
    }

    // Toolbar에 메뉴를 인플레이트 함
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        if (authorIsUser) {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.post_delete_and_modify_menu, menu);
        }
        if (isContainsStorage) // 메뉴 새로 하나 생성
            menu.add(0, Delete_From_Storage, 0, "보관함에서 삭제");

        return authorIsUser || isContainsStorage;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
            case R.id.post_item_delete:
                onClickDelete();
                return authorIsUser;
            case R.id.post_item_modify:
                onClickModifyPost();
                return authorIsUser;
            case Delete_From_Storage:
                onClickDeleteFromStorage();
                return isContainsStorage;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
