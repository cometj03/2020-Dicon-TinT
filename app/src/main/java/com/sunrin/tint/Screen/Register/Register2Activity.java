package com.sunrin.tint.Screen.Register;

import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sunrin.tint.Model.UserModel;
import com.sunrin.tint.R;
import com.sunrin.tint.Util.SharedPreferenceUtil;
import com.sunrin.tint.Util.UserCache;
import com.sunrin.tint.View.LoadingDialog;

import de.hdodenhof.circleimageview.CircleImageView;

public class Register2Activity extends AppCompatActivity {

    CircleImageView profileImageView;
    EditText et_username, et_status;
    Button nextBtn;

    Uri profile;
    String email;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        email = getIntent().getStringExtra("email");

        profileImageView = findViewById(R.id.user_profile);
        et_username = findViewById(R.id.user_name);
        et_status = findViewById(R.id.user_status);
        nextBtn = findViewById(R.id.finish_btn);

        et_username.addTextChangedListener(watcher);
        et_status.addTextChangedListener(watcher);
        nextBtn.setOnClickListener(v -> updateUser());
        profileImageView.setOnClickListener(v -> setProfileImageView());
    }

    private void setProfileImageView() {
        // TODO:
    }

    private void updateUser() {
        String name = et_username.getText().toString().trim();
        String status = et_status.getText().toString();

        LoadingDialog dialog = new LoadingDialog(this);
        dialog.setMessage("프로필 업로드 중...").show();

        FirebaseUploadProfile
                .Upload(profile, name, status,
                        profileLink -> {
                            dialog.setMessage("프로필 업로드 완료!").setFinishListener(() -> {
                                UserCache.setUser(this, new UserModel(name, email, status, profileLink));
                                SharedPreferenceUtil.setString(this, "is_first_app", "false");
                                finish();
                            }).finish(true);
                        },
                        (errMsg, skip) -> dialog.setMessage(errMsg).setFinishListener(() -> {
                            UserCache.setUser(this, new UserModel(email));
                            Toast.makeText(this, "스킵했습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        }).finish(skip));
    }

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkCanNext();
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    private void checkCanNext() {
        boolean username = et_username.getText().toString().trim().isEmpty();
        boolean status = et_status.getText().toString().isEmpty();

        nextBtn.setEnabled(!username && !status);
    }
}
