package com.qiu.houde_mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.qiu.houde_mobilesafe.R;
import com.qiu.houde_mobilesafe.utils.Consts;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2015/10/16.
 */
public class HomeActivity extends Activity {

    @Bind(R.id.tv_home_marquee)
    TextView tvHomeMarquee;
    @Bind(R.id.gv_home)
    GridView gvHome;

    private List<HomeBean> mDatas = new ArrayList<HomeBean>();
    private QuickAdapter<HomeBean> mAdapter;
    private String[] mItems = new String[]{"手机防盗", "通讯卫士", "软件管理", "进程管理",
            "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心"};

    private int[] mPics = new int[]{R.mipmap.home_safe,
            R.mipmap.home_callmsgsafe, R.mipmap.home_apps,
            R.mipmap.home_taskmanager, R.mipmap.home_netmanager,
            R.mipmap.home_trojan, R.mipmap.home_sysoptimize,
            R.mipmap.home_tools, R.mipmap.home_settings};
    private SharedPreferences mPref;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        initDatas();
        mPref = getSharedPreferences(Consts.CONFIG, MODE_PRIVATE);
        mAdapter = new QuickAdapter<HomeBean>(this, R.layout.home_list_item, mDatas) {
            @Override
            protected void convert(BaseAdapterHelper helper, HomeBean item) {
                helper.setText(R.id.tv_home_item, item.getTitle());
                helper.setImageResource(R.id.vi_home_item, item.getImgResId());
            }
        };
        gvHome.setAdapter(mAdapter);
        tvHomeMarquee.setText("宣传打网球的范围分为全奶粉穷疯欺负你如果fire刚iu热舞i股本部分国企哦i半个入编单位年轻哦弄info青年");

        gvHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: {
                        //手机防盗
                        String savedPassword = mPref.getString(Consts.PASSWORD, null);
                        Intent lostIntent = new Intent(HomeActivity.this, GestureLookAcitvity.class);
                        if (!TextUtils.isEmpty(savedPassword)) {
                            lostIntent.putExtra(GestureLookAcitvity.LOCK_TYPE, GestureLookAcitvity.UNLOCK);
                            int[] pwdInt = new int[savedPassword.length()];
                            for (int i = 0; i < savedPassword.length(); i++) {
                                pwdInt[i] = Integer.parseInt(String.valueOf(savedPassword.charAt(i)));
                            }
                            lostIntent.putExtra(GestureLookAcitvity.LOCK_ANSWER, pwdInt);
                        } else {
                            lostIntent.putExtra(GestureLookAcitvity.LOCK_TYPE, GestureLookAcitvity.SET_LOCK);
                        }
                        startActivity(lostIntent);
                    }
                    break;
                    case 7:
                        //高级工具
                        Intent atoolsIntent = new Intent(HomeActivity.this, AToolsActivity.class);
                        startActivity(atoolsIntent);
                        break;
                    case 8:
                        //设置中心
                        Intent settingIntent = new Intent(HomeActivity.this, SettingsActivity.class);
                        startActivity(settingIntent);
                        break;

                }
            }
        });
    }


    private void initDatas() {
        for (int i = 0; i < mItems.length; i++) {
            mDatas.add(new HomeBean(mPics[i], mItems[i]));
        }
    }


    class HomeBean {
        private String title;
        private int imgResId;

        public HomeBean() {
        }

        public HomeBean(int imgResId, String title) {
            this.imgResId = imgResId;
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getImgResId() {
            return imgResId;
        }

        public void setImgResId(int imgResId) {
            this.imgResId = imgResId;
        }
    }

    // 监听按键
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN
                    && event.getRepeatCount() == 0) {
                this.exitApp();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    private long exitTime = 0;

    /**
     * 退出程序
     */
    private void exitApp() {
        // 判断2次点击事件时间
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }
}
