package com.qiu.houde_mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;

import com.qiu.houde_mobilesafe.R;
import com.qiu.houde_mobilesafe.service.LocationService;
import com.qiu.houde_mobilesafe.utils.Consts;
import com.qiu.houde_mobilesafe.utils.Logs;
import com.qiu.houde_mobilesafe.utils.SPUtils;

public class SmsReceiver extends BroadcastReceiver {
    public SmsReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //读取短信
        Object[] objects = (Object[]) intent.getExtras().get("pdus");

        for (Object object : objects) {// 短信最多140字节,
            // 超出的话,会分为多条短信发送,所以是一个数组,因为我们的短信指令很短,所以for循环只执行一次
            SmsMessage message = SmsMessage.createFromPdu((byte[]) object);
            String originatingAddress = message.getOriginatingAddress();// 短信来源号码
            String messageBody = message.getMessageBody();// 短信内容

            Logs.d(originatingAddress + ":" + messageBody);

            if ("#*alarm*#".equals(messageBody)) {
                // 播放报警音乐, 即使手机调为静音,也能播放音乐, 因为使用的是媒体声音的通道,和铃声无关
                MediaPlayer player = MediaPlayer.create(context, R.raw.alarm);
                player.setVolume(1f, 1f);
                player.setLooping(true);
                player.start();
            } else if ("#*location*#".equals(messageBody)) {
                // 获取经纬度坐标
                context.startService(new Intent(context, LocationService.class));// 开启定位服务

                String location = (String) SPUtils.get(context, Consts.LOCATION, "getting location...");

                Logs.d("location:" + location);

            } else if ("#*wipedata*#".equals(messageBody)) {
                Logs.d("远程清除数据");
            } else if ("#*lockscreen*#".equals(messageBody)) {
                Logs.d("远程锁屏");
            }
            abortBroadcast();// 中断短信的传递, 从而系统短信app就收不到内容了
        }

    }
}
