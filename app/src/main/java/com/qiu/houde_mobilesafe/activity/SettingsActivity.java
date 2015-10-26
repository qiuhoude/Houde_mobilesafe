package com.qiu.houde_mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.qiu.houde_mobilesafe.R;
import com.qiu.houde_mobilesafe.service.AddressService;
import com.qiu.houde_mobilesafe.utils.Consts;
import com.qiu.houde_mobilesafe.utils.SPUtils;
import com.qiu.houde_mobilesafe.utils.ServiceStatusUtils;
import com.qiu.houde_mobilesafe.view.SettingClickView;
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
    @Bind(R.id.scv_style)
    SettingClickView scvStyle;
    @Bind(R.id.scv_location)
    SettingClickView scvLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        initUpdateView();
        initAddressView();
        initAddressStyleView();
        initAddressLocation();

    }

    private void initAddressLocation() {
        scvLocation.setDesc("设置归属地提示框的显示位置");
        scvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this,
                        DragViewActivity.class));
            }
        });
    }


    /**
     * 初始化风格
     */
    private void initAddressStyleView() {
        final int style = (Integer) SPUtils.get(this, Consts.ADDRESS_STYLE, 0);
        scvStyle.setDesc(Consts.ADRESS_STYLE_ITEMS[style]);
        scvStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStyleChooseDailog();
            }
        });
    }

    private void showStyleChooseDailog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("归属地提示框风格");

        int style = (Integer) SPUtils.get(this, Consts.ADDRESS_STYLE, 0);// 读取保存的style

        builder.setSingleChoiceItems(Consts.ADRESS_STYLE_ITEMS, style,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SPUtils.put(getApplicationContext(), Consts.ADDRESS_STYLE, which);// 保存选择的风格
                        dialog.dismiss();// 让dialog消失
                        scvStyle.setDesc(Consts.ADRESS_STYLE_ITEMS[which]);// 更新组合控件的描述信息
                    }
                });

        builder.setNegativeButton("取消", null);
        builder.show();
    }

    /**
     * 初始化来电归属地
     */
    private void initAddressView() {
        boolean serviceRunning = ServiceStatusUtils.isServiceRunning(this, AddressService.class.getName());
        initSettingView(sivAddress, serviceRunning, null);
        final Intent service = new Intent(this, AddressService.class);
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
