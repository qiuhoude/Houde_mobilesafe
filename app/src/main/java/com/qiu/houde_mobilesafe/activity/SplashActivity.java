package com.qiu.houde_mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiu.houde_mobilesafe.R;
import com.qiu.houde_mobilesafe.utils.AppUtils;

import butterknife.Bind;
import butterknife.ButterKnife;




public class SplashActivity extends Activity {

    private static final int REDIRECT_MAIN = 1;

    @Bind(R.id.tv_version)
    TextView tvVersion;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.rl_spl_root)
    RelativeLayout rlSplRoot;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == REDIRECT_MAIN) {
                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh);
        ButterKnife.bind(this);
        tvVersion.setText("版本号:" + AppUtils.getVersionName(getApplicationContext()));
        progressBar.setVisibility(View.VISIBLE);
        //渐变的动画
        AlphaAnimation  anim = new AlphaAnimation(0.3F,1F);
        anim.setDuration(3000);
        rlSplRoot.setAnimation(anim);

        mHandler.sendEmptyMessageDelayed(REDIRECT_MAIN, 3000);
    }


}
