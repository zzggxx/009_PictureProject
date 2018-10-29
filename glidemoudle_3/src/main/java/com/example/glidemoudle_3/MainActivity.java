package com.example.glidemoudle_3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mImageView;
    private String img_path = "http://pic.netbian.com/uploads/allimg/180906/180605-1536228365101e.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn).setOnClickListener(this);
        mImageView = (ImageView) findViewById(R.id.image_view);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn:

//                更多的参数放在了这里,使得加载图片的代码更加的简洁
                RequestOptions options = new RequestOptions()
                        .placeholder(R.drawable.place_holder)
                        .error(R.drawable.place_error)
                        .skipMemoryCache(true)//禁用内存缓存
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
//                        .override(20, 20)
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                        添加图片变换,已经有内置的了
//                        .circleCrop()
//                        .centerCrop()
                        .fitCenter();



                Glide.with(this)
                        .asBitmap()
                        .load(img_path)
                        .apply(options)
//                        .addListener(new RequestListener<Bitmap>() {
//                            @Override
//                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
//                                return false;
//                            }
//
//                            @Override
//                            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
//                                return false;
//                            }
//                        })
                        .into(mImageView);
                break;
        }
    }
}
