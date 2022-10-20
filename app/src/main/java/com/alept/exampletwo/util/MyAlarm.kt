package com.alept.exampletwo.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.ContextCompat.startForegroundService
import com.alept.exampletwo.R
import com.alept.exampletwo.activities.AllAppsActivity
import com.alept.exampletwo.activities.YouAreBlocked
import com.alept.exampletwo.database.AddTimer
import com.alept.exampletwo.database.AppDatabase
import com.jakewharton.processphoenix.ProcessPhoenix
import kotlinx.coroutines.*


class MyAlarm : BroadcastReceiver() {
    lateinit var addTimer: List<AddTimer>
    lateinit var appDatabase: AppDatabase
    @OptIn(DelicateCoroutinesApi::class)
    override fun onReceive(
        context: Context,
        intent: Intent
    ) {
        Log.d("Alarm Bell", "Alarm just fired")

        appDatabase = AppDatabase.getInstance(context)!!
        CoroutineScope(Dispatchers.IO).launch {
            addTimer = appDatabase.AppDao().getTimerValue()
        }

        GlobalScope.launch(Dispatchers.IO){
            addTimer = appDatabase.AppDao().getTimerValue()
            if (addTimer.isEmpty() && ::addTimer.isInitialized) {
                appDatabase.AppDao().setIsBlock(AddTimer(0,1))

            } else {
                appDatabase.AppDao().setIsBlock(AddTimer(0,1))

            }
        }

    }

    fun openApp(context: Context, packageName: String?): Boolean {
        val manager = context.packageManager
        return try {
            val i = manager.getLaunchIntentForPackage(packageName!!)
                ?: return false
            //throw new ActivityNotFoundException();
            i.addCategory(Intent.CATEGORY_LAUNCHER)
            context.startActivity(i)
            true
        } catch (e: ActivityNotFoundException) {
            false
        }
        createNotification(context)
    }

    private fun createNotification(context: Context): Notification {
        val notificationChannelId = "ENDLESS SERVICE CHANNEL"

        // depending on the Android API that we're dealing with we will have
        // to use a specific method to create the notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager;
            val channel = NotificationChannel(
                notificationChannelId,
                "Endless Service notifications channel",
                NotificationManager.IMPORTANCE_HIGH
            ).let {
                it.description = "Endless Service channel"
                it.enableLights(true)
                it.lightColor = Color.RED
                it.enableVibration(true)
                it.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                it
            }
            notificationManager.createNotificationChannel(channel)
        }
        val intent = Intent(context, AllAppsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)

        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
       /* let { notificationIntent ->


        }
*/       startActivity(context,intent, Bundle())

        val builder: Notification.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) Notification.Builder(
            context,
            notificationChannelId
        ) else Notification.Builder(context)

        return builder
            .setContentTitle("Endless Service")
            .setContentText("This is your favorite endless service working")
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setTicker("Ticker text")
            .setAutoCancel(true)
            .setPriority(Notification.PRIORITY_HIGH) // for under android 26 compatibility
            .build()
    }
}