package com.qiu.houde_mobilesafe.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 跟App相关的辅助类
 * 
 * 
 * 
 */
public class AppUtils {

	private AppUtils() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");

	}

	/**
	 * 获取应用程序名称
	 */
	public static String getAppName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			int labelRes = packageInfo.applicationInfo.labelRes;
			return context.getResources().getString(labelRes);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * [获取应用程序版本名称信息]
	 * 
	 * @param context
	 * @return 当前应用的版本名称
	 */
	public static String getVersionName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return packageInfo.versionName;

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取SDK版本号
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static int getAndroidOSVersion() {
		int osVersion;
		try {
			osVersion = Integer.valueOf(Build.VERSION.SDK);
		} catch (NumberFormatException e) {
			osVersion = 0;
		}

		return osVersion;
	}

	/**
	 * 获取机器唯一码
	 * 
	 * @param context
	 * @return
	 */
	public static String getPhoneDeviceId(Context context) {
		final TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		final String tmDevice, tmSerial, androidId;
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		androidId = ""
				+ Secure.getString(context.getContentResolver(),
						Secure.ANDROID_ID);

		UUID deviceUuid = new UUID(androidId.hashCode(),
				((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
		String deviceId = deviceUuid.toString();

		return deviceId;
	}

	public static String getDeviceId(Context context) {
		final TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}

	public static String getAndroId(Context context) {
		return Secure
				.getString(context.getContentResolver(), Secure.ANDROID_ID);
	}

	/**
	 * 获取sim卡的序列号
	 * @param context
	 * @return
	 */
	public static String getSimSerialNumber(Context context){
		final TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String simSerialNumber = tm.getSimSerialNumber();
		return simSerialNumber;

	}

	/**
	 * 获取手机号码
	 * 
	 * @param context
	 * @return
	 */
	public static String getPhoneNumber(Context context) {
		TelephonyManager phoneMgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		phoneMgr.getDeviceId();
		return phoneMgr.getLine1Number(); // 获取本机号码
	}
	
	/**
	 * 获取安装之后的apk md5码
	 * @param context
	 * @return 返回md5码
	 */
	public static String getApkMd5(Context context){
		File apkFile = new File(context.getPackageCodePath());
		String apkMD5 = null;
		try {
			apkMD5 = MD5Util.getFileMD5String(apkFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return apkMD5;
	}



	
}
