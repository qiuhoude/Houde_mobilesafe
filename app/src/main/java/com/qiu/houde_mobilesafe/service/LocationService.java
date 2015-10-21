package com.qiu.houde_mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.os.Bundle;

import com.qiu.houde_mobilesafe.utils.Consts;
import com.qiu.houde_mobilesafe.utils.SPUtils;


public class LocationService extends Service {

    private LocationManager lm;
    private MyLocationListener listener;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        // List<String> allProviders = lm.getAllProviders();// 获取所有位置提供者
        // System.out.println(allProviders);

        Criteria criteria = new Criteria();
        criteria.setCostAllowed(true);// 是否允许付费,比如使用3g网络定位
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String bestProvider = lm.getBestProvider(criteria, true);// 获取最佳位置提供者

        listener = new MyLocationListener();
        lm.requestLocationUpdates(bestProvider, 0, 0, listener);// 参1表示位置提供者,参2表示最短更新时间,参3表示最短更新距离
    }

    class MyLocationListener implements LocationListener {

        // 位置发生变化
        @Override
        public void onLocationChanged(Location location) {
            String lot = "" + location.getLongitude();//经度
            String lat = "" + location.getLatitude();//纬度
            String accuracy = "精确度:" + location.getAccuracy();
            String altitude = "海拔:" + location.getAltitude();

            SPUtils.put(getApplicationContext(), Consts.LOCATION, "lat:" + lat + "," + "lot:" + lot);
            stopSelf();//停掉service
        }

        // 位置提供者状态发生变化
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        // 用户打开gps
        @Override
        public void onProviderEnabled(String provider) {
        }

        // 用户关闭gps
        @Override
        public void onProviderDisabled(String provider) {
        }

    }

    @Override
    public void onDestroy() {
        //移除服务
        lm.removeUpdates(listener);
        super.onDestroy();
    }
}
