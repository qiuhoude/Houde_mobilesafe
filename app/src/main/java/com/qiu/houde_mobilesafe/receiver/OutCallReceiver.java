package com.qiu.houde_mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 来电用service监听
 * 去电用receive监听
 */
public class OutCallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String number = getResultData();

    }
}
