package com.qiu.houde_mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.qiu.houde_mobilesafe.R;
import com.qiu.houde_mobilesafe.db.dao.AddressDao;
import com.qiu.houde_mobilesafe.utils.Consts;
import com.qiu.houde_mobilesafe.utils.Logs;
import com.qiu.houde_mobilesafe.utils.SPUtils;

public class AddressService extends Service {

    private TelephonyManager mTm;
    private PhoneStateListener mPhoneListener;
    private OutCallReceiver mOutCallReceiver;

    private WindowManager mWm;

    private View mToastView;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     *
     */
    class OutCallReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String number = getResultData();
            String address = AddressDao.getAddress(context, number);
            showToast(address);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerOutCallReceiver();
        mTm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        mTm.listen(mPhoneListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE:// 空闲状态，没有通话没有响铃
                        Logs.d("空闲状态，没有通话没有响铃");
                        //删除view
                        if (mWm != null && mToastView != null) {
                            mWm.removeView(mToastView);
                        }
                        break;
                    case TelephonyManager.CALL_STATE_RINGING:// 响铃状态
                        Logs.d("响铃状态");
                        String address = AddressDao.getAddress(getApplicationContext(), incomingNumber);
//                        Toasts.showShort(getApplicationContext(), address);
                        showToast(address);
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
        mTm.listen(mPhoneListener, PhoneStateListener.LISTEN_NONE); //停止来电监听
        unregisterOutCallReceiver();
    }

    /**
     * 注册广播
     */
    private void registerOutCallReceiver() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
        mOutCallReceiver = new OutCallReceiver();
        registerReceiver(mOutCallReceiver, filter);
    }

    /**
     * 注销广播
     */
    private void unregisterOutCallReceiver() {
        if (mOutCallReceiver != null) {
            unregisterReceiver(mOutCallReceiver);
        }
    }

    private int startX;
    private int startY;

    private void showToast(String address) {
        mWm = (WindowManager) getSystemService(WINDOW_SERVICE);

        //父布局
        final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
        final WindowManager.LayoutParams params = mParams;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.setTitle("Toast");
//        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
//                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;


        mToastView = View.inflate(this, R.layout.toast_address, null);
        int style = (int) SPUtils.get(this, Consts.ADDRESS_STYLE, 0);
        mToastView.setBackgroundResource(Consts.ADRESS_STYLE_IMGS[style]);
        TextView tv = (TextView) mToastView.findViewById(R.id.tv_toast_number);
        tv.setText(address);
        //设置初始位置
        if (SPUtils.contains(this, Consts.LAST_X)) {
            int lastX = (int) SPUtils.get(this, Consts.LAST_X, 0);
            int lastY = (int) SPUtils.get(this, Consts.LAST_Y, 0);

            mParams.gravity = Gravity.TOP | Gravity.LEFT;
            mParams.x = lastX;
            mParams.y = lastY;
        }
        //设置View监听
        mToastView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int currentX = (int) event.getRawX();
                int currentY = (int) event.getRawY();

                Logs.d("currentX = " + currentX + ", currentY = " + currentY);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = currentX;
                        startY = currentY;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //偏移量
                        int dx = currentX - startX;
                        int dy = currentY - startY;

                        mParams.gravity = Gravity.TOP | Gravity.LEFT;
                        mParams.x = currentX ;
                        mParams.y = currentY ;


                        //重新初始化起点
                        mWm.updateViewLayout(mToastView, mParams);
                        break;
                    case MotionEvent.ACTION_UP:

                        break;
                }
                return true;
            }
        });

        mWm.addView(mToastView, params);


    }
}
