package com.janabo.myim.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.io.File;

/**
 * 图片工具类
 * 作者：janabo on 2016/12/13 14:53
 */
public class ImageUtil {
    public static void displayImage (Context context, String url, ImageView imageView ) {
        Glide.with ( context ).load ( url ).into ( imageView );
    }

    public static void displayImage (Context context, File file, ImageView imageView ) {
        Glide.with ( context ).load ( Uri.fromFile( file) ).into ( imageView );
    }

    public static void displayCircularImage ( final Context context, String url, final ImageView imageView ) {
        Glide.with ( context ).load ( url ).asBitmap ( ).centerCrop ( ).into ( new BitmapImageViewTarget( imageView ) {
            @Override
            protected void setResource ( Bitmap resource ) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create ( context.getResources ( ), resource );
                circularBitmapDrawable.setCircular ( true );
                imageView.setImageDrawable ( circularBitmapDrawable );

            }
        } );
    }
}
