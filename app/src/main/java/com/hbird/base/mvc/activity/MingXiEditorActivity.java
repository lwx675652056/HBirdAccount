package com.hbird.base.mvc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hbird.base.R;
import com.hbird.base.mvc.base.baseActivity.BaseActivityPresenter;
import com.hbird.base.mvc.bean.ReturnBean.ShouRuTagReturn;
import com.hbird.base.mvc.bean.ReturnBean.ZhiChuTagReturn;
import com.hbird.base.mvc.global.modle.GlobalVariables;
import com.hbird.base.mvc.view.dialog.APDUserDateDialog;
import com.hbird.base.mvp.model.entity.table.WaterOrderCollect;
import com.hbird.base.mvp.view.activity.base.BaseActivity;
import com.hbird.base.util.DateUtil;
import com.hbird.base.util.DateUtils;
import com.hbird.base.util.SPUtil;
import com.hbird.base.util.Utils;
import com.ljy.devring.DevRing;

import java.text.DecimalFormat;
import java.util.Date;

import butterknife.BindView;
import sing.common.util.LogUtil;

/**
 * Created by Liul on 2018/7/2.
 * 明细详情-编辑 - 支出 收入
 * 计算器
 */

public class MingXiEditorActivity extends BaseActivity<BaseActivityPresenter> implements View.OnClickListener {
    @BindView(R.id.iv_backs)
    ImageView mBack;
    @BindView(R.id.tv_center_title)
    TextView mCenterTitle;
    @BindView(R.id.tv_right_title)
    TextView mRight;
    @BindView(R.id.et_record)
    EditText mRecord;
    @BindView(R.id.tv_record_num)
    TextView mRecordNum;
    @BindView(R.id.btnMoodRadio)
    RadioGroup mRadioGroup;
    @BindView(R.id.bt_save_add)
    LinearLayout mBtnSaveAdd;
    @BindView(R.id.tv_up_num)
    TextView mUpNum;
    @BindView(R.id.tv_down_num)
    EditText mDownNum;
    @BindView(R.id.anime_finish)
    TextView addFinishBtn;
    @BindView(R.id.clear)
    ImageView mClear;
    @BindView(R.id.anime_add)
    TextView mAdd;
    @BindView(R.id.anime_jian)
    TextView mJian;
    @BindView(R.id.anime_date)
    TextView mDate;
    @BindView(R.id.rl_background)
    RelativeLayout mBackground;
    @BindView(R.id.iv_icon)
    ImageView mIcon;
    @BindView(R.id.tv_type)
    TextView mType;
    @BindView(R.id.btn_happy)
    RadioButton mHappy;
    @BindView(R.id.btn_normal)
    RadioButton mNormal;
    @BindView(R.id.btn_unhappy)
    RadioButton mUnHappy;
    @BindView(R.id.ll_xinqing)
    LinearLayout mXinQing;
    @BindView(R.id.ll_types)
    LinearLayout mTypes;
    @BindView(R.id.ll_jianpan)
    LinearLayout mJianPan;

    private DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private boolean inputTag;
    private int isFirst = 1;
    private WaterOrderCollect event;
    private Integer happiness;
    private boolean typeT = false;
    private String ids;
    private String spendName;
    private String parentId;
    private String parentName;
    private int orderTypes=1;
    private int orderType;
    private String encode;
    private Long time = 0L;
    private boolean shouRu;
    private MingXiEditorActivity.numberWatcher numberWatcher;
    private String possNum;
    private Vibrator vibrator;
    @BindView(R.id.iv_happy)
    ImageView ivHappy;
    @BindView(R.id.iv_normal)
    ImageView ivNormal;
    @BindView(R.id.iv_unhappy)
    ImageView ivUnHappy;
    private boolean b;
    private long chargeDate;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_charge_to_account;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        String bean = getIntent().getStringExtra("JSONBEAN");
        //event = new Gson().fromJson(bean, SingleReturn.class);
        event = new Gson().fromJson(bean, WaterOrderCollect.class);
        mUpNum.setText(event.getMoney()+"");
        orderType = event.getOrderType();
        if(orderType ==1){
            mCenterTitle.setText("支出");
            mXinQing.setVisibility(View.VISIBLE);
            mUpNum.setTextColor(getResources().getColor(R.color.text_333333));
            mUpNum.setCompoundDrawables(getResources().getDrawable(R.mipmap.icon_coin),null,null,null);
            mUpNum.setCompoundDrawablePadding(6);
            mBackground.setBackgroundResource(R.drawable.shape_cycle_blue);
        }else {
            mCenterTitle.setText("收入");
            mXinQing.setVisibility(View.GONE);
            mUpNum.setTextColor(getResources().getColor(R.color.colorPrimary));
            mUpNum.setCompoundDrawables(getResources().getDrawable(R.mipmap.icon_coin2),null,null,null);
            mUpNum.setCompoundDrawablePadding(6);
            mBackground.setBackgroundResource(R.drawable.shape_cycle_yellow);
        }
        mBtnSaveAdd.setVisibility(View.GONE);
        addFinishBtn.setText("保 存");
        addFinishBtn.setTextSize(16);
        mRight.setText("保存");
        mDownNum.setText("");
        mDownNum.setVisibility(View.GONE);
        Glide.with(this).load(event.getIcon()).into(mIcon);
        mType.setText(event.getTypeName());
        GlobalVariables.setmInputMoney(event.getMoney()+"");
        mRecord.setText(event.getRemark());
        String num = mRecord.getText().toString();
        mRecordNum.setText(num.length() + "/20");
        chargeDate = event.getChargeDate().getTime();
        String days = DateUtils.getDays(chargeDate);
        mDate.setText(days);
        mDate.setTextSize(14);
        mDate.setCompoundDrawables(null,null,null,null);
        mDate.setPadding(0,0,0,0);
        Integer spendHappiness = event.getSpendHappiness();
        happiness = spendHappiness;
        if(null!=spendHappiness){
            if(spendHappiness==0){
                //开心
               /* mRadioGroup.check(mHappy.getId());
                mHappy.setBackgroundResource(R.mipmap.icon_mood_happy_blue);*/
                setGif(happiness);

            }else if(spendHappiness==1){
                //一般
               /* mRadioGroup.check(mNormal.getId());
                mNormal.setBackgroundResource(R.mipmap.icon_mood_normal_blue);*/
                setGif(happiness);
            }else {
                /*mRadioGroup.check(mUnHappy.getId());
                mUnHappy.setBackgroundResource(R.mipmap.icon_mood_blue);*/
                setGif(happiness);

            }
        }else {

        }


    }

    @Override
    protected void initEvent() {
        mBack.setOnClickListener(this);
        mRight.setOnClickListener(this);
        mBtnSaveAdd.setOnClickListener(this);
        addFinishBtn.setOnClickListener(this);
        mClear.setOnClickListener(this);
        mAdd.setOnClickListener(this);
        mJian.setOnClickListener(this);
        mDate.setOnClickListener(this);
        mDownNum.setOnClickListener(this);
        mTypes.setOnClickListener(this);
        mUpNum.setOnClickListener(this);
        //设置计算器的计算监听 实时计算结果并展示
        numberWatcher = new numberWatcher();
        mDownNum.addTextChangedListener(numberWatcher);
       /* mDownNum.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String ss = editable+"";
                try {
                    if(TextUtils.isEmpty(ss)){
                        return;
                    }
                    setNumForResult(ss);
                }catch (Exception e){
                    e.printStackTrace();
                    showMessage("数字非法，请重新输入");
                    calculatorClear();
                }


            }
        });*/
        mRecord.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {
                temp = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                encode = Utils.encode(s.toString());

                String num = mRecord.getText().toString();
                mRecordNum.setText(num.length() + "/20");
                if (temp.length() >= 20) {
                    showMessage("最多只能输入20字符");
                }
            }
        });

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i){
                    case R.id.btn_happy:
                        //showMessage("开心");
                        playVoice(R.raw.changgui01);
                        mNormal.setBackgroundResource(R.mipmap.icon_mood_normal_blue);
                        mUnHappy.setBackgroundResource(R.mipmap.icon_mood_blue);
                        mHappy.setBackgroundResource(R.mipmap.icon_mood_happy_blue);
                        happiness = 0;
                        ivHappy.setVisibility(View.VISIBLE);
                        ivNormal.setVisibility(View.GONE);
                        ivUnHappy.setVisibility(View.GONE);
                        setGif(happiness);
                        break;
                    case R.id.btn_normal:
                        //showMessage("一般");
                        playVoice(R.raw.changgui01);
                        mHappy.setBackgroundResource(R.mipmap.icon_mood_happy_blue);
                        mUnHappy.setBackgroundResource(R.mipmap.icon_mood_blue);
                        mNormal.setBackgroundResource(R.mipmap.icon_mood_normal_blue);
                        happiness = 1;
                        ivHappy.setVisibility(View.GONE);
                        ivNormal.setVisibility(View.VISIBLE);
                        ivUnHappy.setVisibility(View.GONE);
                        setGif(happiness);
                        break;
                    case R.id.btn_unhappy:
                        //showMessage("不开心");
                        playVoice(R.raw.changgui01);
                        mNormal.setBackgroundResource(R.mipmap.icon_mood_normal_blue);
                        mHappy.setBackgroundResource(R.mipmap.icon_mood_happy_blue);
                        mUnHappy.setBackgroundResource(R.mipmap.icon_mood_blue);
                        happiness = 2;
                        ivHappy.setVisibility(View.GONE);
                        ivNormal.setVisibility(View.GONE);
                        ivUnHappy.setVisibility(View.VISIBLE);
                        setGif(happiness);
                        break;
                }
            }
        });
        //默认不选中
        mRadioGroup.clearCheck();
    }
    private void setGif(Integer happys) {
        if(happys==0){
            Glide.with(MingXiEditorActivity.this)
                    .asGif()
                    .load(R.drawable.gif_happy)
                    .into(ivHappy);
        }else if(happys==1){
            Glide.with(MingXiEditorActivity.this)
                    .asGif()
                    .load(R.drawable.gif_normal)
                    .into(ivNormal);
        }else if(happys==2){
            Glide.with(MingXiEditorActivity.this)
                    .asGif()
                    .load(R.drawable.gif_unhappy)
                    .into(ivUnHappy);
        }
    }
    private void setNumForResult(String ss) {
        double sum = Utils.sum(ss);
        //支取小数点后两位数
        DecimalFormat    df   = new DecimalFormat("######0.00");
        String format = df.format(sum);
       /* if(sum<0){
            reset();
            return;
        }*/
        mUpNum.setText(format);
    }

    private void reset() {
        mUpNum.setText("");
        mDownNum.setText("");
        inputTag = false;
        GlobalVariables.setmInputMoney("");
        showMessage("不能为负值，请重新输入");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_backs:
                calculatorClear();
                playVoice(R.raw.changgui02);
                finish();
                break;
            case R.id.tv_down_num:
                mDownNum.setFocusable(false);
                mDownNum.setFocusableInTouchMode(false);
                break;
            case R.id.tv_right_title:
                //showMessage("保存");
                saveAndFinish();
                break;
            case R.id.ll_types:
                //选择类型界面
                playVoice(R.raw.changgui01);
                Intent intent = new Intent();
                intent.setClass(this,ChooseAccountTypeActivity.class);
                if(orderType==1){
                    //支出
                    intent.putExtra("TAG","zhichu");
                }else {
                    intent.putExtra("TAG","shouru");
                }
                startActivityForResult(intent,201);
                break;
            case R.id.bt_save_add:
                //showMessage("保存并继续添加");
//                calculatorClear();
//                finish();
                playVoice(R.raw.jizhangfinish);
                saveAndFinish();
                break;
            case R.id.anime_finish:
                playVoice(R.raw.jizhangfinish);
                saveAndFinish();
                break;
            case R.id.clear:
                playVoice(R.raw.changgui01);
                vibrator.vibrate(20);
                if(inputTag){
                    inputTag = false;
                }
                //如果是 点 那么则删除点
                if(GlobalVariables.getmHasDot()){
                    //已经点过了
                    GlobalVariables.setHasDot(false);
                }
                if(TextUtils.isEmpty(mDownNum.getText().toString())){
                    return;
                }
                String str = mDownNum.getText().toString().substring(0, mDownNum.length() - 1);
                GlobalVariables.setmInputMoney(str);
                mDownNum.setText(str);
                if (TextUtils.isEmpty(str)){
                    mUpNum.setText("0.00");
                    mDownNum.setVisibility(View.GONE);
                }

                break;
            case R.id.anime_jian:
                playVoice(R.raw.changgui01);
                vibrator.vibrate(20);
                if(inputTag){
                    showMessage("已输入过");
                    return;
                }
                inputTag = true;
                String jian = "-";
                GlobalVariables.setHasDot(false);
                setNumberKey(jian);
                break;
            case R.id.anime_date:
                playVoice(R.raw.changgui01);
                vibrator.vibrate(20);
                //开始时间
                new APDUserDateDialog(this) {

                    @Override
                    protected void onBtnOkClick(String year, String month, String day) {
                        //判断今天跟选取的年月日 是否为同一天
                        String days = DateUtils.getDays(System.currentTimeMillis());
                        if(!TextUtils.isEmpty(days)){
                            if(TextUtils.equals(year + "/" + month + "/" + day,days)){
                                mDate.setText("今天");
                            }else {
                                mDate.setText(year + "/" + month + "/" + day);
                            }
                        }
                        //mDate.setText(year + "/" + month + "/" + day);
                        mDate.setTextSize(14);
                        mDate.setCompoundDrawables(null,null,null,null);
                        mDate.setPadding(0,0,0,0);
                        String trim = mDate.getText().toString().trim();
                        if(TextUtils.equals(trim,"今天")){
                            time = DateUtil.dateToLong(days);
                        }else {
                            time = DateUtil.dateToLong(mDate.getText().toString().trim());
                        }

                    }
                }.show();
                break;
            case R.id.anime_add:
                vibrator.vibrate(20);
                if(inputTag){
                    showMessage("已输入过");
                    return;
                }
                inputTag = true;
                String add = "+";
                GlobalVariables.setHasDot(false);
                setNumberKey(add);
                break;
            case R.id.tv_up_num:
                //点击收起软键盘
                playVoice(R.raw.changgui01);
                showMessage("请在下方数字键盘上直接操作~");
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if(imm != null){
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                }
                BounceInterpolator bounceInterpolator = new BounceInterpolator();
                showTranslate(bounceInterpolator);
                break;
        }
    }

    private void showTranslate(BounceInterpolator interpolator) {
        //创建平移动画对象
        TranslateAnimation translateAnimation = new TranslateAnimation(
                //参数：Animation.RELATIVE_TO_SELF（先对自身）
                //Animation.RELATIVE_TO_PARENT（相对父容器）
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_PARENT, -0.03f
        );
        //设置持续的时间
        translateAnimation.setDuration(500);
        //设置循环的次数
        translateAnimation.setRepeatCount(1);
        //设置重复的模式，Animation.REVERSE：会进行一次倒序播放
        translateAnimation.setRepeatMode(Animation.REVERSE);
        translateAnimation.setFillAfter(false);
        //设置插值器
        translateAnimation.setInterpolator(interpolator);
        //设置动画
        mJianPan.startAnimation(translateAnimation);
    }

    private void saveAndFinish() {
        String moneyString =  mUpNum.getText().toString();
        if (moneyString.equals("0.00") || GlobalVariables.getmInputMoney().equals(""))
            showMessage("你还没输入金额");
        else {
            //showMessage("保存数据跳转回上个页面");
            //调用编辑接口
            setEditorToAccount();
        }
    }

    private void setEditorToAccount() {
        String id = event.getId();
        String typePid = event.getTypePid();
        String typeId = event.getTypeId();
        String money = mUpNum.getText().toString().trim();
        String typePname = event.getTypePname();
        String typeName = event.getTypeName();
        int isStaged = event.getIsStaged();
        int orderType = event.getOrderType();
        String remark = mRecord.getText().toString().trim();
        String decode=null;
        if(null==encode){
            if(null == remark){
                String aa = Utils.encode(remark.toString());
                decode = Utils.decode(aa).toString().trim();
            }else {
                String aa = Utils.encode(remark.toString());
                decode = Utils.decode(aa).toString().trim();
            }
        }else {
            decode = Utils.decode(encode).toString().trim();
            if(null==decode){
                decode=event.getRemark();
            }
        }
        if(typeT){
            typeId = ids;
            typePid = parentId;
            typeName =spendName;
            typePname = parentName;
            orderType= orderTypes;
        }

        WaterOrderCollect clearReq = new WaterOrderCollect();
        clearReq.setId(id);
        if(time!=0){

           /* String chargeDates = chargeDate.substring(0, chargeDate.length() - 3) + "000";
            Date date = new Date(Long.parseLong(chargeDates));
            w.setChargeDate(date);//记账时间*/


            Date date = new Date(time);
            clearReq.setChargeDate(new Date(time));
        }else {
            time =chargeDate;
            clearReq.setChargeDate(new Date(time));
        }
        clearReq.setTypeName(typeName);
        clearReq.setIsStaged(isStaged);
        clearReq.setMoney(Double.parseDouble(money));
        clearReq.setOrderType(orderType);
        clearReq.setRemark(decode);
        clearReq.setTypeId(typeId);
        clearReq.setTypePid(typePid);
        clearReq.setTypePname(typePname);
        String da = System.currentTimeMillis() / 1000 +"000";
        long ss = Long.parseLong(da);
        Date date = new Date(ss);
        clearReq.setUpdateDate(new Date(ss));//更新时间
        String persionId = SPUtil.getPrefString(this, com.hbird.base.app.constant.CommonTag.USER_INFO_PERSION, "");
        clearReq.setUpdateBy(Integer.valueOf(persionId));
        if(null!=happiness){
            clearReq.setSpendHappiness(happiness);
        }
        //数据库 修改数据'  根据实体类生成sql
        //String sql = SQLUtils.buildUpdateSqlById("WATER_ORDER_COLLECT", clearReq);

        String sql ="update WATER_ORDER_COLLECT set MONEY = "+Double.parseDouble(money)+" , SPEND_HAPPINESS = '"+happiness+"' , TYPE_NAME = '"+typeName+"' , IS_STAGED="+isStaged+" , ORDER_TYPE="+orderType+"" +
                " , REMARK ='"+decode+"' , TYPE_ID='"+typeId+"' , TYPE_PID='"+typePid+"' , TYPE_PNAME='"+typePname+"' , CHARGE_DATE='"+time+"' , UPDATE_DATE='"+ss+"',UPDATE_BY = "+Integer.valueOf(persionId)+"  where ID = '"+id+"'";

        b = DevRing.tableManager(WaterOrderCollect.class).execSQL(sql);
//        b = DevRing.tableManager(WaterOrderCollect.class).updateOne(clearReq);
       // pullToSyncDate();
        if(b){
            setResult(104);
            calculatorClear();
            showMessage("修改成功");
            finish();
        }

       /* String jsonInfo = new JSONUtil<String, EditorReq>().ObjectToJsonStr(clearReq);
        String token = SPUtil.getPrefString(this, CommonTag.GLOABLE_TOKEN, "");
        showProgress("");
        NetWorkManager.getInstance().setContext(this)
                .editorDingDanInfo(jsonInfo, token, new NetWorkManager.CallBack() {
            @Override
            public void onSuccess(BaseReturn b) {
               hideProgress();
                GloableReturn bean = (GloableReturn) b;
                if(TextUtils.equals(bean.getCode(),"200")){
                    setResult(104);
                    calculatorClear();
                    showMessage("修改成功");
                    finish();
                }
            }

            @Override
            public void onError(String s) {
                hideProgress();
                showMessage(s);
            }
        });*/
    }

    private void setNumberKey(String add) {
      /*  if(!Utils.isFastClick()){
            //showMessage("手速太快了");
            return;
        }*/
        mDownNum.setVisibility(View.VISIBLE);
        String money = GlobalVariables.getmInputMoney();
        if (GlobalVariables.getmHasDot() && GlobalVariables.getmInputMoney().length()>2) {
            String dot = money.substring(money.length() - 3, money.length() - 2);
            LogUtil.e("calculatorNumOnclick: " + dot);
            if (dot.equals(".")) {
                //showMessage("陛下，不到一分钱了");
                return;
            }
        }
        //mDownNum.addTextChangedListener(numberWatcher);
        //判断
        String[] positive = money.split("\\+");
        String endStr = null;
        for (String pos : positive) {
            possNum = pos;
            if(pos.contains("-")){
                String[] negative = pos.split("\\-");
                Double temp = 0d;
                for (int is = 0; is < negative.length; is++) {
                    endStr = negative[is];
                }
                //endStr = negative[0];
            }else{
                endStr = pos;
            }
        }
        //判断超过8位数不再继续输入
        if(null!=endStr && endStr.contains(".")){
            String[] split = endStr.split("\\.");
            if(null!=split && split.length>0){
                if(split[0].length()>8){
                    showMessage("您输入的位数过大");
                    return;
                }
            }

        }else {
            if(!TextUtils.isEmpty(endStr)){
                if(endStr.length()>=8){
                    String sub = money.substring(money.length() - 1, money.length());
                    if(TextUtils.equals(sub,"+") || TextUtils.equals(sub,"-")){

                    }else {
                        if(TextUtils.equals(add,"+") || TextUtils.equals(add,"-") ){

                        }else {
                            showMessage("您输入的位数过大");
                            return;
                        }
                    }
                }
            }

        }

        if(isFirst==1){
            //清空之前的数字 重新输入（否则明细详情会把金额带过来导致问题）
            isFirst= isFirst+1;
            GlobalVariables.setmInputMoney(add);
        }else {
            GlobalVariables.setmInputMoney(money+add);
        }

        mDownNum.setText(GlobalVariables.getmInputMoney());
        mDownNum.setSelection(GlobalVariables.getmInputMoney().length());
    }

    // 清零按钮
    public void calculatorClear() {
        GlobalVariables.setmInputMoney("");
        mUpNum.setText("");
        mDownNum.setText("");
        mDownNum.setVisibility(View.GONE);
        GlobalVariables.setHasDot(false);
    }

    // 数字输入按钮
    public void calculatorNumOnclick(View v) {
        playVoice(R.raw.changgui01);
        Button view = (Button) v;
        String digit = view.getText().toString();
        inputTag = false;
        vibrator.vibrate(20);
        setNumberKey(digit);
    }
    // 小数点处理工作
    public void calculatorPushDot(View view) {
        if (GlobalVariables.getmHasDot()) {
            Toast.makeText(getApplicationContext(), "已经输入过小数点了 ━ω━●", Toast.LENGTH_SHORT).show();
        } else {
            GlobalVariables.setmInputMoney(GlobalVariables.getmInputMoney()+".");
            GlobalVariables.setHasDot(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==201 && resultCode==202){
            //支出
            String json = data.getStringExtra("Object");
            if(json==null){
               return;
            }
            shouRu = false;
            typeT = true;
            ZhiChuTagReturn.ResultBean.CommonListBean bean = new Gson().fromJson(json, ZhiChuTagReturn.ResultBean.CommonListBean.class);
            ids = bean.getId()+"";
            orderTypes = 1;
            spendName = bean.getSpendName();
            parentId = bean.getParentId();
            parentName = bean.getParentName();
            mType.setText(spendName);
            Glide.with(this).load(bean.getIcon()).into(mIcon);
            mCenterTitle.setText("支出");
            mXinQing.setVisibility(View.VISIBLE);
            mUpNum.setTextColor(getResources().getColor(R.color.text_333333));
            mUpNum.setCompoundDrawables(getResources().getDrawable(R.mipmap.icon_coin),null,null,null);
            mUpNum.setCompoundDrawablePadding(6);
            mBackground.setBackgroundResource(R.drawable.shape_cycle_blue);

        }
        if(requestCode==201 && resultCode==203){
            //收入
            String json = data.getStringExtra("Object");
            if(json==null){
                return;
            }
            shouRu = true;
            typeT = true;
            ShouRuTagReturn.ResultBean.CommonListBean bean = new Gson().fromJson(json, ShouRuTagReturn.ResultBean.CommonListBean.class);
            ids = bean.getId()+"";
            orderTypes = 2;
            spendName = bean.getIncomeName();
            parentId = bean.getParentId();
            parentName = bean.getParentName();
            mType.setText(spendName);
            Glide.with(this).load(bean.getIcon()).into(mIcon);
            mCenterTitle.setText("收入");
            mXinQing.setVisibility(View.GONE);
            mUpNum.setTextColor(getResources().getColor(R.color.colorPrimary));
            mUpNum.setCompoundDrawables(getResources().getDrawable(R.mipmap.icon_coin2),null,null,null);
            mUpNum.setCompoundDrawablePadding(6);
            mBackground.setBackgroundResource(R.drawable.shape_cycle_yellow);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        calculatorClear();
    }
    private class numberWatcher implements TextWatcher{
        private CharSequence temp;
        private int selectionStart;
        private int selectionEnd;
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            temp=charSequence;
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String ss = editable+"";
            try {
                if(TextUtils.isEmpty(ss)){
                    return;
                }
                if(null!=ss){
                    if(ss.startsWith("+") || ss.startsWith("-")){
                        ss="0"+ss;
                    }
                    setNumForResult(ss);
                }
            }catch (Exception e){
                e.printStackTrace();
                showMessage("数字非法，请重新输入");
                calculatorClear();
            }


        }

    }





}
