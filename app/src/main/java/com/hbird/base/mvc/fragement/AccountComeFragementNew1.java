package com.hbird.base.mvc.fragement;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.hbird.base.R;
import com.hbird.base.mvc.activity.AddMoreTypeActivityNew;
import com.hbird.base.mvc.activity.ChargeToAccount;
import com.hbird.base.mvc.adapter.MyType2Adapter2;
import com.hbird.base.mvc.base.BaseFragement;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.bean.MyTypeItem;
import com.hbird.base.mvc.bean.RequestBean.ExChangeReq;
import com.hbird.base.mvc.bean.ReturnBean.AccountTypes;
import com.hbird.base.mvc.bean.ReturnBean.BiaoQianReturn;
import com.hbird.base.mvc.bean.ReturnBean.CommonListBean;
import com.hbird.base.mvc.bean.ReturnBean.ShouRuTagReturnNew;
import com.hbird.base.mvc.bean.ReturnBean.SystemBiaoqReturn;
import com.hbird.base.mvc.global.CommonTag;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.mvc.widget.DragGridView;
import com.hbird.base.util.SPUtil;
import com.hbird.base.util.SuperSelectComeManager;
import com.hbird.common.Constants;
import com.ljy.devring.util.NetworkUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import sing.common.util.LogUtil;
import sing.util.SharedPreferencesUtil;

/**
 * @author: LiangYX
 * @ClassName: AccountComeFragementNew1
 * @date: 2018/12/17 14:20
 * @Description: 记一笔 - 收入
 */

public class AccountComeFragementNew1 extends BaseFragement{
    @BindView(R.id.draggridview)
    DragGridView draggridview;


    private MyType2Adapter2 myAdapter;
    private AccountComeFragementNew1 mContext = this;
    //private List<MyTypeItem> datas;
    private List<SystemBiaoqReturn.ResultBean.LabelBean.IncomeBean> commonList = new ArrayList<>();
    private String tag;

    private String token;
    //private List<MyTypeItem> datas;

    private String userInfoId;
    private String abTypeId;

    @Override
    public int setContentId() {
        return R.layout.fragement_account_spend;
    }

    @Override
    public void initView() {
        LogUtil.e("AccountComeFragement");
    }

    @Override
    public void initData() {
        tag = getArguments().getString("TAG");
        myAdapter = new MyType2Adapter2(getActivity(),mContext);
        draggridview.setAdapter(myAdapter);

        token = SPUtil.getPrefString(getActivity(), CommonTag.GLOABLE_TOKEN, "");
        userInfoId = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.USER_INFO_PERSION, "");
        abTypeId = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.INDEX_CURRENT_ACCOUNT_TYPE, "");
    }

    @Override
    public void initListener() {
        draggridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //这个地方的判断是为了解决 当用户长按最后一个添加按钮时 不消失的问题
                // 判断对最后一个做长按操作时则标志位-1向前移动一位 手动更改长按点选所选项 从而达到隐藏最后一个的目的
                if(!NetworkUtil.isNetWorkAvailable(getActivity())){
                    showMessage("当前网络不可用，请检查网络");
                    return true;
                }
                if(position==commonList.size()-1){
                    if(position==0){
                        return false;
                    }else {
                        position=position-1;
                    }
                }
                if(position==commonList.size()-1){
                    return false;
                }
                draggridview.startDrag(position);
                myAdapter.setShowType("2");
                getActivity().findViewById(R.id.tv_right_title).setVisibility(View.VISIBLE);
                //activity中的保存按钮 显示与否 再此设置  最外层的保存按钮要传入myAdapter.setShowType("1");
                //textFragWork.setVisibility(View.VISIBLE);
                return true;
            }
        });
        SuperSelectComeManager.getInstance().setCallBackListen(new SuperSelectComeManager.ResCheck() {
            @Override
            public void setCheck(int i, String str) {
                if(i==2){
                    //从ChooseAccountTypeActivity界面传过来的值再此监听并响应回调
                    myAdapter.setShowType("1");
                    getActivity().findViewById(R.id.tv_right_title).setVisibility(View.GONE);
//                    loadDataForNet();
                    loadIconForNet();
                }
            }
        });

        draggridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (TextUtils.equals(myAdapter.getShowType() , "1")){
                    //点击每个条目要做的操作
                    if(position==commonList.size()-1){
                        //说明是最后一个
                        //showMessage("添加。。");
                        playVoice(R.raw.typevoice);
                        if(!NetworkUtil.isNetWorkAvailable(getActivity())){
                            showMessage("当前网络不可用，请检查网络");
                            return;
                        }
                        Intent intent = new Intent();
                        SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.CURRENT_ACCOUNT_ID, abTypeId);
                        intent.setClass(getActivity(),AddMoreTypeActivityNew.class);
                        intent.putExtra("TITLE","收入");
                        ArrayList<String> list = new ArrayList<>();
                        list.clear();
                        for (int i = 0; i < commonList.size(); i++) {
                            list.add(commonList.get(i).getIncomeName());
                        }
                        intent.putStringArrayListExtra("object",list);
                        startActivityForResult(intent,122);
                        //addSomeDate();
                    }else {
                        if(TextUtils.equals(tag,"choose")) {
                            //带数据跳转回上个页面 编辑页面
                            playVoice(R.raw.typevoice);
                            Intent intent = new Intent();
                            intent.putExtra("Object", new Gson().toJson(commonList.get(position)));
                            getActivity().setResult(203, intent);
                            getActivity().finish();
                        }else {
                            playVoice(R.raw.typevoice);
                            Intent intent = new Intent();
                            intent.setClass(getActivity(),ChargeToAccount.class);
                            SystemBiaoqReturn.ResultBean.LabelBean.IncomeBean commonListBean = commonList.get(position);


                            CommonListBean bean = new CommonListBean();
                            bean.setIcon(commonListBean.getIcon());
                            bean.setId(commonListBean.getId() + "");
                            bean.setIncomeName(commonListBean.getIncomeName());
                            String sJson = new Gson().toJson(bean);
                            intent.putExtra("JSONSTR", sJson);
                            intent.putExtra("TAG","收入");
                            startActivityForResult(intent,110);
                        }
                    }
                }else{ //删除item
                    if (canDelete){
                        playVoice(R.raw.changgui01);
                        deleteItems(position);
                    }
                }
            }
        });
        myAdapter.setCommondataChangeInterface(new MyType2Adapter2.CommondataChangeInterface() {
            @Override
            public void setdata(MyTypeItem workContent) {

            }

            @Override
            public void setdatas(List<SystemBiaoqReturn.ResultBean.LabelBean.IncomeBean> datas) {
                //调换位置后上传都服务器
                ExChangeReq req = new ExChangeReq();
                req.setType(2);
                ArrayList<ExChangeReq.relation> relations = new ArrayList<>();
                relations.clear();
                for(int i=0;i<datas.size()-1;i++){
                    ExChangeReq.relation relation = new ExChangeReq.relation();
                    relation.setId(datas.get(i).getId()+"");
                    relation.setPriority(i+1);
                    relations.add(relation);
                }
                req.setRelation(relations);
                String abID = abTypeId+"";
                if (TextUtils.equals(abID, "0")) {
                    //说明是默认的总账本
                    showMessage("请选择账本");
                    return;
                }
                Integer abid = Integer.valueOf(abID);
                req.setAbTypeId(abid);
                String json = new Gson().toJson(req);
                NetWorkManager.getInstance().setContext(getActivity())
                        .postChangeType(json, token, new NetWorkManager.CallBack() {
                            @Override
                            public void onSuccess(BaseReturn b) {
                                AccountTypes b1 = (AccountTypes) b;
                                String version03 = b1.getResult().getVersion();
                                SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.LABEL_VERSION,version03);

                                for (Iterator<SystemBiaoqReturn.ResultBean.LabelBean.IncomeBean> it = datas.iterator(); it.hasNext(); ) {
                                    SystemBiaoqReturn.ResultBean.LabelBean.IncomeBean s = it.next();

                                    if (null == s.getPriority() || s.getIncomeName().equals("添加")) {
                                        it.remove();
                                    }
                                }
                                SharedPreferencesUtil.put("userId_" + userInfoId + "abTypeId_" + abTypeId + "type_income", datas.toString());
                            }

                            @Override
                            public void onError(String s) {
                                showMessage(s);
                            }
                        });
            }
        });
    }

    @Override
    public void loadData() {
    }

    @Override
    public void loadDataForNet() {
        super.loadDataForNet();

        String temp = (String) SharedPreferencesUtil.get("userId_" + userInfoId + "abTypeId_" + abTypeId + "type_income", "");
        List<SystemBiaoqReturn.ResultBean.LabelBean.IncomeBean> list = JSON.parseArray(temp, SystemBiaoqReturn.ResultBean.LabelBean.IncomeBean.class);

        if (list == null || list.size() < 1) {// 本地沒有取網絡
            getData(userInfoId, Integer.parseInt(abTypeId));
        } else {
            addData(list);
        }
    }

    // 网络获取
    private void getData(String userInfoId, int abTypeId) {
        showProgress("");
        NetWorkManager.getInstance().setContext(getActivity())
                .getIncomeLabelsNew(abTypeId, token, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        hideProgress();
                        ShouRuTagReturnNew b1 = (ShouRuTagReturnNew) b;
                        SharedPreferencesUtil.put("userId_" + userInfoId + "abTypeId_" + abTypeId + "type_income", b1.getResult().getCommonList().toString());
                        addData(b1.getResult().getCommonList());
                    }

                    @Override
                    public void onError(String s) {
                        hideProgress();
                        showMessage(s);
                    }
                });
    }

    // 添加数据
    private void addData(List<SystemBiaoqReturn.ResultBean.LabelBean.IncomeBean> tempList) {
        commonList.clear();
        commonList.addAll(tempList);

        SystemBiaoqReturn.ResultBean.LabelBean.IncomeBean temp1 = new SystemBiaoqReturn.ResultBean.LabelBean.IncomeBean();
        temp1.setIncomeName("更多");
        temp1.setIcon(Constants.ACCOUNT_ADD_URL);
        commonList.add(temp1);

        myAdapter.setDatas(commonList, "1");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==110 && resultCode==111){
            getActivity().setResult(322);
            getActivity().finish();
        }
        if(requestCode==122 && resultCode==123){
            //刷新界面
            loadDataForNet();
            //loadIconForNet();
        }
    }

    private void loadIconForNet() {
        loadDataForNet();
    }
    private boolean canDelete = true;// 是否可以删除
    private void deleteItems(final int i) {
        canDelete = false;
        //删除指收入类目
        if(!NetworkUtil.isNetWorkAvailable(getActivity())){
            showMessage("当前网络不可用，请检查网络");
            return;
        }
        if (commonList.size() - 1 < i) {
            return;
        }
        showProgress("");
        NetWorkManager.getInstance().setContext(getActivity())
                .delShouRuType(commonList.get(i).getId()+"",Integer.parseInt(abTypeId), token, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        hideProgress();
                        BiaoQianReturn b1 = (BiaoQianReturn) b;
                        String version = b1.getResult().getVersion();
                        SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.LABEL_VERSION,version);

                        int id = commonList.get(i).getId();
                        for (Iterator<SystemBiaoqReturn.ResultBean.LabelBean.IncomeBean> it = commonList.iterator(); it.hasNext(); ) {
                            SystemBiaoqReturn.ResultBean.LabelBean.IncomeBean s = it.next();

                            if (null == s.getPriority() || s.getId().equals(id)) {
                                it.remove();
                            }
                        }

                        SharedPreferencesUtil.put("userId_" + userInfoId + "abTypeId_" + abTypeId + "type_income", commonList.toString());

                        myAdapter.deleteItem(i);
                        canDelete = true;
                    }

                    @Override
                    public void onError(String s) {
                        showMessage(s);
                        hideProgress();
                        canDelete = true;
                    }
                });
    }
}
