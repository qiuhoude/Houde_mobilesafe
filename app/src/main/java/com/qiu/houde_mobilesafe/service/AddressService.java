package com.qiu.houde_mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.qiu.houde_mobilesafe.db.dao.AddressDao;
import com.qiu.houde_mobilesafe.utils.Logs;
import com.qiu.houde_mobilesafe.utils.Toasts;

public class AddressService extends Service {

    private TelephonyManager mTm;
    private PhoneStateListener mPhoneListener;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mTm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        mTm.listen(mPhoneListener = new PhoneStateListener(){
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state){
                    case TelephonyManager.CALL_STATE_IDLE:// 空闲状态，没有通话没有响铃
                        Logs.d("空闲状态，没有通话没有响铃");
                        break;
                    case TelephonyManager.CALL_STATE_RINGING:// 响铃状态
                        Logs.d("响铃状态");
                        String address = AddressDao.getAddress(getApplicationContext(),incomingNumber);
                        Toasts.showShort(getApplicationContext(),address);
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:// 通话状态
                        Logs.d("通话状态");
                        break;
                }

                super.onCallStateChanged(state, incomingNumber);
            }
        },PhoneStateListener.LISTEN_CALL_STATE);//监听手机通话状态的变化
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        mTm.listen(mPhoneListener,PhoneStateListener.LISTEN_NONE); //停止来电监听
    }
}
