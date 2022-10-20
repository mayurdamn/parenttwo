package com.alept.exampletwo

import android.app.Application
import android.content.Intent
import android.os.CountDownTimer
import android.util.Log
import androidx.core.content.ContextCompat
import com.alept.exampletwo.activities.YouAreBlocked
import com.alept.exampletwo.util.SharePreferences
import java.util.concurrent.TimeUnit

class MyApplication : Application() {
    private lateinit var countDownTimer: CountDownTimer
    var globalVar = "I am Global Variable"
    var REMAINING_TIME = 2


}