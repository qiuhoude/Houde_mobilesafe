package com.qiu.houde_mobilesafe.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.qiu.houde_mobilesafe.R;
import com.qiu.houde_mobilesafe.service.KillProcessService;
import com.qiu.houde_mobilesafe.utils.Consts;
import com.qiu.houde_mobilesafe.utils.SPUtils;
import com.qiu.houde_mobilesafe.utils.ServiceStatusUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2015/11/10.
 */
public class TaskManagerSettingActivity extends Activity {

    @Bind(R.id.cb_status)
    CheckBox cbStatus;
    @Bind(R.id.cb_status_kill_process)
    CheckBox cbStatusKillProcess;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager_setting);
        ButterKnife.bind(this);
        mContext = getApplicationContext();
        boolean isShowSys = (boolean) SPUtils.get(mContext, Consts.IS_SHOW_SYSTEM, true);
        cbStatus.setChecked(isShowSys);
        cbStatusKillProcess.setChecked(ServiceStatusUtils.isServiceRunning(mContext, KillProcessService.class.getName()));

        cbStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SPUtils.put(mContext, Consts.IS_SHOW_SYSTEM, isChecked);
            }
        });

        final Intent killServices = new Intent(this, KillProcessService.class);
        cbStatusKillProcess.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startService(killServices);
                } else {
                    stopService(killServices);
                }
            }
        });
    }


}
