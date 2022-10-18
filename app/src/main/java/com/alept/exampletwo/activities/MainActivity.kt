package com.alept.exampletwo.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.alept.exampletwo.R
import com.alept.exampletwo.activities.BlockedScreen


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //checking if the app is started first time to ask for the needed permissions
        val prefs = getSharedPreferences("first", MODE_PRIVATE)
        val first = prefs.getString("first", "yes")
        if (first!!.contains("yes")){
            alertDialog()
            val editor = getSharedPreferences("first", MODE_PRIVATE).edit()
            editor.putString("first", "No")
            editor.apply()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.op1 -> {
               startActivity(Intent(this,AllAppsActivity::class.java))

            }
        }
        return true
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

}