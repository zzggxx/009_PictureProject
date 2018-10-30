package com.example.glidemoudle_3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.glidemoudle_3.custom.DownloadImageTarget;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mImageView;
    private String img_path = "http://pic.netbian.com/uploads/allimg/180906/180605-1536228365101e.jpg";
    private String TAG = "MainActivity";

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


                /*----------------------------拿到图片的缓存路径,一定是保证是本地未保存的图片的------*/
                DownloadImageTarget downloadImageTarget = new DownloadImageTarget();
                Glide.with(this)
                        .load(img_path)
                        .downloadOnly(downloadImageTarget);
                downloadImageTarget.setCacheSize(new DownloadImageTarget.cacheSize() {
                    @Override
                    public void setCacheDirSize(String path) {
                        long totalSizeOfFilesInDir = getTotalSizeOfFilesInDir(new File(path));
                        Log.i(TAG, "onClick: " + totalSizeOfFilesInDir);
                    }
                });
                /*--------------------------------------------------------------------------------*/


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

    // 递归方式 计算文件的大小
    private long getTotalSizeOfFilesInDir(final File file) {
        if (file.isFile())
            return file.length();
        final File[] children = file.listFiles();
        long total = 0;
        if (children != null)
            for (final File child : children)
                total += getTotalSizeOfFilesInDir(child);
        return total;
    }

}
