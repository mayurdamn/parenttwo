package com.alept.exampletwo.activities

import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alept.exampletwo.MyApplication
import com.alept.exampletwo.database.AllAppsTable
import com.alept.exampletwo.database.AppDatabase
import com.alept.exampletwo.databinding.ActivityAppBlockingBinding
import com.alept.exampletwo.util.SharePreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class AppBlockingActivity : AppCompatActivity() {
    lateinit var binding: ActivityAppBlockingBinding
    lateinit var appDatabase: AppDatabase
    lateinit var appsTable: List<AllAppsTable>
    var appName :String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppBlockingBinding.inflate(layoutInflater)
        val veiw = binding.root
        setContentView(veiw)
        appDatabase = AppDatabase.getInstance(this)!!
         appName = intent.getStringExtra("name")!!
        val appPackage = intent.getStringExtra("package")
        val icon = intent.getParcelableExtra<Bitmap>("icon")
        binding.appName.text = appName
        binding.appIcon.setImageBitmap(icon)
        // checking if the app is already blocked or not

       /* thread {

            runOnUiThread {

            }
        }*/
        //adding the app to the block list if its not blocked already and removing if its blocked using switch
        binding.blockChip.setOnCheckedChangeListener { _, b ->
            if (b) {
                MyApplication().REMAINING_TIME=1
                SharePreferences(this).putString(SharePreferences.ALLOWED_TIME,"1")
                thread {

                    if (appsTable.isEmpty() && ::appsTable.isInitialized) {
                        appDatabase.AppDao().setBlockedApp(AllAppsTable(appName!!, appPackage!!, 1))


                    } else {
                        appDatabase.AppDao().updateBlockedApp(
                            AllAppsTable(
                                appsTable[0].id,
                                appName!!,
                                appPackage!!,
                                1,
                            )
                        )
                    }
                }

                SharePreferences(this).putString(appName,appPackage)

            } else {
                thread {
                    if (appsTable.isNotEmpty()) {
                        appDatabase.AppDao()
                            .updateBlockedApp(AllAppsTable(appsTable[0].id, appName!!, appPackage!!, 0))
                    }
                    if (appName != null) {
                        binding.blockChip1.isChecked=false
                        //SharePreferences(this).removeBadKeys(appName)
                        SharePreferences(this).putBoolean(SharePreferences.ALLOWED_TIME,false)
                    }
                }
            }
        }

        binding.blockChip1.setOnCheckedChangeListener {_, b ->
            if(b){
                SharePreferences(this).putBoolean(SharePreferences.ALLOWED_TIME,true)
            }else{
                SharePreferences(this).putBoolean(SharePreferences.ALLOWED_TIME,false)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        GlobalScope.launch(Dispatchers.IO){
            appsTable = appDatabase.AppDao().checkApp(appName!!)
            launch(Dispatchers.Main){

                if (appsTable.isNotEmpty()){
                    binding.blockChip.isChecked = appsTable[0].isBlocked == 1
                }
            }

        }
    }
}