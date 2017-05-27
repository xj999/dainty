package com.luxj.daintys.image.gilde;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.luxj.daintys.image.listener.ImageBitmapListener;
import com.luxj.daintys.image.listener.ImageDownLoadListener;
import com.luxj.daintys.image.listener.ImageLoadingListener;
import com.luxj.daintys.util.DimensUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Luxj
 * @date create 2017/4/27
 * @description glide图片加载
 */
public abstract class GlideLoader {

    /**
     * 清除缓存
     */
    protected static void clearDiskCache(final Context context) {
        new Thread() {
            @Override
            public void run() {
                Glide.get(context).clearDiskCache();
            }
        }.start();

    }

    /**
     * 加载圆形图片 带占位符带边框
     */
    protected static void loadBorderCirclePic(String path, ImageView imageView, int placeholderid, int color) {
        if (placeholderid == 0) {
            placeholderid = getDefaultCircleImage();
        }
        if (color != 0)
            color = ContextCompat.getColor(imageView.getContext(), color);
        Glide.with(imageView.getContext()).load(path).bitmapTransform(new GlideCircleTransform(imageView.getContext(), 2, color)).skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(placeholderid).fallback(placeholderid).dontAnimate().into(imageView);
    }

    /**
     * 加载自定义圆角图片 可选上圆角 下圆角   坐上圆角 等等
     */
    protected static void loadBorderRoundImage(String path, int radius, ImageView imageView, RoundedCornersTransformation.CornerType cornerType, int placeholder, float borderWidth, int borderColor) {
        if (radius == 0) {
            radius = 3;
        }
        if (placeholder == 0) {
            placeholder = getDefaultImage();
        }
        if (borderColor != 0) {
            borderColor = ContextCompat.getColor(imageView.getContext(), borderColor);
        }
        Glide.with(imageView.getContext())
                .load(path)
                .bitmapTransform(new CenterCrop(imageView.getContext()), new RoundedCornersTransformation(imageView.getContext(), DimensUtil.dpToPixels(imageView.getContext(), radius), 0,
                        cornerType, borderWidth, borderColor))
                .placeholder(placeholder)
                .into(imageView);
    }


    /**
     * 默认
     *
     * @param placeholder_id 占位符资源id
     */
    protected static void loadDefault(final String path, final ImageView imageView, int placeholder_id, final ImageLoadingListener imageLoadingListener) {
        RequestListener<String, GlideDrawable> requestListener = new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                if (imageLoadingListener != null) {
                    imageLoadingListener.onError(imageView, path);
                }
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                if (imageLoadingListener != null) {
                    imageLoadingListener.onSuccess(imageView, path);
                }
                return false;
            }
        };

        RequestListener<String, GifDrawable> gifRequestListener = new RequestListener<String, GifDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GifDrawable> target, boolean isFirstResource) {
                if (imageLoadingListener != null) {
                    imageLoadingListener.onError(imageView, path);
                }
                return false;
            }

            @Override
            public boolean onResourceReady(GifDrawable resource, String model, Target<GifDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                if (imageLoadingListener != null) {
                    imageLoadingListener.onSuccess(imageView, path);
                }
                return false;
            }
        };
        if (placeholder_id == 0) {
            placeholder_id = getDefaultImage();
        }
        if (getFileType(path).toLowerCase().equals("gif")) {
            Glide.with(imageView.getContext()).load(path).asGif().centerCrop().placeholder(placeholder_id).fallback(placeholder_id).dontAnimate().listener(gifRequestListener).into(imageView);
        } else {
            Glide.with(imageView.getContext()).load(path).centerCrop().placeholder(placeholder_id).fallback(placeholder_id).dontAnimate().listener(requestListener).into(imageView);
        }
    }

    protected static void getBitmapForUrl(Context context, final String url, final ImageBitmapListener listener) {
        Glide.with(context)
                .load(url)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        listener.getBitmapSuccess(url, resource);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        listener.getBitmapError(url);
                    }
                });
    }

    /**
     * 获取文件类型
     */
    private static String getFileType(String url) {
        if (url == null || url.equals("")) {
            return "";
        }
        int typeIndex = url.lastIndexOf(".");
        if (typeIndex != -1) {
            return url.substring(typeIndex + 1).toLowerCase();
        }

        return "";
    }


    /**
     * 设置默认占位图
     */
    private static int getDefaultImage() {
        return -1;
    }

    /**
     * 设置默认圆形占位图
     */
    private static int getDefaultCircleImage() {
        return 0;
    }

    /**
     * 回调的地址不是在UI线程完成
     */
    protected static void downLoadImage(
            final Context context,
            final String url, String savePath, String name,
            final ImageDownLoadListener listener) {
        if (TextUtils.isEmpty(url)) {
            listener.onDownLoadFail();
            return;
        }
        InputStream fromStream = null;
        OutputStream toStream = null;
        try {
            File cacheFile = Glide.with(context).load(url).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
            if (cacheFile == null || !cacheFile.exists()) {
                listener.onDownLoadFail();
                return;
            }
            File dir = new File(savePath);
            boolean dirMark;
            dirMark = dir.exists() || dir.mkdir();
            if (dirMark) {
                File file = new File(dir, name + getFileType(cacheFile.getAbsolutePath()));
                fromStream = new FileInputStream(cacheFile);
                toStream = new FileOutputStream(file);
                byte length[] = new byte[1024];
                int count;
                while ((count = fromStream.read(length)) > 0) {
                    toStream.write(length, 0, count);
                }
                //用广播通知相册进行更新相册
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri uri = Uri.fromFile(file);
                intent.setData(uri);
                context.sendBroadcast(intent);
                listener.onDownLoadSuccess(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            listener.onDownLoadFail();
        } finally {
            if (fromStream != null) {
                try {
                    fromStream.close();
                    assert toStream != null;
                    toStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
