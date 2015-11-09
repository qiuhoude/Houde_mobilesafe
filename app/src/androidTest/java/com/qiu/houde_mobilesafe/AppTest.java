package com.qiu.houde_mobilesafe;

import android.content.Context;
import android.test.AndroidTestCase;
import android.text.format.Formatter;

import com.qiu.houde_mobilesafe.bean.TaskInfo;
import com.qiu.houde_mobilesafe.engine.AppInfos;
import com.qiu.houde_mobilesafe.service.AddressService;
import com.qiu.houde_mobilesafe.utils.Logs;
import com.qiu.houde_mobilesafe.utils.SystemInfoUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/10/22.
 */
public class AppTest extends AndroidTestCase {

    private Context mContext;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.mContext = getContext();
    }

    public void test() {
//        boolean serviceRunning = ServiceStatusUtils.isServiceRunning(, );
        String name = AddressService.class.getName();
        System.out.print(name + "xxxx");
        Logs.d(name);
    }

    public void testRegexp() {
        Logs.d("开始正则");
        String regexp = "(\\d+)";
        String ss = "MemTotal        1028632 kB";
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(ss);
        while (matcher.find()) {
            String s = matcher.group(1);
            Logs.d(Formatter.formatFileSize(mContext, Long.parseLong(s) * 1024));
        }
    }


    public void testTaskInfo() {
        long count = SystemInfoUtils.getRunningProcessCount(mContext);
        long avail = SystemInfoUtils.getAvailRam(mContext);
        long total1 = SystemInfoUtils.getTotalRam(mContext);
        long total2 = SystemInfoUtils.getTotalRamSupport(mContext);

        Logs.d("count " + count);
        Logs.d("avail " + Formatter.formatFileSize(mContext, avail));
        Logs.d("total1 " + Formatter.formatFileSize(mContext, total1));
        Logs.d("total2 " + Formatter.formatFileSize(mContext, total2));
    }

    public void testTaskInfo2() {
        List<TaskInfo> taskInfos = AppInfos.getTaskInfos(mContext);
        for (TaskInfo i : taskInfos) {
            Logs.d(i.toString());
        }
    }

}
