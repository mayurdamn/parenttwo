package com.alept.exampletwo.util

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast
import androidx.core.app.ActivityCompat.finishAffinity
import com.alept.exampletwo.activities.YouAreBlocked
import com.alept.exampletwo.database.AllAppsTable
import com.alept.exampletwo.database.AppDatabase
import kotlin.concurrent.thread
import kotlin.properties.Delegates


class ActiveAppChecker : AccessibilityService() {
    lateinit var allAppsTable: List<AllAppsTable>
    lateinit var appDatabase: AppDatabase
    var TAG = "blockApp()"
    var isBlock by Delegates.notNull<Boolean>()

    override fun onCreate() {
        super.onCreate()
    }

    override fun onAccessibilityEvent(p0: AccessibilityEvent?) {
        Log.e(TAG, "--Inside Accessiblity")
       // isBlock = SharePreferences(this).getBoolean(SharePreferences.ALLOWED_TIME)
        appDatabase = AppDatabase.getInstance(applicationContext)!!
        // getting the running package name
        val runningPackName: String = p0?.packageName.toString()
        val eventType: String = p0?.eventType.toString()
        val action: String = p0?.action.toString()
        val source: String = p0?.source.toString()
        val className: String = p0?.className.toString()
       /* Log.e(
            TAG,
            "--Inside Accessiblity$runningPackName $source  ${className.toString()} ${eventType.toString()}"
        )*/
        Log.e(
            TAG,
            "------>Time Start first,${SharePreferences.REMAINING_TIME}")
        Log.e(
            TAG,
            "------>Time Start ${SharePreferences(this).getString(SharePreferences.ALLOWED_TIME)}")

        SharePreferences.REMAINING_TIME =SharePreferences(this).getString(SharePreferences.ALLOWED_TIME).toInt()
        thread {
            allAppsTable = appDatabase.AppDao().getApps()

            when (SharePreferences.REMAINING_TIME) {
                1 -> {
                    isBlock = false
                    for (i in allAppsTable) {
                        if (runningPackName.contains(i.packageName)) {
                            if (i.isBlocked == 1) {
                                Log.e(TAG, "--Inside ${i.isBlocked}${i.packageName}")
                                blockApp(i.packageName, i.appName)
                            }
                        }
                    }
                }
                0 -> {
                    isBlock = true
                    Log.e(
                        TAG,
                        "------>Time Start ${SharePreferences(this).getString(SharePreferences.ALLOWED_TIME)}")
                }
                2 -> {

                   // blockApp("i.packageName", "i.appName")
                    Log.e(
                        TAG,
                        "------>Time Start ${SharePreferences(this).getString(SharePreferences.ALLOWED_TIME)}")
                    for (i in allAppsTable) {
                        if (runningPackName.contains(i.packageName)) {
                            if (i.isBlocked == 1) {
                                Log.e(TAG, "--Inside ${i.isBlocked}${i.packageName}")
                                blockApp(i.packageName, i.appName)
                            }
                        }
                    }
                }


            }

            /* if (isBlock) {

             } else if(!isBlock ) {

             }*/


            // checking if running app is blocked in the database


        }
    }

    // method called for showing the block screen
    private fun blockApp(packageName: String, appName: String) {
        val appName = SharePreferences(applicationContext).getString(appName)
        Log.e(TAG, "------>Inside blockApp YouAreBlocked $packageName   $appName")

        startActivity(
            Intent(
                applicationContext,
                YouAreBlocked::class.java
            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )

    }

    override fun onInterrupt() {
    }


    override fun onDestroy() {
        super.onDestroy()

    }
}