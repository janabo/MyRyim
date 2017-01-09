package com.janabo.myim;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

        String imgurl = getIntent().getStringExtra("imgurl");
//        if(isbd){
//            ImageOptions imageOptions = new ImageOptions.Builder()
//                    // 是否忽略GIF格式的图片
//                    .setIgnoreGif(false)
//                    // 图片缩放模式
//                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
//                    // 得到ImageOptions对象
//                    .build();
//            x.image().bind(img, imgurl, imageOptions);
//        }else {
  //          ImageUtil.displayImage(mContext, imgurl, img);
        Glide.with(mContext)
                .load(imgurl)
                .fitCenter()//缩放类型
                .into(img);
        //       }

        img_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              finish();
            }
        });
    }



}
