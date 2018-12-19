package com.hbird.base.mvc.fragement;

import android.content.Intent;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.hbird.base.R;
import com.hbird.base.mvc.activity.AddMoreTypeActivity;
import com.hbird.base.mvc.activity.ChargeToAccount;
import com.hbird.base.mvc.adapter.MyType2Adapter;
import com.hbird.base.mvc.base.BaseFragement;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.bean.MyTypeItem;
import com.hbird.base.mvc.bean.RequestBean.ExChangeReq;
import com.hbird.base.mvc.bean.ReturnBean.AccountTypes;
import com.hbird.base.mvc.bean.ReturnBean.BiaoQianReturn;
import com.hbird.base.mvc.bean.ReturnBean.ShouRuTagReturn;
import com.hbird.base.mvc.global.CommonTag;
import com.hbird.base.mvc.global.CommonUserIInfo;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.mvc.widget.DragGridView;
import com.hbird.base.mvp.model.entity.table.HbirdUserCommUseIncome;
import com.hbird.base.util.DBUtil;
import com.hbird.base.util.L;
import com.hbird.base.util.SPUtil;
import com.hbird.base.util.SuperSelectComeManager;
import com.ljy.devring.DevRing;
import com.ljy.devring.util.NetworkUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Liul on 2018/7/02.
 * 收入界面
 */

public class AccountComeFragement extends BaseFragement{
    @BindView(R.id.draggridview)
    DragGridView draggridview;


    private MyType2Adapter myAdapter;
    private AccountComeFragement mContext = this;
    //private List<MyTypeItem> datas;
    private List<ShouRuTagReturn.ResultBean.CommonListBean> commonList;
    private String tag;
    private List<ShouRuTagReturn.ResultBean.CommonListBean> mydate;


    @Override
    public int setContentId() {
        return R.layout.fragement_account_spend;
    }

    @Override
    public void initView() {
        L.liul("AccountComeFragement");
    }

    @Override
    public void initData() {
        tag = getArguments().getString("TAG");
        myAdapter = new MyType2Adapter(getActivity(),mContext);
        draggridview.setAdapter(myAdapter);
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
                        intent.setClass(getActivity(),AddMoreTypeActivity.class);
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
                            ShouRuTagReturn.ResultBean.CommonListBean commonListBean = commonList.get(position);
                            String sJson = new Gson().toJson(commonListBean);
                            intent.putExtra("JSONSTR",sJson);
                            intent.putExtra("TAG","收入");
                            startActivityForResult(intent,110);
                        }
                    }

                }else{
                    //删除item
                    playVoice(R.raw.changgui01);
                    deleteItems(position);
                    myAdapter.deleteItem(position);
                }
            }
        });
        myAdapter.setCommondataChangeInterface(new MyType2Adapter.CommondataChangeInterface() {
            @Override
            public void setdata(MyTypeItem workContent) {

            }

            @Override
            public void setdatas(List<ShouRuTagReturn.ResultBean.CommonListBean> datas) {
                //调换位置后上传都服务器
                mydate = datas;
                String token = SPUtil.getPrefString(getActivity(),CommonTag.GLOABLE_TOKEN, "");
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
                String s = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID, "0");
                req.setAbTypeId(Integer.valueOf(s));
                String json = new Gson().toJson(req);
                NetWorkManager.getInstance().setContext(getActivity())
                        .postChangeType(json, token, new NetWorkManager.CallBack() {
                            @Override
                            public void onSuccess(BaseReturn b) {
                                AccountTypes b1 = (AccountTypes) b;
                                String version03 = b1.getResult().getVersion();
                                SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.LABEL_VERSION,version03);
                            }

                            @Override
                            public void onError(String s) {
                                showMessage(s);
                            }
                        });
            }
        });
    }
   /* private void addSomeDate() {
        //每次添加之前 把最后一个 "添加" 删除 (添加完数据后 再加上)
        datas.remove(datas.get(datas.size()-1));
        MyTypeItem items = new MyTypeItem();
        items.setName("工资");
        items.setImgAddress("http://p9twjlzxw.bkt.clouddn.com/1528970084225_DIY_30px.png");
        datas.add(items);
        MyTypeItem itemsAdd = new MyTypeItem();
        itemsAdd.setName("添加");
        itemsAdd.setImgAddress(CommonUserIInfo.account_add_url);
        datas.add(itemsAdd);
        myAdapter.setDatas(datas, "1");
    }*/

    @Override
    public void loadData() {

    }

    @Override
    public void loadDataForNet() {
        super.loadDataForNet();
        int abtype = SPUtil.getPrefInt(getActivity(), com.hbird.base.app.constant.CommonTag.ACCOUNT_AB_TYPEID, 0);
        String userId = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.USER_INFO_PERSION, "");
        String sql = "select * from HBIRD_USER_COMM_USE_INCOME where AB_TYPE_ID ="+abtype+" and USER_INFO_ID="+userId+"";
        Cursor cursor = DevRing.tableManager(HbirdUserCommUseIncome.class).rawQuery(sql, null);
        commonList = new ArrayList<>();
        commonList.clear();
        if(cursor!=null){
            commonList = DBUtil.changeToListTyp(cursor, commonList, ShouRuTagReturn.ResultBean.CommonListBean.class);
        }
        ShouRuTagReturn.ResultBean.CommonListBean commonListBean = new ShouRuTagReturn.ResultBean.CommonListBean();
        commonListBean.setIncomeName("添加");
        commonListBean.setIcon(CommonUserIInfo.account_add_url);
        commonList.add(commonListBean);
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
        DevRing.tableManager(HbirdUserCommUseIncome.class).deleteAll();
        String ss = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.USER_INFO_PERSION, "");
        Integer userInfoId = Integer.valueOf(ss);
        if(null!=mydate){
            for (int i = 0; i < mydate.size()-1; i++) {
                ShouRuTagReturn.ResultBean.CommonListBean b = mydate.get(i);
                HbirdUserCommUseIncome w = new HbirdUserCommUseIncome();
                w.setId(b.getId()+"");
                w.setPriority(b.getPriority());
                w.setIcon(b.getIcon());
                w.setParentName(b.getParentName());
                w.setParentId(b.getParentId());
                w.setIncomeName(b.getIncomeName());
                w.setMark(b.getMark());
                w.setAbTypeId(b.getAbTypeId());
                w.setUserInfoId(userInfoId);
                DevRing.tableManager(HbirdUserCommUseIncome.class).insertOne(w);
            }
        }

        loadDataForNet();

       /* String token = SPUtil.getPrefString(getActivity(), CommonTag.GLOABLE_TOKEN, "");
        showProgress("");
        NetWorkManager.getInstance().setContext(getActivity())
                .getShouRuTag(token, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        hideProgress();
                        ShouRuTagReturn b1 = (ShouRuTagReturn) b;
                        commonList = b1.getResult().getCommonList();
                        //添加一个 "添加"
                        ShouRuTagReturn.ResultBean.CommonListBean commonListBean = new ShouRuTagReturn.ResultBean.CommonListBean();
                        commonListBean.setIncomeName("添加");
                        commonListBean.setIcon(CommonUserIInfo.account_add_url);
                        commonList.add(commonListBean);
                        myAdapter.setDatas(commonList, "1");
                    }

                    @Override
                    public void onError(String s) {
                        hideProgress();
                        showMessage(s);
                    }
                });*/
        /*datas = new ArrayList<>();
        for (int i=0;i<6;i++){

            MyTypeItem items = new MyTypeItem();
            if(i==5){
                items.setName("添加");
                items.setImgAddress(CommonUserIInfo.account_add_url);
            }else {
                items.setName("工资"+i);
                items.setImgAddress("http://p9twjlzxw.bkt.clouddn.com/1528970084225_DIY_30px.png");
            }
            datas.add(items);
        }
        myAdapter.setDatas(datas, "1");*/
    }

    private void deleteItems(final int i) {
        //删除指收入类目
        if(!NetworkUtil.isNetWorkAvailable(getActivity())){
            showMessage("当前网络不可用，请检查网络");
            return;
        }

        String bookIdType = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.INDEX_CURRENT_ACCOUNT_TYPE, "0");
        int abTypeId = Integer.valueOf(bookIdType);
        String token = SPUtil.getPrefString(getActivity(), CommonTag.GLOABLE_TOKEN, "");
        showProgress("");
        NetWorkManager.getInstance().setContext(getActivity())
                .delShouRuType(commonList.get(i).getId()+"",abTypeId, token, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        hideProgress();
                        BiaoQianReturn b1 = (BiaoQianReturn) b;
                        String version = b1.getResult().getVersion();
                        SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.LABEL_VERSION,version);
                        String sqlDel = "delete from HBIRD_USER_COMM_USE_INCOME where id='"+commonList.get(i).getId()+"'";
                        boolean b2 = DevRing.tableManager(HbirdUserCommUseIncome.class).execSQL(sqlDel);
                        if(b2){
                            commonList.remove(i);
                            showMessage("成功");
                        }
                    }

                    @Override
                    public void onError(String s) {
                        showMessage(s);
                        hideProgress();
                    }
                });

    }
}
