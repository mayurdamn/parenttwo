package com.alept.exampletwo.util

import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.alept.exampletwo.MyApplication
import com.alept.exampletwo.activities.YouAreBlocked
import com.alept.exampletwo.database.AddTimer
import com.alept.exampletwo.database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit



class WorkManagerTimer(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context,workerParams) {
    private lateinit var countDownTimer: CountDownTimer
    private var timer: Timer? = null
    private var timerTask: TimerTask? = null
    lateinit var addTimer: List<AddTimer>
    lateinit var appDatabase: AppDatabase
    override suspend fun doWork(): Result {

        CoroutineScope(Dispatchers.IO).launch {
            val h = Handler(Looper.getMainLooper())
            h.post(Runnable {
                //here show dialog
                startTimer(30)
            })


          //  startTimer()

           // LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent1)

        }

        return Result.success()
    }
   init {
       appDatabase = AppDatabase.getInstance(applicationContext)!!
       CoroutineScope(Dispatchers.IO).launch {
           addTimer = appDatabase.AppDao().getTimerValue()
       }
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

                GlobalScope.launch(Dispatchers.IO){
                    if (addTimer.isEmpty() && ::addTimer.isInitialized) {
                        appDatabase.AppDao().setIsBlock(AddTimer(0,1))


                    } else {
                        appDatabase.AppDao().setIsBlock(AddTimer(0,1))

                    }
                }
                MyApplication().REMAINING_TIME = 1
                SharePreferences(applicationContext).putString(SharePreferences.ALLOWED_TIME, "1")


               /* startActivity(
                    applicationContext,
                    Intent(
                        applicationContext,
                        YouAreBlocked::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), null
                )*/

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

    var counter:Int=0
 /*   fun startTimer() {
        timer = Timer()
        timerTask = object : TimerTask() {
            override fun run() {
                Log.i("Count", "=========  " + counter++)
                for (x in 0 until 100){
                    Log.i("Count___", "=========  " + x)
                    if(x==100){
                        stoptimertask()
                    }
                }

            }
        }
        timer!!.scheduleAtFixedRate(timerTask,  1000, 1000) //
    }

    fun stoptimertask() {
        if (timer != null) {
            timer!!.cancel()
            timer = null
        }
    }*/
}