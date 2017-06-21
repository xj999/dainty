package com.luxj.daintys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author Luxj
 * @date create 2017/6/20
 * @description
 */
public abstract class BaseActivity extends AppCompatActivity {
    private ImageButton r1Btn;
    private ImageButton r2Btn;
    private TextView nav_title;
    private LinearLayout rootContent;
    private View layout_nav;
    private Button r3Btn;
    private static long lastClickTime;
    private boolean flag = false;
    Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDelegate().setContentView(R.layout.layout_base);
        window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        nav_title = (TextView) window.findViewById(R.id.nav_title);
        window.findViewById(R.id.layout_nav).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNavLayoutClick();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(android.R.color.holo_blue_bright));
        }
        ImageButton nav_back = (ImageButton) window.findViewById(R.id.nav_back);
        rootContent = (LinearLayout) window.findViewById(R.id.content);
        layout_nav = window.findViewById(R.id.layout_nav);
        r1Btn = (ImageButton) window.findViewById(R.id.nav_right1);
        r2Btn = (ImageButton) window.findViewById(R.id.nav_right2);
        r3Btn = (Button) window.findViewById(R.id.nav_right3);
        if (r1Btn != null) {
            r1Btn.setVisibility(View.GONE);
            r1Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onR1BtnClick();
                }
            });
        }
        //
        if (r2Btn != null) {
            r2Btn.setVisibility(View.GONE);
            r2Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onR2BtnClick();
                }
            });
        }
        if (r3Btn != null) {
            r3Btn.setVisibility(View.GONE);
            r3Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onR3BtnClick();
                }
            });
        }
        if (nav_back != null) {
            nav_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
        data(savedInstanceState);
    }

    protected void onNavLayoutClick() {
        closeKeyBoard(this);
    }

    /**
     * 设置状态栏颜色
     */
    public void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(color));
        }
    }


    /**
     * 设置title
     *
     * @param titleText
     */
    public void setTitleText(String titleText) {
        if (nav_title != null) {
            if (titleText.length() > 14) {
                titleText = titleText.substring(0, 13) + "...";
            }
            nav_title.setText(titleText);
        }
    }

    /**
     * 设置头部隐藏
     */
    public void setNavGone() {
        if (layout_nav != null) {
            layout_nav.setVisibility(View.GONE);
        }
    }

    /**
     *
     */
    protected void setR1BtnImage(int resource) {
        try {
            r1Btn.setVisibility(View.VISIBLE);
            r1Btn.setImageResource(resource);
        } catch (Exception e) {
        }
    }

    /**
     *
     */
    protected void setR2BtnImage(int resource) {
        try {
            if (resource == 0) {
                r2Btn.setVisibility(View.GONE);
            } else {
                r2Btn.setVisibility(View.VISIBLE);
                r2Btn.setImageResource(resource);
            }
        } catch (Exception e) {
        }
    }

    /**
     * 设置右侧按钮文字
     *
     * @param text
     */
    protected void setR3BtnText(String text) {
        if (text == null || text.isEmpty()) {
            r3Btn.setVisibility(View.GONE);
        } else {
            r3Btn.setVisibility(View.VISIBLE);
            r3Btn.setText(text);
        }
    }

    /**
     */
    protected void onR1BtnClick() {
    }

    /**
     */
    protected void onR2BtnClick() {
    }

    /**
     */
    protected void onR3BtnClick() {
    }


    @Override
    public View findViewById(int id) {
        return rootContent.findViewById(id);
    }

    private void clearContentView() {
        rootContent.removeAllViews();
    }

    @Override
    public void setContentView(int layoutResID) {
        clearContentView();
        View v = getLayoutInflater().inflate(layoutResID, rootContent, true);
        View child = ((LinearLayout) v).getChildAt(0);
        if (child instanceof RecyclerView) {
            Log.e("bbb", "RecyclerView setContentView: RecyclerView");
        } else if (child instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) child).getChildCount(); i++) {
                if (((ViewGroup) child).getChildAt(i) instanceof RecyclerView) {
                    Log.e("bbb", "RelativeLayout setContentView: RecyclerView");
                    break;
                }
            }
        }
    }


    @Override
    public void setContentView(View view) {
        clearContentView();
        rootContent.addView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        clearContentView();
        rootContent.addView(view, params);
    }

    private void data(Bundle savedInstanceState) {
        initData(savedInstanceState);
    }

    protected abstract void initData(Bundle savedInstanceState);

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN && flag) {
            if (isFastDoubleClick()) {
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (timeD >= 0 && timeD <= 1000) {
            return true;
        } else {
            lastClickTime = time;
            return false;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private ProgressDialog dialog;

    public void showToast(String msg) {
    }

    public void showToast(int id, String temp) {
    }

    public void showToast(int stringId) {
    }

    public void showLoading() {
        if (dialog != null && dialog.isShowing()) return;
        dialog = new ProgressDialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(getString(R.string.loading_value));
        dialog.setCancelable(false);
        dialog.show();
    }

    public void showLoading(String msg) {
        if (dialog != null && dialog.isShowing()) return;
        dialog = new ProgressDialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.setMessage(msg);
        dialog.show();
    }

    public void showLoading(int id) {
        if (dialog != null && dialog.isShowing()) return;
        dialog = new ProgressDialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(getString(id));
        dialog.setCancelable(false);
        dialog.show();
    }


    public void dismissLoading() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        closeKeyBoard(this);
        super.onBackPressed();
    }

    private static InputMethodManager imm;

    /**
     * 关闭键盘
     */
    public static void closeKeyBoard(Activity activity) {
        try {
            if (imm == null) {
                imm = (InputMethodManager) activity
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
            }
            imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getWindowToken(), 0);
        } catch (Exception e) {
        }
    }


    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(source);
        }
    }

}
