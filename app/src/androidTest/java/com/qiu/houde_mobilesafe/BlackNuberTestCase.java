package com.qiu.houde_mobilesafe;

import android.content.Context;
import android.test.AndroidTestCase;

import com.qiu.houde_mobilesafe.bean.BlackNumberInfo;
import com.qiu.houde_mobilesafe.db.dao.BlackNumberDao;

import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2015/10/27.
 */
public class BlackNuberTestCase extends AndroidTestCase {

    private Context mContext;

    @Override
    protected void setUp() throws Exception {
        mContext = getContext();
        super.setUp();
    }

    public void testAdd() {
        BlackNumberDao dao = new BlackNumberDao(mContext);
        Random random = new Random();
        for (int i = 0; i < 200; i++) {
            Long number = 13300000000l + i;
            BlackNumberInfo bean = new BlackNumberInfo(number + "", String.valueOf(random.nextInt(3) + 1));
            dao.add(bean);
        }
    }

    public void testDelete(){
        BlackNumberDao dao = new BlackNumberDao(mContext);
        boolean delete = dao.delete("13300000000");
        assertEquals(true,delete);
    }

    public void testFind(){
        BlackNumberDao dao = new BlackNumberDao(mContext);
        String number = dao.findNumber("13300000004");
        System.out.println(number);
    }

    public void testFindAll(){
        BlackNumberDao dao = new BlackNumberDao(mContext);
        List<BlackNumberInfo> blackNumberInfos = dao.findAll();
        for (BlackNumberInfo blackNumberInfo : blackNumberInfos) {
            System.out.println(blackNumberInfo.getMode() + "" + blackNumberInfo.getNumber());
        }
    }
}
