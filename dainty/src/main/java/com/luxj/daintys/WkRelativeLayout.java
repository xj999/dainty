package com.luxj.daintys;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * loading控件
 * lxj 2015/7/25.
 */
public class WkRelativeLayout extends RelativeLayout implements OnClickListener {
    private static final int NET_ERROR = R.mipmap.lib_net_error;//网络错误图标
    private static final int NO_DATA = R.mipmap.lib_no_data;//服务器无数据图标
    private static final int id = 11;
    private View rootView;
    private ImageView wkLoadImage;//图片
    private ProgressBar progressBar;//加载图标
    private RelativeLayout allview;//总布局
    private TextView wkload_text, wkload_reLoad;//提示文字,重载按钮
    private Context mContext;
    //
    private String default_loadMsg = "";//默认加载文字
    private String default_loadFail = "";//默认加载失败文字
    private String default_loadNetError = "";//默认网络错误文字
    private String default_loadNoData = "";//默认加载无数据文字
    //
    private boolean isLoadSuccess = false;//是否首次加载成功
    //
    private OnReTryListener listener;

    public WkRelativeLayout(Context context) {
        this(context, null);
    }

    public WkRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WkRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WkRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
        initData();
        mContext = context;
        rootView = LayoutInflater.from(context).inflate(R.layout.layout_wkload, null);
        wkLoadImage = (ImageView) rootView.findViewById(R.id.wkload_img);
        allview = (RelativeLayout) rootView.findViewById(R.id.allview);
        progressBar = (ProgressBar) rootView.findViewById(R.id.wkload_loading);
        wkload_text = (TextView) rootView.findViewById(R.id.wkload_text);
        wkload_reLoad = (TextView) rootView.findViewById(R.id.wkload_reload);
        //
        addView(rootView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    private void initData() {
        default_loadMsg = getResources().getString(R.string.lib_loading);
        default_loadNoData = getResources().getString(R.string.lib_load_no_data);
        default_loadNetError = getResources().getString(R.string.lib_net_error);
    }

    public void setAllviewColor() {
        allview.setBackgroundColor(mContext.getResources().getColor(R.color.white));
    }

    /**
     * 初次加载
     */
    public void loadState() {
        loadState(default_loadMsg);
    }

    /**
     * 初次加载
     *
     * @param loadMsg 加载时提示文字
     */
    public void loadState(String loadMsg) {
        wkload_text.setText(loadMsg);
        setState(LoadState.LOADING, loadMsg);
    }

    /**
     * 加载失败
     */
    public void loadFail() {
        loadFail(default_loadFail);
    }

    /**
     * 加载失败
     *
     * @param failMsg 加载失败文字提示
     */
    public void loadFail(String failMsg) {
        setState(LoadState.LOAD_FAIL, failMsg);
    }

    /**
     * 网络错误
     */
    public void loadNetError() {
        loadNetError(default_loadNetError);
    }

    /**
     * 网络错误
     *
     * @param msg 网络错误文字提示
     */
    public void loadNetError(String msg) {
        setState(LoadState.UNCONNECT, msg);
    }

    /**
     * 无数据
     */
    public void loadNoData() {
        loadNoData(default_loadNoData);
    }

    /**
     * 无数据
     *
     * @param msg 无数据时，文字提示内容
     */
    public void loadNoData(String msg) {
        setState(LoadState.NODATA, msg);
    }


    /**
     * 加载成功
     */
    public void loadSuccess() {
        setState(LoadState.SUCCESS, "");
    }

    /**
     * 设置重新加载数据接口
     *
     * @param listener
     */
    public void setOnReTryListener(OnReTryListener listener) {
        this.listener = listener;
    }

    private void setState(LoadState state, String msg) {
        hideAllChildViews();
        if (state == LoadState.UNCONNECT || state == LoadState.LOAD_FAIL) {// 网络未连接 // 加载失败 ：连接服务器失败，服务端异常，数据异常解析失败
            wkLoadImage.setVisibility(VISIBLE);
            wkLoadImage.setImageResource(NET_ERROR);
            wkload_text.setVisibility(VISIBLE);
            wkload_reLoad.setVisibility(VISIBLE);
            wkload_text.setText(default_loadNetError);
            rootView.setOnClickListener(this);
        } else if (state == LoadState.LOADING) { // 加载中
            progressBar.setVisibility(VISIBLE);
            wkload_text.setVisibility(VISIBLE);
            wkload_text.setText(default_loadMsg);
            wkload_text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.0f);
            wkload_text.setTextColor(0xffcacaca);
        } else if (state == LoadState.NODATA) {// 无数据
            wkLoadImage.setVisibility(VISIBLE);
            wkLoadImage.setImageResource(NO_DATA);
            wkload_text.setVisibility(VISIBLE);
            if (msg.equals("")) {
                wkload_text.setText(default_loadNoData);
            } else {
                wkload_text.setText(msg);
            }
        } else if (state == LoadState.SUCCESS) { // 加载成功
            isLoadSuccess = true;
            showAllChildViews();
        }
    }

    /**
     * 隐藏所有子view
     */
    private void hideAllChildViews() {
        int viewCount = getChildCount();
        for (int i = 0; i < viewCount; i++) {
            getChildAt(i).setVisibility(GONE);
        }
        wkLoadImage.setVisibility(GONE);
        progressBar.setVisibility(GONE);
        wkload_text.setVisibility(GONE);
        wkload_reLoad.setVisibility(INVISIBLE);
        rootView.setVisibility(VISIBLE);
        rootView.setOnClickListener(null);
        //
        wkload_text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22.0f);
        wkload_text.setTextColor(0xff7f7f7f);

    }

    /**
     * 显示所有子view
     */
    private void showAllChildViews() {
        int viewCount = getChildCount();
        for (int i = 0; i < viewCount; i++) {
            getChildAt(i).setVisibility(VISIBLE);
        }
        rootView.setVisibility(GONE);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (rootView.hashCode() == v.hashCode()) {//重新加载按钮
            if (listener != null) {
                listener.onReTryClick();
            }
        }
    }


    public enum LoadState {
        UNCONNECT, // 网络未连接
        LOADING,   // 加载中
        LOAD_FAIL, // 加载失败 ：连接服务器失败，服务端异常，数据异常解析失败
        NODATA,    // 无数据
        SUCCESS    // 加载成功
    }

    public interface OnReTryListener {
        /**
         * 重新加载数据
         */
        public void onReTryClick();
    }


}
