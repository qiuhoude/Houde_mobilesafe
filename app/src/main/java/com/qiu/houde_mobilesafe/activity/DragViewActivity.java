package com.qiu.houde_mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiu.houde_mobilesafe.R;
import com.qiu.houde_mobilesafe.utils.Consts;
import com.qiu.houde_mobilesafe.utils.SPUtils;
import com.qiu.houde_mobilesafe.utils.ScreenUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2015/10/23.
 */
public class DragViewActivity extends Activity {

    @Bind(R.id.tv_top)
    TextView tvTop;
    @Bind(R.id.tv_bottom)
    TextView tvBottom;
    @Bind(R.id.iv_drag)
    ImageView ivDrag;

    long[] mHits = new long[2];// 数组长度表示要点击的次数
    int startX;
    int startY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.TransparentActivity);
        setContentView(R.layout.activity_drag_view);
        ButterKnife.bind(this);

        //获取屏幕的宽高
        final int screenH = ScreenUtils.getScreenHeight(this);
        final int screenW = ScreenUtils.getScreenWidth(this);

        //初始化位置,不能使用ivDrag.layout()方法，原因：这是ivDrag都还没有初始化
        if (SPUtils.contains(this, Consts.LAST_X)) {
            int lastX = (int) SPUtils.get(this, Consts.LAST_X, 0);
            int lastY = (int) SPUtils.get(this, Consts.LAST_Y, 0);
            showTop(lastY > screenH / 2);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivDrag.getLayoutParams();
            params.leftMargin = lastX;
            params.topMargin = lastY;
            ivDrag.setLayoutParams(params);
        }


        //双击居中
        ivDrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击代码，android setting 中点击版本的代码例子
                System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
                mHits[mHits.length - 1] = SystemClock.uptimeMillis();// 开机后开始计算的时间(开机时间)
                if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
                    // 把图片居中
                    ivDrag.layout(screenW / 2 - ivDrag.getWidth() / 2,
                            ivDrag.getTop(), screenW / 2 + ivDrag.getWidth()
                                    / 2, ivDrag.getBottom());
                }
            }
        });

        ivDrag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int currentX = (int) event.getRawX();
                int currentY = (int) event.getRawY();
//                Logs.d("currentX = " + currentX + ", currentY = " + currentY);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = currentX;
                        startY = currentY;
                        break;
                    case MotionEvent.ACTION_MOVE:

                        int dx = currentX - startX;
                        int dy = currentY - startY;


                        //上下左右的位置
                        int l = ivDrag.getLeft() + dx;
                        int r = ivDrag.getRight() + dx;
                        int t = ivDrag.getTop() + dy;
                        int b = ivDrag.getBottom() + dy;


                        //消除移动到边界缩小的问题
                        if (l < 0 || r > screenW || t < 0 || b > screenH - ScreenUtils.getStatusHeight(getApplicationContext())) {

                        } else {
                            //更新页面
                            ivDrag.layout(l, t, r, b);
                        }
                        showTop(ivDrag.getTop() > screenH / 2);

                        //重新初始化起点
                        startX = currentX;
                        startY = currentY;
                        break;
                    case MotionEvent.ACTION_UP:
                        //保存数据
                        SPUtils.put(getApplicationContext(), Consts.LAST_X, ivDrag.getLeft());
                        SPUtils.put(getApplicationContext(), Consts.LAST_Y, ivDrag.getTop());
                        break;
                }

                return false; //将时间传递给双击时间
            }
        });
    }

    private void showTop(boolean isShow) {
        if (isShow) {
            tvTop.setVisibility(View.VISIBLE);
            tvBottom.setVisibility(View.INVISIBLE);
        } else {
            tvTop.setVisibility(View.INVISIBLE);
            tvBottom.setVisibility(View.VISIBLE);
        }
    }
}
