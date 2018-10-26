package com.example.glidemoudle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.example.glidemoudle.transfrom.CircleCrop;

/**
 * glide讲解
 */
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
//                simpleTarget();
            }
        });

    }

    private void simpleTarget() {

//        泛型GlideDrawable是不确定的gif还是静态图,若是肯定的静态图泛型可以直接指定Bitmap.
        SimpleTarget<GlideDrawable> simpleTarget = new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
//                获取到将要放置到imageview中的图片,此时可以进行任意的操作.
                mImageView.setImageDrawable(resource);
            }
        };
        Glide.with(this)
                .load(img_path)
                .into(simpleTarget);
    }

    private void loadImg() {

        /**
         * 上下文,fragment,activity(销毁则销毁了,可能会导致空指针),application.context()(生命周期内一直加载)
         * 第一个with()方法的源码还是比较好理解的。其实就是为了得到一个RequestManager对象而已，
         * 然后Glide会根据我们传入with()方法的参数来确定图片加载的生命周期
         */
        Glide.with(this)

                //load(),可以从url,uri,recourse,路径等地方加载.
                //最终load()方法返回的其实就是一个DrawableTypeRequest对象,从哪里加载和加载的格式的判断
                .load(img_path)

                //自动判定图片的格式,是gif图还是静态图,若是动态的asBitmap()可取gif第一帧变为静态图,但是静态不能变为gif(会加载失败).
                //.asBitmap()

                //内部已经做了压缩处理避免了内存浪费(根据imageview的大小压缩原图大小),但是也可以直接的指定裁剪大小(gif裁剪有点问题).
                //.override(50,50)

                //占位图
                .placeholder(R.drawable.place_holder)
                .error(R.drawable.place_error)

                //内存的缓存策略(true不使用内存缓存,若是不禁用默认就是开启的)
                //.skipMemoryCache(true)

                //监听回调
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                })

                .transform(new CircleCrop(this))

                //本地缓存策略设置(不缓存每一次都从网络加载)
                //DiskCacheStrategy.NONE：表示不缓存任何内容。
                //DiskCacheStrategy.SOURCE：表示只缓存原始图片。
                //DiskCacheStrategy.RESULT：表示只缓存转换过后的图片（默认选项）。
                //DiskCacheStrategy.ALL ：表示既缓存原始图片，也缓存转换过后的图片
                .diskCacheStrategy(DiskCacheStrategy.NONE)

                //指定放置位置
                .into(mImageView);
    }
}
