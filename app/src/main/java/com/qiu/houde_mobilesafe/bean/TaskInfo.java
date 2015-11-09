package com.qiu.houde_mobilesafe.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2015/11/9.
 */
public class TaskInfo {

    private Drawable icon;
    private String name;
    /**
     * byte 为单位
     */
    private long memsize;
    private boolean userTask;
    private String packageName;
    private boolean checked;

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getMemsize() {
        return memsize;
    }

    public void setMemsize(long memsize) {
        this.memsize = memsize;
    }

    public boolean isUserTask() {
        return userTask;
    }

    public void setUserTask(boolean userTask) {
        this.userTask = userTask;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "TaskInfo{" +
                "packageName='" + packageName + '\'' +
                ", name='" + name + '\'' +
                ", memsize=" + memsize +
                ", userTask=" + userTask +
                '}';
    }
}
