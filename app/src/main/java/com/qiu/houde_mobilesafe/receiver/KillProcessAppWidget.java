package com.qiu.houde_mobilesafe.receiver;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import com.qiu.houde_mobilesafe.service.KillProcesWidgetService;
import com.qiu.houde_mobilesafe.utils.Logs;

/**
 * Created by Administrator on 2015/11/11.
 */
public class KillProcessAppWidget extends AppWidgetProvider {


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        //Logs.d("onReceive");

    }

    @Override
    public void onDisabled(Context context) {
        //当桌面上面所有的桌面小控件都删除的时候才调用
        super.onDisabled(context);
        //关闭服务
        Intent service = new Intent(context,KillProcesWidgetService.class);
        context.stopService(service);
        Logs.d("onDisabled");
    }


    @Override
    public void onEnabled(Context context) {
        //控件激活的时候调用
        super.onEnabled(context);
        //因为广播接受者的生命周期只有10秒,所以要调用服务才完成操作,开启服务器
        Intent service = new Intent(context,KillProcesWidgetService.class);
        context.startService(service);
        Logs.d("onEnabled");
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        //删除控件的时候调用
//        Logs.d("onDeleted");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
//        Logs.d("onUpdate");
        //每创建一个控件都会调用
    }


}
