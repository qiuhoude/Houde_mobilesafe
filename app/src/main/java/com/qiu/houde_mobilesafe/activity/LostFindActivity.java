package com.qiu.houde_mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qiu.houde_mobilesafe.R;
import com.qiu.houde_mobilesafe.utils.Consts;
import com.qiu.houde_mobilesafe.utils.SPUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2015/10/19.
 */
public class LostFindActivity extends Activity {


    @Bind(R.id.tv_lost_phone)
    TextView tvLostPhone;
    @Bind(R.id.iv_lost_protect)
    ImageView ivLostProtect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean configed = (Boolean) SPUtils.get(this, Consts.CONFIGED, false);// 判断是否进入过设置向导
        if (configed) {
            setContentView(R.layout.activity_lost_find);
            ButterKnife.bind(this);
            tvLostPhone.setText((String) SPUtils.get(getApplicationContext(), Consts.SAFE_PHONE, ""));
            if((Boolean)SPUtils.get(getApplicationContext(), Consts.PROTECT, false)){
                ivLostProtect.setImageResource(R.mipmap.lock);
            }else {
                ivLostProtect.setImageResource(R.mipmap.unlock);
            }


        } else {
            // 跳转设置向导页
            startActivity(new Intent(this, Setup1Activity.class));
            finish();
        }
    }

    /**
     * 重新进入设置向导
     *
     * @param view
     */
    public void reEnter(View view) {
        startActivity(new Intent(this, Setup1Activity.class));
        finish();
    }
}
