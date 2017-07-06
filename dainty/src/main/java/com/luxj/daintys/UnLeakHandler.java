package com.luxj.daintys;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * @author Luxj
 * @date create 2017/6/30
 * @description
 */
public class UnLeakHandler extends Handler {
    private UnLeakHandlerCallBack lis;
    private final WeakReference<Activity> mActivity;

    public UnLeakHandler(Activity activity, UnLeakHandlerCallBack lis) {
        mActivity = new WeakReference<>(activity);
        this.lis = lis;
    }

    @Override
    public void handleMessage(Message msg) {
        if (lis != null) {
            if (mActivity.get() == null) {
                return;
            }
            lis.handleMessage(msg);
        }
    }

    public interface UnLeakHandlerCallBack {
        void handleMessage(Message msg);
    }
}
