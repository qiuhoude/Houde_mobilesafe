package com.qiu.houde_mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.qiu.houde_mobilesafe.R;
import com.qiu.houde_mobilesafe.service.AddressService;
import com.qiu.houde_mobilesafe.utils.Consts;
import com.qiu.houde_mobilesafe.utils.SPUtils;
import com.qiu.houde_mobilesafe.utils.ServiceStatusUtils;
import com.qiu.houde_mobilesafe.view.SettingItemView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2015/10/16.
 */
public class SettingsActivity extends Activity {

    @Bind(R.id.siv_update)
    SettingItemView sivUpdate;
    @Bind(R.id.siv_address)
    SettingItemView sivAddress;
    private SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        initUpdateView();
        initAddressView();

    }

    /**
     * 初始化来电归属地
     */
    private void initAddressView() {
        boolean serviceRunning = ServiceStatusUtils.isServiceRunning(this, AddressService.class.getName());
        initSettingView(sivAddress, serviceRunning, null);
        final  Intent service = new Intent(this,AddressService.class);
        sivAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sivAddress.isChecked()) {
                    // 设置不勾选
                    sivAddress.setChecked(false);
                    stopService(service);
                } else {
                    sivAddress.setChecked(true);
                    startService(service);
                }
            }
        });
    }

    /**
     * 初始化更新控件
     */
    private void initUpdateView() {
        boolean autoUpdate = (Boolean) SPUtils.get(getApplicationContext(), Consts.AUTO_UPDATE, false);
        initSettingView(sivUpdate, autoUpdate, Consts.AUTO_UPDATE);

    }


    private void initSettingView(final SettingItemView view, boolean state, final String spKye) {
        //初始化状态
        if (state) {
            view.setChecked(true);
        } else {
            view.setChecked(false);
        }

        //设置监听
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 判断当前的勾选状态
                if (view.isChecked()) {
                    // 设置不勾选
                    view.setChecked(false);
                    // 更新sp
                    if (!TextUtils.isEmpty(spKye)) {
                        SPUtils.put(getApplicationContext(), spKye, false);
                    }
                } else {
                    view.setChecked(true);
                    // 更新sp
                    if (!TextUtils.isEmpty(spKye)) {
                        SPUtils.put(getApplicationContext(), spKye, true);
                    }
                }
            }
        });
    }

}
