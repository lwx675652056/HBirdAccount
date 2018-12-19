package com.hbird.base.mvc.activity;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.hbird.base.mvc.global.CommonTag;
import com.hbird.base.mvp.model.http.DownloadApiService;
import com.hbird.base.util.SPUtil;
import com.ljy.devring.DevRing;
import com.ljy.devring.http.support.body.ProgressInfo;
import com.ljy.devring.http.support.observer.DownloadObserver;
import com.ljy.devring.http.support.throwable.HttpThrowable;
import com.ljy.devring.util.FileUtil;
import com.ljy.devring.util.RingToast;

import java.io.File;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * Created by Liul on 2018/7/13.
 */

public class DownLoadService extends Service {

    private File mFileSave;
    DownloadObserver mDownloadObserver;
    private String url;

    @Override
    public void onCreate() {
        url = SPUtil.getPrefString(this, CommonTag.UPDATE_URL, "");
        if (mFileSave == null) {
            mFileSave = FileUtil.getFile(FileUtil.getExternalCacheDir(this), "fengniao.apk");
        }

        downloadFile(mFileSave);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void downloadFile(File file) {
        if (mDownloadObserver == null) {
            mDownloadObserver = new DownloadObserver(url) {
                @Override
                public void onResult(boolean isSaveSuccess, String filePath) {
                        if (isSaveSuccess) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                                File apkfile = new File(filePath);
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                }
                                intent.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }else {
                                Intent intent = new Intent();
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setAction(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.fromFile(new File(filePath)),
                                        "application/vnd.android.package-archive");
                                startActivity(intent);
                            }

                            stopSelf();
                        }else {
                            RingToast.show("下载失败" + filePath);
                        }
                }

                @Override
                public void onError(long progressInfoId, HttpThrowable throwable) {

                    RingToast.show("下载失败" + throwable.message);
                }

                @Override
                public void onProgress(ProgressInfo progressInfo) {

                }
            };
        }
        Observable<ResponseBody> observable = DevRing.httpManager().getService(DownloadApiService.class).downloadFile(url);
        DevRing.httpManager().downloadRequest(file, observable, mDownloadObserver, null);

    }

}
