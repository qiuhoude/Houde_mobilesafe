package com.qiu.houde_mobilesafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.qiu.houde_mobilesafe.db.AppLockOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/12.
 */
public class AppLockDao {

    public static final String URI_CHANGE = "content://com.qiu.mobile.watchdog.change";
    private AppLockOpenHelper helper;
    private Context mContext;
    public AppLockDao(Context context) {
        mContext = context;
        helper = new AppLockOpenHelper(context);
    }

    /**
     * 添加到程序所里面
     *
     * @param packageName
     *            包名
     */
    public void add(String packageName) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("packagename", packageName);
        db.insert("info", null, values);
        db.close();
        mContext.getContentResolver().notifyChange(Uri.parse(URI_CHANGE), null);

    }

    /**
     * 从程序锁里面删除当前的包
     *
     * @param packageName
     */
    public void delete(String packageName) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("info", "packagename=?", new String[] { packageName });
        db.close();
        //自定义内容观察者
        mContext.getContentResolver().notifyChange(Uri.parse(URI_CHANGE),null);
    }
    /**
     * 查询当前的包是否在程序锁里面
     * @param packageName
     * @return
     */
    public boolean find(String packageName) {
        boolean result = false;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("info", null, "packagename=?",
                new String[] { packageName }, null, null, null);
        if (cursor.moveToNext()) {
            result = true;
        }
        cursor.close();
        db.close();
        return result;

    }
    /**
     * 查询全部的锁定的包名
     * @return
     */
    public List<String> findAll(){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("info", new String[]{"packagename"}, null, null, null, null, null);
        List<String> packnames = new ArrayList<String>();
        while(cursor.moveToNext()){
            packnames.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return packnames;
    }
}
