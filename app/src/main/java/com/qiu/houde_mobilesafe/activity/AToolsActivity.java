package com.qiu.houde_mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.qiu.houde_mobilesafe.R;

/**
 * Created by Administrator on 2015/10/21.
 */
public class AToolsActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);
    }

    /**归属地查询*/
    public void numberAddressQuery(View v){
        startActivity(new Intent(this, AddressActivity.class));
    }
}
