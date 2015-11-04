package com.qiu.houde_mobilesafe.service;

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
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsMessage;

import com.android.internal.telephony.ITelephony;
import com.qiu.houde_mobilesafe.db.dao.BlackNumberDao;
import com.qiu.houde_mobilesafe.utils.Logs;

import java.lang.reflect.Method;

public class CallSafeService extends Service {

    private BlackNumberDao dao;

    private InnerSmsReceiver receiver;

    private TelephonyManager mTm;

    private PhoneStateListener mPhoneListener;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dao = new BlackNumberDao(this);
        //用代码注册广播接收者
        receiver = new InnerSmsReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);//设置广播优先级
        registerReceiver(receiver, filter);

        //开启监听电话
        dao = new BlackNumberDao(getApplicationContext());
        mTm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        mTm.listen(mPhoneListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE:// 空闲状态，没有通话没有响铃
                        Logs.d("空闲状态，没有通话没有响铃");

                        break;
                    case TelephonyManager.CALL_STATE_RINGING:// 响铃状态
                        Logs.d("响铃状态");
                        String mode = dao.findNumber(incomingNumber);
                        if ("1".equals(mode) || "3".equals(mode)) {
                            Logs.d("黑名单的电话号码，马上挂断");
                            //挂断电话
                            endCall();
                            //删除记录
                            deleteCalllog(incomingNumber);
                        }
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:// 通话状态
                        Logs.d("通话状态");
                        break;
                }
                super.onCallStateChanged(state, incomingNumber);
            }
        }, PhoneStateListener.LISTEN_CALL_STATE);//监听手机通话状态的变化
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
        mTm.listen(mPhoneListener, PhoneStateListener.LISTEN_NONE); //停止来电监听
    }


    private class InnerSmsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //接收短信
            Object[] objs = (Object[]) intent.getExtras().get("pdus");
            for (Object obj : objs) {
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) obj);
                String sender = sms.getOriginatingAddress();//得到一个电话号码
                //看一看这个电话号码是否是黑名单里面的
                String mode = dao.findNumber(sender);
                if ("2".equals(mode) || "3".equals(mode)) {
                    abortBroadcast();//把这个广播终止掉
                }
            }
        }
    }

    /**
     * 开机过程其实就是各种服务加载的过程。
     * <p/>
     * getSystemService()得到的服务把相关API隐藏了，只提供常用的方法，那么想要得到原生的TelephonyManager的方法就得绕开使用这个方法；
     */
    private void endCall() {
//		ServiceManager.getService(TELEPHONY_SERVICE);
        try {
            //得到ServiceManager的字节码
            Class clazz = CallSafeService.class.getClassLoader().loadClass("android.os.ServiceManager");
            //得到字节码的方法
            Method method = clazz.getDeclaredMethod("getService", String.class);
            //调用方法得到远程服务代理类
            IBinder b = (IBinder) method.invoke(null, TELEPHONY_SERVICE);
            //获取到原生未经包装的系统电话的管理服务
            ITelephony telephony = ITelephony.Stub.asInterface(b);
            telephony.endCall();//挂断电话
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 利用内容观察者和内容提供者删除呼叫记录
     * 需要权限
     * <uses-permission android:name="android.permission.READ_CALL_LOG"/>
     * <uses-permission android:name="android.permission.WRITE_CALL_LOG"/>
     * 解释删除有时成功，有时不成功的情况；
     * 立刻把电话挂断了，但呼叫的生成并不是同步的代码；它是一个异步的代码。
     * 用观察者去监听日志产生后再去删除
     */
    private void deleteCalllog(String incomingNumber) {
        ContentResolver resolver = getContentResolver();
        Uri url = Uri.parse("content://call_log/calls");
        resolver.delete(url, "number=?", new String[]{incomingNumber});
        getContentResolver().registerContentObserver(url, true, new MyContentObserver(new Handler(), incomingNumber));

    }


    /**
     * 自定义内容观察者
     * Android2.3模拟器上需要多加权限
     * <p/>
     * <uses-permission android:name="android.permission.WRITE_CONTACTS"/>
     */
    private class MyContentObserver extends ContentObserver {
        private String incomingNumber;

        public MyContentObserver(Handler handler, String incomingNumber) {
            super(handler);
            this.incomingNumber = incomingNumber;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            //删除呼叫记录
            deleteCalllog(incomingNumber);
            //取消注册内容观察者
            getContentResolver().unregisterContentObserver(this);
        }
    }

}
