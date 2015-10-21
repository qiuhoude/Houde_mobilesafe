package com.qiu.houde_mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.qiu.houde_mobilesafe.R;
import com.qiu.houde_mobilesafe.utils.Consts;
import com.qiu.houde_mobilesafe.utils.SPUtils;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * 第4个设置向导页
 *
 * @author Kevin
 */
public class Setup4Activity extends BaseSetupActivity {

    @Bind(R.id.cc_setup)
    CheckBox ccSetup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        ButterKnife.bind(this);

        boolean protect = (Boolean) SPUtils.get(getApplicationContext(), "protect", false);
        // 根据sp保存的状态,更新checkbox
        if (protect) {
            ccSetup.setText("防盗保护已经开启");
            ccSetup.setChecked(true);
        } else {
            ccSetup.setText("防盗保护没有开启");
            ccSetup.setChecked(false);
        }

        ccSetup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ccSetup.setText("防盗保护已经开启");
                    SPUtils.put(getApplicationContext(), Consts.PROTECT, true);
                } else {
                    ccSetup.setText("防盗保护没有开启");
                    SPUtils.put(getApplicationContext(), Consts.PROTECT, false);
                }
            }
        });
    }

    @Override
    protected void showPreviousPage() {

        startActivity(new Intent(this, Setup3Activity.class));
        finish();
    }

    @Override
    protected void showNextPage() {
        startActivity(new Intent(this, LostFindActivity.class));
        finish();
        SPUtils.put(getApplicationContext(), Consts.CONFIGED, true);// 更新sp,表示已经展示过设置向导了,下次进来就不展示啦
    }

}
