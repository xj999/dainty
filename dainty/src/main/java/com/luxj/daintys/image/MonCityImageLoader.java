package com.luxj.daintys.image;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.luxj.daintys.image.gilde.RoundedCornersTransformation;
import com.luxj.daintys.image.listener.ImageBitmapListener;
import com.luxj.daintys.image.listener.ImageDownLoadListener;
import com.luxj.daintys.image.listener.ImageLoadingListener;
import com.luxj.daintys.image.strategy.BaseImageStrategy;
import com.luxj.daintys.image.strategy.GlideStrategy;


/**
 * @author Luxj
 * @date create 2017/4/27
 * @description 加载图片入口  可传入不同策略实现不同框架加载图片
 */
public class MonCityImageLoader {

    private static MonCityImageLoader mInstance;
    private BaseImageStrategy mStrategy;

    public MonCityImageLoader() {
        mStrategy = new GlideStrategy();
    }

    private MonCityImageLoader(BaseImageStrategy strategy) {
        mStrategy = strategy;
    }

    /**
     * 使用自定义策略
     */
    public static MonCityImageLoader getInstance(@NonNull BaseImageStrategy strategy) {
        if (mInstance == null) {
            synchronized (MonCityImageLoader.class) {
                if (mInstance == null) {
                    mInstance = new MonCityImageLoader(strategy);
                    return mInstance;
                }
            }
        }
        return mInstance;
    }

    /**
     * 使用默认策略
     */
    public static MonCityImageLoader getInstance() {
        if (mInstance == null) {
            synchronized (MonCityImageLoader.class) {
                if (mInstance == null) {
                    mInstance = new MonCityImageLoader();
                    return mInstance;
                }
            }
        }
        return mInstance;
    }

    /**
     * 默认加载图片
     *
     * @param placeholder 占位符
     */
    public void loadImage(@NonNull String url, @DrawableRes int placeholder, @NonNull ImageView imageView, @NonNull ImageLoadingListener imageLoadingListener) {
        mStrategy.loadImage(url, placeholder, imageView, imageLoadingListener);
    }

    /**
     * 默认加载图片
     *
     * @param placeholder 占位符
     */
    public void loadImage(@NonNull String url, @DrawableRes int placeholder, @NonNull ImageView imageView) {
        mStrategy.loadImage(url, placeholder, imageView);
    }

    /**
     * 默认加载图片 /使用默认占位符
     */
    public void loadImage(@NonNull String url, @NonNull ImageView imageView, @NonNull ImageLoadingListener imageLoadingListener) {
        mStrategy.loadImage(url, imageView, imageLoadingListener);
    }

    /**
     * 默认加载图片 /使用默认占位符
     */
    public void loadImage(@NonNull String url, @NonNull ImageView imageView) {
        mStrategy.loadImage(url, imageView);
    }

    /**
     * 加载自定义圆角图片 默认占位符
     *
     * @param cornerType 自定义圆角类型 CornerType.ALL 4周圆角    CornerType.TOP 头部圆角 具体查看{@link RoundedCornersTransformation.CornerType <tt>RoundedCornersTransformation.CornerType</tt>}
     */
    public void loadCustomRoundImage(@NonNull String url, @NonNull RoundedCornersTransformation.CornerType cornerType, @NonNull ImageView imageView) {
        mStrategy.loadCustomRoundImage(url, cornerType, imageView);
    }

    /**
     * 加载圆角图片  无法自定义 默认占位符
     */
    public void loadRoundImage(@NonNull String url, @NonNull ImageView imageView) {
        mStrategy.loadCustomRoundImage(url, RoundedCornersTransformation.CornerType.ALL, imageView);
    }

    /**
     * 加载圆角图片  默认占位符  自定义边框颜色
     */
    public void loadBorderRoundImage(@NonNull String url, @NonNull ImageView imageView, @ColorRes int borderColor) {
        mStrategy.loadBorderRoundImage(url, RoundedCornersTransformation.CornerType.ALL, imageView, borderColor);
    }

    /**
     * 加载圆角图片  默认占位符  自定义边框尺寸、颜色
     */
    public void loadBorderRoundImage(@NonNull String url, @NonNull ImageView imageView, float borderWidth, @ColorRes int borderColor) {
        mStrategy.loadBorderRoundImage(url, RoundedCornersTransformation.CornerType.ALL, imageView, borderWidth, borderColor);
    }

    /**
     * 加载圆角图片，自定义占位符  自定义边框尺寸、颜色
     */
    public void loadBorderRoundImage(@NonNull String url, @NonNull ImageView imageView, @DrawableRes int placeholder, float borderWidth, @ColorRes int borderColor) {
        mStrategy.loadBorderRoundImage(url, RoundedCornersTransformation.CornerType.ALL, imageView, placeholder, borderWidth, borderColor);
    }

    /**
     * 加载圆角图片  自定义占位符  自定义边框颜色
     */
    public void loadBorderRoundImage(@NonNull String url, @NonNull ImageView imageView, @DrawableRes int placeholder, @ColorRes int borderColor) {
        mStrategy.loadBorderRoundImage(url, RoundedCornersTransformation.CornerType.ALL, imageView, placeholder, borderColor);
    }


    /**
     * 加载圆角图片  无法自定义 默认占位符 自定义圆角角度
     */
    public void loadRoundImage(@NonNull String url, @NonNull ImageView imageView, int radius) {
        mStrategy.loadCustomRoundImage(url, radius, RoundedCornersTransformation.CornerType.ALL, imageView);
    }

    /**
     * 加载圆角图片  无法自定义 默认占位符 自定义圆角角度
     */
    public void loadRoundImage(@NonNull String url, @DrawableRes int placeholder, @NonNull ImageView imageView, int radius) {
        mStrategy.loadCustomRoundImage(url, radius, placeholder, RoundedCornersTransformation.CornerType.ALL, imageView);
    }

    /**
     * 展示圆角图片   自定义占位符
     *
     * @param placeholder 占位符
     */
    public void loadRoundImage(@NonNull String url, @DrawableRes int placeholder, @NonNull ImageView imageView) {
        mStrategy.loadRoundImage(url, placeholder, imageView);
    }

    /**
     * 展示圆形图片 自定义占位符
     *
     * @param placeholder 占位符
     */
    public void loadCircleImage(@NonNull String url, @DrawableRes int placeholder, @NonNull ImageView imageView) {
        mStrategy.loadCircleImage(url, placeholder, imageView);
    }

    /**
     * 展示圆形图片 自定义占位符 带边框
     *
     * @param placeholder 占位符
     */
    public void loadBorderCircleImage(@NonNull String url, @DrawableRes int placeholder, @ColorRes int borderColor, @NonNull ImageView imageView) {
        mStrategy.loadBorderCircleImage(url, imageView, placeholder, borderColor);
    }

    /**
     * 展示原型图片 默认占位符
     */
    public void loadCircleImage(@NonNull String url, @NonNull ImageView imageView) {
        mStrategy.loadCircleImage(url, imageView);
    }

    /**
     * 展示原型图片 默认占位符 带边框
     */
    public void loadBorderCircleImage(@NonNull String url, @NonNull ImageView imageView, @ColorRes int borderColor) {
        mStrategy.loadBorderCircleImage(url, imageView, borderColor);
    }

    /**
     * 通过url获取bitmap对象 异步回调
     *
     * @param listener 回调
     */
    public void getBitmap(@NonNull Context context, @NonNull String url, @NonNull ImageBitmapListener listener) {
        mStrategy.getBitmap(context, url, listener);
    }

    /**
     * 清除硬盘缓存
     */
    public void clearImageDiskCache(@NonNull Context context) {
        mStrategy.clearImageDiskCache(context);
    }

    /**
     * 清除内存缓存
     */
    public void clearImageMemoryCache(@NonNull Context context) {
        mStrategy.clearImageMemoryCache(context);
    }

    /**
     * 保存图片到本地  异步回调
     *
     * @param url          图片地址
     * @param savePath     保存地址
     * @param saveFileName 文件名称
     * @param listener     回调
     */
    public void saveImage(@NonNull Context context, @NonNull String url, @NonNull String savePath, @NonNull String saveFileName, @NonNull ImageDownLoadListener listener) {
        //fixme 尚未测试
        mStrategy.saveImage(context, url, savePath, saveFileName, listener);
    }
}
