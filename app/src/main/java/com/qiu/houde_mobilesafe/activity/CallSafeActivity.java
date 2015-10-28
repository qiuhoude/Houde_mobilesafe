package com.qiu.houde_mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import com.qiu.houde_mobilesafe.utils.Toasts;

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
        initDate();
    }

    private void initDate() {
        //开启进度条
        llPb.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //分批加载数据
                List<BlackNumberInfo> envet = dao.findPar2(mStartIndex, maxCount);
                EventBus.getDefault().post(envet);
            }
        }).start();
    }

    public void onEventMainThread(List<BlackNumberInfo> envet) {
        //关闭进度条
        llPb.setVisibility(View.INVISIBLE);
        gl_Refresh.refreshComplete();
        if (envet.size() != 0) {
            blackNumberInfos.addAll(0, envet);
            mAdapter.clear();
            mAdapter.addAll(blackNumberInfos);
            mStartIndex += maxCount;
        }
    }


    private void initView() {
        //create a SwipeMenuCreator to add items.
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
//                SwipeMenuItem openItem = new SwipeMenuItem(
//                        getApplicationContext());
//                // set item background
//                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
//                        0xCE)));
//                // set item width
//                openItem.setWidth(DensityUtils.dp2px(getApplicationContext(), 90L));
//                // set item title
//                openItem.setTitle("编辑");
//                // set item title fontsize
//                openItem.setTitleSize(18);
//                // set item title font color
//                openItem.setTitleColor(Color.WHITE);
//                // add to menu
//                menu.addMenuItem(openItem);

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
//                    case 0:
//                        //edit
//
//                        break;
                    case 0:
                        // delete
                        if (dao.delete(blackNumberInfos.get(position).getNumber())){
                            mAdapter.remove(position);
                            Toasts.showShort(getApplicationContext(),"删除成功");
                        }
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

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

        gl_Refresh.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                initDate();
            }

        });
    }

    public void addBlackNumber(View v) {
        //添加
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View dialog_view = View.inflate(this, R.layout.dialog_add_black_number, null);
        final EditText et_number = (EditText) dialog_view.findViewById(R.id.et_number);

        Button btn_ok = (Button) dialog_view.findViewById(R.id.btn_ok);

        Button btn_cancel = (Button) dialog_view.findViewById(R.id.btn_cancel);

        final CheckBox cb_phone = (CheckBox) dialog_view.findViewById(R.id.cb_phone);

        final CheckBox cb_sms = (CheckBox) dialog_view.findViewById(R.id.cb_sms);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_number = et_number.getText().toString().trim();
                if(TextUtils.isEmpty(str_number)){
                    Toast.makeText(CallSafeActivity.this, "请输入黑名单号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!dao.findNumber(str_number).equals("")){
                    Toasts.showShort(getApplicationContext(),"你输入的号码已经存在");
                    return;
                }

                String mode = "";

                if(cb_phone.isChecked()&& cb_sms.isChecked()){
                    mode = "1";
                }else if(cb_phone.isChecked()){
                    mode = "2";
                }else if(cb_sms.isChecked()){
                    mode = "3";
                }else{
                    Toast.makeText(CallSafeActivity.this,"请勾选拦截模式",Toast.LENGTH_SHORT).show();
                    return;
                }
                BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
                blackNumberInfo.setNumber(str_number);
                blackNumberInfo.setMode(mode);
                blackNumberInfos.add(0,blackNumberInfo);
                //把电话号码和拦截模式添加到数据库。
                dao.add(blackNumberInfo);
                mAdapter.clear();
                mAdapter.addAll(blackNumberInfos);
                dialog.dismiss();
            }
        });
        dialog.setView(dialog_view);
        dialog.show();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Unregister
        EventBus.getDefault().unregister(this);
    }


}
