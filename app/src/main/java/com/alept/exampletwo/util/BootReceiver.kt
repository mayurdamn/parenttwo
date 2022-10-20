package com.alept.exampletwo.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.alept.exampletwo.activities.AllAppsActivity

class BootReceiver : BroadcastReceiver(){

    override fun onReceive(context: Context?, intent: Intent?) {
        val myIntent = Intent(context, AllAppsActivity::class.java)
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context!!.startActivity(myIntent)
    }
}