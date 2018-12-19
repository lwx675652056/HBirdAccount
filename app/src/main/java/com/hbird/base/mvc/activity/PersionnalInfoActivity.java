package com.hbird.base.mvc.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hbird.base.R;
import com.hbird.base.app.GestureUtil;
import com.hbird.base.app.constant.CommonTag;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.bean.RequestBean.persionalReq;
import com.hbird.base.mvc.bean.ReturnBean.GeRenInfoReturn;
import com.hbird.base.mvc.bean.ReturnBean.QiNiuReturn;
import com.hbird.base.mvc.fragement.MeFragement;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.mvc.view.dialog.MyChooseSexDialog;
import com.hbird.base.mvc.view.dialog.MyHangYeDialog;
import com.hbird.base.mvc.view.dialog.MyZhiWeiDialog;
import com.hbird.base.mvc.widget.CityPickerDialog;
import com.hbird.base.mvc.widget.TimePickerDialog;
import com.hbird.base.mvp.presenter.base.BasePresenter;
import com.hbird.base.mvp.view.activity.base.BaseActivity;
import com.hbird.base.mvp.view.activity.login.loginActivity;
import com.hbird.base.util.ConstantSet;
import com.hbird.base.util.L;
import com.hbird.base.util.SDCardUtils;
import com.hbird.base.util.SPUtil;
import com.hbird.base.util.Utils;
import com.ljy.devring.DevRing;
import com.ljy.devring.image.support.GlideApp;
import com.qiniu.android.common.Zone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;

import butterknife.BindView;

import static android.R.attr.data;
import static android.R.attr.key;

/**
 * Created by Liul on 2018/7/4.
 * 个人信息管理
 */

public class PersionnalInfoActivity extends BaseActivity<BasePresenter> implements View.OnClickListener{
    @BindView(R.id.iv_back)
    ImageView mLeftBack;
    @BindView(R.id.center_title)
    TextView mCenterTitle;
    @BindView(R.id.right_title2)
    TextView mRightTitle;
    @BindView(R.id.imagess)
    com.hbird.base.mvc.widget.cycleView mHeadImg;
    @BindView(R.id.ll_gender)
    LinearLayout mGender;
    @BindView(R.id.ll_birthday)
    LinearLayout mBirthday;
    @BindView(R.id.ll_city)
    LinearLayout mCity;
    @BindView(R.id.ll_business)
    LinearLayout mBusiness;
    @BindView(R.id.ll_job)
    LinearLayout mJob;
    @BindView(R.id.tv_birthday)
    TextView mBirthdayText;
    @BindView(R.id.tv_city)
    TextView mCitys;
    @BindView(R.id.tv_hangye)
    TextView mHangye;
    @BindView(R.id.tv_zhiwei)
    TextView mZhiwei;
    @BindView(R.id.tv_sex)
    TextView mText;
    @BindView(R.id.tv_nicheng)
    EditText mNiCheng;
    @BindView(R.id.tv_ids)
    TextView mIds;
    @BindView(R.id.ll_nicheng)
    LinearLayout mNicheng;


    File file ;
    Uri imageUri ;
    private CityPickerDialog cityPickerDialog;
    private TimePickerDialog  time_Dialog;
    private MyHangYeDialog hangye_dialog;
    private MyZhiWeiDialog zhiwei_dialog;
    private MyChooseSexDialog sex_dialog;
    private static final String IMAGE_FILE_LOCATION = ConstantSet.LOCALFILE;
    private String auth = "";
    private String imgUrl="";//照片头像 在七牛云的存储地址
    private String birthdayTime="";
    private String province="";
    private String mCitytext ="";

    @Override
    protected int getContentLayout() {
        return R.layout.activity_persionnal_info;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initDialog();
        String jsons = getIntent().getStringExtra("JSON");
        if(!TextUtils.isEmpty(jsons)){
            GeRenInfoReturn info = new Gson().fromJson(jsons, GeRenInfoReturn.class);
            String sex = info.getResult().getSex();
            long birthday = info.getResult().getBirthday();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date="";
            if (birthday==0){

            }else {
                date  = dateFormat.format(birthday);
            }
            String provinceName = info.getResult().getProvinceName();
            String cityName = info.getResult().getCityName();
            String profession = info.getResult().getProfession();
            String profion = info.getResult().getPosition();
            int id = info.getResult().getId();
            String nickName = info.getResult().getNickName();
            if(TextUtils.isEmpty(nickName)){
                String phones = Utils.getHiddenPhone(info.getResult().getMobile());
                nickName = phones;
            }
            String avatarUrl = info.getResult().getAvatarUrl();
            if(TextUtils.equals(sex,"1")){
                sex = "男";
            }else if(TextUtils.equals(sex,"2")){
                sex = "女";
            }else {
                sex = "--";
            }
            mText.setText(sex);
            if(!TextUtils.isEmpty(date)){
                mBirthdayText.setText(date);
            }
            if(!TextUtils.isEmpty(provinceName)){
                mCitys.setText(provinceName+" "+cityName);
            }
            if(!TextUtils.isEmpty(profession)){
                mHangye.setText(profession);
            }
            if(!TextUtils.isEmpty(profion)){
                mZhiwei.setText(profion);
            }
            mIds.setText(id+"");
            mNiCheng.setText(nickName);
            GlideApp.with(this)
                    .load(avatarUrl)
                    .placeholder(R.mipmap.ic_me_normal_headr)
                    .centerCrop()
                    .error(R.mipmap.ic_me_normal_headr)
                    .into(mHeadImg);
            //Glide.with(this).load(avatarUrl).into(mHeadImg);
            if(!TextUtils.isEmpty(mNiCheng.getText().toString().trim())){
                mNiCheng.requestFocus();
                mNiCheng.setCursorVisible(false);
                mNiCheng.setSelection(mNiCheng.getText().toString().trim().length());
            }else {
                mNiCheng.setCursorVisible(false);
            }
        }


    }

    private void initDialog() {
        cityPickerDialog = new CityPickerDialog(this);
        cityPickerDialog.setCallback(new CityPickerDialog.OnClickCallback() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onSure(String provinces, String city, String county, String data) {
                province = provinces;
                mCitytext = city;
                mCitys.setText(province +" "+city);
            }
        });
        time_Dialog = new TimePickerDialog(this);
        time_Dialog.setCallback(new TimePickerDialog.OnClickCallback() {
            @Override
            public void onCancel() {
                time_Dialog.dismiss();
            }

            @Override
            public void onSure(int year, int month, int day, int hour, int minter, long time) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                birthdayTime = time+"";
                String date  = dateFormat.format(time);
                mBirthdayText.setText(date);
                time_Dialog.dismiss();
            }
        });
        zhiwei_dialog = new MyZhiWeiDialog(this);
        zhiwei_dialog.setCallback(new MyZhiWeiDialog.OnClickCallback() {
            @Override
            public void onCancel() {
                zhiwei_dialog.dismiss();
            }

            @Override
            public void onSure(String data) {
                mZhiwei.setText(data);
                zhiwei_dialog.dismiss();
            }
        });
        hangye_dialog = new MyHangYeDialog(this);
        hangye_dialog.setCallback(new MyHangYeDialog.OnClickCallback() {
            @Override
            public void onCancel() {
                hangye_dialog.dismiss();
            }

            @Override
            public void onSure(String data) {
                mHangye.setText(data);
                hangye_dialog.dismiss();
            }
        });
        sex_dialog = new MyChooseSexDialog(this);
        sex_dialog.setCallback(new MyChooseSexDialog.OnClickCallback() {
            @Override
            public void onCancel() {
                sex_dialog.dismiss();
            }

            @Override
            public void onSure(String data) {
                mText.setText(data);
                sex_dialog.dismiss();
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        file = new File(IMAGE_FILE_LOCATION);
        if(!file.exists()){

            SDCardUtils.makeRootDirectory(IMAGE_FILE_LOCATION);
        }

        file=new File(IMAGE_FILE_LOCATION+ ConstantSet.USERTEMPPIC);
        imageUri = Uri.fromFile(file);
        getQiNiuToken();
        //getGeRenInfo();
    }

    private void getGeRenInfo() {
        //获取个人信息

    }

    private void getQiNiuToken() {
        String token = SPUtil.getPrefString(this, com.hbird.base.mvc.global.CommonTag.GLOABLE_TOKEN, "");
        NetWorkManager.getInstance().setContext(this)
                .postQiNiuToken(token, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        QiNiuReturn b1 = (QiNiuReturn) b;
                        auth = b1.getResult().getAuth();
                    }

                    @Override
                    public void onError(String s) {
                        showMessage(s);
                    }
                });
    }

    @Override
    protected void initEvent() {
        mHeadImg.setOnClickListener(this);
        mRightTitle.setOnClickListener(this);
        mGender.setOnClickListener(this);
        mBirthday.setOnClickListener(this);
        mCity.setOnClickListener(this);
        mBusiness.setOnClickListener(this);
        mJob.setOnClickListener(this);
        mLeftBack.setOnClickListener(this);
        mNicheng.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                playVoice(R.raw.changgui02);
                finish();
                break;
            case R.id.ll_nicheng:
                playVoice(R.raw.changgui02);
                mNiCheng.setCursorVisible(true);
                mNiCheng.setSelection(mNiCheng.getText().toString().trim().length());
                InputMethodManager inputManager = (InputMethodManager)mNiCheng.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(mNiCheng, 0);
                break;
            case R.id.imagess:
                playVoice(R.raw.changgui02);
                //showMessage("更换头像");
                final Dialog builder = new Dialog(this,R.style.AlertDialogStyle);
                View uv = getLayoutInflater().from(this).inflate(R.layout.upload_user_pic, null);
                TextView photograph = (TextView) uv.findViewById(R.id.Photograph);
                TextView selectPic = (TextView) uv.findViewById(R.id.selectImage_from_local);
                TextView dimissDialoag = (TextView) uv.findViewById(R.id.dimiss_dialoag);
                builder.setContentView(uv);
                Window window = builder.getWindow();
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.width = window.getWindowManager().getDefaultDisplay().getWidth();
                window .setAttributes(lp);
                window.setGravity(Gravity.BOTTOM);
                builder.setCanceledOnTouchOutside(true);
                builder.show();
                MyDialoagListener myDialoagListener = new MyDialoagListener(builder);
                photograph.setOnClickListener(myDialoagListener);
                selectPic.setOnClickListener(myDialoagListener);
                dimissDialoag.setOnClickListener(myDialoagListener);
                break;
            case R.id.right_title2:
                playVoice(R.raw.changgui02);
                //showMessage("完成");
                setFinishDate();
                break;

            case R.id.ll_gender:
                playVoice(R.raw.changgui02);
                //showMessage("选择男女");
                sex_dialog.show();
                break;
            case R.id.ll_birthday:
                //showMessage("选择生日");
                playVoice(R.raw.changgui02);
                time_Dialog.show();
                break;
            case R.id.ll_city:
                //showMessage("选择城市");
                playVoice(R.raw.changgui02);
                cityPickerDialog.show();
                break;
            case R.id.ll_business:
                //showMessage("选择行业");
                playVoice(R.raw.changgui02);
                hangye_dialog.show();
                break;
            case R.id.ll_job:
                //showMessage("选择职位");
                playVoice(R.raw.changgui02);
                zhiwei_dialog.show();
                break;
        }
    }

    private void setFinishDate() {
        String token = SPUtil.getPrefString(this, com.hbird.base.mvc.global.CommonTag.GLOABLE_TOKEN, "");
        persionalReq req = new persionalReq();
        if(!TextUtils.isEmpty(imgUrl)){
            req.setAvatarUrl(imgUrl);
        }
        req.setBirthday(birthdayTime);
        req.setProvinceName(province);
        req.setCityName(mCitytext);
        req.setNickName(mNiCheng.getText().toString().trim());
        if(TextUtils.equals("--",mZhiwei.getText().toString().trim())){
            req.setPosition("");//职位
        }else {
            req.setPosition(mZhiwei.getText().toString().trim());//职位
        }
        if(TextUtils.equals("--",mHangye.getText().toString().trim())){
            req.setProfession("");//行业
        }else {
            req.setProfession(mHangye.getText().toString().trim());//行业
        }
        if(TextUtils.equals("--",mCitys.getText().toString().trim())){
            req.setProvinceName("");
            req.setCityName("");
        }else {
            String s = mCitys.getText().toString().trim().split(" ")[0];
            String s2 = mCitys.getText().toString().trim().split(" ")[1];
            req.setProvinceName(s);
            req.setCityName(s2);
        }

        String sex = mText.getText().toString().trim();
        String sexIndex = "0";
        if(TextUtils.equals(sex,"男")){
            sexIndex = "1";
        }else if(TextUtils.equals(sex,"女")){
            sexIndex = "2";
        }
        req.setSex(sexIndex);
        String json = new Gson().toJson(req);
        showProgress("");
        NetWorkManager.getInstance().setContext(this)
                .setPersionalInfos(json, token, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        hideProgress();
                        showMessage("修改成功");

                    }

                    @Override
                    public void onError(String s) {
                        hideProgress();
                        showMessage(s);
                    }
                });
    }


    /***
     * 头像更改监听
     */
    private class MyDialoagListener implements View.OnClickListener {

        private Dialog alertDialog;

        public MyDialoagListener(Dialog alertDialog) {
            this.alertDialog = alertDialog;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.Photograph:
                    alertDialog.dismiss();
                    if (Utils.cameraIsCanUse()){
                        try{
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            startActivityForResult(intent, ConstantSet.TAKEPICTURE);

                        }catch (Exception e){
                            L.liu("个人界面相机调取异常 = " + e.getMessage());
                        }
                    }else{
                        showMessage("请打开相机权限");
                    }
                    break;

                case R.id.selectImage_from_local:
                    Intent sintent = new Intent(PersionnalInfoActivity.this, SelectImagesFromLocalActivity.class);
                    startActivityForResult(sintent, ConstantSet.SELECTPICTURE);
                    alertDialog.dismiss();
                    break;

                case R.id.dimiss_dialoag:
                    alertDialog.dismiss();
                    break;
                default:break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=RESULT_OK){
            return;
        }
        switch (requestCode) {
            case ConstantSet.SELECTPICTURE:
                Intent scutIntent = new Intent(this, ClippingPageActivity.class);
                scutIntent.putExtra("type", "selectPicture");
                scutIntent.putExtra("path",data.getStringExtra("path"));
                startActivityForResult(scutIntent,ConstantSet.CROPPICTURE);
                break;
            case ConstantSet.TAKEPICTURE:
                Intent tcutIntent = new Intent(this, ClippingPageActivity.class);
                tcutIntent.putExtra("type", "takePicture");
                startActivityForResult(tcutIntent,ConstantSet.CROPPICTURE);
                break;
            case ConstantSet.CROPPICTURE:
                byte[] bis = data.getByteArrayExtra("result");
                String url = data.getStringExtra("url");
                Bitmap bitmap = BitmapFactory.decodeByteArray(bis, 0, bis.length);
                mHeadImg.setImageBitmap(bitmap);
                //上传头像
                UpImages(url);
                break;
        }
    }

    private void UpImages(String s) {
        //关键参数
        //params.addBodyParameter("File", new File(s));
        Configuration config = new Configuration.Builder()
        .zone(Zone.zone1)
        .build();
        UploadManager uploadManager = new UploadManager(config);
        uploadManager.put(new File(s), null, auth,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject res) {
                        //res包含hash、key等信息，具体字段取决于上传策略的设置
                        if(info.isOK()) {
                            Log.i("qiniu", "Upload Success");
                            //imgUrl = "http://p9tz2oly9.bkt.clouddn.com/";
                            imgUrl = "";
                            try {
                                String key1 = res.getString("key");
                                imgUrl = key1;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            // String o = (String)res.get(key);
                        } else {
                            Log.i("qiniu", "Upload Fail");
                            //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                        }
                        Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + res);
                    }
                }, null);
    }

}
