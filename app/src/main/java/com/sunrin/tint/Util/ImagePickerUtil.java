package com.sunrin.tint.Util;

import android.Manifest;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;

public class ImagePickerUtil {

    private static PermissionListener permissionListener;
    private static List<Uri> selectedImages;

    public interface OnImageSelectedListener {
        void onSelected(Uri image);
    }

    public interface OnImagesSelectedListener {
        void onSelected(List<Uri> images);
    }

    // 이미지 한 장
    public static void PickImage(Context context, FragmentActivity fa, String title, OnImageSelectedListener s) {
        permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                TedBottomPicker.with(fa)
                        .setTitle(title)
                        .showTitle(true)
                        .show(s::onSelected);   // uri -> s.onSelected(uri); 와 같음
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(context, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        checkPermission(context, permissionListener);
    }

    // 이미지 여러 장
    public static void PickImages(Context context, FragmentActivity fa, OnImagesSelectedListener s) {
        permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                TedBottomPicker.with(fa)
                        .setPeekHeight(1600)
                        .showTitle(false)
                        .setCompleteButtonText("Done")
                        .setEmptySelectionText("No Selected")
                        .setSelectedUriList(selectedImages)
                        .showMultiImage(uriList -> {
                            selectedImages = uriList;
                            s.onSelected(uriList);
                        });
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(context, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        checkPermission(context, permissionListener);
    }

    private static void checkPermission(Context context, PermissionListener p) {
        TedPermission
                .with(context)
                .setPermissionListener(p)
                .setDeniedMessage("권한을 거부하시면 서비스를 이용하실 수 없습니다.\n\n[설정] > [권한]에서 권한 사용을 설정할 수 있습니다.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }
}
