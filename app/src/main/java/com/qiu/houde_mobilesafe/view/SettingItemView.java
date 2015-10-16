package com.qiu.houde_mobilesafe.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiu.houde_mobilesafe.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2015/10/16.
 */
public class SettingItemView extends RelativeLayout {

    @Bind(R.id.tv_settings_title)
    TextView tvSettingsTitle;
    @Bind(R.id.tv_settings_desc)
    TextView tvSettingsDesc;
    @Bind(R.id.cb_settings_status)
    CheckBox cbSettingsStatus;

    private String mTitle;
    private String mDesc_on;
    private String mDesc_off;

    public SettingItemView(Context context) {
        this(context, null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
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
                case R.styleable.SettingItemView_desc_on:
                    mDesc_on = a.getString(attr);
                    break;
                case R.styleable.SettingItemView_desc_off:
                    mDesc_off = a.getString(attr);
                    break;
            }
        }
        a.recycle();
        //加载布局
        initView();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.view_settings_item, this);
        ButterKnife.bind(this);
        setTitle(mTitle);

    }
    public void setTitle(String title) {
        tvSettingsTitle.setText(title);
    }

    public void setDesc(String desc) {
        tvSettingsDesc.setText(desc);
    }

    /**
     * 返回勾选状态
     *
     * @return
     */
    public boolean isChecked() {
        return cbSettingsStatus.isChecked();
    }

    public void setChecked(boolean check) {
        cbSettingsStatus.setChecked(check);

        // 根据选择的状态,更新文本描述
        if (check) {
            setDesc(mDesc_on);
        } else {
            setDesc(mDesc_off);
        }
    }
}
