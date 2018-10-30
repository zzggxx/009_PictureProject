package com.example.glidemoudle_3.custom;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;

public class DownloadImageTarget implements Target<File> {

    private static final String TAG = "DownloadImageTarget";

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void onLoadStarted(Drawable placeholder) {
    }

    @Override
    public void onLoadFailed(@Nullable Drawable errorDrawable) {

    }

    @Override
    public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
        Log.d(TAG, resource.getParentFile().getPath());
        Log.d(TAG, resource.getPath());
        if (mCacheSize != null) {
            mCacheSize.setCacheDirSize(resource.getParentFile().getPath());
        }
    }

    @Override
    public void onLoadCleared(Drawable placeholder) {
    }

    @Override
    public void getSize(SizeReadyCallback cb) {
        cb.onSizeReady(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
    }

    @Override
    public void removeCallback(@NonNull SizeReadyCallback cb) {

    }

    @Override
    public void setRequest(Request request) {
    }

    @Override
    public Request getRequest() {
        return null;
    }


    public interface cacheSize {
        void setCacheDirSize(String path);
    }

    cacheSize mCacheSize;

    public void setCacheSize(cacheSize cacheSize) {
        mCacheSize = cacheSize;
    }

}
