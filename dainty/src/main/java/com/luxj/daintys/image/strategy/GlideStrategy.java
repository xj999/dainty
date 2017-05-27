package com.luxj.daintys.image.strategy;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.luxj.daintys.image.gilde.GlideLoader;
import com.luxj.daintys.image.gilde.RoundedCornersTransformation;
import com.luxj.daintys.image.listener.ImageBitmapListener;
import com.luxj.daintys.image.listener.ImageDownLoadListener;
import com.luxj.daintys.image.listener.ImageLoadingListener;

/**
 * @author Luxj
 * @date create 2017/4/27
 * @description glide策略
 */
public class GlideStrategy extends GlideLoader implements BaseImageStrategy {
    @Override
    public void loadImage(String url, int placeholder, ImageView imageView, ImageLoadingListener imageLoadingListener) {
        loadDefault(url, imageView, placeholder, imageLoadingListener);
    }

    @Override
    public void loadImage(String url, int placeholder, ImageView imageView) {
        loadDefault(url, imageView, placeholder, null);
    }

    @Override
    public void loadImage(String url, ImageView imageView, ImageLoadingListener imageLoadingListener) {
        loadDefault(url, imageView, 0, imageLoadingListener);
    }

    @Override
    public void loadImage(String url, ImageView imageView) {
        loadDefault(url, imageView, 0, null);
    }

    @Override
    public void loadRoundImage(String url, ImageView imageView) {
        loadBorderRoundImage(url, 3, imageView, RoundedCornersTransformation.CornerType.ALL, 0, 0, 0);
    }

    @Override
    public void loadRoundImage(String url, int placeholder, ImageView imageView) {
        loadBorderRoundImage(url, 3, imageView, RoundedCornersTransformation.CornerType.ALL, placeholder, 0, 0);
    }

    @Override
    public void loadBorderRoundImage(String url, RoundedCornersTransformation.CornerType cornerType, ImageView imageView, int borderColor) {
        loadBorderRoundImage(url, 3, imageView, cornerType, 0, 0, borderColor);
    }

    @Override
    public void loadBorderRoundImage(String url, RoundedCornersTransformation.CornerType cornerType, ImageView imageView, int placeholder, int borderColor) {
        loadBorderRoundImage(url, 3, imageView, cornerType, placeholder, 0, borderColor);
    }

    @Override
    public void loadBorderRoundImage(String url, RoundedCornersTransformation.CornerType cornerType, ImageView imageView, float borderWidth, int borderColor) {
        loadBorderRoundImage(url, 3, imageView, cornerType, 0, borderWidth, borderColor);
    }

    @Override
    public void loadBorderRoundImage(String url, RoundedCornersTransformation.CornerType cornerType, ImageView imageView, int placeholder, float borderWidth, int borderColor) {
        loadBorderRoundImage(url, 3, imageView, cornerType, placeholder, borderWidth, borderColor);
    }

    @Override
    public void loadCustomRoundImage(String url, RoundedCornersTransformation.CornerType cornerType, ImageView imageView) {
        loadBorderRoundImage(url, 3, imageView, cornerType, 0, 0, 0);
    }

    @Override
    public void loadCustomRoundImage(String url, int radius, RoundedCornersTransformation.CornerType cornerType, ImageView imageView) {
        loadBorderRoundImage(url, radius, imageView, cornerType, 0, 0, 0);
    }

    @Override
    public void loadCustomRoundImage(String url, int radius, int placeholder, RoundedCornersTransformation.CornerType cornerType, ImageView imageView) {
        loadBorderRoundImage(url, radius, imageView, cornerType, placeholder, 0, 0);
    }

    /**
     * 显示圆形图片
     *
     * @param url         地址
     * @param placeholder 自定义占位符
     */
    @Override
    public void loadCircleImage(String url, int placeholder, ImageView imageView) {
        loadBorderCirclePic(url, imageView, placeholder, 0);
    }

    /**
     * 显示圆形图片 默认占位符
     *
     * @param url 地址
     */
    @Override
    public void loadCircleImage(String url, ImageView imageView) {
        loadBorderCirclePic(url, imageView,0,0);
    }

    @Override
    public void loadBorderCircleImage(String url, ImageView imageView, int color) {
        loadBorderCirclePic(url, imageView, 0,color);
    }

    @Override
    public void loadBorderCircleImage(String url, ImageView imageView, int placeholder, int color) {
        loadBorderCirclePic(url, imageView, placeholder, color);
    }

    @Override
    public void getBitmap(Context context, String url, ImageBitmapListener listener) {
        getBitmapForUrl(context, url, listener);
    }


    /**
     * 清除硬盘缓存
     */
    @Override
    public void clearImageDiskCache(Context context) {
        clearDiskCache(context);
    }

    /**
     * 清除内存缓存
     */
    @Override
    public void clearImageMemoryCache(Context context) {
        Glide.get(context).clearMemory();
    }

    /**
     * 下载图片
     *
     * @param url          图片地址
     * @param Path         保存地址
     * @param saveFileName 保存名称
     * @param listener     回调
     */
    @Override
    public void saveImage(Context context, String url, String Path, String saveFileName, ImageDownLoadListener listener) {
        downLoadImage(context, url, Path, saveFileName, listener);
    }
}
