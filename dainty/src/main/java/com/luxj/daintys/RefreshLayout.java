package com.luxj.daintys;


import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.luxj.daintys.util.DimensUtil;

import java.util.Calendar;


/**
 * 刷新控制view
 */
public class RefreshLayout extends LinearLayout {
    public static final String TAG = "RefreshLayout";
    /**
     * 下拉刷新状态
     */
    public static final int REFRESH_BY_PULLDOWN = 0;

    /**
     * 松开刷新状态
     */
    public static final int REFRESH_BY_RELEASE = 1;
    /**
     * 正在刷新状态
     */
    public static final int REFRESHING = 2;
    /**
     * 刷新成功状态
     */
    public static final int REFRESHING_SUCCESS = 3;
    /**
     * 刷新失败状态
     */
    public static final int REFRESHING_FAILD = 4;

    private View refreshView;
    private int mRefreshTargetTop;
    private ObjectAnimator mRefreshAnim;

    //下拉刷新相关布局
    private TextView mRefreshTipTv;

    private RefreshListener mRefreshListener;
    private int mLastY;
    // 是否可刷新标记
    private boolean isRefreshEnabled = true;

    int refreshState = REFRESH_BY_PULLDOWN;

    private Context mContext;

    private int mHeadCount = 0;
    private RecyclerView mRecy;
    private View mChildView;
    private boolean mCanTouch = true; //刷新时不允许子view滑动

    public RefreshLayout(Context context) {
        this(context, null);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        //刷新视图顶端的的view
        refreshView = LayoutInflater.from(mContext).inflate(R.layout.layout_refresh_header, null);
        initRefreshView();
        mRefreshTargetTop = -DimensUtil.dpToPixels(mContext, 60);
        LayoutParams lp = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, -mRefreshTargetTop);
        lp.topMargin = mRefreshTargetTop;
        lp.gravity = Gravity.CENTER;
        addView(refreshView, mHeadCount, lp);
        mHeadCount++;
        mRefreshAnim = ObjectAnimator.ofFloat(refreshView, "bbb", 0.0f, 1.0f);

    }

    private void initRefreshView() {
        mRefreshTipTv = (TextView) refreshView.findViewById(R.id.tv_tip);
    }

    /**
     * head不能滚动 不建议使用 除非特殊需求
     */
    @Deprecated
    public void addHeadView(View view, int hight) {
        LayoutParams lp2 = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, DimensUtil.dpToPixels(mContext, hight));
        addView(view, mHeadCount, lp2);
        mHeadCount++;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mCanTouch) {
            return false;
        }
        int y = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //记录下y坐标
                mLastY = y;
                break;

            case MotionEvent.ACTION_MOVE:
                //y移动坐标
                int m = y - mLastY;
                if (isRefreshEnabled)
                    doMovement(m);
                //记录下此刻y坐标
                this.mLastY = y;
                break;

            case MotionEvent.ACTION_UP:
                fling();
                break;
        }
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev) && mCanTouch;
    }

    /**
     * up事件处理
     */
    private void fling() {
        LayoutParams lp = (LayoutParams) refreshView.getLayoutParams();
        if (lp.topMargin > 0) {//拉到了触发可刷新事件
            refresh();
        } else {//收回
            animRefreshView(lp.topMargin, mRefreshTargetTop, 300);
        }
    }


    private void refresh() {
        LayoutParams lp = (LayoutParams) this.refreshView.getLayoutParams();
        int i = lp.topMargin;
        animRefreshView(i, 0, 200);
        refreshing();
        if (mRefreshListener != null) {
            mRefreshListener.onRefresh();
            setRefreshState(REFRESHING);
        }
    }

    /**
     * 下拉move事件处理
     *
     * @param moveY
     */
    private void doMovement(int moveY) {
        if (mRefreshAnim.isRunning() || refreshState == REFRESHING) {
            return;
        }
        LayoutParams lp = (LayoutParams) refreshView.getLayoutParams();
        float f1 = lp.topMargin;
        int i = (int) (f1 + moveY * 0.4F);
        if (i >= mRefreshTargetTop) {//如果下拉大于-60dp的高度,动态刷新子视图
            lp.topMargin = i;
            refreshView.setLayoutParams(lp);
            refreshView.invalidate();
            invalidate();
        }

        if (lp.topMargin > 0) {//松开刷新状态
            if (refreshState != REFRESH_BY_RELEASE) {
                pullUpToRefresh();
                setRefreshState(REFRESH_BY_RELEASE);
            }
        } else {//下拉刷新状态
            if (refreshState != REFRESH_BY_PULLDOWN) {
                setRefreshState(REFRESH_BY_PULLDOWN);
                pullDownToRefresh();
            }

        }

    }

    /**
     * 设置是否可以刷新
     *
     * @param b
     */
    public void setRefreshEnabled(boolean b) {
        this.isRefreshEnabled = b;
    }

    /**
     * 设置刷新回调
     *
     * @param listener
     */
    public void setmRefreshListener(RefreshListener listener) {
        this.mRefreshListener = listener;
    }

    /**
     * 获取当前刷新状态
     *
     * @return
     */
    public int getRefreshState() {
        return refreshState;
    }

    /**
     * 设置当前刷新状态
     *
     * @param refreshState
     */
    public void setRefreshState(int refreshState) {
        this.refreshState = refreshState;
    }


    /**
     * 结束刷新事件
     */
    public void finishRefresh(boolean isOK) {
        LayoutParams lp = (LayoutParams) this.refreshView.getLayoutParams();
        final int i = lp.topMargin;
        if (isOK) {
            refreshOK();
        } else {
            refreshFailed();
        }
        if (!mRefreshAnim.isRunning() && refreshState != REFRESHING) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    animRefreshView(i, mRefreshTargetTop, 500);
                }
            }, 300);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (!isRefreshEnabled) {
            return false;
        }
        int action = e.getAction();
        int y = (int) e.getRawY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastY = y;
                break;

            case MotionEvent.ACTION_MOVE:
                if (y > mLastY && canScroll()) {
                    return true;
                }
                //记录下此刻y坐标
                this.mLastY = y;
                break;
        }
        return false;
    }

    private boolean canScroll() {
        if (getChildCount() > 1) {
            mChildView = this.getChildAt(1);
            if (mChildView instanceof ListView) {
                int top = ((ListView) mChildView).getChildAt(0).getTop();
                int pad = ((ListView) mChildView).getListPaddingTop();
                if ((Math.abs(top - pad)) < 3 &&
                        ((ListView) mChildView).getFirstVisiblePosition() == 0) {
                    return true;
                } else {
                    return false;
                }
            } else if (mChildView instanceof ScrollView) {
                if (((ScrollView) mChildView).getScrollY() == 0) {
                    return true;
                } else {
                    return false;
                }
            } else if (mChildView instanceof WebView) {
                if (((WebView) mChildView).getScrollY() == 0) {
                    return true;
                } else {
                    return false;
                }
            } else if (mChildView instanceof GridView) {
                int top = ((GridView) mChildView).getChildAt(0).getTop();
                int pad = ((GridView) mChildView).getListPaddingTop();
                if ((Math.abs(top - pad)) < 3 &&
                        ((GridView) mChildView).getFirstVisiblePosition() == 0) {
                    return true;
                } else {
                    return false;
                }
            } else if (mChildView instanceof RecyclerView) {
                mRecy = (RecyclerView) mChildView;
                RecyclerView.LayoutManager manager = ((RecyclerView) mChildView).getLayoutManager();
                int top = 0;
                if (manager instanceof LinearLayoutManager) {
                    top = ((LinearLayoutManager) manager).findFirstVisibleItemPosition();
                } else if (manager instanceof StaggeredGridLayoutManager) {
                    top = ((StaggeredGridLayoutManager) manager).findFirstVisibleItemPositions(null)[0];
                }

                if (((RecyclerView) mChildView).getChildAt(0).getY() == 0 && top == 0) {
                    return true;
                } else {
                    return false;
                }

            } else {  //不是可滚动的view    直接返回可刷新
                return true;
            }

        }
        return false;
    }

    /**
     * 从开始位置滑动到结束位置
     *
     * @param startHeight
     * @param endHeight
     */
    public void animRefreshView(final int startHeight, final int endHeight, int duration) {
        mRefreshAnim.start();
        mRefreshAnim.setDuration(duration);
        mRefreshAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cVal = (Float) animation.getAnimatedValue();
                LayoutParams lp = (LayoutParams) refreshView.getLayoutParams();
                int k = startHeight + (int) (cVal * (endHeight - startHeight));
                lp.topMargin = k;
                refreshView.setLayoutParams(lp);
                refreshView.invalidate();
                invalidate();
            }
        });

    }

    /**
     * 刷新监听接口
     *
     * @author Nono
     */
    public interface RefreshListener {
        void onRefresh();
    }

    /**
     * 下拉刷新状态
     */
    public void pullDownToRefresh() {
        setRefreshState(REFRESH_BY_PULLDOWN);
        mRefreshTipTv.setText("下拉可以刷新");
    }

    /**
     * 松开刷新状态
     */
    public void pullUpToRefresh() {
        setRefreshState(REFRESH_BY_RELEASE);
        mRefreshTipTv.setText("松开立即刷新");
    }

    /**
     * 正在刷新状态
     */
    public void refreshing() {
        setRefreshState(REFRESHING);
        mRefreshTipTv.setText("正在刷新数据...");
        mCanTouch = false;
    }


    /**
     * 刷新成功状态
     */
    public void refreshOK() {
        setRefreshState(REFRESHING_SUCCESS);
        mRefreshTipTv.setText("刷新成功");
        recyclerviewScrollToTop();
    }

    /**
     * 刷新失败状态
     */
    public void refreshFailed() {
        setRefreshState(REFRESHING_FAILD);
        mRefreshTipTv.setText("刷新失败");
        recyclerviewScrollToTop();
    }

    private void recyclerviewScrollToTop() {
        if (mRecy != null && mRecy.getChildCount() > 0) {
            mRecy.scrollToPosition(0);
        }

        mCanTouch = true;
    }

}
