package com.qiu.houde_mobilesafe.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ListView;
import android.widget.TextView;

import com.qiu.houde_mobilesafe.R;
import com.qiu.houde_mobilesafe.activity.AppLockActivity;
import com.qiu.houde_mobilesafe.adapter.BaseAdapterHelper;
import com.qiu.houde_mobilesafe.adapter.QuickAdapter;
import com.qiu.houde_mobilesafe.bean.AppInfo;
import com.qiu.houde_mobilesafe.db.dao.AppLockDao;
import com.qiu.houde_mobilesafe.engine.AppInfos;
import com.qiu.houde_mobilesafe.utils.Logs;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class LockFragment extends Fragment implements AppLockActivity.TransmitDate {


    @Bind(R.id.tv_lock)
    TextView tvLock;
    @Bind(R.id.list_view)
    ListView listView;

    private AppLockDao dao;
    private View parentView;
    private List<AppInfo> appInfos;
    private List<AppInfo> lockLists;
    private QuickAdapter mAdapter;
    AppLockActivity.TransmitDate mtd;

    public static LockFragment newInstance() {
        LockFragment fragment = new LockFragment();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mtd = (AppLockActivity.TransmitDate) ((AppLockActivity) getActivity()).getmFragments().get(0);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_lock, container, false);
        ButterKnife.bind(this, parentView);
        //获取数据
        initDate();
        initView();
        return parentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Logs.d("lock onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Logs.d("lock onResume");
    }

    private void initView() {

        listView.setAdapter(mAdapter = new QuickAdapter<AppInfo>(getActivity(), R.layout.item_lock, lockLists) {
            @Override
            protected void convert(BaseAdapterHelper helper, final AppInfo item) {
                helper.setImageDrawable(R.id.iv_icon, item.getIcon());
                helper.setText(R.id.tv_name, item.getApkName());
                helper.setBackgroundRes(R.id.ibt_lock, R.drawable.btn_lock_selector);
                final View parent = helper.getView();
                helper.setOnClickListener(R.id.ibt_lock, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 初始化一个位移动画
                        TranslateAnimation translateAnimation = new TranslateAnimation(
                                Animation.RELATIVE_TO_SELF, 0,
                                Animation.RELATIVE_TO_SELF, -1.0f,
                                Animation.RELATIVE_TO_SELF, 0,
                                Animation.RELATIVE_TO_SELF, 0);
                        translateAnimation.setDuration(500);
                        parent.startAnimation(translateAnimation);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                SystemClock.sleep(500);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // 添加到数据库里面
                                        dao.delete(item.getApkPackageName());
                                        // 从当前的页面移除对象
                                        mAdapter.remove(item);
                                        mtd.transmit(item);
                                        tvLock.setText("已加锁(" + mAdapter.getCount() + ")个");
                                    }
                                });
                            }
                        }).start();
                    }
                });
            }
        });
        tvLock.setText("已加锁(" + mAdapter.getCount() + ")个");

    }

    private void initDate() {
        appInfos = AppInfos.getAppInfos(getActivity());
        dao = new AppLockDao(getActivity());
        lockLists = new ArrayList<>();

        for (AppInfo info : appInfos) {
            if (dao.find(info.getApkPackageName())) {
                if (info.getApkPackageName().equals(info.getApkName())) {
                    continue;
                }
                lockLists.add(info);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void transmit(AppInfo info) {
        mAdapter.add(info);
        tvLock.setText("已加锁(" + mAdapter.getCount() + ")个");

    }

}
