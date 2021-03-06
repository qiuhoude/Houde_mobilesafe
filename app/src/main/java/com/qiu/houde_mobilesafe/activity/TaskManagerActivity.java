package com.qiu.houde_mobilesafe.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.qiu.houde_mobilesafe.R;
import com.qiu.houde_mobilesafe.bean.TaskInfo;
import com.qiu.houde_mobilesafe.engine.AppInfos;
import com.qiu.houde_mobilesafe.utils.Consts;
import com.qiu.houde_mobilesafe.utils.SPUtils;
import com.qiu.houde_mobilesafe.utils.SystemInfoUtils;
import com.qiu.houde_mobilesafe.utils.Toasts;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by Administrator on 2015/11/9.
 */
public class TaskManagerActivity extends Activity {

    @Bind(R.id.tv_task_process_count)
    TextView tvTaskProcessCount;
    @Bind(R.id.tv_task_memory)
    TextView tvTaskMemory;
    @Bind(R.id.list_view)
    StickyListHeadersListView listView;

    private Context mContext;
    private List<TaskInfo> taskInfos;
    private List<TaskInfo> userTaskInfos;
    private List<TaskInfo> systemTaskInfos;
    private MyAdapter myAdapter;
    private ActivityManager mAm;

    private int mRunningProcessCount;
    private long mAvailRam;
    private long mTotalRam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager);
        mContext = getApplicationContext();
        mAm = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initDate();
        initView();
    }

    private void initView() {

        tvTaskProcessCount.setText("运行中进程" + mRunningProcessCount + "个");
        tvTaskMemory.setText("剩余/总内存 " +
                Formatter.formatFileSize(mContext, mAvailRam) + "/"
                + Formatter.formatFileSize(mContext, mTotalRam));
        listView.setAdapter(myAdapter = new MyAdapter(this, taskInfos));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TaskInfo info = taskInfos.get(position);
                info.setChecked(!info.isChecked());
                myAdapter.notifyDataSetChanged();
            }
        });
    }


    private void initDate() {

        //初始化title
        mRunningProcessCount = SystemInfoUtils.getRunningProcessCount(mContext);
        mAvailRam = SystemInfoUtils.getAvailRam(mContext);
        mTotalRam = SystemInfoUtils.getTotalRam(mContext);

        taskInfos = AppInfos.getTaskInfos(mContext);
        userTaskInfos = new ArrayList<TaskInfo>();
        systemTaskInfos = new ArrayList<TaskInfo>();

        for (TaskInfo taskInfo : taskInfos) {
            //用户程序
            if (taskInfo.isUserTask()) {
                userTaskInfos.add(taskInfo);
            } else {
                //控制是否显示系统进程
                if ((boolean)SPUtils.get(mContext, Consts.IS_SHOW_SYSTEM,true)) {
                    systemTaskInfos.add(taskInfo);
                }
            }
        }
        taskInfos.clear();
        taskInfos.addAll(userTaskInfos);
        taskInfos.addAll(systemTaskInfos);
    }


    class MyAdapter extends BaseAdapter implements StickyListHeadersAdapter {
        private List<TaskInfo> countries;
        private LayoutInflater inflater;

        public MyAdapter(Context context, List<TaskInfo> countries) {
            inflater = LayoutInflater.from(context);
            this.countries = countries;
        }

        @Override
        public View getHeaderView(int position, View convertView, ViewGroup parent) {
            ViewHolderHeader holder;
            if (convertView == null) {
                holder = new ViewHolderHeader();
                convertView = inflater.inflate(R.layout.item_app_header, parent, false);
                holder.tv_header = (TextView) convertView.findViewById(R.id.tv_app_header);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolderHeader) convertView.getTag();
            }
            if (getHeaderId(position) == 0L) {
                holder.tv_header.setText("用户进程 " + userTaskInfos.size());
            } else {
                holder.tv_header.setText("系统进程" + systemTaskInfos.size());
            }
            return convertView;
        }

        @Override
        public long getHeaderId(int position) {
            if (countries.get(position).isUserTask()) {
                return 0;
            }
            return 1;
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
            ViewHolder holder = null;
            TaskInfo info = countries.get(position);
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_task_manager, parent, false);
                holder.iv_app_icon = (ImageView) convertView.findViewById(R.id.iv_app_icon);
                holder.tv_app_name = (TextView) convertView.findViewById(R.id.tv_app_name);
                holder.tv_app_memory_size = (TextView) convertView.findViewById(R.id.tv_app_memory_size);
                holder.tv_app_status = (CheckBox) convertView.findViewById(R.id.cb_app_status);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            holder.iv_app_icon.setImageDrawable(info.getIcon());
            holder.tv_app_name.setText(info.getName());
            holder.tv_app_memory_size.setText("内存占用:" + Formatter.formatFileSize(mContext, info.getMemsize()));
            if (info.getPackageName().equals(mContext.getPackageName())) {
                holder.tv_app_status.setVisibility(View.INVISIBLE);
            } else if (info.isChecked()) {
                holder.tv_app_status.setChecked(true);
                holder.tv_app_status.setVisibility(View.VISIBLE);
            } else if (!info.isChecked()) {
                holder.tv_app_status.setChecked(false);
                holder.tv_app_status.setVisibility(View.VISIBLE);
            }
            return convertView;
        }
    }

    static class ViewHolder {
        ImageView iv_app_icon;
        TextView tv_app_name;
        TextView tv_app_memory_size;
        CheckBox tv_app_status;
    }

    static class ViewHolderHeader {
        TextView tv_header;
    }


    /**
     * 全选
     *
     * @param v
     */
    public void selectAll(View v) {
        for (TaskInfo info : taskInfos) {
            info.setChecked(true);
        }
        myAdapter.notifyDataSetChanged();
    }

    /**
     * 反选
     *
     * @param v
     */
    public void selectOppsite(View v) {
        for (TaskInfo info : taskInfos) {
            info.setChecked(!info.isChecked());
        }
        myAdapter.notifyDataSetChanged();

    }

    /**
     * 清理
     *
     * @param v
     */
    public void killProcess(View v) {

        List<TaskInfo> killLists = new ArrayList<TaskInfo>();

        // 清理的总共的进程个数
        int totalCount = 0;
        // 清理的进程的大小
        int killMem = 0;

        for (TaskInfo info : taskInfos) {
            if (info.getPackageName().equals(getPackageName())||!info.isChecked()) {
                continue;
            }
            killLists.add(info);
            totalCount++;
            killMem += info.getMemsize();
        }

        for (TaskInfo info : killLists) {
            taskInfos.remove(info);
            if (info.isUserTask()) {
                userTaskInfos.remove(info);
            } else {
                systemTaskInfos.remove(info);
            }
            mAm.killBackgroundProcesses(info.getPackageName());
        }
        Toasts.showShort(this, "共清理" + totalCount + "个进程,释放" + Formatter.formatFileSize(mContext, killMem) + "内存");

        //设置title

        tvTaskProcessCount.setText("运行中进程" + (mRunningProcessCount-totalCount) + "个");
        tvTaskMemory.setText("剩余/总内存 " +
                Formatter.formatFileSize(mContext, (mAvailRam+killMem)) + "/"
                + Formatter.formatFileSize(mContext, mTotalRam));
        myAdapter.notifyDataSetChanged();
    }

    /**
     * 设置
     *
     * @param
     */
    public void openSetting(View v) {
        startActivity(new Intent(this,TaskManagerSettingActivity.class));
    }
}
