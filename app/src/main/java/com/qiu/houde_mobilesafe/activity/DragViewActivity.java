package com.qiu.houde_mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
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
        //初始化位置,不能使用ivDrag.layout()方法，原因：这是ivDrag都还没有初始化
        if (SPUtils.contains(this, Consts.LAST_X)) {
            int lastX = (int) SPUtils.get(this, Consts.LAST_X, 0);
            int lastY = (int) SPUtils.get(this, Consts.LAST_Y, 0);
            showTop(lastY < screenH / 2);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivDrag.getLayoutParams();
            params.leftMargin = lastX;
            params.topMargin = lastY;
            ivDrag.setLayoutParams(params);
        }


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

                        //更新页面
                        ivDrag.layout(l, t, r, b);
                        showTop(ivDrag.getTop() < screenH / 2);

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


                return true;
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
