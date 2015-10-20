package com.qiu.houde_mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;

import com.qiu.houde_mobilesafe.R;


/**
 * 第3个设置向导页
 * 
 * @author Kevin
 * 
 */
public class Setup3Activity  extends BaseSetupActivity  {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
	}

	@Override
	protected void showPreviousPage() {
		startActivity(new Intent(this, Setup2Activity.class));
		finish();
	}

	@Override
	protected void showNextPage() {
		startActivity(new Intent(this, Setup4Activity.class));
		finish();
	}

}
