package com.hbird.base.mvc.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hbird.base.R;


/**
 * Created by Liul on 2018/06/28.
 */

public class ButtonTitle extends RelativeLayout {

    private Context context;
    private TextView textTitle;
    private ImageView imageView;
    private int count = 0; //出现哪个UI

    public ButtonTitle(Context context) {
        this(context, null);
    }

    public ButtonTitle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ButtonTitle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        RelativeLayout rl = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.view_button_title, this, true);
        textTitle = (TextView) rl.findViewById(R.id.bar_tv);
        imageView = (ImageView) rl.findViewById(R.id.bar_iv);
    }

    public void setImageView(int id){
        imageView.setImageResource(id);
        textTitle.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    public void setImage(int id){
        imageView.setImageResource(id);
        imageView.setMaxHeight(50);
        imageView.setMaxWidth(50);
    }

    public void setText(String msg){
        textTitle.setText(msg);
        textTitle.setPadding(0 , 10 , 0 , 0);
    }
    public void setTextColor(int i){
        textTitle.setTextColor(i);
    }

    public void setView(int i ){
        textTitle.setTextColor(getResources().getColor(R.color.text_bdbdbd));
        switch (i){
            case 1:
                textTitle.setText("明细");
                imageView.setImageResource(R.mipmap.ic_mingxi_down);
                break;
            case 2:
                textTitle.setText("图表");
                imageView.setImageResource(R.mipmap.ic_tubiao_down);
                break;
            case 3:
                textTitle.setText("我的");
                imageView.setImageResource(R.mipmap.ic_wo_down);
                break;

            default:break;
        }
    }

}
