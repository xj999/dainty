package com.luxj.daintys;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;

/**
 * @author Luxj
 * @date create 2017/6/27
 * @description
 */
public class TopLoadingView extends LinearLayout {
    private View circle1, circle2, circle3;
    private int flag = 1;
    private AnimationSet set1, set2, set3;

    public TopLoadingView(Context context) {
        super(context);
        init(context);
    }

    public TopLoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TopLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOrientation(HORIZONTAL);
        setBackgroundColor(context.getResources().getColor(R.color.transparent));
        circle1 = new View(context);
        LayoutParams lp = new LayoutParams(24, 24);
        lp.leftMargin = 30;
        circle1.setBackgroundResource(R.drawable.yuan);
        circle1.setAlpha(0.5f);
        addView(circle1, lp);
        circle2 = new View(context);
        circle2.setBackgroundResource(R.drawable.yuan);
        circle2.setAlpha(0.5f);
        addView(circle2, lp);
        circle3 = new View(context);
        circle3.setBackgroundResource(R.drawable.yuan);
        circle3.setAlpha(0.5f);
        addView(circle3, lp);

        set1 = new AnimationSet(true);
        set1.addAnimation(getAlphaAnim());
        set1.addAnimation(getScaleAnim());
        set1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                circle1.setAlpha(1.0f);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                switch (flag) {
                    case 1:
                    case 5:
                        flag = 2;
                        circle2.startAnimation(set2);
                        break;
                }
                circle1.setAlpha(0.5f);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        set2 = new AnimationSet(true);
        set2.addAnimation(getAlphaAnim());
        set2.addAnimation(getScaleAnim());
        set2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                circle2.setAlpha(1.0f);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                switch (flag) {
                    case 2:
                        flag = 3;
                        circle3.startAnimation(set3);
                        break;
                    case 4:
                        flag = 5;
                        circle1.startAnimation(set1);
                        break;
                }
                circle2.setAlpha(0.5f);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        set3 = new AnimationSet(true);
        set3.addAnimation(getAlphaAnim());
        set3.addAnimation(getScaleAnim());
        set3.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                circle3.setAlpha(1.0f);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                switch (flag) {
                    case 3:
                        flag = 4;
                        circle2.startAnimation(set2);
                        break;
                }
                circle3.setAlpha(0.5f);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        startAnimation();
    }

    private AlphaAnimation getAlphaAnim() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.4f, 1.0f);
        alphaAnimation.setDuration(500);
        return alphaAnimation;
    }

    private ScaleAnimation getScaleAnim() {
        ScaleAnimation scaleAni = new ScaleAnimation(1.0f, 1.5f, 1.0f, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAni.setDuration(500);
        return scaleAni;
    }

    public void startAnimation() {
        circle1.startAnimation(set1);
    }

    public void clearAnimation() {
        circle1.clearAnimation();
        circle2.clearAnimation();
        circle3.clearAnimation();
        flag = 1;
    }

}
