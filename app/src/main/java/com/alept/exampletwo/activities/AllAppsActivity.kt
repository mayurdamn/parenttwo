package com.alept.exampletwo.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.alept.exampletwo.R
import com.alept.exampletwo.adapter.AllAppsAdapter
import com.alept.exampletwo.databinding.ActivityAllAppsBinding
import com.alept.exampletwo.util.SharePreferences
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread


class AllAppsActivity : AppCompatActivity() {
    lateinit var binding: ActivityAllAppsBinding
    private lateinit var countDownTimer: CountDownTimer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllAppsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        when(SharePreferences(this).getString(SharePreferences.ALLOWED_TIME)){
            "0"->{SharePreferences.REMAINING_TIME =0}
            "1"->{SharePreferences.REMAINING_TIME =1}
            "2"->{SharePreferences.REMAINING_TIME =2}

        }


        checkPermissionFromUser()
        binding.allAppRecyclerView.layoutManager = LinearLayoutManager(this)
        thread {
            // getting list of all the installed apps apart from this app
            val strings = ArrayList<String>()
            val drawables = ArrayList<Drawable>()
            val packages = ArrayList<String>()
            var i = 0
            val apps: List<ApplicationInfo> = packageManager.getInstalledApplications(0)
            for (app in apps) {
                if (packageManager
                        .getLaunchIntentForPackage(app.packageName) != null && !app.packageName.contains(
                        "com.alept.exampletwo"
                    )
                ) {
                    strings.add(i, packageManager.getApplicationLabel(app).toString())
                    drawables.add(i, packageManager.getApplicationIcon(app))
                    packages.add(app.packageName)
                    i++
                }
            }
            // making three arrays for passing to the adapter
            for (i1 in strings.indices) for (j1 in i1 + 1 until strings.size) {
                var tempDrw: Drawable
                var tempPck: String
                var tempNm: String
                if (strings[i1].compareTo(strings[j1], ignoreCase = true) > 0) {
                    tempNm = strings[i1]
                    tempDrw = drawables[i1]
                    tempPck = packages[i1]
                    strings[i1] = strings[j1]
                    drawables[i1] = drawables[j1]
                    packages[i1] = packages[j1]
                    strings[j1] = tempNm
                    drawables[j1] = tempDrw
                    packages[j1] = tempPck
                }
            }
            runOnUiThread {
                // passing data to the adatper for showing the list of installed apps
                binding.allAppRecyclerView.adapter =
                    AllAppsAdapter(this, strings, drawables, packages)
            }
        }


    }

    private fun checkPermissionFromUser() {
        val prefs = getSharedPreferences("first", MODE_PRIVATE)
        val first = prefs.getString("first", "yes")
        if (first!!.contains("yes")) {
            alertDialog()
            val editor = getSharedPreferences("first", MODE_PRIVATE).edit()
            editor.putString("first", "No")
            editor.apply()
        }


    }

    // to show the plus icon to redirect time screen
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.op1 -> {
                //startActivity(Intent(this,BlockedScreen::class.java))
                openActivityForResult()
            }
        }
        return true
    }

    private fun openActivityForResult() {
        val intent = Intent(this, BlockedScreen::class.java)
        resultLauncher.launch(intent)
    }


    private var resultLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            Log.d("data", data?.getStringExtra("valueTimer").toString())
            val timeInt = data?.getStringExtra("valueTimer").toString().toInt()
            SharePreferences.REMAINING_TIME = 0
            SharePreferences(this).putString(SharePreferences.ALLOWED_TIME, "0")
            startTimer(timeInt)

        }
    }

    // dialog shown for permissions
    private fun alertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.perminfo))
        builder.setTitle(getString(R.string.alerttitle))
        builder.setPositiveButton(
            getString(R.string.ok)
        ) { _, _ ->
            // if user presses ok this part is executed
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            startActivity(intent)
        }
        builder.setNegativeButton(
            getString(R.string.cancel)
        ) { _, _ -> finish() }
        // if user presses cancel this part is executed
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun startTimer(time: Int) {
        binding.tvSecond.visibility = View.VISIBLE
        countDownTimer = object : CountDownTimer(time.toLong() * 1000, time.toLong()) {
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
                binding.tvSecond.text = hms.toString()
                //Log.e("Timer----->", hms.toString())
            }

            override fun onFinish() {
                binding.tvSecond.text = ""
                binding.tvSecond.visibility = View.GONE
                SharePreferences.REMAINING_TIME = 1
                SharePreferences(this@AllAppsActivity).putString(SharePreferences.ALLOWED_TIME, "1")

                /*finish()
                startActivity(
                    Intent(
                        applicationContext,
                        YouAreBlocked::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )*/
            }
        }
        countDownTimer.start()
    }

}