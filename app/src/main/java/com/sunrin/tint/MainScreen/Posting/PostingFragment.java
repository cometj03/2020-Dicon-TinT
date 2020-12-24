package com.sunrin.tint.MainScreen.Posting;

import android.Manifest;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.sunrin.tint.Filter;
import com.sunrin.tint.MainActivity;
import com.sunrin.tint.Model.PostModel;
import com.sunrin.tint.R;
import com.sunrin.tint.Util.DateUtil;
import com.sunrin.tint.Util.FirebaseUploadPost;
import com.sunrin.tint.Util.UserCache;

import java.util.Arrays;
import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;

public class PostingFragment extends Fragment {

    Context mContext;

    Button postBtn;
    EditText titleText, subtitleText, contentText;
    ImageView imgBtn;

    private List<Uri> selectedImages;
    private boolean isImageSelected;

    private ViewGroup selectedImageContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posting, container, false);
        isImageSelected = false;

        postBtn = view.findViewById(R.id.postBtn);
        titleText = view.findViewById(R.id.titleText);
        subtitleText = view.findViewById(R.id.subtitleText);
        contentText = view.findViewById(R.id.contentText);
        imgBtn = view.findViewById(R.id.imgBtn);
        selectedImageContainer = view.findViewById(R.id.selected_image_container);

        postBtn.setOnClickListener(v -> UploadPost());
        imgBtn.setOnClickListener(v -> GetImages());
        titleText.addTextChangedListener(textWatcher);
        contentText.addTextChangedListener(textWatcher);

        return view;
    }


    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            CheckIfCanUpload();
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    private void UploadPost() {
        String title = titleText.getText().toString();
        String subTitle = subtitleText.getText().toString();
        String content = contentText.getText().toString();

        // TODO: get filters and apply
        List<Filter> filters = Arrays.asList(Filter.eFashion);
        String documentID = "Post_" + DateUtil.getFileNameWithDate();

        FirebaseUploadPost
                .UploadPost(mContext, new PostModel(documentID, filters, selectedImages, title, subTitle, content),
                        () -> PostDone(documentID),
                        errorMsg -> Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show());
    }

    private void GetImages() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                TedBottomPicker.with(getActivity())
                        .setPeekHeight(1600)
                        .showTitle(false)
                        .setCompleteButtonText("Done")
                        .setEmptySelectionText("No Select")
                        .setSelectedUriList(selectedImages)
                        .showMultiImage(uriList -> {
                            selectedImages = uriList;
                            isImageSelected = !uriList.isEmpty();
                            showUriList(uriList);
                        });
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(mContext, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        checkPermission(permissionListener);
    }

    private void checkPermission(PermissionListener permissionlistener) {
        TedPermission
                .with(mContext)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("권한을 거부하시면 서비스를 이용하실 수 없습니다.\n\n[설정] > [권한]에서 권한 사용을 설정할 수 있습니다.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    private void showUriList(List<Uri> uriList) {
        // Remove all views before
        // adding the new ones.
        selectedImageContainer.removeAllViews();

        selectedImageContainer.setVisibility(View.VISIBLE);

        int widthPixel = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics());
        int heightPixel = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics());


        for (Uri uri : uriList) {

            View imageHolder = LayoutInflater.from(mContext).inflate(R.layout.post_image_item, null);
            ImageView thumbnail = imageHolder.findViewById(R.id.media_image);

            Glide.with(thumbnail)
                    .load(uri.toString())
                    .apply(new RequestOptions().fitCenter())
                    .into(thumbnail);

            selectedImageContainer.addView(imageHolder);

            thumbnail.setLayoutParams(new FrameLayout.LayoutParams(widthPixel, heightPixel));

        }
    }

    private void CheckIfCanUpload() {
        boolean t = titleText.getText().toString().isEmpty();
        boolean s = subtitleText.getText().toString().isEmpty();
        postBtn.setEnabled(isImageSelected && !t && !s);
    }

    // Firebase Firestore으로 이미지 업로드
    /*private List<String> UploadImage(Uri file) {
        List<String> imageIDs = new ArrayList<>();
        imageIDs.add(DateUtil.getFileNameWithDate() + "(1)");

        if (file != null) {
            ProgressDialog progressDialog = new ProgressDialog(mContext);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/" + imageIDs.get(0));
            ref.putFile(file)
                    .addOnSuccessListener(taskSnapshot -> {
                        //Toast.makeText(mContext, "Image Uploaded", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    })
                    .addOnProgressListener(snapshot -> {
                        double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        progressDialog.setMessage("Uploaded " + (int)progress + "%");
                    });
        }

        return imageIDs;
    }*/

    private void PostDone(String docId) {
        UserCache.updateUser(mContext, docId, UserCache.UPDATE_POST);
        titleText.setText("");
        subtitleText.setText("");
        contentText.setText("");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
