package com.luxj.daintys.image.listener;

import android.graphics.Bitmap;

/**
 * @author Luxj
 * @date create 2017/4/28
 * @description 异步获取bitmap回调
 */
public interface ImageBitmapListener {
    void getBitmapSuccess(String path, Bitmap bitmap);

    void getBitmapError(String path);
}
