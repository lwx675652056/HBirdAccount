package com.hbird.base.mvc.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hbird.base.R;
import com.hbird.base.mvc.base.baseActivity.BaseActivityPresenter;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.bean.ReturnBean.CheckVersionReturn;
import com.hbird.base.mvc.global.CommonTag;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.mvc.view.dialog.updateDialog;
import com.hbird.base.mvp.model.http.DownloadApiService;
import com.hbird.base.mvp.view.activity.base.BaseActivity;
import com.hbird.base.util.SPUtil;
import com.hbird.base.util.Utils;
import com.ljy.devring.DevRing;
import com.ljy.devring.http.support.body.ProgressInfo;
import com.ljy.devring.http.support.observer.DownloadObserver;
import com.ljy.devring.http.support.throwable.HttpThrowable;
import com.ljy.devring.util.RingToast;
import com.ljy.devring.util.RxLifecycleUtil;

import java.io.File;

import butterknife.BindView;
import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * Created by Liul on 2018/7/5.
 * 关于我们
 */

public class AboutOursActivity extends BaseActivity<BaseActivityPresenter> implements View.OnClickListener {
    @BindView(R.id.center_title)
    TextView mCenterTitle;
    @BindView(R.id.iv_back)
    ImageView mBack;
    @BindView(R.id.right_title2)
    TextView mRightTitle;
    @BindView(R.id.iv_guanzhu)
    LinearLayout mGuanZhu;
    //版本号
    @BindView(R.id.tv_version)
    TextView mVersion;
    private File mFileSave;
    DownloadObserver mDownloadObserver;
    private String version2;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_about_ours;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mCenterTitle.setText("关于我们");
        mRightTitle.setVisibility(View.GONE);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        version2 = Utils.getVersion(this);
        mVersion.setText("V"+version2);
    }

    @Override
    protected void initEvent() {
        mBack.setOnClickListener(this);
        mGuanZhu.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                playVoice(R.raw.changgui02);
                finish();
                break;
            case R.id.iv_guanzhu:
                //showMessage("关注喽！");
                playVoice(R.raw.changgui02);
                ClipboardManager cmb = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText("fengniaojizhang01");
                showMessage("已将公众号复制到粘贴板，请打开微信搜索关注");
                //updateVersion();
                break;
        }
    }

    private void updateVersion() {
        String token = SPUtil.getPrefString(this, CommonTag.GLOABLE_TOKEN, "");
        final String version = "0.0.1";//测试用
        if(TextUtils.isEmpty(version2)){
            return;
        }
        NetWorkManager.getInstance().setContext(this).
                getCheckVersion(token, version, new NetWorkManager.CallBack() {

            private String url;

            @Override
            public void onSuccess(BaseReturn b) {
                CheckVersionReturn b1 = (CheckVersionReturn) b;
                if(null!=b1.getResult()){
                    final CheckVersionReturn.ResultBean result = b1.getResult();
                    String ver = result.getVersion();
                    showMessage(ver.compareTo(version)+"");
                    url = result.getUrl();
                    if(ver.compareTo(version)>0){
                        //执行下载操作
                        new updateDialog(AboutOursActivity.this).builder()
                                .setMsg(result.getUpdateLog())
                                .setUpdateButton(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        playVoice(R.raw.changgui02);
                                        Intent intent = new Intent();
                                        SPUtil.setPrefString(getApplicationContext(), CommonTag.UPDATE_URL,url);
                                        intent.setClass(getApplicationContext(),DownLoadService.class);
                                        startService(intent);
                                    }
                                })
                                .setCancleButton(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        playVoice(R.raw.changgui02);
                                        int status = result.getInstallStatus();
                                        if(status==1){
                                            //强制更新  否则退出程序
                                            DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH).put(CommonTag.GLOABLE_TOKEN, "");
                                            DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH).put(CommonTag.GLOABLE_TOKEN_EXPIRE, "");
                                            DevRing.activityListManager().killAll();
                                            finish();
                                        }
                                    }
                                }).show();



                      /*  //下载操作  先放到上面的服务中操作 后面看需求
                        RxLifecycleUtil.getActivityLifeSubject(AboutOursActivity.this.toString()).onNext(ActivityEvent.DESTROY);
                        //开启新的下载请求
                        if (mFileSave == null) {
                            mFileSave = FileUtil.getFile(FileUtil.getExternalCacheDir(AboutOursActivity.this), "fengniao.apk");
                        }
                        downloadFile(mFileSave,url);*/

                    }else {
                        showMessage("当前已经是最新版本了");
                    }
                }else {
                    showMessage("当前已经是最新版本了");
                }
            }

            @Override
            public void onError(String s) {

            }
        });
    }

    /**
     * 下载文件
     * @param file 下载内容将保存到该目标文件
     */
    public void downloadFile(File file,String url) {
        //不为空则不重新构造DownloadObserver，避免创造了多个进度监听回调
        if (mDownloadObserver == null) {
            //DownloadObserver构造函数传入要要监听的下载地址
            mDownloadObserver = new DownloadObserver(url) {
                @Override
                public void onResult(boolean isSaveSuccess, String filePath) {

                    if (isSaveSuccess) {
                        RingToast.show("下载成功" + filePath);
                        Intent intent = new Intent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(new File(filePath)),
                                "application/vnd.android.package-archive");
                        startActivity(intent);
                    } else {
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
        DevRing.httpManager().downloadRequest(file, observable, mDownloadObserver,
                RxLifecycleUtil.bindUntilDestroy(this));

    }
}
