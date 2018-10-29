package com.example.glidemoudle_3.custom;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.module.GlideModule;

/**
 * 常规设置,里边有一些glide是直接的设置好并没有给我们暴露出来,比如里边的图片缓存位置.
 * <p>
 * 添加注解让Glide自动识别,这是Glide 4.x的一大进步.
 */

@com.bumptech.glide.annotation.GlideModule
public class MyGlideModule implements GlideModule {

    public static final int DISK_CACHE_SIZE = 500 * 1024 * 1024;

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
//        默认路径:sdcard/Android/包名/cache/image_manager_disk_cache
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, DISK_CACHE_SIZE));
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {

    }
}
