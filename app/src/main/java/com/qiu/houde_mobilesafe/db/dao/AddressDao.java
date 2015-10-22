package com.qiu.houde_mobilesafe.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.qiu.houde_mobilesafe.utils.Consts;

/**
 * Created by Administrator on 2015/10/22.
 */
public class AddressDao {


    public static String getAddress(Context context, String phoneNum) {
        String address = "未知号码";
        String path = context.getFilesDir().getAbsolutePath() + "/" + Consts.ADDRESS_DB;
        SQLiteDatabase database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);

        //手机号码
        if (phoneNum.matches("^1[3-8]\\d{9}$")) {
            Cursor cursor = database.rawQuery("select location from data2 where id=(select outkey from data1 where id=?)",
                    new String[]{phoneNum.substring(0, 7)});
            while (cursor.moveToNext()) {
                address = cursor.getString(cursor.getColumnIndex("location"));
            }
            cursor.close();
        } else if (phoneNum.matches("^\\d+")) {
            switch (phoneNum.length()) {
                case 3:
                    address = "报警电话";
                    break;
                case 4:
                    address = "模拟器";
                    break;
                case 5:
                    address = "客服电话";
                    break;
                case 7:
                case 8:
                    address = "本地电话";
                    break;
                default:
                    // 01088881234
                    // 048388888888
                    if (phoneNum.startsWith("0") && phoneNum.length() > 10) {// 有可能是长途电话
                        // 有些区号是4位,有些区号是3位(包括0)

                        // 先查询4位区号
                        Cursor cursor = database.rawQuery(
                                "select location from data2 where area =?",
                                new String[]{phoneNum.substring(1, 4)});

                        if (cursor.moveToNext()) {
                            address = cursor.getString(0);
                        } else {
                            cursor.close();

                            // 查询3位区号
                            cursor = database.rawQuery(
                                    "select location from data2 where area =?",
                                    new String[]{phoneNum.substring(1, 3)});

                            if (cursor.moveToNext()) {
                                address = cursor.getString(0);
                            }

                            cursor.close();
                        }
                    }
                    break;
            }
        }
        database.close();
        return address;
    }
}
