package com.example.demo;

import com.example.demo.MyAIDLService.Stub;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class AidlService extends Service {

	protected static final String TAG = AidlService.class.getSimpleName();

	@Override
	public void onCreate() {
		super.onCreate();
		mHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case WHAT_SHOW_TOAST:
					Toast.makeText(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
					break;

				default:
					break;
				}
			}
		};
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder.asBinder();
	}

	private static final int WHAT_SHOW_TOAST = 0;
	private static Handler mHandler;

	MyAIDLService.Stub binder = new Stub() {

		@Override
		public void print(String text) throws RemoteException {
			Log.i(TAG, text);
			mHandler.obtainMessage(WHAT_SHOW_TOAST, text).sendToTarget();
		}
	};

}
