package com.luxj.daintys;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class HomePage extends BaseActivity {
    RefreshLayout refreshableView;
    final int SUCCESS = 1;
    final int FAILED = 0;
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

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_homepage);
        setTitleText("测试标题");
        final WkRelativeLayout relativeLayout = (WkRelativeLayout) findViewById(R.id.main);
        relativeLayout.loadSuccess();
        findViewById(R.id.b1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayout.loadFail();
            }
        });
        relativeLayout.setOnReTryListener(new WkRelativeLayout.OnReTryListener() {
            @Override
            public void onReTryClick() {
                relativeLayout.loadSuccess();
            }
        });
        refreshableView = (RefreshLayout) findViewById(R.id.refresh);

        for (int i = 0; i < 5; i++) {
            TextView tv = new TextView(this);
            tv.setText(i + "号头部Textview");
            tv.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            tv.setGravity(Gravity.CENTER);
            refreshableView.addHeadView(tv, 50);
        }
        refreshableView.setRefreshListener(new RefreshLayout.RefreshListener() {

            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        handler.sendEmptyMessage(SUCCESS);

                    }
                }, 500);


            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }


}
