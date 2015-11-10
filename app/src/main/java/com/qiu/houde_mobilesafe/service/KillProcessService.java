package com.qiu.houde_mobilesafe.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import java.util.List;

public class KillProcessService extends Service {

    private LockScreenReceiver receiver;
    @Override
    public IBinder onBind(Intent intent) {
        throw null;
    }


    private class LockScreenReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //获取到进程管理器
            ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
            //获取到手机上面所以正在运行的进程
            List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : appProcesses) {
                activityManager.killBackgroundProcesses(runningAppProcessInfo.processName);
            }


        }
    }
    @Override
    public void onCreate() {
        super.onCreate();
        receiver = new LockScreenReceiver();
        //锁屏的过滤器,锁屏时后清理内存
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        //注册一个锁屏的广播
        registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        receiver = null;
    }
}
