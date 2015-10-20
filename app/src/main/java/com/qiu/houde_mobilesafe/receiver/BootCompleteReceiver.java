package com.qiu.houde_mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.qiu.houde_mobilesafe.utils.AppUtils;
import com.qiu.houde_mobilesafe.utils.Consts;
import com.qiu.houde_mobilesafe.utils.Logs;
import com.qiu.houde_mobilesafe.utils.SPUtils;

/**
 * 开机启动的广播接受者
 */
public class BootCompleteReceiver extends BroadcastReceiver {
    public BootCompleteReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //获取储存的sim序列号
        String sim = (String) SPUtils.get(context, Consts.SIM_SERIAL,"");
        if(!TextUtils.isEmpty(sim)){
            //获取当前的sim卡序列号
            String currentSim = AppUtils.getSimSerialNumber(context);
            if (currentSim.equals(sim)){
                Logs.d("手机安全");
            }else{
                Logs.d("sim卡有变化，需要发送短信！");
            }

        }

    }
}
