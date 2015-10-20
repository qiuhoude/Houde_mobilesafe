package com.qiu.houde_mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.os.Bundle;
import com.qiu.houde_mobilesafe.R;
import com.qiu.houde_mobilesafe.utils.Consts;

/**
 * Created by Administrator on 2015/10/19.
 */
public class LostFindActivity extends Activity {
    private SharedPreferences mPrefs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPrefs = getSharedPreferences(Consts.CONFIG, MODE_PRIVATE);

        boolean configed = mPrefs.getBoolean(Consts.CONFIGED, false);// 判断是否进入过设置向导
        if (configed) {
            setContentView(R.layout.activity_lost_find);
        } else {
            // 跳转设置向导页
            startActivity(new Intent(this, Setup1Activity.class));
            finish();
        }
    }

    /**
     * 重新进入设置向导
     *
     * @param view
     */
    public void reEnter(View view) {
        startActivity(new Intent(this, Setup1Activity.class));
        finish();
    }
}
