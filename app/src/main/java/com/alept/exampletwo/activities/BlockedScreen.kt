package com.alept.exampletwo.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.alept.exampletwo.MyApplication
import com.alept.exampletwo.R
import com.alept.exampletwo.database.AddTimer
import com.alept.exampletwo.database.AppDatabase
import com.alept.exampletwo.util.AlarmManagerSer
import com.alept.exampletwo.util.MyAlarm
import com.alept.exampletwo.util.SharePreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

// screen which is shown when the app the blocked
class BlockedScreen : AppCompatActivity() {
    lateinit var buttonStart: Button
    lateinit var buttonStop: Button
    lateinit var buttonClose: Button
    lateinit var mEditText: EditText
    lateinit var tvSecond: TextView
    lateinit var tvBlockUnblock: TextView
    var timeValue :String =""
    lateinit var appDatabase: AppDatabase
    lateinit var addTimer: List<AddTimer>
    private lateinit var localBroadcastManager: LocalBroadcastManager
    private lateinit var countDownTimer: CountDownTimer
    lateinit var pendingIntent: PendingIntent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blocked_screen)
        localBroadcastManager = LocalBroadcastManager.getInstance(this)
        appDatabase = AppDatabase.getInstance(this)!!
        initUI()

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initUI() {
        buttonClose = findViewById(R.id.buttonClose)
        buttonStart = findViewById(R.id.buttonStart)
        buttonStop = findViewById(R.id.buttonStop)
        mEditText = findViewById(R.id.editTime)
        mEditText = findViewById(R.id.editTime)
        tvSecond = findViewById(R.id.tvSecond)
        tvBlockUnblock = findViewById(R.id.tvBlockUnblock)

        GlobalScope.launch(Dispatchers.IO){
            addTimer = appDatabase.AppDao().getTimerValue()
            launch(Dispatchers.Main){

                if (addTimer.isNotEmpty()){

                }
            }

        }
        buttonStart.setOnClickListener {

            if (mEditText.text.toString().isNotEmpty() && mEditText.text.toString().isNotBlank()) {
                //SharePreferences.REMAINING_TIME = 0
                //startTimer(tvSecond, mEditText.text.toString().toInt())
               // SharePreferences(this).putBoolean(SharePreferences.ALLOWED_TIME, false)
                timeValue = mEditText.text.toString()
                MyApplication().REMAINING_TIME = 0
                SharePreferences(this).putString(SharePreferences.ALLOWED_TIME, "0")

                GlobalScope.launch(Dispatchers.IO){
                    if (addTimer.isEmpty() && ::addTimer.isInitialized) {
                        appDatabase.AppDao().setIsBlock(AddTimer(0,0))

                    } else {
                        appDatabase.AppDao().setIsBlock(AddTimer( 0,0))
                    }
                }

                /*val myIntent = Intent(this@BlockedScreen, MyAlarm ::class.java)//.putExtra("somevalue",timeValue)
                pendingIntent = PendingIntent.getService(this@BlockedScreen, 0, myIntent, FLAG_IMMUTABLE)
                val alarmManager: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val calendar: Calendar = Calendar.getInstance()
                calendar.timeInMillis = System.currentTimeMillis()
                calendar.add(Calendar.SECOND, 3)
                alarmManager.setRepeating(
                    AlarmManager.RTC,
                    timeValue.toLong() * 1000,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )
                Toast.makeText(baseContext, "Starting Service Alarm", Toast.LENGTH_LONG).show()*/
                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

// Intent part
                val intent = Intent(this, MyAlarm::class.java)
                intent.action = "FOO_ACTION"
                intent.putExtra("KEY_FOO_STRING", "Medium AlarmManager Demo")

                val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, FLAG_IMMUTABLE)

// Alarm time
                val ALARM_DELAY_IN_SECOND = 10
                val alarmTimeAtUTC = System.currentTimeMillis() + ALARM_DELAY_IN_SECOND * 1_000L

// Set with system Alarm Service
// Other possible functions: setExact() / setRepeating() / setWindow(), etc
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTimeAtUTC, pendingIntent)
                setBackResult()
            }

        }
        buttonStop.setOnClickListener {
            // sendBroadCast("0")
            countDownTimer.cancel()
            //MyApplication()(this).putBoolean(MyApplication().ALLOWED_TIME, true)
            MyApplication().REMAINING_TIME = 1
        }
        buttonClose.setOnClickListener {
            // sendBroadCast("0")
           // MyApplication()(this).putBoolean(MyApplication().ALLOWED_TIME, true)
            MyApplication().REMAINING_TIME = 0
            finish();
        }
    }

    private fun setBackResult() {
        Log.d("data-<",timeValue)
        val intent = Intent(this, AllAppsActivity::class.java)
        intent.putExtra("valueTimer", timeValue)
        if(timeValue.isNotBlank()){

            setResult(RESULT_OK, intent)
        } else {
            setResult(RESULT_CANCELED, intent)
        }
        finish()
    }


    // when user presses the back button the app goes to the home screen
    override fun onBackPressed() {

    // startActivity(Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME))
        setBackResult()
    }


    private fun startTimer(tvSecond: TextView, time: Int) {
        tvBlockUnblock.text = "You are free to use all apps "
        countDownTimer = object : CountDownTimer(time.toLong() * 1000, time.toLong()) {
            override fun onTick(millisUntilFinished: Long) {
                //  tvSecond.text = ("00:" + millisUntilFinished / 1000)
                //here you can have your logic to set text to edittext
                // Loger.LogError("timer-->", (millisUntilFinished / 1000).toString() + "---" + time)
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
                tvSecond.text = hms.toString()
                Log.e("Timer----->", hms.toString())
            }

            override fun onFinish() {
                tvSecond.text = ""
                tvBlockUnblock.text = "Blocked "
                SharePreferences(this@BlockedScreen).putBoolean(
                    SharePreferences.ALLOWED_TIME,
                    true
                )
                /* finish();
                 startActivity(intent);*/
                finish()
                startActivity(
                    Intent(
                        applicationContext,
                        YouAreBlocked::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
        }
        countDownTimer.start()
    }

    override fun onDestroy() {
        super.onDestroy()

    }

}
