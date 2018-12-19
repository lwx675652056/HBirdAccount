package com.hbird.base.mvc.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hbird.base.R;
import com.hbird.base.mvc.global.modle.ImageBean;
import com.hbird.base.mvc.global.modle.ImageFolderBean;
import com.hbird.base.mvc.global.modle.ImageGridViewAdapter;
import com.hbird.base.mvc.global.modle.ImagesFolderPopupWindow;
import com.hbird.base.mvc.global.modle.SpinnerProgressDialoag;
import com.hbird.base.mvp.presenter.base.BasePresenter;
import com.hbird.base.mvp.view.activity.base.BaseActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SelectImagesFromLocalActivity extends BaseActivity<BasePresenter> implements
        View.OnClickListener,ImagesFolderPopupWindow.FinishOnItemClickListener,AdapterView.OnItemClickListener {

    private GridView mgridView;

    private SpinnerProgressDialoag msp;

    private LinearLayout mSelectImages;

    private TextView mFolderName;

    private ImageGridViewAdapter madapter;

    private ImagesFolderPopupWindow pop;

    private ArrayList<ImageBean> marrayList=new ArrayList<>();//所有图片的数据

    private ArrayList<ImageFolderBean> arrayList=new ArrayList<>();//popuwindow的适配器数据源

    private HashMap<String,ArrayList<ImageBean>> mgroupMap=new HashMap<>();

    @Override
    protected int getContentLayout() {
        return R.layout.activity_select_from_local;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        msp=new SpinnerProgressDialoag(this);
        initTitle();
        mgridView=(GridView)findViewById(R.id.imagesGridView);
        mgridView.setOnItemClickListener(this);
        mSelectImages=(LinearLayout)findViewById(R.id.selectImagesFromFolder);
        mSelectImages.setOnClickListener(this);

        mFolderName=(TextView)findViewById(R.id.imagesFolderName);

        initImageLoader();

        madapter=new ImageGridViewAdapter(this);

        mgridView.setAdapter(madapter);

        pop=new ImagesFolderPopupWindow(this);

        pop.setFinishOnItemClickListener(this);

        new RequestLocalImages().execute();

    }

    @Override
    protected void initEvent() {

    }


    private void initTitle() {

        ImageView rightImage = (ImageView) findViewById(R.id.id_img_right);
        TextView title = (TextView) findViewById(R.id.id_title);
        ImageView back = (ImageView) findViewById(R.id.id_back);
        rightImage.setVisibility(View.GONE);
        title.setText("图片");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        ImageBean imageBean=(ImageBean)parent.getItemAtPosition(position);

        Intent itToClip=new Intent();
        itToClip.putExtra("path",imageBean.getImagePath());
        setResult(RESULT_OK, itToClip);
        finish();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.selectImagesFromFolder:

                pop.showPopupWindow(v);

                break;

        }

    }


    @Override
    public void OnFinishedClick(String name) {

        madapter.setArrayList(mgroupMap.get(name));
        mFolderName.setText(name);

    }



    /***
     * 请求本地图片数据
     */
    private class  RequestLocalImages extends AsyncTask<String,Integer,String> {

        public RequestLocalImages() {
            super();
        }

        @Override
        protected String doInBackground(String... params) {

            Cursor imageCursor = getContentResolver().
                    query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            new String[]{MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID},
                            null, null, MediaStore.Images.Media._ID);

            //把存储所有图片的数据集合放到map中来
             mgroupMap.put("所有图片",marrayList);

            if (imageCursor != null && imageCursor.getCount() > 0) {

                while (imageCursor.moveToNext()) {
                    ImageBean item = new ImageBean(imageCursor.
                            getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA)));

                    mgroupMap.get("所有图片").add(item);//每个图片都加入进去

                    String parentPath=new File(item.getImagePath()).getParentFile().getName();

                    //比较是否是同一个文件夹，如果不是的话，重建以文件夹为key
                    if(!mgroupMap.containsKey(parentPath)){

                        ArrayList<ImageBean> arrayList=new ArrayList<>();
                        arrayList.add(item);
                        mgroupMap.put(parentPath,arrayList);


                    }else{

                        mgroupMap.get(parentPath).add(item);

                    }

                }

              subGroupOfImage(mgroupMap);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {


            madapter.setArrayList(mgroupMap.get("所有图片"));

            pop.setArrayList(arrayList);

            super.onPostExecute(s);
        }
    }


    /**
     * map集合中的数据添加到popwindow适配器数据源 集合 arraylist中
     * @param mgroupMap
     */
    private void subGroupOfImage(HashMap<String,ArrayList<ImageBean>> mgroupMap){

        if(mgroupMap==null||mgroupMap.size()==0){

            return;
        }

        Iterator<Map.Entry<String,ArrayList<ImageBean>>> iterator=mgroupMap.entrySet().iterator();

        while (iterator.hasNext()){

            Map.Entry<String,ArrayList<ImageBean>> entry=iterator.next();

            ImageFolderBean ifb=new ImageFolderBean();
            ifb.setFirstImage(entry.getValue().get(0).getImagePath());
            ifb.setFolderName(entry.getKey());
            ifb.setImages(entry.getValue().size());
            if(!entry.getKey().equals("所有图片")){

                ifb.setIsSelected(false);
                arrayList.add(ifb);
            }else{

                ifb.setIsSelected(true);
                arrayList.add(0,ifb);
            }
        }
    }



    private void initImageLoader() {
//        if (options == null) {
//            DisplayImageOptions.Builder displayBuilder = new DisplayImageOptions.Builder();
//            displayBuilder.cacheInMemory(true);
//            displayBuilder.cacheOnDisk(true);
//            displayBuilder.showImageOnLoading(R.mipmap.default_photo);
//            displayBuilder.showImageForEmptyUri(R.mipmap.default_photo);
//            displayBuilder.considerExifParams(true);
//            displayBuilder.bitmapConfig(Bitmap.Config.RGB_565);
//            displayBuilder.imageScaleType(ImageScaleType.EXACTLY);
//            displayBuilder.displayer(new FadeInBitmapDisplayer(300));
//            options = displayBuilder.build();
//        }
//
//        if (!ImageLoader.getInstance().isInited()) {
//            ImageLoaderConfiguration.Builder loaderBuilder = new ImageLoaderConfiguration.Builder(getApplication());
//            loaderBuilder.memoryCacheSize(getMemoryCacheSize());
//
//            try {
//                File cacheDir = new File(getExternalCacheDir() + File.separator + ConstantSet.IMAGE_CACHE_DIRECTORY);
//                loaderBuilder.diskCache(new LruDiskCache(cacheDir, DefaultConfigurationFactory.createFileNameGenerator(), 500 * 1024 * 1024));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            ImageLoader.getInstance().init(loaderBuilder.build());
//        }

    }

    private int getMemoryCacheSize() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;
        // 4 bytes per pixel
        return screenWidth * screenHeight * 4 * 3;
    }
}
