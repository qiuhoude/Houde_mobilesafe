package com.qiu.houde_mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.qiu.houde_mobilesafe.R;
import com.qiu.houde_mobilesafe.service.WatchDogService;
import com.qiu.houde_mobilesafe.utils.Logs;
import com.qiu.houde_mobilesafe.utils.Toasts;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2015/11/12.
 */
public class EnterPwdActivity extends Activity {


    @Bind(R.id.et_pwd)
    EditText etPwd;
    @Bind(R.id.bt_ok)
    Button btOk;
    @Bind(R.id.bt_1)
    Button bt1;
    @Bind(R.id.bt_2)
    Button bt2;
    @Bind(R.id.bt_3)
    Button bt3;
    @Bind(R.id.bt_4)
    Button bt4;
    @Bind(R.id.bt_5)
    Button bt5;
    @Bind(R.id.bt_6)
    Button bt6;
    @Bind(R.id.bt_7)
    Button bt7;
    @Bind(R.id.bt_8)
    Button bt8;
    @Bind(R.id.bt_9)
    Button bt9;
    @Bind(R.id.bt_clean_all)
    Button btCleanAll;
    @Bind(R.id.bt_0)
    Button bt0;
    @Bind(R.id.bt_delete)
    Button btDelete;

    private String packageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pwd);
        ButterKnife.bind(this);
        packageName  = getIntent().getStringExtra(WatchDogService.PACKAGE_NAME);

        initView();
    }

    private void initView() {
        etPwd.setFocusable(false);
        //清空
        btCleanAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPwd.setText("");
            }
        });
        //删除
        btDelete.setOnClickListener(new View.OnClickListener() {
            private String str;

            @Override
            public void onClick(View v) {
                str = etPwd.getText().toString();
                if (str.length() == 0) {
                    return;
                }
                str=str.substring(0, str.length() - 1);
                etPwd.setText(str);
                etPwd.setSelection(str.length());
            }
        });
        //ok
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = etPwd.getText().toString();
                if ("123".equals(result)) {
                    Logs.d("密码输入正确");
                    Intent intent = new Intent();
                    // 发送广播。停止保护
                    intent.setAction("com.qiu.mobile.stoptask");
                    // 跟狗说。现在停止保护短信
                    intent.putExtra("packageName", packageName);

                    sendBroadcast(intent);

                    finish();
                } else {
                    Toasts.showShort(getApplicationContext(), "密码错误");
                }
            }
        });
        setBtnOnClick(bt0);
        setBtnOnClick(bt1);
        setBtnOnClick(bt2);
        setBtnOnClick(bt3);
        setBtnOnClick(bt4);
        setBtnOnClick(bt5);
        setBtnOnClick(bt6);
        setBtnOnClick(bt7);
        setBtnOnClick(bt8);
        setBtnOnClick(bt9);
    }

    private void setBtnOnClick(final Button bt) {
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = etPwd.getText().toString();
                if (str.length()>20){
                    Toasts.showShort(getApplicationContext(),"密码长度不能大于20");
                    return;
                }
                str = str + bt.getText().toString();
                etPwd.setText(str);
                etPwd.setSelection(str.length());
            }
        });
    }

    @Override
    public void onBackPressed() {
        // 当用户输入后退健 的时候。我们进入到桌面
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addCategory("android.intent.category.MONKEY");
        startActivity(intent);
    }

}
