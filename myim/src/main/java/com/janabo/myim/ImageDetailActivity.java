package com.janabo.myim;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 作者：janabo on 2016/12/13 15:56
 */
@ContentView(R.layout.activity_img_detail)
public class ImageDetailActivity extends AppCompatActivity {
    Context mContext = this;
    @ViewInject(R.id.img)
    ImageView img;
    @ViewInject(R.id.img_lin)
    LinearLayout img_lin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.gf_black));
        }
        String imgurl = getIntent().getStringExtra("imgurl");
        Glide.with(mContext)
                .load(imgurl)
                .fitCenter()//缩放类型
                .into(img);
        img_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              finish();
            }
        });
    }



}
