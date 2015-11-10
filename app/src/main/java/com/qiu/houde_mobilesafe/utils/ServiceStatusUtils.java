package com.qiu.houde_mobilesafe.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by Administrator on 2015/10/22.
 */
public class ServiceStatusUtils {


    /**
     * 检测服务是否正在运行
     *
     * @return
     */
    public static boolean isServiceRunning(Context ctx, String serviceName) {

        ActivityManager am = (ActivityManager) ctx
                .getSystemService(Context.ACTIVITY_SERVICE);
        //拿到100个运行中的服务
        List<ActivityManager.RunningServiceInfo> runingServices = am.getRunningServices(200);
        for(ActivityManager.RunningServiceInfo runinfo : runingServices){
            String className = runinfo.service.getClassName();
            if (className.equals(serviceName)){
                //服务存在
                return true;
            }
        }

        return false;
    }
}
