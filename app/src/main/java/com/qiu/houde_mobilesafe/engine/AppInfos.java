package com.qiu.houde_mobilesafe.engine;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug.MemoryInfo;

import com.qiu.houde_mobilesafe.R;
import com.qiu.houde_mobilesafe.bean.AppInfo;
import com.qiu.houde_mobilesafe.bean.TaskInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/3.
 */
public class AppInfos {
    /**
     * 获取全部应用的信息
     *
     * @param context
     * @return
     */
    public static List<AppInfo> getAppInfos(Context context) {

        List<AppInfo> packageAppInfos = new ArrayList<AppInfo>();

        //获取到包的管理者
        PackageManager packageManager = context.getPackageManager();
        //获取到安装包
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);

        for (PackageInfo installedPackage : installedPackages) {

            AppInfo appInfo = new AppInfo();

            //获取到应用程序的图标
            Drawable drawable = installedPackage.applicationInfo.loadIcon(packageManager);

            appInfo.setIcon(drawable);

            //获取到应用程序的名字
            String apkName = installedPackage.applicationInfo.loadLabel(packageManager).toString();

            appInfo.setApkName(apkName);

            //获取到应用程序的包名
            String packageName = installedPackage.packageName;

            appInfo.setApkPackageName(packageName);

            //获取到apk资源的路径
            String sourceDir = installedPackage.applicationInfo.sourceDir;

            File file = new File(sourceDir);
            //apk的长度
            long apkSize = file.length();

            appInfo.setApkSize(apkSize);

//            Logs.d("---------------------------");
//            Logs.d("程序的名字:" + apkName);
//            Logs.d("程序的包名:" + packageName);
//            Logs.d("程序的大小:" + apkSize);

            //获取到安装应用程序的标记
            int flags = installedPackage.applicationInfo.flags;

            if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                //表示系统app
                appInfo.setUserApp(false);
            } else {
                //表示用户app
                appInfo.setUserApp(true);
            }

            if ((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0) {
                //表示在sd卡
                appInfo.setIsRom(false);
            } else {
                //表示内存
                appInfo.setIsRom(true);
            }

            packageAppInfos.add(appInfo);
        }

        return packageAppInfos;
    }


    /**
     * 得到系统正在运行的进程信息
     *
     * @param context
     * @return
     */
    public static List<TaskInfo> getTaskInfos(Context context) {
        List<TaskInfo> infos = new ArrayList<TaskInfo>();
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        // 得到包管理器
        PackageManager pm = context.getPackageManager();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {

            TaskInfo taskInfo = new TaskInfo();
            // 注意导包android.os.Debug.MemoryInfo
            MemoryInfo[] memoryInfo = am
                    .getProcessMemoryInfo(new int[]{info.pid});
            //进程在虚拟机的大小
            // memoryInfo[1].dalvikPrivateDirty;
            //进程在本地运行的大小-jni调用的C代码运行的大小
            // memoryInfo[1].nativePrivateDirty;
            //获取占用内存ram大小 单位是kb
            int memory = memoryInfo[0].getTotalPrivateDirty();
            // 内存占用大小
            long memorySize = memory * 1024L;
            taskInfo.setMemsize(memorySize);
            // 包名、进程名
            String packageName = info.processName;
            taskInfo.setPackageName(packageName);

            PackageInfo packInfo = null;
            try {
                packInfo = pm.getPackageInfo(packageName, 0);
                // 图标
                Drawable icon = packInfo.applicationInfo.loadIcon(pm);
                taskInfo.setIcon(icon);
                // 软件名称
                String name = packInfo.applicationInfo.loadLabel(pm).toString();
                taskInfo.setName(name);
                int falgs = packInfo.applicationInfo.flags;

                if ((falgs & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    // 用户进程
                    taskInfo.setUserTask(true);
                } else {
                    // 系统进程
                    taskInfo.setUserTask(false);
                }
                if (!packageName.equals(context.getPackageName())){
                    taskInfo.setChecked(true);
                }else {
                    taskInfo.setChecked(false);
                }
            } catch (PackageManager.NameNotFoundException e) {
                //e.printStackTrace();
                //android有些系统进程名是找不到名字和图标的
                taskInfo.setName(packageName);
                taskInfo.setIcon(context.getResources().getDrawable(
                        R.mipmap.ic_launcher));
                taskInfo.setUserTask(false);
            }

            infos.add(taskInfo);
        }
        return infos;
    }

}
