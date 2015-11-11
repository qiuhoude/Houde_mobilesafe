package com.qiu.houde_mobilesafe.service;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.text.format.Formatter;
import android.widget.RemoteViews;

import com.qiu.houde_mobilesafe.R;
import com.qiu.houde_mobilesafe.receiver.KillProcessAppWidget;
import com.qiu.houde_mobilesafe.utils.Logs;
import com.qiu.houde_mobilesafe.utils.SystemInfoUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class KillProcesWidgetService extends Service {

    private AppWidgetManager widgetManager;
    private ScheduledExecutorService executor;
    private Context mContext;

    private RemoteViews rv;

    private BroadcastReceiver offReceiver;

    /**
     * 定时更新数据
     */
    private class RefreshDateTask implements Runnable {
        @Override
        public void run() {
            //加载远程布局
            rv = new RemoteViews(mContext.getPackageName(), R.layout.process_widget);
            //获取当前运行进程数
            int processCount = SystemInfoUtils.getRunningProcessCount(mContext);
            rv.setTextViewText(R.id.process_count, "正在运行的软件:" + String.valueOf(processCount));
            //获取到当前手机上面的可用内存
            long availMem = SystemInfoUtils.getAvailRam(mContext);
            rv.setTextViewText(R.id.process_memory, "可用内存:" + Formatter.formatFileSize(getApplicationContext(), availMem));

            Logs.d("正在运行的软件:" + String.valueOf(processCount) + ", 可用内存:"
                    + Formatter.formatFileSize(getApplicationContext(), availMem));

            //按钮的点击
            //设置清理内存的广播
            Intent intent = new Intent();
            intent.setAction("com.qiu.houde_mobilesafe.killall");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            rv.setOnClickPendingIntent(R.id.btn_clear, PendingIntent.getBroadcast(mContext, 0, intent, 0));

            //第一个参数表示上下文
            //第二个参数表示当前有哪一个广播进行去处理当前的桌面小控件
            ComponentName provider = new ComponentName(getApplicationContext(), KillProcessAppWidget.class);

            widgetManager.updateAppWidget(provider, rv);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 解决耗电 锁屏去掉widget
     */
    private class InnerScreenReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                Logs.d("锁屏");
                stopWidgetUpdate();
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                Logs.d("开屏");
                startWidgetUpdate();
            }
        }


    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        widgetManager = AppWidgetManager.getInstance(getApplicationContext());
        rv = new RemoteViews(mContext.getPackageName(), R.layout.process_widget);
        startWidgetUpdate();

        //注册广播
        offReceiver = new InnerScreenReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(offReceiver, filter);

    }

    private void startWidgetUpdate() {
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(new RefreshDateTask(), 0, 5, TimeUnit.SECONDS);
    }

    private void stopWidgetUpdate() {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
            executor = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopWidgetUpdate();
        //关闭广播
        if (offReceiver != null) {
            unregisterReceiver(offReceiver);
        }
    }


}
