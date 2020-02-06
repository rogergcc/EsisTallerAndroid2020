package com.rogergcc.imagenesesis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class MiImageLoader  extends AppCompatActivity {
    private ImageLoader imageLoader;
    ImageView miimagen;
    ProgressBar miprogress;
    private DisplayImageOptions options;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_image_loader);
        miimagen=findViewById(R.id.miimagen);
        miprogress=findViewById(R.id.miprogress);
        imageLoader = ImageLoader.getInstance();

        Log.i("fasf","imageloader");
        imageLoader.init(ImageLoaderConfiguration.createDefault(MiImageLoader.this));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.cargando)
                .build();
        imageLoader.displayImage("https://esis2020.000webhostapp.com/897603.jpg", miimagen, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                miimagen.setVisibility(View.GONE);
                miprogress.setVisibility(View.VISIBLE);
            }
            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            }
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                miimagen.setVisibility(View.VISIBLE);
                miprogress.setVisibility(View.GONE);
                miimagen.setImageBitmap(loadedImage);
                miimagen.setScaleType(ImageView.ScaleType.FIT_XY);
            }
            @Override
            public void onLoadingCancelled(String imageUri, View view) {
            }

        });
    }
}