package com.qiu.houde_mobilesafe.utils;

import com.qiu.houde_mobilesafe.R;

/**
 * Created by Administrator on 2015/10/20.
 */
public class Consts {

    public static final String CONFIG = "config";
    public static final String SIM_SERIAL = "sim_serial";
    public static final String PASSWORD = "password";
    public static final String CONFIGED = "configed";
    public static final String SAFE_PHONE = "safe_phone";
    public static final String PROTECT = "protect";
    public static final String LOCATION = "location";
    public static final String ADDRESS_DB = "address.db";
    public static final String AUTO_UPDATE = "auto_update";
    public static final String ADDRESS_STYLE = "address_style";

    //风格
    public static final String[] ADRESS_STYLE_ITEMS = new String[]{"半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿"};
    public static final int[] ADRESS_STYLE_IMGS = new int[]{R.drawable.call_locate_white,
            R.drawable.call_locate_orange, R.drawable.call_locate_blue,
            R.drawable.call_locate_gray, R.drawable.call_locate_green};

    public static final String LAST_X = "lastX";
    public static final String LAST_Y = "lastY";
    public static final String SAFE_DB = "safe.db";
}
