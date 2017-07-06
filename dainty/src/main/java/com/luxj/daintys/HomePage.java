package com.luxj.daintys;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends BaseActivity implements UnLeakHandler.UnLeakHandlerCallBack {
    RefreshLayout refreshableView;
    RecyclerView recy;
    final int SUCCESS = 1;
    final int FAILED = 0;
    boolean aa;
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
        refreshableView = (RefreshLayout) findViewById(R.id.refresh);
        for (int i = 0; i < 1; i++) {
            TextView tv = new TextView(this);
            tv.setText(i + "号头部Textview");
            tv.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            tv.setGravity(Gravity.CENTER);
//            refreshableView.addHeadView(tv, 50);
        }

        refreshableView.setmRefreshListener(new RefreshLayout.RefreshListener() {

            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        handler.sendEmptyMessage(SUCCESS);
                        handlerb.sendEmptyMessage(1);

                    }
                }, 5500);


            }
        });

        MyAdapter adapter = new MyAdapter();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("测试数据===" + i);
        }
        adapter.setData(list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);


    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }


    @Override
    public void handleMessage(Message msg) {
        Log.e("handlerb", "handleMessage: " + msg.what);
    }
}
