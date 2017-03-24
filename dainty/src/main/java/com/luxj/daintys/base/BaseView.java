package com.luxj.daintys.base;

/**
 * 负责视图更新
 * Created by 陆晓杰 on 2017/3/24.
 */

public interface BaseView<T> {
    void setPresenter(T presenter);

    void showLoading();

    void dismissLoading();

    void showLoading(String msg);

    void showLoading(int id);

    void showToast(String msg);

    void showToast(int stringid, String temp);

    void showToast(int stringid);

}
