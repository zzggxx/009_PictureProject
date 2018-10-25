package com.example.picturewall;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewTreeObserver;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity {

    /**
     * 用于展示照片墙的GridView
     */
    private GridView mPhotoWall;

    /**
     * GridView的适配器
     */
    private PhotoWallAdapter mAdapter;

    private int mImageThumbSize;
    private int mImageThumbSpacing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageThumbSize = getResources().getDimensionPixelSize(
                R.dimen.image_thumbnail_size);
        mImageThumbSpacing = getResources().getDimensionPixelSize(
                R.dimen.image_thumbnail_spacing);

        mPhotoWall = (GridView) findViewById(R.id.photo_wall);

        mAdapter = new PhotoWallAdapter(this, 0, Images.imageThumbUrls, mPhotoWall);
        mPhotoWall.setAdapter(mAdapter);
        mPhotoWall.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {

                        final int numColumns = (int) Math.floor(
                                mPhotoWall.getWidth() / (mImageThumbSize + mImageThumbSpacing));

                        if (numColumns > 0) {
                            int columnWidth = (mPhotoWall.getWidth() / numColumns) - mImageThumbSpacing;
                            mAdapter.setItemHeight(columnWidth);
                            mPhotoWall.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
//        方法用于将内存中的操作记录同步到日志文件（也就是journal文件）当中
        mAdapter.fluchCache();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出程序时结束所有的下载任务
        mAdapter.cancelAllTasks();
    }

    /**
     * 1. size()
     这个方法会返回当前缓存路径下所有缓存数据的总字节数，以byte为单位，如果应用程序中需要在界面上显示当前缓存数据的总大小，就可以通过调用这个方法计算出来。

     2.flush()
     这个方法用于将内存中的操作记录同步到日志文件（也就是journal文件）当中。这个方法非常重要，因为DiskLruCache能够正常工作的前提就是要依赖于journal文件中的内容。前面在讲解写入缓存操作的时候我有调用过一次这个方法，但其实并不是每次写入缓存都要调用一次flush()方法的，频繁地调用并不会带来任何好处，只会额外增加同步journal文件的时间。比较标准的做法就是在Activity的onPause()方法中去调用一次flush()方法就可以了。

     3.close()
     这个方法用于将DiskLruCache关闭掉，是和open()方法对应的一个方法。关闭掉了之后就不能再调用DiskLruCache中任何操作缓存数据的方法，通常只应该在Activity的onDestroy()方法中去调用close()方法。

     4.delete()
     这个方法用于将所有的缓存数据全部删除，比如说网易新闻中的那个手动清理缓存功能，其实只需要调用一下DiskLruCache的delete()方法就可以实现了
     */
}
