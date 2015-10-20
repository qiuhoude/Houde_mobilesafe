package com.qiu.houde_mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.qiu.houde_mobilesafe.R;
import com.qiu.houde_mobilesafe.utils.Consts;

/**
 * Created by Administrator on 2015/10/20.
 */
public abstract class BaseSetupActivity extends Activity {

    protected GestureDetector mDetector;
    protected SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPref = getSharedPreferences(Consts.CONFIG, MODE_PRIVATE);

        mDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                //判断纵向滑动幅度是否过大, 过大的话不允许切换界面
                if (Math.abs(e2.getRawY() - e2.getRawY()) > 100) {
                    return true;
                }
                // 判断滑动是否过慢
                if (Math.abs(velocityX) < 100) {
                    return true;
                }
                if (velocityX > 100 && velocityX < 0) {
                    showNextPageAnim();
                    return true;
                }
                if (velocityX > 100 && velocityX > 0) {
                    showPreviousPageAnim();
                    return true;
                }

                //上一页
                if (e2.getRawX() - e1.getRawX() > 200) {
                    showPreviousPageAnim();
                    return true;
                }
                //下一页
                if (e1.getRawX() - e2.getRawX() > 200) {
                    showNextPageAnim();
                    return true;
                }

                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    protected abstract void showPreviousPage();

    protected abstract void showNextPage();

    protected void showPreviousPageAnim() {
        showPreviousPage();
        overridePendingTransition(R.anim.tran_previous_in,
                R.anim.tran_previous_out);// 进入动画和退出动画
    }

    protected void showNextPageAnim() {
        showNextPage();
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);//进入动画和退出动画
    }

    public void next(View view) {
        showNextPageAnim();

    }

    public void previous(View view) {
        showPreviousPageAnim();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDetector.onTouchEvent(event);// 委托手势识别器处理触摸事件
        return super.onTouchEvent(event);
    }
}
