package com.qiu.houde_mobilesafe.bean;

/**
 * Created by Administrator on 2015/10/27.
 */
public class BlackNumberInfo {
    /**
     * 黑名单电话号码
     */
    private String number;
    /**
     * 黑名单拦截模式
     * 1 全部拦截 电话拦截 + 短信拦截
     * 2 电话拦截
     * 3 短信拦截
     */
    private String mode;

    public BlackNumberInfo() {
    }

    public BlackNumberInfo(String number, String mode) {
        this.number = number;
        this.mode = mode;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    @Override
    public String toString() {
        return "BlackNumberInfo{" +
                "number='" + number + '\'' +
                ", mode='" + mode + '\'' +
                '}';
    }
}
