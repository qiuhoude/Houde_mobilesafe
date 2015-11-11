package com.qiu.houde_mobilesafe.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
import com.qiu.houde_mobilesafe.utils.Consts;
import com.qiu.houde_mobilesafe.utils.Logs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
        AlphaAnimation anim = new AlphaAnimation(0.3F, 1F);
        anim.setDuration(3000);
        rlSplRoot.setAnimation(anim);

        //拷贝地址数据库
        copyDB(Consts.ADDRESS_DB);
        //拷贝病毒数据库
        copyDB(Consts.ANTIVIRUS_DB);
        //创建快捷图标
        createShortcut();

        mHandler.sendEmptyMessageDelayed(REDIRECT_MAIN, 3000);

    }


    /**
     * 拷贝数据库
     *
     * @param dbName
     */
    private void copyDB(String dbName) {
        InputStream in = null;
        OutputStream out = null;
        File fileDB = new File(getFilesDir(), dbName);
        if (fileDB.exists()) {
            Logs.d(dbName+"已经存在");
            return;
        }
        try {
            in = getAssets().open(dbName);
            out = new FileOutputStream(fileDB);
            int len = 0;
            byte[] buff = new byte[1024];
            while ((len = in.read(buff)) != -1) {
                out.write(buff, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 创建桌面图标
     */
    private void createShortcut() {

        Intent shortCut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        //不允许重复创建
        shortCut.putExtra("duplicate", false);
        //名字
        shortCut.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
        //快捷图片
        shortCut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.mipmap.ic_launcher));

        //跳转的位置
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setClass(getApplicationContext(), SplashActivity.class);
        shortCut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);

        //发送广播
        sendBroadcast(shortCut);
        Logs.d("创建快捷图标");
    }

    // 判读是否已经存在快捷方式
    public boolean isExistShortCut() {
        boolean isInstallShortcut = false;
        final ContentResolver cr = this.getContentResolver();
        // 本人的2.2系统是”com.android.launcher2.settings”,网上见其他的为"com.android.launcher.settings"
        final String AUTHORITY = "com.android.launcher2.settings";
        final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/favorites?notify=true");
        Cursor c = cr.query(CONTENT_URI, new String[]{"title", "iconResource"}, "title=?", new String[]{getString(R.string.app_name)}, null);
        if (c != null && c.getCount() > 0) {
            isInstallShortcut = true;
            Logs.d("已经存在快捷方式");
        }
        Logs.d("不存在快捷方式");
        return isInstallShortcut;
    }
}
