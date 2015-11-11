package com.qiu.houde_mobilesafe.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.qiu.houde_mobilesafe.utils.Toasts;

import java.util.List;

public class KillProcessAllReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //获取到进程管理器
        ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        //获取到手机上面所以正在运行的进程
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : appProcesses) {
            activityManager.killBackgroundProcesses(runningAppProcessInfo.processName);
        }
        //立刻刷新界面

        Toasts.showShort(context,"清理完毕");
    }
}
