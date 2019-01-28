package com.hbird.util.image;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.hbird.base.mvc.activity.SelectImagesFromLocalActivity;
import com.hbird.base.util.Utils;
import com.hbird.common.Constants;
import com.hbird.common.dialog.BottomChooseDialog;

import java.io.File;
import java.io.IOException;

import sing.util.ToastUtil;

public class PicChooserHelper {

    private final int TAKE_PHOTO_CODE = 1000;// 拍照
    private final int SELECT_PHOTO_CODE = 1001;// 图库
    private final int CUT_PICTURE_CODE = 1002;// 裁剪

    private File file;// 拍的照片
    private String filePath = Constants.TAKE_PHOTO_PATH;// 拍照的原图地址
    private File cropFile;// 剪切后的图片
    private String cropPath = Constants.CUT_PHOTO_PATH;// 剪切的原图地址

    private Activity activity;

    private OnChooseListener onChooseListener;

    public PicChooserHelper(Activity activity, OnChooseListener onChooseListener) {
        this.activity = activity;
        this.onChooseListener = onChooseListener;

        file = new File(filePath);
        cropFile = new File(cropPath);

        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();

            if (cropFile.exists()) {
                cropFile.delete();
            }
            cropFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void show() {
        new BottomChooseDialog(activity, position -> {
            if (position == 0) {// 拍照
                if (Utils.cameraIsCanUse()) {
                    choosePicFromCamera();
                } else {
                    ToastUtil.showShort("请打开相机权限");
                }

//                if (PermissionUtil.checkAndRequest(activity, Manifest.permission.CAMERA, 1000)) {
//                    choosePicFromCamera();
//                }
            } else if (position == 1) {// 图库
                choosePicFromAlbum();
            }
        }).show();
    }

    // 拍照
    private void choosePicFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri temp = Uri.fromFile(file);
        // 启动相机程序
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, temp);
        activity.startActivityForResult(intent, TAKE_PHOTO_CODE);
    }

    // 图库
    private void choosePicFromAlbum() {
//        Intent intent = new Intent(Intent.ACTION_PICK, null);
//        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//        activity.startActivityForResult(intent, SELECT_PHOTO_CODE);
        Intent sintent = new Intent(activity, SelectImagesFromLocalActivity.class);
        activity.startActivityForResult(sintent, SELECT_PHOTO_CODE);
    }

    // 裁剪
    public void startPhotoZoom(Uri uri) {// 这里的uri也要经过处理
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);

        intent.putExtra("aspectX", 1);// 比例
        intent.putExtra("aspectY", 1);

        intent.putExtra("outputX", 300);// 输出大小
        intent.putExtra("outputY", 300);

        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cropFile));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        activity.startActivityForResult(intent, CUT_PICTURE_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == TAKE_PHOTO_CODE) {// 拍照返回
            Uri mUri = Uri.fromFile(file);
            startPhotoZoom(mUri); //进行裁剪
        } else if (resultCode == Activity.RESULT_OK && requestCode == SELECT_PHOTO_CODE) {// 选图返回
            if (data != null) {
                String a = data.getStringExtra("path");
                if (!TextUtils.isEmpty(a)) {
                    startPhotoZoom(Uri.parse(a));//进行裁剪
                }
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == CUT_PICTURE_CODE) {// 裁图返回
            onChooseListener.result(cropPath);
        }
    }


    public interface OnChooseListener {
        void result(String path);
    }
}