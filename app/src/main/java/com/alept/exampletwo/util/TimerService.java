package com.alept.exampletwo.util;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

public class TimerService extends Service {
    private static String LOG_TAG = "TimerService";
    private IBinder mBinder = new MyBinder();

    Handler onlineTimeHandler = new Handler();
    long startTime = 0L, timeInMilliseconds = 0L, timeSwapBuff = 0L, updatedTime = 0L;
    int mins, secs, hours;
    String time = "";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(LOG_TAG, "in onCreate");
        startTime = SystemClock.uptimeMillis();
        onlineTimeHandler.post(updateTimerThread);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.v(LOG_TAG, "in onBind");
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.v(LOG_TAG, "in onRebind");
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.v(LOG_TAG, "in onUnbind");
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(LOG_TAG, "in onDestroy");
        if (onlineTimeHandler != null) {
            onlineTimeHandler.removeCallbacks(updateTimerThread);
        }
    }

    public class MyBinder extends Binder {
        TimerService getService() {
            return TimerService.this;
        }
    }

    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            secs = (int) (updatedTime / 1000);
            hours = secs / (60 * 60);
            mins = secs / 60;
            secs = secs % 60;
            if (mins >= 60) {
                mins = 0;
            }

            time = String.format("%02d", hours) + ":" + String.format("%02d", mins) + ":"
                    + String.format("%02d", secs);
            Log.e("-------------",time);

            Intent intent = new Intent();
            intent.setAction("com.app.Timer.UpdateTime");
            intent.putExtra("time", time);
            sendBroadcast(intent);

            onlineTimeHandler.postDelayed(this, 1 * 1000);

        }

    };

}