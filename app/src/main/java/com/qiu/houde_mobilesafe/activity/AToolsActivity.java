package com.qiu.houde_mobilesafe.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.qiu.houde_mobilesafe.R;
import com.qiu.houde_mobilesafe.utils.Logs;
import com.qiu.houde_mobilesafe.utils.SmsUtils;
import com.qiu.houde_mobilesafe.utils.Toasts;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2015/10/21.
 */
public class AToolsActivity extends Activity {



    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);
        ButterKnife.bind(this);
    }

    /**
     * 归属地查询
     */
    public void numberAddressQuery(View v) {
        startActivity(new Intent(this, AddressActivity.class));
    }


    /**
     * 备份短信
     *
     * @param view
     */
    public void backUpsms(View view) {
        //初始化一个进度条的对话框
        pd = new ProgressDialog(AToolsActivity.this);
        pd.setTitle("提示");
        pd.setMessage("稍安勿躁。正在备份。你等着吧。。");
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.show();
        new Thread() {
            public void run() {
                boolean result = SmsUtils.backUp(AToolsActivity.this, new SmsUtils.BackUpCallBackSms() {

                    @Override
                    public void onBackUpSms(int process) {
                        pd.setProgress(process);

                    }

                    @Override
                    public void befor(int count) {
                        pd.setMax(count);
                    }
                });
                Logs.d("dismiss");
                pd.dismiss();
                if (result) {
                    //安全弹吐司的方法
                    Toasts.showNoUIThread(AToolsActivity.this, "备份成功", 0);
                } else {
                    Toasts.showNoUIThread(AToolsActivity.this, "备份失败", 0);
                }

            }
        }.start();
    }
}
