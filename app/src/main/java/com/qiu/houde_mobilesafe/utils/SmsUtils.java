package com.qiu.houde_mobilesafe.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;


public class SmsUtils {

	/**
	 * 备份短信的接口
	 * @author Administrator
	 *
	 */
	public interface BackUpCallBackSms{

		public void befor(int count);

		public void onBackUpSms(int process);

	}


	public static boolean backUp(Context context, BackUpCallBackSms callback) {
		/**
		 * 目的 ： 备份短信：
		 *
		 * 1 判断当前用户的手机上面是否有sd卡 
		 * 2 权限 --- 
		 *   使用内容观察者
		 * 3 写短信(写到sd卡)
		 *
		 *
		 */
		// 判断当前sd卡的状态
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			// 如果能进来就说明用户有SD卡
			ContentResolver resolver = context.getContentResolver();
			// 获取短信的路径
			Uri uri = Uri.parse("content://sms/");
			// type = 1 接收短信
			// type = 2 发送短信
			// cursor 表示游标的意思
			Cursor cursor = resolver.query(uri, new String[] { "address",
					"date", "type", "body" }, null, null, null);
			//获取当前一共有多少条短信
			int count = cursor.getCount();
			//设置pd的最大值
//			pd.setMax(count);

			callback.befor(count);

			//进度条默认是0
			int process = 0;

			// 写文件
			try {
				// 把短信备份到sd卡 第二个参数表示名字
				File file = new File(Environment.getExternalStorageDirectory(),
						"backup.xml");

				FileOutputStream os = new FileOutputStream(file);
				// 得到序列化器
				// 在android系统里面所有有关xml的解析都是pull解析
				XmlSerializer serializer = Xml.newSerializer();
				// 把短信序列化到sd卡然后设置编码格式
				serializer.setOutput(os, "utf-8");
				// standalone表示当前的xml是否是独立文件 ture表示文件独立。yes
				serializer.startDocument("utf-8", true);
				// 设置开始的节点 第一个参数是命名空间。第二个参数是节点的名字
				serializer.startTag(null, "smss");
				//设置smss节点上面的属性值 第二个参数是名字。第三个参数是值
				serializer.attribute(null, "size", String.valueOf(count));
				// 游标往下面进行移动
				while (cursor.moveToNext()) {
					Logs.d("----------------------------");
					Logs.d("address = " + cursor.getString(0));
					Logs.d("date = " + cursor.getString(1));
					Logs.d("type = " + cursor.getString(2));
					Logs.d("body = " + cursor.getString(3));

					serializer.startTag(null, "sms");

					serializer.startTag(null, "address");
					// 设置文本的内容
					serializer.text(cursor.getString(0));

					serializer.endTag(null, "address");

					serializer.startTag(null, "date");

					serializer.text(cursor.getString(1));

					serializer.endTag(null, "date");

					serializer.startTag(null, "type");

					serializer.text(cursor.getString(2));

					serializer.endTag(null, "type");

					serializer.startTag(null, "body");

					//读取短信的内容
					/**
					 * 加密：第一个参数表示加密种子(密钥)
					 *     第二个参数表示加密的内容
					 */
					serializer.text(CryptoUtils.encrypt("123", cursor.getString(3)));

					serializer.endTag(null, "body");

					serializer.endTag(null, "sms");
					//序列化完一条短信之后就需要++
					process++;

					callback.onBackUpSms(process);


				}
				cursor.close();

				serializer.endTag(null, "smss");

				serializer.endDocument();

				os.flush();

				os.close();

				return true;

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return false;
	}

}
