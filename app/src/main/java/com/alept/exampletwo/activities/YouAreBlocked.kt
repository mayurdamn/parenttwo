package com.alept.exampletwo.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alept.exampletwo.MyApplication
import com.alept.exampletwo.R
import com.alept.exampletwo.util.SharePreferences

class YouAreBlocked : AppCompatActivity() {
    lateinit var closeImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_you_are_blocked)
        Log.e("YouAreBlocked", "YouAreBlocked")
        closeImage = findViewById(R.id.close)
        var str = SharePreferences(this).getString(SharePreferences.ALLOWED_TIME)
        Log.e("YouAreBlocked", "YouAreBlocked $str")
        when (str) {
            "0" -> {
                MyApplication().REMAINING_TIME = 0
            }
            "1" -> {
                MyApplication().REMAINING_TIME = 1
            }
            "2" -> {
                MyApplication().REMAINING_TIME = 2
            }
            else -> {
                MyApplication().REMAINING_TIME = 2
                SharePreferences(this).putString(SharePreferences.ALLOWED_TIME,"2")
            }
        }
        closeImage.setOnClickListener {
           // startActivity(Intent(this, AllAppsActivity::class.java))
            finish()
        }
    }


    override fun onBackPressed() {
       // startActivity(Intent(this, AllAppsActivity::class.java))
        finish()
    }
}