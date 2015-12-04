package com.example.demo;

import com.example.demo.ForeService.MyBinder;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

public class MainActivity extends Activity {
	protected static final String TAG = MainActivity.class.getSimpleName();

	private ForeService foreService;
	private boolean connected = false;

	private ServiceConnection connection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.i(TAG, "onServiceDisconnected");
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.i(TAG, "onServiceConnected");
			foreService = ((MyBinder) service).getForeService();
			connected = true;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void onClick(View v) {
		Intent intent = new Intent(this, ForeService.class);
		switch (v.getId()) {
		case R.id.btn_startService:
			startService(intent);
			break;
		case R.id.btn_stopService:
			if (!ForeService.needStop()) {
				return;
			}
			stopService(intent);
			break;
		case R.id.btn_bindService:
			bindService(intent, connection, BIND_AUTO_CREATE);
			break;
		case R.id.btn_unbindService:
			if (!ForeService.isBound(connected)) {
				return;
			}
			unbindService(connection);
			break;

		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		if (ForeService.isBound(connected)) {
			unbindService(connection);
		}
		if (ForeService.needStop()) {
			stopService(new Intent(this, ForeService.class));
		}
		super.onDestroy();
	}
}
