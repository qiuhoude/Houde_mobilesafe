package com.qiu.houde_mobilesafe.utils;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/11/9.
 */
public class SystemInfoUtils {

    /**
     * 获取正在运行中进程的总个数
     *
     * @param context
     * @return
     */
    public static int getRunningProcessCount(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        return am.getRunningAppProcesses().size();
    }


    /**
     * 获取手机可用的内存信息ram
     *
     * @param context
     * @return
     */
    public static long getAvailRam(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(outInfo);
        return outInfo.availMem;//byte 为单位的long类型的可用内存大小
    }


    /**
     * 获取手机可用的总内存信息ram
     * api 16 之后才能使用这个
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static long getTotalRam(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(outInfo);
        return outInfo.totalMem;
    }


    /**
     * 获取手机可用的总内存信息ram
     * 兼容低版本
     *
     * @param context
     * @return
     */
    public static long getTotalRamSupport(Context context) {
        long total = 0L;
        FileInputStream fis = null;
        BufferedReader bufferedReader = null;
        try {
            File file = new File("/proc/meminfo");
            fis = new FileInputStream(file);
            bufferedReader = new BufferedReader(new InputStreamReader(fis));
            //读取第一行数据就ok了
            // MemTotal:        1028632 kB
            String line = bufferedReader.readLine();
            Logs.d(line);
            //用正则取数据
            String regexp = "(\\d+)";
            Pattern pattern = Pattern.compile(regexp);
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                String s = matcher.group(1);
                Logs.d(s);
                total = Long.parseLong(s) * 1024L;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return total;

    }

}
