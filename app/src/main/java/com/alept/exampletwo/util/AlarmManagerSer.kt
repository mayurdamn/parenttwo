package com.alept.exampletwo.util

import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.alept.exampletwo.MyApplication
import com.alept.exampletwo.activities.AllAppsActivity
import com.alept.exampletwo.activities.YouAreBlocked
import com.alept.exampletwo.database.AddTimer
import com.alept.exampletwo.database.AppDatabase
import java.util.concurrent.TimeUnit

class AlarmManagerSer() :Service(){

    lateinit var countDownTimer:CountDownTimer
    lateinit var addTimer: List<AddTimer>
    lateinit var appDatabase: AppDatabase
   /* init {
        appDatabase = AppDatabase.getInstance(this@AlarmManagerSer)!!
        CoroutineScope(Dispatchers.IO).launch {
            addTimer = appDatabase.AppDao().getTimerValue()
        }
    }*/
    override fun onCreate() {
      //  Toast.makeText(this, "MyAlarmService.onCreate()", Toast.LENGTH_LONG).show();
        super.onCreate()
    }
    override fun onBind(intent: Intent?): IBinder? {
        Toast.makeText(this, "MyAlarmService.onBind()", Toast.LENGTH_LONG).show();
        return null
    }
    override fun onDestroy() {
        super.onDestroy()
      //  Toast.makeText(this, "MyAlarmService.onDestroy()", Toast.LENGTH_LONG).show()
    }
    override fun onStart(intent: Intent?, startId: Int) {
        super.onStart(intent, startId)
       // Toast.makeText(this, "MyAlarmService.onStart()${intent!!.data.toString()}", Toast.LENGTH_LONG).show()
      /*  var value = intent!!.getIntExtra("somevalue",0)
        Log.e("Timer Worker@@@----->", value.toString())*/
        /*startTimer(30)*/
    }
    override fun onUnbind(intent: Intent?): Boolean {
      //  Toast.makeText(this, "MyAlarmService.onUnbind()", Toast.LENGTH_LONG).show()
        return super.onUnbind(intent)
    }

    private fun startTimer(time: Int) {
        Log.e("Timer Worker----->", time.toString())
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

              /*  GlobalScope.launch(Dispatchers.IO){
                    if (addTimer.isEmpty() && ::addTimer.isInitialized) {
                        appDatabase.AppDao().setIsBlock(AddTimer(0,1))


                    } else {
                        appDatabase.AppDao().setIsBlock(AddTimer(0,1))

                    }
                }*/
                MyApplication().REMAINING_TIME = 1
                SharePreferences(this@AlarmManagerSer).putString(SharePreferences.ALLOWED_TIME, "1")
                 startActivity( Intent(this@AlarmManagerSer, AllAppsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),null
                 )

            }
        }
        countDownTimer.start()
        /*  GlobalScope.launch((Dispatchers.IO)) {
            if(Looper.myLooper()==null){

                Looper.prepare()
            }
            launch((Dispatchers.IO)){

            }

        }*/
    }
}