package com.qiu.houde_mobilesafe.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;

import com.qiu.houde_mobilesafe.activity.EnterPwdActivity;
import com.qiu.houde_mobilesafe.db.dao.AppLockDao;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WatchDogService extends Service {

    private ActivityManager mAm;
    private AppLockDao dao;
    private List<String> appLockInfos;
    private WatchDogReceiver receiver;
    //临时停止保护的包名
    private String tempStopPackageName ="";
    private Context mContext;
    private ScheduledExecutorService executor;
    public static final String PACKAGE_NAME = "packageName";

    class WatchDogReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.qiu.mobile.stoptask")){
                //停止保护当前进程
                tempStopPackageName = intent.getStringExtra(PACKAGE_NAME);

            }else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
                startWatDog();
            }else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
                stopWatchDog();
            }
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class AppLockContentObserver extends ContentObserver {


        public AppLockContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            appLockInfos = dao.findAll();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        dao = new AppLockDao(mContext);
        //获取所以需要加锁的报名
        appLockInfos = dao.findAll();
        //注册广播接受者
        ContentResolver resolver = getContentResolver();
        resolver.registerContentObserver(Uri.parse(AppLockDao.URI_CHANGE),true,
                new AppLockContentObserver(new Handler()));


        //获取activity管理器
        mAm = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        //注册广播接受者
        receiver = new WatchDogReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.qiu.mobile.stoptask");
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(receiver,intentFilter);
        startWatDog();
    }

    /*5.0之后失效
       //由于这个狗一直在后台运行。为了避免程序阻塞。
                    //获取到当前正在运行的任务栈
                    List<ActivityManager.RunningTaskInfo> tasks = mAm.getRunningTasks(1);
                    //获取到最上面的进程
                    ActivityManager.RunningTaskInfo taskInfo = tasks.get(0);
                    //获取到最顶端应用程序的包名
                    String packageName = taskInfo.topActivity.getPackageName();
                    */

    class MyTask implements Runnable {



        @Override
        public void run() {

            List<ActivityManager.RunningAppProcessInfo> processes = mAm.getRunningAppProcesses();
            ActivityManager.RunningAppProcessInfo process = processes.get(0);
            String packageName = getPackageManager().getPackagesForUid(process.uid)[0];
            if (appLockInfos.contains(packageName)){
                if (!packageName.equals(tempStopPackageName)){
                    Intent intent = new Intent(WatchDogService.this,EnterPwdActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(PACKAGE_NAME,packageName);
                    startActivity(intent);
                }
             }

        }
    }

    private void startWatDog() {
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(new MyTask(),0,1, TimeUnit.SECONDS);
    }

    private void stopWatchDog(){
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
            executor = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopWatchDog();
        unregisterReceiver(receiver);
    }
}
