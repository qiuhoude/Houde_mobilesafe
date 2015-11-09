package com.qiu.houde_mobilesafe.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.qiu.houde_mobilesafe.R;
import com.qiu.houde_mobilesafe.bean.AppInfo;
import com.qiu.houde_mobilesafe.engine.AppInfos;
import com.qiu.houde_mobilesafe.utils.Toasts;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by Administrator on 2015/11/3.
 */
public class AppManagerActivity extends Activity implements View.OnClickListener {

    public static final int REQUEST_CODE_DELETE = 0xff;
    @Bind(R.id.tv_rom)
    TextView tvRom;
    @Bind(R.id.tv_sd)
    TextView tvSd;
    @Bind(R.id.lv_app_manger)
    StickyListHeadersListView lvAppManger;

    private Context mContext;
    private List<AppInfo> appInfos;
    private List<AppInfo> userAppInfos;
    private List<AppInfo> systemAppInfos;
    private PopupWindow popupWindow;
    private AppInfo clickAppInfo;
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manager);
        ButterKnife.bind(this);
        mContext = this;
        initDate();
        initView();
    }

    private void initView() {
        //获取rom的剩余控件
        long rom_freeSpace = Environment.getDataDirectory().getFreeSpace();
        //获取到SD卡的剩余空间
        long sd_freeSpace = Environment.getExternalStorageDirectory().getFreeSpace();
        tvRom.setText("内存可用:" + Formatter.formatFileSize(this, rom_freeSpace));
        tvSd.setText("sd卡可用:" + Formatter.formatFileSize(this, sd_freeSpace));
        lvAppManger.setAdapter(mAdapter = new MyAdapter(mContext, appInfos, userAppInfos.size(), systemAppInfos.size()));
        lvAppManger.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取到当前点击的item对象
                Object obj = lvAppManger.getItemAtPosition(position);
                if (obj != null && obj instanceof AppInfo) {

                    clickAppInfo = (AppInfo) obj;

                    View contentView = View.inflate(AppManagerActivity.this, R.layout.item_popup, null);

                    LinearLayout ll_uninstall = (LinearLayout) contentView.findViewById(R.id.ll_uninstall);

                    LinearLayout ll_start = (LinearLayout) contentView.findViewById(R.id.ll_start);

                    LinearLayout ll_detail = (LinearLayout) contentView.findViewById(R.id.ll_detail);

                    ll_uninstall.setOnClickListener(AppManagerActivity.this);
                    ll_start.setOnClickListener(AppManagerActivity.this);
                    ll_detail.setOnClickListener(AppManagerActivity.this);
                    popupWindowDismiss();


                    popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    //需要注意：使用PopupWindow 必须设置背景。不然没有动画
                    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    int[] location = new int[2];
                    //获取view展示到窗体上面的位置
                    view.getLocationInWindow(location);

                    popupWindow.showAtLocation(parent, Gravity.LEFT + Gravity.TOP, 90, location[1]);
                    ScaleAnimation sa = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    sa.setDuration(300);
                    contentView.startAnimation(sa);
                }
            }
        });
        lvAppManger.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //关闭窗口
                popupWindowDismiss();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                popupWindowDismiss();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //运行
            case R.id.ll_start:
                Intent start_localIntent = this.getPackageManager().getLaunchIntentForPackage(clickAppInfo.getApkPackageName());
                this.startActivity(start_localIntent);
                popupWindowDismiss();
                clickAppInfo = null;
                break;
            //卸载
            case R.id.ll_uninstall:
                if (clickAppInfo.isUserApp()) {
                    Intent uninstall_localIntent = new Intent("android.intent.action.DELETE", Uri.parse("package:" + clickAppInfo.getApkPackageName()));
                    startActivity(uninstall_localIntent);
                } else {
                    Toasts.showShort(mContext, "系统应用需要root权限后才能卸载");
                }
                popupWindowDismiss();

                break;
            //详情
            case R.id.ll_detail:
                Intent detail_intent = new Intent();
                detail_intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                detail_intent.addCategory(Intent.CATEGORY_DEFAULT);
                detail_intent.setData(Uri.parse("package:" + clickAppInfo.getApkPackageName()));
                startActivity(detail_intent);
                popupWindowDismiss();
                clickAppInfo = null;
                break;
        }

    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if (requestCode == REQUEST_CODE_DELETE) {

        }
    }

    private void initDate() {
        //获取到所有安装到手机上面的应用程序
        appInfos = AppInfos.getAppInfos(AppManagerActivity.this);

        //用户程序的集合
        userAppInfos = new ArrayList<AppInfo>();
        //系统程序的集合
        systemAppInfos = new ArrayList<AppInfo>();

        for (AppInfo appInfo : appInfos) {
            //用户程序
            if (appInfo.isUserApp()) {
                userAppInfos.add(appInfo);
            } else {
                systemAppInfos.add(appInfo);
            }
        }
        appInfos.clear();
        appInfos.addAll(userAppInfos);
        appInfos.addAll(systemAppInfos);

    }

    private void popupWindowDismiss() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //只有卸载的时候才会进来
        if (clickAppInfo != null) {
            int appSize = mContext.getPackageManager().getInstalledPackages(0).size();
            if (appSize != appInfos.size()) {
                appInfos.remove(clickAppInfo);
                if (clickAppInfo.isUserApp()) {
                    mAdapter.userSizeReduce();
                } else {
                    mAdapter.sysSizeReduce();
                }
                mAdapter.notifyDataSetChanged();
            }
            clickAppInfo = null;
        }
    }


    /**
     * 适配器
     */
    class MyAdapter extends BaseAdapter implements StickyListHeadersAdapter {

        private List<AppInfo> countries;
        private LayoutInflater inflater;
        private int userSize;
        private int sysSize;

        public MyAdapter(Context context, List<AppInfo> countries, int userSize, int sysSize) {
            inflater = LayoutInflater.from(context);
            this.countries = countries;
            this.userSize = userSize;
            this.sysSize = sysSize;
        }

        public void userSizeReduce() {

            userSize--;
        }

        public void sysSizeReduce() {
            sysSize--;
        }

        @Override
        public View getHeaderView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolderHeader holder;
            if (convertView == null) {
                holder = new ViewHolderHeader();
                convertView = inflater.inflate(R.layout.item_app_header, viewGroup, false);
                holder.tv_header = (TextView) convertView.findViewById(R.id.tv_app_header);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolderHeader) convertView.getTag();
            }
            if (getHeaderId(position) == 0L) {
                holder.tv_header.setText("用户程序(" + userSize + ")");
            } else {
                holder.tv_header.setText("系统程序(" + sysSize + ")");
            }

//            Logs.d("position" + position);
            return convertView;
        }

        @Override
        public long getHeaderId(int position) {
            //Logs.d("position" +position);
            boolean isUserApp = countries.get(position).isUserApp();
            if (isUserApp) {
                return 0;
            } else {
                return 1;
            }
        }

        @Override
        public int getCount() {
            return countries.size();
        }

        @Override
        public Object getItem(int position) {
            return countries.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            AppInfo appInfo = countries.get(position);
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_app_manager, parent, false);
                holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.tv_apk_size = (TextView) convertView.findViewById(R.id.tv_apk_size);
                holder.tv_location = (TextView) convertView.findViewById(R.id.tv_location);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.iv_icon.setImageDrawable(appInfo.getIcon());
            holder.tv_apk_size.setText(Formatter.formatFileSize(AppManagerActivity.this, appInfo.getApkSize()));
            holder.tv_name.setText(appInfo.getApkName());
            if (appInfo.isRom()) {
                holder.tv_location.setText("手机内存");
            } else {
                holder.tv_location.setText("外部存储");
            }
            return convertView;
        }

    }

    static class ViewHolder {
        ImageView iv_icon;
        TextView tv_apk_size;
        TextView tv_location;
        TextView tv_name;
    }

    static class ViewHolderHeader {
        TextView tv_header;
    }


    @Override
    protected void onDestroy() {
        popupWindowDismiss();
        super.onDestroy();
    }
}
