package com.qiu.houde_mobilesafe.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.qiu.houde_mobilesafe.R;
import com.qiu.houde_mobilesafe.bean.AppInfo;
import com.qiu.houde_mobilesafe.fragment.LockFragment;
import com.qiu.houde_mobilesafe.fragment.UnLockFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2015/11/12.
 */
public class AppLockActivity extends FragmentActivity implements View.OnClickListener{

    @Bind(R.id.tv_unlock)
    TextView tvUnlock;
    @Bind(R.id.tv_lock)
    TextView tvLock;
    @Bind(R.id.vp_lock)
    ViewPager vpLock;


    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private FragmentPagerAdapter mAdapter;

    public List<Fragment> getmFragments() {
        return mFragments;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_lock);
        ButterKnife.bind(this);

        initView();

        tvUnlock.setOnClickListener(this);
        tvLock.setOnClickListener(this);

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        };

        vpLock.setAdapter(mAdapter);
        vpLock.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                resetTite();
                switch (position){
                    case 0:
                        tvUnlock.setBackgroundResource(R.mipmap.tab_pressed);
                        break;
                    case 1:
                        tvLock.setBackgroundResource(R.mipmap.tab_pressed);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }


   private void resetTite(){
       tvLock.setBackgroundResource(R.mipmap.tab_default);
       tvUnlock.setBackgroundResource(R.mipmap.tab_default);

   }


    private void initView() {
        LockFragment lockFragment = LockFragment.newInstance();
        UnLockFragment unLockFragment = UnLockFragment.newInstance();
        mFragments.add(unLockFragment);
        mFragments.add(lockFragment);
    }

    @Override
    public void onClick(View v) {
        resetTite();
        switch (v.getId()){
            case R.id.tv_unlock:
                tvUnlock.setBackgroundResource(R.mipmap.tab_pressed);
                vpLock.setCurrentItem(0);
                break;
            case R.id.tv_lock:
                tvLock.setBackgroundResource(R.mipmap.tab_pressed);
                vpLock.setCurrentItem(1);
                break;
        }
    }

    public interface TransmitDate{
        /**
         * 数据更新时候调用
         * @param info
         */
        void transmit(AppInfo info);
    }
}
