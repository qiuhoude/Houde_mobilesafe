package com.qiu.houde_mobilesafe.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.qiu.houde_mobilesafe.R;


/**
 * 第4个设置向导页
 *
 * @author Kevin
 */
public class Setup4Activity extends BaseSetupActivity {

    private SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);

        mPref = getSharedPreferences("config", MODE_PRIVATE);
    }

    @Override
    protected void showPreviousPage() {
        startActivity(new Intent(this, Setup3Activity.class));
        finish();
    }

    @Override
    protected void showNextPage() {
        startActivity(new Intent(this, LostFindActivity.class));
        finish();
        mPref.edit().putBoolean("configed", true).commit();// 更新sp,表示已经展示过设置向导了,下次进来就不展示啦
    }

}
