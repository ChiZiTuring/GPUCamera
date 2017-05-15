package com.gyb.android.gpucamera;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity 
		implements OnClickListener {
	private static final String TAG = "MainActivity";
	private Button mBtn_switch;
	private Button mBtn_test;
	private CameraView mCameraView;
	private boolean mTestBigger = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mBtn_switch = (Button)findViewById(R.id.btn_switch);
		mBtn_switch.setOnClickListener(this);
		mBtn_test = (Button)findViewById(R.id.btn_testBigger);
		mBtn_test.setOnClickListener(this);
		mCameraView = (CameraView)findViewById(R.id.mCameraView);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mCameraView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mCameraView.onPause();
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
			case R.id.btn_switch:	//切换摄像头
				Log.d(TAG,"clock id switch...");
				mCameraView.switchCamera();
				break;
			case R.id.btn_testBigger:
				Log.d(TAG,"test bigger..");
				if(mTestBigger) {
					mCameraView.setTestBigger(!mTestBigger);
					mTestBigger = false;
				}else{
					mCameraView.setTestBigger(!mTestBigger);
					mTestBigger = true;
				}
				break;
			
			default:
				break;
		}
	}
}
