package com.qiu.houde_mobilesafe.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qiu.houde_mobilesafe.R;
import com.qiu.houde_mobilesafe.utils.AppUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

;


public class SplashActivity extends AppCompatActivity {

    private static final int REDIRECT_MAIN = 1;

    @InjectView(R.id.tv_version)
    TextView tvVersion;
    @InjectView(R.id.progressBar)
    ProgressBar progressBar;

    private static Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == REDIRECT_MAIN){

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        tvVersion.setText("版本号:" + AppUtils.getVersionName(getApplicationContext()));
        progressBar.setVisibility(View.INVISIBLE);

        mHandler.sendEmptyMessageDelayed(REDIRECT_MAIN, 1000);
    }


}
