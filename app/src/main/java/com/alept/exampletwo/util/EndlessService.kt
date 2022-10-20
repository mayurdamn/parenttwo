package com.alept.exampletwo.util

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.*
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.alept.exampletwo.MyApplication
import com.alept.exampletwo.R
import com.alept.exampletwo.activities.AllAppsActivity
import com.alept.exampletwo.activities.YouAreBlocked
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class EndlessService : Service() {
    private lateinit var countDownTimer: CountDownTimer
    override fun onBind(intent: Intent): IBinder? {
        Log.e("Intent","Some component want to bind with the service")
        // We don't provide binding, so return null
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e("onStartCommand","onStartCommand executed with startId: $startId")
        if (intent != null) {
            val action = intent.action
            Log.e("action","using an intent with action $action")

        } else {
            Log.e(
                "----","with a null intent. It has been probably restarted by the system."
            )
        }
        var notification = createNotification()
        startForeground(1, notification)
        // by returning this we make sure the service is restarted if the system kills the service
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        val h = Handler(Looper.getMainLooper())
        h.post(Runnable {
            startTimer(30)
            //here show dialog
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("onDestroy","The service has been destroyed".toUpperCase())
        Toast.makeText(this, "Service destroyed", Toast.LENGTH_SHORT).show()
    }

    private fun createNotification(): Notification {
        val notificationChannelId = "ENDLESS SERVICE CHANNEL"

        // depending on the Android API that we're dealing with we will have
        // to use a specific method to create the notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager;
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

        val pendingIntent: PendingIntent = Intent(this, AllAppsActivity::class.java).let { notificationIntent ->
            PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
        }

        val builder: Notification.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) Notification.Builder(
            this,
            notificationChannelId
        ) else Notification.Builder(this)

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

    private fun startTimer(time: Int) {
        Log.e("Timer Worker----->", time.toString())

        CoroutineScope(Dispatchers.IO).launch {
            if(Looper.myLooper()==null){

                Looper.prepare()
            }
            /*launch((Dispatchers.IO)){

            }*/
            countDownTimer = object : CountDownTimer(30.toLong() * 1000, 30.toLong()) {
                override fun onTick(millisUntilFinished: Long) {

                    val hms = String.format(
                        "%02d:%02d",

                        (TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) -
                                TimeUnit.HOURS.toMinutes(
                                    TimeUnit.MILLISECONDS.toHours(
                                        millisUntilFinished
                                    )
                                )),
                        (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                        ))
                    )
                    Log.e("Timer Worker----->", hms.toString())
                }

                override fun onFinish() {
                    /*GlobalScope.launch(Dispatchers.IO){
                        if (addTimer.isEmpty() && ::addTimer.isInitialized) {
                            appDatabase.AppDao().setIsBlock(AddTimer(0,1))


                        } else {
                            appDatabase.AppDao().setIsBlock(AddTimer(0,1))

                        }
                    }
    */
                    MyApplication().REMAINING_TIME = 1
                    SharePreferences(applicationContext).putString(SharePreferences.ALLOWED_TIME, "1")


                    ContextCompat.startActivity(
                        applicationContext,
                        Intent(
                            applicationContext,
                            YouAreBlocked::class.java
                        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), null
                    )

                }
            }
            countDownTimer.start()

        }

    }
}