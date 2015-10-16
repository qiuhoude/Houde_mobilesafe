package com.qiu.houde_mobilesafe.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.qiu.houde_mobilesafe.R;
import com.qiu.houde_mobilesafe.view.SettingItemView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2015/10/16.
 */
public class SettingsActivity extends Activity {

    @Bind(R.id.siv_update)
    SettingItemView sivUpdate;
    private SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        mPref = getSharedPreferences("config", Context.MODE_PRIVATE);

        boolean autoUpdate = mPref.getBoolean("auto_update", true);

        if (autoUpdate ) {
            sivUpdate.setChecked(true);
        }else {
            sivUpdate.setChecked(false);
        }


        sivUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 判断当前的勾选状态
                if (sivUpdate.isChecked()) {
                    // 设置不勾选
                    sivUpdate.setChecked(false);
                    // sivUpdate.setDesc("自动更新已关闭");
                    // 更新sp
                    mPref.edit().putBoolean("auto_update", false).commit();
                } else {
                    sivUpdate.setChecked(true);
                    // sivUpdate.setDesc("自动更新已开启");
                    // 更新sp
                    mPref.edit().putBoolean("auto_update", true).commit();
                }
            }
        });
    }
}
