package com.qiu.houde_mobilesafe.activity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.qiu.houde_mobilesafe.R;
import com.qiu.houde_mobilesafe.bean.BlackNumberInfo;
import com.qiu.houde_mobilesafe.db.dao.BlackNumberDao;
import com.qiu.houde_mobilesafe.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by Administrator on 2015/10/26.
 */
public class CallSafeActivity extends Activity {

    @Bind(R.id.listView)
    SwipeMenuListView listView;
    @Bind(R.id.ll_pb)
    LinearLayout llPb;
    @Bind(R.id.gl_refresh)
    PtrClassicFrameLayout gl_Refresh;

    private BlackNumberDao dao;
    private List<BlackNumberInfo> blackNumberInfos;
    private int mStartIndex = 0; //开始的位置
    private int maxCount = 20;//每页展示20条数据
    private int totalPage;
    private int totalNumber;
    private QuickAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_safe);
        ButterKnife.bind(this);

        // Register
        EventBus.getDefault().register(this);
        dao = new BlackNumberDao(getApplicationContext());
        blackNumberInfos = new ArrayList<BlackNumberInfo>();
        initView();

        //开启进度条
        llPb.setVisibility(View.VISIBLE);
        initDate();


    }

    private void initDate() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //分批加载数据
                if (blackNumberInfos.size() == 0) {
                    blackNumberInfos.addAll(dao.findPar2(mStartIndex, maxCount));
                    EventBus.getDefault().post(new Integer(0));

                } else {
                    //把后面的数据。追加到blackNumberInfos集合里面。防止黑名单被覆盖
                    blackNumberInfos.addAll(0, dao.findPar2(mStartIndex, maxCount));
                    EventBus.getDefault().post(new Integer(1));
                }

            }
        }).start();
    }

    private void initView() {
        //create a SwipeMenuCreator to add items.
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(DensityUtils.dp2px(getApplicationContext(), 90L));
                // set item title
                openItem.setTitle("编辑");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(DensityUtils.dp2px(getApplicationContext(), 90L));
                // set a icon
                deleteItem.setIcon(R.mipmap.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        listView.setMenuCreator(creator);
        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // open
                        break;
                    case 1:
                        // delete
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        gl_Refresh.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                initDate();
            }

        });
    }

    public void onEventMainThread(Integer event) {
        mStartIndex += maxCount;
        if (event == 0) {
            llPb.setVisibility(View.INVISIBLE);
            QuickAdapter s = null;
            //填充数据
            listView.setAdapter(mAdapter = new QuickAdapter<BlackNumberInfo>(this, R.layout.item_call_safe, blackNumberInfos) {
                @Override
                protected void convert(BaseAdapterHelper helper, BlackNumberInfo item) {
                    helper.setText(R.id.tv_number, item.getNumber());
                    String mode = item.getMode();
                    if (mode.equals("1")) {
                        mode = "来电拦截+短信";
                    } else if (mode.equals("2")) {
                        mode = "电话拦截";
                    } else if (mode.equals("3")) {
                        mode = "短信拦截";
                    }
                    helper.setText(R.id.tv_mode, mode);
                }
            });
        } else if (event == 1) {
            gl_Refresh.refreshComplete();
            mAdapter.addAll(blackNumberInfos);
        }
    }


    public void addBlackNumber(View v) {
        //添加
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Unregister
        EventBus.getDefault().unregister(this);
    }


}
