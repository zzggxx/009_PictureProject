package com.example.glidemoudle.custom;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;
import com.example.glidemoudle.http.OkHttpGlideUrlLoader;
import com.example.glidemoudle.http.ProgressInterceptor;

import java.io.InputStream;

import okhttp3.OkHttpClient;

/**
 * 常规设置,里边有一些glide是直接的设置好并没有给我们暴露出来,比如里边的图片缓存位置.
 */

public class MyGlideModule implements GlideModule {

    public static final int DISK_CACHE_SIZE = 500 * 1024 * 1024;

    /**
     * setMemoryCache()
     * 用于配置Glide的内存缓存策略，默认配置是LruResourceCache。
     * <p>
     * setBitmapPool()
     * 用于配置Glide的Bitmap缓存池，默认配置是LruBitmapPool。
     * <p>
     * setDiskCache()
     * 用于配置Glide的硬盘缓存策略，默认配置是InternalCacheDiskCacheFactory。
     * <p>
     * setDiskCacheService()
     * 用于配置Glide读取缓存中图片的异步执行器，默认配置是FifoPriorityThreadPoolExecutor，也就是先入先出原则。
     * <p>
     * setResizeService()
     * 用于配置Glide读取非缓存中图片的异步执行器，默认配置也是FifoPriorityThreadPoolExecutor。
     * <p>
     * setDecodeFormat()
     * 用于配置Glide加载图片的解码模式，默认配置是RGB_565。
     *
     * @param context
     * @param builder
     */

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
//        默认路径:sdcard/Android/包名/cache/image_manager_disk_cache
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, DISK_CACHE_SIZE));
//        更改默认的RGB_565为何Picasso的ARGB_8888;两字节变为了四字节.
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
//        请求组件替换，并且加入拦截器
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new ProgressInterceptor());
        OkHttpClient okHttpClient = builder.build();
        glide.register(GlideUrl.class, InputStream.class, new OkHttpGlideUrlLoader.Factory(okHttpClient));
    }
}
