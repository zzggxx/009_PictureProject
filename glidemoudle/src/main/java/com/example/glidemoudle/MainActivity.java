package com.example.glidemoudle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class MainActivity extends AppCompatActivity {

    private String img_path = "http://pic.netbian.com/uploads/allimg/180906/180605-1536228365101e.jpg";
    private String img_path_gif = "http://upfile.asqql.com/2009pasdfasdfic2009s305985-ts/2018-8/201882720381991880.gif";
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = (ImageView) findViewById(R.id.image_view);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadImg();
            }
        });
    }

    private void loadImg() {

        //上下文,fragment,activity(销毁则销毁了,可能会导致空指针),application.context()(生命周期内一直加载)
        Glide.with(this)

                //load(),可以从url,uri,recourse,路径等地方加载.
                .load(img_path_gif)

                //自动判定图片的格式,是gif图还是静态图,若是动态的asBitmap()可取gif第一帧变为静态图,但是静态不能变为gif(会加载失败).
                //.asBitmap()

                //内部已经做了压缩处理避免了内存浪费(根据imageview的大小压缩原图大小),但是也可以直接的指定裁剪大小(gif裁剪有点问题).
                //.override(50,50)

                //占位图
                .placeholder(R.drawable.place_holder)
                .error(R.drawable.place_error)

                //缓存策略设置(不缓存每一次都从网络加载)
                .diskCacheStrategy(DiskCacheStrategy.NONE)

                //指定放置位置
                .into(mImageView);
    }
}
