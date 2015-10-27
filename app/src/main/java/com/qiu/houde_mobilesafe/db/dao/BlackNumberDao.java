package com.qiu.houde_mobilesafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.qiu.houde_mobilesafe.bean.BlackNumberInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/27.
 */
public class BlackNumberDao {

    public static final String TABLE_NAME = "blacknumber";
    private BlackNumberDBOpenHelper dbhelper;
    private Context mContext;

    public BlackNumberDao(Context context) {
        dbhelper = new BlackNumberDBOpenHelper(context);
        mContext = context;
    }

    /**
     * 增
     *
     * @param bean
     * @return
     */
    public boolean add(BlackNumberInfo bean) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("number", bean.getNumber());
        values.put("mode", bean.getMode());
        long rowid = db.insert(TABLE_NAME, null, values);
        db.close();
        if (rowid == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 根据号码删除
     *
     * @param number
     * @return
     */
    public boolean delete(String number) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        int row = db.delete(TABLE_NAME, "number=?", new String[]{number});
        db.close();
        if (row == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 通过电话号码修改拦截的模式
     *
     * @param number
     */
    public boolean changeNumberMode(String number, String mode) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("mode", mode);
        int rownumber = db.update(TABLE_NAME, values, "number=?", new String[]{number});
        db.close();
        if (rownumber == 0) {
            return false;
        } else {
            return true;
        }

    }

    public String findNumber(String number){
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{"mode"}, "number=?", new String[]{number}, null, null, null);
        String mode = "";
        if (cursor.moveToNext()){
            mode = cursor.getString(cursor.getColumnIndex("mode"));
        }
        cursor.close();
        db.close();
        return mode;
    }

    public List<BlackNumberInfo> findAll(){
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{"mode", "number"}, null, null, null, null, null);
        List<BlackNumberInfo> infos = new ArrayList<BlackNumberInfo>();
        while (cursor.moveToNext()){
            String mode = cursor.getString(cursor.getColumnIndex("mode"));
            String number = cursor.getString(cursor.getColumnIndex("number"));
            BlackNumberInfo info = new BlackNumberInfo(number,mode);
            infos.add(info);
        }
        cursor.close();
        db.close();
        return infos;
    }

}
