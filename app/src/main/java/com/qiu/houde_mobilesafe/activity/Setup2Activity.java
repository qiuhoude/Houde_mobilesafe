package com.qiu.houde_mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.qiu.houde_mobilesafe.R;
import com.qiu.houde_mobilesafe.utils.AppUtils;
import com.qiu.houde_mobilesafe.utils.Consts;
import com.qiu.houde_mobilesafe.utils.SPUtils;
import com.qiu.houde_mobilesafe.view.SettingItemView;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * 第2个设置向导页
 *
 * @author Kevin
 */
public class Setup2Activity extends BaseSetupActivity {

    @Bind(R.id.siv_sim)
    SettingItemView sivSim;
    @Bind(R.id.imageView1)
    ImageView imageView1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        ButterKnife.bind(this);
        //设置状态
        boolean isBind_sim = false;

        if (SPUtils.contains(getApplicationContext(), Consts.SIM_SERIAL)) {
            isBind_sim = true;
        }
        sivSim.setChecked(isBind_sim);

        sivSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 判断当前的勾选状态
                if (sivSim.isChecked()) {
                    // 设置不勾选
                    sivSim.setChecked(false);
                    SPUtils.remove(getApplicationContext(), Consts.SIM_SERIAL);
                } else {
                    sivSim.setChecked(true);
                    //保存sim serial码
                    String sim = AppUtils.getSimSerialNumber(getApplicationContext());
                    SPUtils.put(getApplicationContext(), Consts.SIM_SERIAL, sim);
                }
            }
        });

    }

    @Override
    protected void showPreviousPage() {
        startActivity(new Intent(this, Setup1Activity.class));
        finish();
    }

    @Override
    protected void showNextPage() {
        startActivity(new Intent(this, Setup3Activity.class));
        finish();

    }

}
