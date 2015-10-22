package com.qiu.houde_mobilesafe;

import android.test.InstrumentationTestCase;

import com.qiu.houde_mobilesafe.service.AddressService;
import com.qiu.houde_mobilesafe.utils.Logs;

/**
 * Created by Administrator on 2015/10/22.
 */
public class AppTest extends InstrumentationTestCase {

    public void test(){
//        boolean serviceRunning = ServiceStatusUtils.isServiceRunning(, );
       String name = AddressService.class.getName();
        System.out.print(name+"xxxx");
        Logs.d(name);
    }
}
