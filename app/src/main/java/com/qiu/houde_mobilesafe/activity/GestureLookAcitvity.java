package com.qiu.houde_mobilesafe.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qiu.houde_mobilesafe.R;
import com.qiu.houde_mobilesafe.utils.ScreenUtils;
import com.qiu.houde_mobilesafe.utils.Toasts;
import com.qiu.houde_mobilesafe.view.GestureLockViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2015/10/19.
 */
public class GestureLookAcitvity extends Activity {

    /**
     * 设置锁
     */
    public static final int SET_LOCK = 0x11;
    /**
     * 解锁
     */
    public static final int UNLOCK = 0x22;
    @Bind(R.id.glvg_lock)
    GestureLockViewGroup glvgLock;
    @Bind(R.id.ibt_ok_lock)
    Button ibtOkLock;
    @Bind(R.id.ll_lock_btlayout)
    LinearLayout llLockBtlayout;
    @Bind(R.id.tv_lock_title)
    TextView tvLockTitle;

    /**
     * 当前状态页面
     */
    private int mCurrentLockType = SET_LOCK;
    private int[] mLockAnswer = null;


    public static final String LOCK_TYPE = "locktyep";
    public static final String LOCK_ANSWER = "lockAnswer";

    private SharedPreferences mref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_lock);
        ButterKnife.bind(this);

        initView();

        glvgLock.setOnGestureLockViewListener(new GestureLockViewGroup.OnGestureLockViewListener() {
            @Override
            public void onBlockSelected(int cId) {

            }

            @Override
            public void onGestureEvent(boolean matched) {

                //是否匹配密码
                if (mCurrentLockType == UNLOCK) {
                    if (matched) {
                        Intent intent = new Intent(GestureLookAcitvity.this, LostFindActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onUnmatchedExceedBoundary() {
                if (mCurrentLockType == UNLOCK) {
                   // Toasts.showShort(GestureLookAcitvity.this, "已经输错5次");
                }
            }
        });

        ibtOkLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentLockType == SET_LOCK) {

                    if (glvgLock.getChoose().size() == 4) {
                        Toasts.showShort(getApplicationContext(), "您没有设置密码！");
                    } else {
                        StringBuilder sb = new StringBuilder();
                        for (Integer i : glvgLock.getChoose()) {
                            sb.append(i);
                        }
                        String password = sb.toString();

                        //进储存密码
                        mref.edit().putString("password", password).commit();
                        //页面跳转
                        Intent intent = new Intent(GestureLookAcitvity.this, LostFindActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }

    private void initView() {
        mCurrentLockType = getIntent().getIntExtra(LOCK_TYPE, SET_LOCK);
        if (mCurrentLockType == UNLOCK) {
            //如果是解锁页面就需要获取密码
            mLockAnswer = getIntent().getIntArrayExtra(LOCK_ANSWER);
            llLockBtlayout.setVisibility(View.INVISIBLE);
            glvgLock.setAnswer(mLockAnswer);
            tvLockTitle.setText("解锁手势密码");
        } else if (mCurrentLockType == SET_LOCK) {
            //如果是设置密码
            glvgLock.setFingerUpColor(0xFF378FC9);
            tvLockTitle.setText("设置手势密码");
        }
        mref = getSharedPreferences("config", Context.MODE_PRIVATE);

        //重新设置gestureLock的宽高
        //获取屏幕的宽度
        int screenW = ScreenUtils.getScreenWidth(this);
        ViewGroup.LayoutParams params = glvgLock.getLayoutParams();
        params.width = screenW - 30;
        params.height = screenW - 30;
        glvgLock.setLayoutParams(params);
    }


}
