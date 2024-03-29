package com.hbird.base.util;

import android.os.Environment;

import java.io.File;

/**
 * Created by Liul on 2018-07-05.
 */
public class ConstantSet {

    //文件

    public static final String LOCALFILE = Environment.
            getExternalStorageDirectory().getAbsolutePath() +
            File.separator + "CompleteUpdateHeader" + File.separator + "userPic" + File.separator;

    public static final String LOCALFILE2 = Environment.
            getExternalStorageDirectory().getAbsolutePath() +
            File.separator + "Photos" + File.separator + "userPic" + File.separator;

    public static final String USERTEMPPIC="userTemp.jpg";

    public static final String USERPIC="user.jpg";

    public static final String IMAGE_CACHE_DIRECTORY="images";

    //图片裁剪

    public static final int TAKEPICTURE = 0X1;

    public static final int SELECTPICTURE = 0X2;

    public static final int CROPPICTURE = 0X3;
}
