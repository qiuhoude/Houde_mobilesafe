package com.qiu.houde_mobilesafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.qiu.houde_mobilesafe.bean.BlackNumberInfo;
import com.qiu.houde_mobilesafe.db.BlackNumberDBOpenHelper;

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

    /**
     * 分页加载数据
     *
     * @param pageNumber 表示当前是哪一页
     * @param pageSize   表示每一页有多少条数据
     * @return limit 表示限制当前有多少数据
     * offset 表示跳过 从第几条开始
     */
    public List<BlackNumberInfo> findPar(int pageNumber, int pageSize) {
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select number,mode from blacknumber limit ? offset ?", new String[]{String.valueOf(pageSize),
                String.valueOf(pageSize * pageNumber)});
        List<BlackNumberInfo> blackNumberInfos = new ArrayList<BlackNumberInfo>();
        while (cursor.moveToNext()) {
            BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
            blackNumberInfo.setMode(cursor.getString(1));
            blackNumberInfo.setNumber(cursor.getString(0));
            blackNumberInfos.add(blackNumberInfo);
        }
        cursor.close();
        db.close();
        return blackNumberInfos;
    }

    /**
     * 分批加载数据
     * @param startIndex  开始的位置
     * @param maxCount    每页展示的最大的条目
     * @return
     */
    public List<BlackNumberInfo> findPar2(int startIndex, int maxCount) {
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select number,mode from blacknumber  order by _id desc limit ? offset ?", new String[]{String.valueOf(maxCount),
                String.valueOf(startIndex)});
        List<BlackNumberInfo> blackNumberInfos = new ArrayList<BlackNumberInfo>();
        while (cursor.moveToNext()) {
            BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
            blackNumberInfo.setMode(cursor.getString(1));
            blackNumberInfo.setNumber(cursor.getString(0));
            blackNumberInfos.add(0,blackNumberInfo);
        }
        cursor.close();
        db.close();
        return blackNumberInfos;
    }




    /**
     * 获取总的记录数
     * @return
     */
    public int getTotalNumber(){
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from blacknumber", null);
        cursor.moveToNext();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count;
    }

}
