package com.qiu.houde_mobilesafe.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiu.houde_mobilesafe.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2015/10/23.
 */
public class SettingClickView extends RelativeLayout {

    @Bind(R.id.tv_settings_desc)
    TextView tvSettingsDesc;
    @Bind(R.id.tv_settings_title)
    TextView tvSettingsTitle;

    private String mTitle;

    public SettingClickView(Context context) {
        this(context, null);
    }

    public SettingClickView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingClickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取属性
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.SettingItemView, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.SettingItemView_title:
                    mTitle = a.getString(attr);
                    break;
            }
        }
        a.recycle();
        //加载布局
        initView();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.view_settings_click, this);
        ButterKnife.bind(this);
        tvSettingsTitle.setText(mTitle);
    }

    public void setTitle(String title) {
        tvSettingsTitle.setText(title);
    }

    public void setDesc(String desc) {
        tvSettingsDesc.setText(desc);
    }
}
