package com.alept.exampletwo.util

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log

class TimeNew :Service(){
    private val LOG_TAG = "TimerService"
    private val mBinder: IBinder = MyBinder()
    var onlineTimeHandler: Handler = Handler(Looper.getMainLooper())
    var startTime = 0L
    var timeInMilliseconds:kotlin.Long = 0L
    var timeSwapBuff:kotlin.Long = 0L
    var updatedTime:kotlin.Long = 0L
    var mins = 0
    var secs:kotlin.Int = 0
    var hours:kotlin.Int = 0
    var time = ""




    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }
    override fun onCreate() {
        super.onCreate()
        Log.v(LOG_TAG, "in onCreate")
        startTime = SystemClock.uptimeMillis()
        onlineTimeHandler.post(updateTimerThread)
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.v(LOG_TAG, "in onBind")
        return mBinder
    }

    override fun onRebind(intent: Intent?) {
        Log.v(LOG_TAG, "in onRebind")
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.v(LOG_TAG, "in onUnbind")
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(LOG_TAG, "in onDestroy")
        if (onlineTimeHandler != null) {
            onlineTimeHandler.removeCallbacks(updateTimerThread)
        }
    }
   inner class MyBinder : Binder() {
        fun getService(): MyBinder {
            return this@MyBinder
        }
    }
    private val updateTimerThread: Runnable = object : Runnable {
        override fun run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime
            updatedTime = timeSwapBuff + timeInMilliseconds
            secs = (updatedTime / 1000).toInt()
            hours = secs / (60 * 60)
            mins = secs / 60
            secs = secs % 60
            if (mins >= 60) {
                mins = 0
            }
            time = (String.format("%02d", hours) + ":" + String.format("%02d", mins) + ":"
                    + String.format("%02d", secs))
            val intent = Intent()
            intent.action = "com.app.Timer.UpdateTime"
            intent.putExtra("time", time)
            sendBroadcast(intent)
            onlineTimeHandler.postDelayed(this, (1 * 1000).toLong())
        }
    }

}