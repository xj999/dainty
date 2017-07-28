package com.luxj.daintys;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends BaseActivity implements UnLeakHandler.UnLeakHandlerCallBack {
    RefreshLayout refreshableView;
    RecyclerView recy;
    final int SUCCESS = 1;
    final int FAILED = 0;
    boolean aa;
    MyAdapter adapter;
    private Handler handlerb = new UnLeakHandler(this, this);
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    refreshableView.finishRefresh(true);
                    break;
                case FAILED:
                    refreshableView.finishRefresh(false);
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_homepage);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recy);
        final WkRelativeLayout relativeLayout = (WkRelativeLayout) findViewById(R.id.main);
        relativeLayout.loadSuccess();
        relativeLayout.setOnReTryListener(new WkRelativeLayout.OnReTryListener() {
            @Override
            public void onReTryClick() {
                relativeLayout.loadSuccess();
            }
        });


        adapter = new MyAdapter();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("测试数据===" + i);
        }
        adapter.setData(list);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);


    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }


    @Override
    public void handleMessage(Message msg) {
        Log.e("handlerb", "handleMessage: " + msg.what);
    }
}
