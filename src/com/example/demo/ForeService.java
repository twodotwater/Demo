package com.example.demo;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class ForeService extends Service {

	private static final String TAG = ForeService.class.getSimpleName();

	public static final int DESTROYED = -1;
	public static final int UNBOUND = 0;
	public static final int CREATED = 1;
	public static final int BOUND = 2;

	private static int state = DESTROYED;

	private int requestCode = 1001;
	private int counter = 0;
	private Notification notification;
	private Builder builder = new Notification.Builder(this);

	public static int getState() {
		return state;
	}

	public static boolean needStop() {
		return state >= UNBOUND;
	}

	public static boolean isCreated() {
		return state >= CREATED;
	}

	public static boolean isBound(boolean connected) {
		return connected && state >= BOUND;
	}

	@Override
	public void onCreate() {
		Log.i(TAG, "onCreate");
		super.onCreate();
		Intent intent = new Intent(this, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent,
				Intent.FLAG_ACTIVITY_CLEAR_TOP);
		builder.setSmallIcon(R.drawable.ic_launcher).setContentIntent(pendingIntent);
		refreshNotification("onCreate");
		state = CREATED;
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "onDestroy");
		super.onDestroy();
		state = DESTROYED;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "onStartCommand");
		refreshNotification("onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}

	public void refreshNotification(CharSequence title) {
		builder.setContentTitle(title).setContentText("refresh times: " + (++counter));
		notification = builder.build();
		startForeground(1, notification);
	}

	private MyBinder myBinder = new MyBinder();

	@Override
	public boolean onUnbind(Intent intent) {
		Log.i(TAG, "onUnbind");
		refreshNotification("onUnbind");
		state = UNBOUND;
		return super.onUnbind(intent);
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.i(TAG, "onBind");
		refreshNotification("onBind");
		state = BOUND;
		return myBinder;
	}

	class MyBinder extends Binder {

		public ForeService getForeService() {
			return ForeService.this;
		}
	}
}
